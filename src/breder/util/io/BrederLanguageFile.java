package breder.util.io;

import java.io.File;

import breder.util.util.FileUtil;

public class BrederLanguageFile extends File {

  public BrederLanguageFile() {
    super(new HomeFile(), "blng");
  }

  public BrederLanguageFile(String... paths) {
    super(FileUtil.build(new File(new HomeFile(), "blng"), paths).toString());
  }

}
