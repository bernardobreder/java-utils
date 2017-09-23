package breder.util.util;

public class GenericConfigurator extends AbstractConfigurator {

  public GenericConfigurator(String prefix, String name) {
    super(prefix, name);
  }

  public String getFilename() {
    return this.get("breder.filename");
  }

  public void setFilename(String value) {
    this.set("breder.filename", value);
  }

}
