package breder.util.swing.validator;

import breder.util.swing.Validator;
import breder.util.swing.table.BTable;

public class TableSelectedValidator implements Validator {

  private final BTable<?> table;

  public TableSelectedValidator(BTable<?> table) {
    super();
    this.table = table;
  }

  @Override
  public boolean valid(String text) {
    return this.table.getSelectedRow() != null;
  }

}
