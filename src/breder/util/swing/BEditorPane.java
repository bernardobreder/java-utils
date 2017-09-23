package breder.util.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import breder.util.lookandfeel.LookAndFeel;
import breder.util.swing.tree.AbstractTreeNode;
import breder.util.util.input.InputStreamUtil;

public class BEditorPane extends JScrollPane {

  protected AbstractTreeNode node;

  private Component rule;

  protected static final int UNDEFINED = 0;

  protected static final int WORD = 1;

  protected static final int SYMBOL = 2;

  protected static final int NUMBER = 3;

  public BEditorPane(AbstractTreeNode index, String text) {
    this.node = index;
    this.setViewportView(new BEditor());
    this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    this.getTextPane().setText(text.replace("\r", ""));
    this.setRowHeaderView(this.rule = this.buildRule());
    this.addStylesToDocument();
    this.buildStyles();
    this.getTextPane().addKeyListener(new Key());
  }

  protected Component buildRule() {
    Rule rule = new Rule();
    // Font font = this.getTextPane().getFont();
    // list.setFixedCellHeight(this.getTextPane().getFontMetrics(font)
    // .getHeight());
    // int size = this.getLineCount();
    // for (Integer n = 0; n < size; n++) {
    // model.addElement(n.toString());
    // }
    return rule;
  }

  public JTextPane getTextPane() {
    return (JTextPane) this.getViewport().getView();
  }

  public void selectLine(int lineIndex) {
    Rule list = this.getRowHeaderView();
    list.setSelectedIndex(lineIndex);
    list.repaint();
    int height =
      getTextPane().getFontMetrics(getTextPane().getFont()).getHeight();
    Point p = new Point(0, height * lineIndex);
    Rectangle r = this.getViewport().getViewRect();
    if (p.y < r.y || p.y > r.y + r.height) {
      this.getViewport().setViewPosition(p);
    }
  }

  public int getLineCount() {
    String text = this.getTextPane().getText();
    int len = 1, index = 0;
    index = text.indexOf('\n', index + 1);
    while (index != -1) {
      index = text.indexOf('\n', index + 1);
      len++;
    }
    return len;
  }

  public Rule getRowHeaderView() {
    return (Rule) this.getRowHeader().getView();
  }

  protected void buildStyles() {
    this.buildStyles(0, this.getTextPane().getText().length());
  }

  protected void buildStyles(int begin, int length) {
    StyledDocument doc = this.getTextPane().getStyledDocument();
    doc.setCharacterAttributes(begin, length, doc
      .getStyle(StyleContext.DEFAULT_STYLE), true);
  }

  protected boolean isSpace(char c) {
    return c == ' ' || c == '\t' || c == '\r' || c == '\n';
  }

  public int getConst(char c) {
    if (this.isWord(c)) {
      return WORD;
    }
    else if (this.isSymbol(c)) {
      return SYMBOL;
    }
    else if (this.isNumber(c)) {
      return NUMBER;
    }
    else {
      return UNDEFINED;
    }
  }

  protected boolean isNumber(char c) {
    return false;
  }

  protected boolean isWord(char c) {
    return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '$'
      || c == '_';
  }

  protected boolean isSymbol(char c) {
    return c == '.' || c == ',' || c == ';' || c == '{' || c == '}' || c == '('
      || c == ')' || c == '<' || c == '>' || c == '!' || c == '=' || c == '-'
      || c == '+' || c == '*' || c == '/' || c == '&' || c == '|' || c == '#'
      || c == '?' || c == ':';
  }

  protected void addStylesToDocument() {
    StyledDocument doc = this.getTextPane().getStyledDocument();
    Style def =
      StyleContext.getDefaultStyleContext()
        .getStyle(StyleContext.DEFAULT_STYLE);

    Style regular = doc.addStyle("regular", def);
    StyleConstants.setFontFamily(def, "SansSerif");

    Style s = doc.addStyle("italic", regular);
    StyleConstants.setItalic(s, true);

    s = doc.addStyle("bold", regular);
    StyleConstants.setBold(s, true);

    s = doc.addStyle("small", regular);
    StyleConstants.setFontSize(s, 10);

    s = doc.addStyle("large", regular);
    StyleConstants.setFontSize(s, 16);
  }

  public void onKeyEvent(int index) {
    String text = this.getText();
    int begin, end;
    {
      int aux = index - 1;
      while (true) {
        char c = text.charAt(aux);
        if (this.isSymbol(c) || this.isSpace(c) || aux == 0) {
          break;
        }
        aux--;
      }
      begin = aux;
    }
    {
      int aux = index - 1;
      while (aux != text.length()) {
        char c = text.charAt(aux);
        if (this.isSymbol(c) || this.isSpace(c)) {
          break;
        }
        aux++;
      }
      end = aux;
    }
    begin = Math.max(0, begin - 50);
    end = Math.min(text.length(), end - begin + 50);
    while (begin != 0 && text.charAt(begin - 1) != ' ') {
      begin--;
    }
    this.buildStyles(0, text.length());
  }

  public AbstractTreeNode getNode() {
    return node;
  }

  public String getText() {
    return this.getTextPane().getText();
  }

  protected class Key extends KeyAdapter {

    @Override
    public void keyReleased(KeyEvent e) {
      onKeyEvent(getTextPane().getSelectionStart());
      BEditorPane.this.validate();
      rule.invalidate();
      rule.repaint();
      BEditorPane.this.repaint();
    }

  }

  public class BEditor extends JTextPane {

    public BEditor() {
      this.setDocument(new TabDocument());
    }

    @Override
    public Dimension getPreferredSize() {
      FontMetrics fm = getTextPane().getFontMetrics(getTextPane().getFont());
      int lines = getLineCount();
      int width =
        this.getParent().getParent().getParent().getSize().width - 2
          * BEditorPane.this.getVerticalScrollBar().getWidth();
      return new Dimension(width, lines * fm.getHeight());
    }

    public boolean getScrollableTracksViewportWidth() {
      return false;
    }

  }

  private class Rule extends Component {

    private int selectedIndex;

    public Rule() {
      this.selectedIndex = -1;
    }

    public void paint(Graphics g) {
      FontMetrics fm = getTextPane().getFontMetrics(getTextPane().getFont());
      {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
      }
      {
        g.setColor(Color.BLUE);
        int y = this.selectedIndex * fm.getHeight();
        g.fillRect(0, y, getWidth(), fm.getHeight());
      }
      {
        g.setColor(Color.BLACK);
        int y = 0;
        int lines = getLineCount();
        g.setFont(getTextPane().getFont());
        g.getFont().deriveFont(Font.BOLD);
        for (int n = 0; n < lines + 1; n++) {
          g.drawString("" + n, 0, y);
          y += fm.getHeight();
        }
      }
    }

    public void setSelectedIndex(int lineIndex) {
      this.selectedIndex = lineIndex;
    }

    @Override
    public Dimension getPreferredSize() {
      FontMetrics fm = getTextPane().getFontMetrics(getTextPane().getFont());
      int lines = getLineCount();
      String string = "" + lines;
      return new Dimension(fm.stringWidth(string), lines * fm.getHeight());
    }

  }

  public static class TabDocument extends DefaultStyledDocument {

    @Override
    public void insertString(int offs, String str, AttributeSet a)
      throws BadLocationException {
      if (str.equals("\n")) {
        String text = this.getText(0, offs);
        int lastIndexOf = text.lastIndexOf('\n');
        if (lastIndexOf >= 0) {
          int n = lastIndexOf + 1;
          while (n < offs && text.charAt(n) == ' ') {
            n++;
            str += ' ';
          }
        }
      }
      str = str.replaceAll("\t", "    ");
      super.insertString(offs, str, a);
    }

  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        LookAndFeel.getInstance().installNimbus();
        JFrame frame = new JFrame();
        String text;
        try {
          text =
            new String(InputStreamUtil.getBytes(new FileInputStream(new File(
              "src/breder/util/swing/BEditorPane.java").getAbsolutePath())));
        }
        catch (IOException e) {
          e.printStackTrace();
          text = "";
        }
        final BEditorPane editor = new BEditorPane(null, text);
        frame.add(editor);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        SwingUtilities.invokeLater(new Runnable() {

          @Override
          public void run() {
            editor.selectLine(5);
          }
        });
        SwingUtilities.invokeLater(new Runnable() {

          @Override
          public void run() {
            editor.selectLine(7);
          }
        });
      }

    });
  }
}
