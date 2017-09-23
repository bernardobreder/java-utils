package breder.util.util;

import java.awt.Desktop;
import java.net.URL;

/**
 * Carrega o browser do sistema operacional
 * 
 * 
 * @author bbreder
 */
public class SOBrowser {

  /**
   * Carrega uma pagina
   * 
   * @param url
   */
  public static void open(String url) {
    url = url.trim();
    if (url.length() == 0) {
      return;
    }
    if (!url.startsWith("http://")) {
      if (!url.startsWith("www.")) {
        url = "www." + url;
      }
      url = "http://" + url;
    }
    try {
      Desktop.getDesktop().browse(new URL(url).toURI());
    }
    catch (Exception e) {
    }
  }

}
