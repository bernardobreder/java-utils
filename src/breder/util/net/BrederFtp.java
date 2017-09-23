package breder.util.net;

import java.io.IOException;

import breder.util.swing.BOptionPane;

/**
 * Ftp do breder.org
 * 
 * 
 * @author Bernardo Breder
 */
public class BrederFtp extends Ftp {

  /**
   * Construtor
   * 
   * @param password
   * @param path webapps/pub/abc.jpg
   * @throws IOException
   */
  public BrederFtp(String password, String path) throws IOException {
    super("breder.org", "breder", password, "../../" + path);
  }

  /**
   * Construtor
   * 
   * @param path webapps/pub/abc.jpg
   * @throws IOException
   */
  public BrederFtp(String path) throws IOException {
    this(new String(BOptionPane.showPasswordDialog(null, "Passowrd",
      "Password of breder.org")), path);
  }

}
