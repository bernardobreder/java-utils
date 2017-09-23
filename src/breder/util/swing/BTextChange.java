package breder.util.swing;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import breder.util.swing.validator.ChangeValidator;
import breder.util.task.RemoteTask;
import breder.util.task.Task;

public class BTextChange extends BComponent implements ITextField {

  private BTextField textField;

  private JButton button;

  private BParentValidator parentValidator = new BParentValidator();

  private ChangeValidator validation;

  private String old;

  public BTextChange(String old) {
    this.old = old;
    this.init();
  }

  @Override
  protected Component buildComponent() {
    JPanel panel = new JPanel(new GridBagLayout());
    {
      panel.add((this.textField = new BTextField(old, false)).getComponent(),
        new GBC(0, 0).horizontal());
      textField.addValidator(this.validation = new ChangeValidator(old));
      this.parentValidator.addValidable(textField);
    }
    {
      panel.add(this.button = new JButton("Save"), new GBC(1, 0));
      this.parentValidator.addComponent(this.button);
    }
    return panel;
  }

  @Override
  public boolean fireValidator() {
    return this.textField.fireValidator();
  }

  public void setTask(final Task task) {
    if (task instanceof RemoteTask) {
      RemoteTask remoteTask = (RemoteTask) task;
    }
    this.button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        task.start();
        //				task.join();
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            old = textField.getText();
            validation.setOldValue(old);
            parentValidator.fireValidator();
          }
        });
      }
    });
  }

  public String getText() {
    return textField.getText();
  }

  @Override
  public void setText(String text) {
    textField.setText(text);
  }

}
