package breder.util.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import breder.util.resource.Resource;

public class BTextField extends BComponent implements ITextField {

  private JTextComponent textField;

  private BImage image;

  private static BufferedImage trueImage;

  private static BufferedImage falseImage;

  private IMask mask;

  static {
    try {
      trueImage = Resource.getInstance().getValidatorTrueImage();
    }
    catch (Throwable t) {
      trueImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
    }
    try {
      falseImage = Resource.getInstance().getValidatorFalseImage();
    }
    catch (Throwable t) {
      falseImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
    }
  }

  public BTextField() {
    this("", true);
  }

  public BTextField(String text, boolean imageShow) {
    this(text, imageShow, new JTextField());
  }

  public BTextField(String text, boolean imageShow, JTextComponent textField) {
    this.textField = textField;
    this.init();
    this.textField.setText(text);
    if (!imageShow) {
      this.image.getParent().remove(this.image);
    }
  }

  @Override
  public Component buildComponent() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.add(this.textField, new GBC(0, 0).horizontal());
    panel.add(this.image = new BImage(trueImage), new GBC(1, 0));
    this.textField.addKeyListener(new Key());
    this.textField.setPreferredSize(new Dimension(250, this.textField
      .getPreferredSize().height));
    return panel;
  }

  public JTextComponent getTextField() {
    return this.textField;
  }

  @Override
  public String getText() {
    return textField.getText();
  }

  @Override
  public void setText(String t) {
    textField.setText(t);
  }

  public IMask getMask() {
    return mask;
  }

  public void setMask(IMask mask) {
    this.mask = mask;
    this.textField.setText(mask.getText());
  }

  @Override
  public boolean fireValidator() {
    String text = this.getText();
    for (Validator validator : this.getValidators()) {
      if (!validator.valid(text)) {
        this.fireValidatorFalse();
        return false;
      }
    }
    this.fireValidatorTrue();
    return true;
  }

  private void fireValidatorTrue() {
    if (this.image != null) {
      if (this.image.getImage() != trueImage) {
        this.image.setImage(trueImage);
        this.image.repaint();
      }
    }
  }

  private void fireValidatorFalse() {
    if (this.image != null) {
      if (this.image.getImage() != falseImage) {
        this.image.setImage(falseImage);
        this.image.repaint();
      }
    }
  }

  private class Key extends KeyAdapter {

    private int endIndex;

    private int startIndex;

    @Override
    public void keyPressed(KeyEvent e) {
      startIndex = textField.getSelectionStart();
      endIndex = textField.getSelectionEnd();
    }

    @Override
    public void keyTyped(KeyEvent e) {
      if (e.getKeyChar() == KeyEvent.CHAR_UNDEFINED
        || e.getKeyChar() == KeyEvent.VK_ENTER || e.getModifiers() != 0) {
        return;
      }
      if (mask != null) {
        int index = textField.getSelectionStart();
        textField.setSelectionEnd(index);
        String masktext = mask.getText();
        StringBuilder text = new StringBuilder(textField.getText());
        if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE
          || e.getKeyChar() == KeyEvent.VK_DELETE) {
          if (text.length() != masktext.length()) {
            if (startIndex == endIndex) {
              text.insert(index, masktext.charAt(index));
              if (e.getKeyChar() == KeyEvent.VK_DELETE) {
                index++;
              }
            }
            else {
              text =
                text.replace(startIndex, endIndex, masktext.substring(
                  startIndex, endIndex));
            }
            textField.setText(text.toString());
            textField.setSelectionStart(index);
            textField.setSelectionEnd(index);
          }
        }
        else {
          if (index == masktext.length()) {
            index--;
          }
          else {
            if (startIndex != endIndex) {
              for (int n = startIndex; n < endIndex; n++) {
                if (masktext.charAt(n) == ' ') {
                  text.setCharAt(n, e.getKeyChar());
                }
                else {
                  do {
                    n++;
                  } while (masktext.charAt(n) != ' ');
                  n--;
                }
                index++;
              }
              index--;
            }
            else {
              if (masktext.charAt(index) == ' ') {
                text.setCharAt(index, e.getKeyChar());
              }
              else {
                do {
                  index++;
                } while (masktext.charAt(index) != ' ');
              }
            }
          }
          {
            if (index != text.length()) {
              while (masktext.charAt(index) != ' ') {
                index--;
              }
              text.deleteCharAt(index);
            }
            textField.setText(text.toString());
            textField.setSelectionStart(index);
            textField.setSelectionEnd(index);
          }
        }
      }
      fireParentValidator();
    }

    @Override
    public void keyReleased(KeyEvent e) {
      fireParentValidator();
    }

  }

  public static void main(String[] args) {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new BTextField().getComponent());
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

}
