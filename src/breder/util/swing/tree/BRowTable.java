package breder.util.swing.tree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.plaf.metal.MetalBorders.TableHeaderBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import breder.util.swing.model.IObjectModel;
import breder.util.swing.model.StaticObjectModel;
import breder.util.swing.table.BTable;

public class BRowTable<E> extends BTable<E[]> {

  private int rowOver, columnOver;

  public BRowTable(IObjectModel<E[]> model) {
    super(model);
    this.confTable();
    this.confRow(model);
    this.confColumn();
    this.confScroll();
    this.confCorner();
    this.confCell();
    this.confMouse();
  }

  public void pack() {
    int height = 0;
    {
      height += this.getInsets().top + this.getInsets().bottom;
      height += this.getTable().getTableHeader().getPreferredSize().getHeight();
      height += this.getTable().getPreferredSize().getHeight();
      height += this.getHorizontalScrollBar().getPreferredSize().getHeight();
    }
    int width = 0;
    {
      width += this.getInsets().left + this.getInsets().right;
      width += this.getRowHeader().getPreferredSize().getWidth();
      width += this.getTable().getPreferredSize().getWidth();
      width += this.getVerticalScrollBar().getPreferredSize().getWidth();
    }
    this.setPreferredSize(new Dimension(width, height));
  }

  private void confTable() {
    this.getTable().setCellSelectionEnabled(true);
  }

  private void confColumn() {
    final JTableHeader header = this.getTable().getTableHeader();
    header.setDefaultRenderer(new HeaderCellRenderer());
  }

  private void confRow(ListModel model) {
    JList list = new JList(new RowModel());
    list.setCellRenderer(new HeaderCellRenderer());
    list.setFixedCellHeight(this.getTable().getRowHeight());
    this.setRowHeaderView(list);
  }

  private void confScroll() {
    this.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    this
      .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    this
      .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
  }

  private void confMouse() {
    Mouse mouse = new Mouse();
    this.getTable().addMouseListener(mouse);
    this.getTable().addMouseMotionListener(mouse);
  }

  private void confCell() {
    this.getTable().setDefaultRenderer(Object.class, new CellRenderer());
    this.getTable().setGridColor(Color.BLACK);
  }

  private void confCorner() {
    JLabel label = new JLabel();
    label.setOpaque(true);
    label.setBackground(Color.LIGHT_GRAY);
    label.setBorder(new TableHeaderBorder());
    this.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, label);
  }

  private class RowModel extends StaticObjectModel<String> {

    @Override
    public String getRow(int index) {
      return getModel().getValueAt(index, -1).toString();
    }

    @Override
    public int getSize() {
      return getModel().getSize();
    }

  }

  private class Mouse extends MouseAdapter {

    public Mouse() {
      rowOver = -2;
      columnOver = -2;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
      rowOver = e.getY() / getTable().getRowHeight();
      columnOver =
        e.getX() / getTable().getColumnModel().getColumn(0).getWidth();
      repaint();
      getTable().getTableHeader().repaint();
      getRowHeader().repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
      rowOver = -2;
      columnOver = -2;
      repaint();
      getTable().getTableHeader().repaint();
      getRowHeader().repaint();
    }

  }

  private class CellRenderer implements TableCellRenderer {

    private JLabel label = new JLabel();

    public CellRenderer() {
      this.label.setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {
      this.label.setText(value.toString());
      if (isSelected) {
        this.label.setBackground(new Color(225, 225, 225));
      }
      else {
        this.label.setBackground(Color.WHITE);
      }
      if (row == rowOver && column == columnOver) {
        this.label.setForeground(Color.BLUE);
      }
      else {
        this.label.setForeground(Color.BLACK);
      }
      return this.label;
    }

  }

  private class HeaderCellRenderer implements TableCellRenderer,
    ListCellRenderer {

    private JLabel label = new JLabel("", JLabel.CENTER);

    public HeaderCellRenderer() {
      this.label.setOpaque(true);
      this.label.setBackground(Color.LIGHT_GRAY);
      this.label.setPreferredSize(new Dimension(100, 20));
      this.label.setBorder(new TableHeaderBorder());
    }

    public Component getCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {
      this.label.setText(value.toString());
      if (row == rowOver || column == columnOver) {
        this.label.setForeground(Color.BLUE);
      }
      else {
        this.label.setForeground(Color.BLACK);
      }
      return this.label;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {
      return this.getCellRendererComponent(table, value, isSelected, hasFocus,
        row, column);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
      int index, boolean isSelected, boolean cellHasFocus) {
      return this.getCellRendererComponent(getTable(), value, isSelected,
        cellHasFocus, index, -1);
    }

  }
}
