package breder.util.swing.richeditor;

import java.io.ByteArrayInputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.Text;
import org.dom4j.io.SAXReader;

/**
 * Classe utilitaria
 * 
 * 
 * @author bbreder
 */
public class HtmlUtil {

  /**
   * Filtra o html
   * 
   * @param html
   * @return html filtrado
   */
  public static String filter(String html) {
    SAXReader reader = new SAXReader();
    try {
      Document doc = reader.read(new ByteArrayInputStream(html.getBytes()));
      Element root = doc.getRootElement();
      filter(root);
      System.out.println(doc.getRootElement().asXML());
      return doc.getRootElement().asXML();
    }
    catch (DocumentException e) {
      return html;
    }
  }

  /**
   * Filtra o html
   * 
   * @param node
   */
  public static void filter(Element node) {
    int size = node.nodeCount();
    for (int n = 0; n < size; n++) {
      Node element = node.node(n);
      if (element instanceof Text) {
        Text text = (Text) element;
        if (text.getText().trim().length() == 0) {
          node.remove(element);
          n--;
          size = node.nodeCount();
        }
      }
      else {
        filter((Element) element);
      }
    }
  }

  /**
   * Testador
   * 
   * @param args
   */
  public static void main(String[] args) {
  }

}
