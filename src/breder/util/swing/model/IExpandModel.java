package breder.util.swing.model;

public interface IExpandModel extends IModel {

  public IExpandModel getExpandModel(int rowIndex, int columnIndex);

  public void fireExpandModel(IExpandModel model);

}
