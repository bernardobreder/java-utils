package breder.util.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import breder.util.swing.model.IObjectModel;
import breder.util.swing.table.BFilterTable;
import breder.util.swing.table.IOpenCellListener;

public class BTextChoice<E> extends BComponent {

  private IObjectModel<E> model;

  private BTextField textField;

  private JButton button;

  private final List<E> itens = new ArrayList<E>();

  private int selectionMode;

  private List<Runnable> listeners;

  public BTextChoice(IObjectModel<E> model) {
    this.model = model;
    this.init();
    this.setMultiSelectionMode(false);
  }

  @Override
  protected Component buildComponent() {
    JPanel panel = new JPanel(new BorderLayout());
    {
      panel.add((textField = new BTextField()).getComponent(),
        BorderLayout.CENTER);
      this.textField.getTextField().setEnabled(false);
    }
    {
      panel.add(button = new JButton("..."), BorderLayout.EAST);
      this.button.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
          button.setEnabled(false);
          setSelect(null);
          fireSelectedListener();
          BFrame parent =
            (BFrame) SwingUtilities.getRoot(BTextChoice.this.getComponent());
          new ButtonAction(parent).setVisible(true);
        }

      });
    }
    return panel;
  }

  public void addValidator(Validator validator) {
    textField.addValidator(validator);
  }

  public boolean fireValidator() {
    return textField.fireValidator();
  }

  public BTextField getTextField() {
    return textField;
  }

  public String getText() {
    StringBuilder sb = new StringBuilder();
    for (int n = 0; n < this.itens.size(); n++) {
      E item = this.itens.get(n);
      sb.append(item.toString());
      if (n != this.itens.size() - 1) {
        sb.append(", ");
      }
    }
    return sb.toString();
  }

  public void setEnable(boolean b) {
    this.button.setEnabled(b);
  }

  public E getItem() {
    if (this.itens.size() == 0) {
      return null;
    }
    else {
      return this.itens.get(0);
    }
  }

  public void setSelect(E element) {
    this.itens.clear();
    if (element != null) {
      this.itens.add(element);
    }
    this.textField.setText(this.getText());
  }

  public List<E> getItems() {
    return Collections.unmodifiableList(itens);
  }

  protected List<Runnable> getListeners() {
    if (this.listeners == null) {
      this.listeners = new ArrayList<Runnable>();
    }
    return listeners;
  }

  public void addListener(Runnable run) {
    this.getListeners().add(run);
  }

  public boolean hasListener() {
    return listeners != null && listeners.size() > 0;
  }

  public void setMultiSelectionMode(boolean flag) {
    if (flag) {
      this.selectionMode = ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
    }
    else {
      this.selectionMode = ListSelectionModel.SINGLE_SELECTION;
    }
  }

  public void fireSelectedListener() {
    if (this.hasListener()) {
      for (Runnable run : this.getListeners()) {
        run.run();
      }
    }
  }

  private class ButtonAction extends BFrame {

    private BFilterTable<E> table;

    private final BParentValidator validator = new BParentValidator();

    public ButtonAction(BFrame parent) {
      super(parent);
      this.build();
      this.pack();
      this.setSize(640, this.getHeight());
      this.setLocationRelativeTo(null);
      BFrameUtil.showAsModal(this, parent);
    }

    private void build() {
      this.setLayout(new BorderLayout());
      this.add(this.buildTable(), BorderLayout.CENTER);
      this.add(this.buildButton(), BorderLayout.SOUTH);
    }

    private Component buildTable() {
      this.table = new BFilterTable<E>(model);
      this.table.getTable().getTable().setSelectionMode(selectionMode);
      this.table.getTable().getTable().getSelectionModel()
        .addListSelectionListener(new ListSelectionListener() {

          @Override
          public void valueChanged(ListSelectionEvent e) {
            validator.fireValidator();
          }
        });
      this.table.addOpenCellListener(new IOpenCellListener<E>() {

        @Override
        public void actionPerformed(int row, E cell) {
          table.getTable().getTable().getSelectionModel().clearSelection();
          table.getTable().getTable().getSelectionModel().setSelectionInterval(
            row, row);
          onSelectAction();
        }

        @Override
        public JPopupMenu getPopupMenu(int row, E cell) {
          return null;
        }
      });
      this.validator.addValidable(this.table);
      return this.table.getComponent();
    }

    private Component buildButton() {
      JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      {
        JButton button = new JButton("Selecionar");
        this.validator.addComponent(button);
        this.getRootPane().setDefaultButton(button);
        button.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
            onSelectAction();
          }
        });
        panel.add(button);
      }
      {
        JButton button = new JButton("Fechar");
        button.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
            onCloseAction();
          }
        });
        panel.add(button);
      }
      return panel;
    }

    @Override
    public void close() {
      button.setEnabled(true);
      fireValidator();
      fireParentValidator();
      super.close();
    }

    protected void onSelectAction() {
      int[] rows = table.getTable().getTable().getSelectedRows();
      if (rows.length == 0) {
        rows = new int[] { 0 };
      }
      itens.clear();
      for (int n = 0; n < rows.length; n++) {
        E item = this.table.getModel().getRow(rows[n]);
        itens.add(item);
      }
      textField.setText(getText());
      this.close();
      fireSelectedListener();
      fireValidator();
      fireParentValidator();
    }

    protected void onCloseAction() {
      this.close();
    }

  }

}
