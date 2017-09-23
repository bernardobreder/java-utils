package breder.util.swing.richeditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;

import breder.util.lookandfeel.LookAndFeel;
import breder.util.util.SOBrowser;

/**
 * Componente de texto rico
 * 
 * 
 * @author bbreder
 */
public class RichEditor extends JPanel {

  private JEditorPane htmlText;
  private JTextArea sourceText;

  /**
   * Construtor
   */
  public RichEditor() {
    this.setLayout(new BorderLayout());
    this.add(this.buildCenter(), BorderLayout.CENTER);
    this.fireRefreshHtmlPanel();
  }

  /**
   * Atualiza o painel de Html
   */
  public void fireRefreshHtmlPanel() {
    String text = this.sourceText.getText();
    this.htmlText.setText(text);
  }

  /**
   * Constroi o componente do centro
   * 
   * @return componente
   */
  private Component buildCenter() {
    JTabbedPane tab = new JTabbedPane(JTabbedPane.TOP);
    tab.addTab("Visualizando", this.buildHtmlPanel());
    tab.addTab("Alterar", this.buildSourcePanel());
    tab.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        fireRefreshHtmlPanel();
      }
    });
    tab.setSelectedIndex(1);
    return tab;
  }

  /**
   * Painel de fonte
   * 
   * @return painel
   */
  private Component buildSourcePanel() {
    JPanel panel = new JPanel(new BorderLayout());
    Component toolPanel = this.buildTool();
    if (toolPanel != null) {
      panel.add(toolPanel, BorderLayout.NORTH);
    }
    panel.add(this.buildSourceArea(), BorderLayout.CENTER);
    return panel;
  }

  /**
   * Painel de exibição
   * 
   * @return panel
   */
  private Component buildHtmlPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(this.buildHtmlArea(), BorderLayout.CENTER);
    return panel;
  }

  /**
   * Cria as ferramentas
   * 
   * @return componente
   */
  private Component buildTool() {
    JToolBar bar = new JToolBar();
    bar.setFloatable(false);
    if (this.hasParagraphButton()) {
      JButton c = new JButton("<P>");
      c.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          int index = sourceText.getSelectionStart();
          sourceText.setText(new StringBuilder(sourceText.getText()).insert(
            index, "<p></p>").toString());
          sourceText.requestFocus();
          sourceText.setSelectionStart(index + 3);
          sourceText.setSelectionEnd(sourceText.getSelectionStart());
        }
      });
      bar.add(c);
    }
    if (this.hasBreakLineButton()) {
      JButton c = new JButton("<BR>");
      c.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          int index = sourceText.getSelectionStart();
          sourceText.setText(new StringBuilder(sourceText.getText()).insert(
            index, "<br/>\n").toString());
          sourceText.requestFocus();
          sourceText.setSelectionStart(index + 6);
          sourceText.setSelectionEnd(sourceText.getSelectionStart());
        }
      });
      bar.add(c);
    }
    if (this.hasBoldButton()) {
      JButton c = new JButton("<B>");
      c.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          int index = sourceText.getSelectionStart();
          sourceText.setText(new StringBuilder(sourceText.getText()).insert(
            index, "<b></b>").toString());
          sourceText.requestFocus();
          sourceText.setSelectionStart(index + 3);
          sourceText.setSelectionEnd(sourceText.getSelectionStart());
        }
      });
      bar.add(c);
    }
    if (this.hasBoldButton()) {
      JButton c = new JButton("<I>");
      c.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          int index = sourceText.getSelectionStart();
          sourceText.setText(new StringBuilder(sourceText.getText()).insert(
            index, "<i></i>").toString());
          sourceText.requestFocus();
          sourceText.setSelectionStart(index + 3);
          sourceText.setSelectionEnd(sourceText.getSelectionStart());
        }
      });
      bar.add(c);
    }
    if (this.hasBoldButton()) {
      JButton c = new JButton("<A>");
      c.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          int index = sourceText.getSelectionStart();
          sourceText.setText(new StringBuilder(sourceText.getText()).insert(
            index, "<a href='www.com'>(...)</i>").toString());
          sourceText.requestFocus();
          sourceText.setSelectionStart(index + 9);
          sourceText.setSelectionEnd(sourceText.getSelectionStart());
        }
      });
      bar.add(c);
    }
    if (bar.getComponentCount() == 0) {
      return null;
    }
    else {
      return bar;
    }
  }

  /**
   * Cria a caixa de texto
   * 
   * @return componente
   */
  private Component buildHtmlArea() {
    this.htmlText = new JEditorPane();
    this.htmlText.addHyperlinkListener(new HyperlinkListener() {
      @Override
      public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
          SOBrowser.open(e.getDescription());
        }
      }
    });
    this.htmlText.setEditable(false);
    this.htmlText.setEditorKit(new HTMLEditorKit());
    JScrollPane pane = new JScrollPane(htmlText);
    return pane;
  }

  /**
   * Cria a caixa de texto
   * 
   * @return componente
   */
  private Component buildSourceArea() {
    this.sourceText = new JTextArea();
    JScrollPane pane = new JScrollPane(sourceText);
    return pane;
  }

  /**
   * Indica se terá o botão de paragrafo
   * 
   * @return pergunta
   */
  protected boolean hasParagraphButton() {
    return true;
  }

  /**
   * Indica se terá o botão de paragrafo
   * 
   * @return pergunta
   */
  protected boolean hasBreakLineButton() {
    return true;
  }

  /**
   * Indica se terá o botão de paragrafo
   * 
   * @return pergunta
   */
  protected boolean hasBoldButton() {
    return true;
  }

  /**
   * Indica se terá o botão de paragrafo
   * 
   * @return pergunta
   */
  protected boolean hasItalicButton() {
    return true;
  }

  /**
   * Indica se terá o botão de paragrafo
   * 
   * @return pergunta
   */
  protected boolean hasReferenceButton() {
    return true;
  }

  /**
   * Inicializador
   * 
   * @param args
   */
  public static void main(String[] args) {
    LookAndFeel.getInstance().installSeaglass();
    JFrame frame = new JFrame();
    frame.add(new RichEditor());
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setSize(800, 600);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

}
