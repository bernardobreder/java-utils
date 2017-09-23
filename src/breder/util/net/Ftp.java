package breder.util.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;

/**
 * Ftp
 * 
 * 
 * @author Bernardo Breder
 */
public class Ftp {

  /** Url */
  private URLConnection url;

  /**
   * Construtor
   * 
   * @param host por exemplo : breder.org
   * @param username por exemplo : breder
   * @param password
   * @param path
   * @throws IOException
   */
  public Ftp(String host, String username, String password, String path)
    throws IOException {
    this.url =
      new URL(MessageFormat.format("ftp://{0}:{1}@ftp.{2}/{3}", username,
        password, host, path)).openConnection();
  }

  /**
   * Recupera o output
   * 
   * @return output
   * @throws IOException
   */
  public OutputStream getOutputStream() throws IOException {
    return this.url.getOutputStream();
  }

  /**
   * Recupera o input
   * 
   * @return input
   * @throws IOException
   */
  public InputStream getInputStream() throws IOException {
    return this.url.getInputStream();
  }

}
