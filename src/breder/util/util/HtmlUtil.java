package breder.util.util;

/**
 * Representa um codificador que transforma texto comum em texto que possa ser
 * utilizado em uma p�gina html.
 * 
 * Transforma caracteres inv�lidos para seus respectivos c�digos html e
 * transforma tamb�m \n em &lt;br&gt;.
 * 
 */
public class HtmlUtil {

  /**
   * Array que mapeia um caracter normal a sua vers�o codificada para html. Esse
   * mapeamento � feito utilizando o inteiro que representa o caracter como
   * �ndice do array. O valor encontrado neste �ndice � o caracter codificado.
   */
  private static final String[] ASCII_TO_HTML;

  /**
   * Template utilizado na cria��o de um documento html. Documentos html s�o
   * formados pela tag &lt;html&gt; contendo texto codificado em html.
   */
  private static final String HTML_DOC_TEMPLATE = "<html>%s</html>";

  static {

    ASCII_TO_HTML = new String[Character.MAX_VALUE];
    for (int inx = 0; inx < ASCII_TO_HTML.length; inx++) {
      ASCII_TO_HTML[inx] = String.valueOf((char) inx);
    }

    ASCII_TO_HTML['\n'] = "<br>";

    ASCII_TO_HTML[' '] = "&nbsp;";
    ASCII_TO_HTML['\"'] = "&quot;";
    ASCII_TO_HTML['\''] = "&apos;"; // (n�o funciona no IE) &#39;
    ASCII_TO_HTML['&'] = "&amp;";
    ASCII_TO_HTML['<'] = "&lt;";
    ASCII_TO_HTML['>'] = "&gt;";

    ASCII_TO_HTML['�'] = "&iexcl;";
    ASCII_TO_HTML['�'] = "&cent;";
    ASCII_TO_HTML['�'] = "&pound;";
    ASCII_TO_HTML['�'] = "&curren;";
    ASCII_TO_HTML['�'] = "&yen;";
    ASCII_TO_HTML['�'] = "&brvbar;";
    ASCII_TO_HTML['�'] = "&sect;";
    ASCII_TO_HTML['�'] = "&uml;";
    ASCII_TO_HTML['�'] = "&copy;";
    ASCII_TO_HTML['�'] = "&ordf;";
    ASCII_TO_HTML['�'] = "&laquo;";
    ASCII_TO_HTML['�'] = "&not;";
    ASCII_TO_HTML['�'] = "&reg;";
    ASCII_TO_HTML['�'] = "&macr;";
    ASCII_TO_HTML['�'] = "&deg;";
    ASCII_TO_HTML['�'] = "&plusmn;";
    ASCII_TO_HTML['�'] = "&sup2;";
    ASCII_TO_HTML['�'] = "&sup3;";
    ASCII_TO_HTML['�'] = "&acute;";
    ASCII_TO_HTML['�'] = "&micro;";
    ASCII_TO_HTML['�'] = "&para;";
    ASCII_TO_HTML['�'] = "&middot;";
    ASCII_TO_HTML['�'] = "&cedil;";
    ASCII_TO_HTML['�'] = "&sup1;";
    ASCII_TO_HTML['�'] = "&ordm;";
    ASCII_TO_HTML['�'] = "&raquo;";
    ASCII_TO_HTML['�'] = "&frac14;";
    ASCII_TO_HTML['�'] = "&frac12;";
    ASCII_TO_HTML['�'] = "&frac34;";
    ASCII_TO_HTML['�'] = "&iquest;";
    ASCII_TO_HTML['�'] = "&times;";
    ASCII_TO_HTML['�'] = "&divide;";

    ASCII_TO_HTML['�'] = "&Agrave;";
    ASCII_TO_HTML['�'] = "&Aacute;";
    ASCII_TO_HTML['�'] = "&Acirc;";
    ASCII_TO_HTML['�'] = "&Atilde;";
    ASCII_TO_HTML['�'] = "&Auml;";
    ASCII_TO_HTML['�'] = "&Aring;";
    ASCII_TO_HTML['�'] = "&AElig;";
    ASCII_TO_HTML['�'] = "&Ccedil;";
    ASCII_TO_HTML['�'] = "&Egrave;";
    ASCII_TO_HTML['�'] = "&Eacute;";
    ASCII_TO_HTML['�'] = "&Ecirc;";
    ASCII_TO_HTML['�'] = "&Euml;";
    ASCII_TO_HTML['�'] = "&Igrave;";
    ASCII_TO_HTML['�'] = "&Iacute;";
    ASCII_TO_HTML['�'] = "&Icirc;";
    ASCII_TO_HTML['�'] = "&Iuml;";
    ASCII_TO_HTML['�'] = "&ETH;";
    ASCII_TO_HTML['�'] = "&Ntilde;";
    ASCII_TO_HTML['�'] = "&Ograve;";
    ASCII_TO_HTML['�'] = "&Oacute;";
    ASCII_TO_HTML['�'] = "&Ocirc;";
    ASCII_TO_HTML['�'] = "&Otilde;";
    ASCII_TO_HTML['�'] = "&Ouml;";
    ASCII_TO_HTML['�'] = "&Oslash;";
    ASCII_TO_HTML['�'] = "&Ugrave;";
    ASCII_TO_HTML['�'] = "&Uacute;";
    ASCII_TO_HTML['�'] = "&Ucirc;";
    ASCII_TO_HTML['�'] = "&Uuml;";
    ASCII_TO_HTML['�'] = "&Yacute;";
    ASCII_TO_HTML['�'] = "&THORN;";
    ASCII_TO_HTML['�'] = "&szlig;";
    ASCII_TO_HTML['�'] = "&agrave;";
    ASCII_TO_HTML['�'] = "&aacute;";
    ASCII_TO_HTML['�'] = "&acirc;";
    ASCII_TO_HTML['�'] = "&atilde;";
    ASCII_TO_HTML['�'] = "&auml;";
    ASCII_TO_HTML['�'] = "&aring;";
    ASCII_TO_HTML['�'] = "&aelig;";
    ASCII_TO_HTML['�'] = "&ccedil;";
    ASCII_TO_HTML['�'] = "&egrave;";
    ASCII_TO_HTML['�'] = "&eacute;";
    ASCII_TO_HTML['�'] = "&ecirc;";
    ASCII_TO_HTML['�'] = "&euml;";
    ASCII_TO_HTML['�'] = "&igrave;";
    ASCII_TO_HTML['�'] = "&iacute;";
    ASCII_TO_HTML['�'] = "&icirc;";
    ASCII_TO_HTML['�'] = "&iuml;";
    ASCII_TO_HTML['�'] = "&eth;";
    ASCII_TO_HTML['�'] = "&ntilde;";
    ASCII_TO_HTML['�'] = "&ograve;";
    ASCII_TO_HTML['�'] = "&oacute;";
    ASCII_TO_HTML['�'] = "&ocirc;";
    ASCII_TO_HTML['�'] = "&otilde;";
    ASCII_TO_HTML['�'] = "&ouml;";
    ASCII_TO_HTML['�'] = "&oslash;";
    ASCII_TO_HTML['�'] = "&ugrave;";
    ASCII_TO_HTML['�'] = "&uacute;";
    ASCII_TO_HTML['�'] = "&ucirc;";
    ASCII_TO_HTML['�'] = "&uuml;";
    ASCII_TO_HTML['�'] = "&yacute;";
    ASCII_TO_HTML['�'] = "&thorn;";
    ASCII_TO_HTML['�'] = "&yuml;";
  }

  /**
   * Codifica o texto dado para o formato html.
   * 
   * @param text Texto a ser codificado para html.
   * @return o texto codificado ou <code>null</code> caso este tenha sido o
   *         valor do par�metro texto passado.
   */
  public static final String encode(final String text) {

    if (null == text) {
      return null;
    }

    StringBuilder sb = new StringBuilder(text.length());

    char[] characters = text.toCharArray();
    for (char aChar : characters) {
      sb.append(ASCII_TO_HTML[aChar]);
    }

    return sb.toString();
  }

  /**
   * Codifica o texto como um documento html. Um documento html � composto por
   * um texto codificado para html envolto pela tag &lt;html&gt;.
   * 
   * @param text Texto a ser codificado para html que ficar� envolto pela tag
   *        &lt;html&gt;.
   * @return um documento html contendo o texto passado ou <code>null</code>
   *         caso este tenha sido o valor do par�metro texto passado.<br>
   */
  public static final String encodeDoc(final String text) {
    String encodedText = encode(text);
    if (null == encodedText) {
      return null;
    }
    else {
      return String.format(HTML_DOC_TEMPLATE, encodedText);
    }
  }
}
