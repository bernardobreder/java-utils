package breder.util.util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

/**
 * Utilitario
 * 
 * 
 * @author Bernardo Breder
 */
public class ClipboardUtil {

  /**
   * Copia um texto
   * 
   * @param text
   */
  public static void copy(String text) {
    StringSelection stringSelection = new StringSelection(text);
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    clipboard.setContents(stringSelection, null);
  }

  /**
   * Copia um texto
   * 
   * @return conteudo
   */
  public static String paste() {
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    Transferable contents = clipboard.getContents(null);
    try {
      if (contents != null) {
        Object data = contents.getTransferData(DataFlavor.stringFlavor);
        if (data instanceof String) {
          return (String) data;
        }
        else {
          return data.toString();
        }
      }
    }
    catch (Exception e) {
    }
    return null;
  }

  /**
   * Testador
   * 
   * @param args
   */
  public static void main(String[] args) {
    copy("Teste");
    System.out.println(paste());
  }

}
