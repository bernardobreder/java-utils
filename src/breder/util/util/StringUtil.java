package breder.util.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilitário para String
 * 
 * 
 * @author Bernardo Breder
 */
public class StringUtil {

  /**
   * Trim do conteúdo
   * 
   * @param content
   * @return trim
   */
  public static String trim(String content) {
    int index = content.indexOf("  ");
    if (index == -1) {
      return content;
    }
    StringBuilder sb = new StringBuilder(content);
    sb.replace(index, index + 2, " ");
    return trim(sb.toString());
  }

  /**
   * Realiza um parse no texto capturando os emails encontrado.
   * 
   * @param emails
   * @return emails
   */
  public static List<String> trimEmails(String emails) {
    if (emails == null) {
      return new ArrayList<String>(0);
    }
    List<String> list = new ArrayList<String>();
    int index = emails.indexOf('@');
    while (index >= 0) {
      int begin = index - 1;
      for (;;) {
        if (begin <= 0) {
          break;
        }
        char c = emails.charAt(begin);
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '-'
          || c == '_' || c == '.') {
          begin--;
          continue;
        }
        begin++;
        break;
      }
      int end = index + 1;
      int length = emails.length();
      for (;;) {
        if (end >= length) {
          break;
        }
        char c = emails.charAt(end);
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '-'
          || c == '_' || c == '.') {
          end++;
          continue;
        }
        break;
      }
      list.add(emails.substring(begin, end));
      index = emails.indexOf('@', end);
    }
    return list;
  }

  /**
   * Divide
   * 
   * @param content
   * @return split
   */
  public static String[] split(String content) {
    List<String> list = new ArrayList<String>();
    char[] chars = trim(content).toCharArray();
    for (int b = 0, n = 0; n < chars.length; n++) {
      char c = chars[n];
      if (c > 32) {
        char sc = c;
        if (c == '\'' || c == '\"') {
          b = ++n;
          for (; n < chars.length; n++) {
            if (chars[n] == sc) {
              break;
            }
          }
          list.add(content.substring(b, n));
          continue;
        }
        else if (Character.isLetter(c)) {
          b = n;
          for (; n < chars.length; n++) {
            if (!Character.isLetter(chars[n])) {
              break;
            }
          }
          list.add(content.substring(b, n--));
          continue;
        }
        else if (Character.isDigit(c)) {
          b = n;
          for (; n < chars.length; n++) {
            if (!Character.isDigit(chars[n])) {
              break;
            }
          }
          list.add(content.substring(b, n--));
          continue;
        }
        list.add(content.substring(n, n + 1));
      }
    }
    return list.toArray(new String[list.size()]);
  }

  /**
   * Contain
   * 
   * @param splits
   * @param strings
   * @return flag
   */
  public static boolean contain(String[] splits, String... strings) {
    if (strings.length == 0) {
      return false;
    }
    for (int n = 0; n < splits.length; n++) {
      String split = splits[n];
      if (strings[0].equals(split)) {
        if (n + strings.length > splits.length) {
          return false;
        }
        boolean found = true;
        for (int m = 1; m < strings.length; m++) {
          String string = strings[m];
          if (!string.equals(splits[n + m])) {
            found = false;
            break;
          }
          if (found) {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * Aplica o Inner
   * 
   * @param name
   * @return inner
   */
  public static String toInnerCase(String name) {
    char[] chars = name.toLowerCase().toCharArray();
    boolean space = true;
    for (int n = 0; n < chars.length; n++) {
      char c = chars[n];
      if (c == ' ') {
        space = true;
      }
      else if (space) {
        chars[n] = Character.toUpperCase(c);
        space = false;
      }
    }
    return new String(chars);
  }

  /**
   * Junta linhas
   * 
   * @param lines
   * @return join
   */
  public static String joinLine(List<String> lines) {
    return joinLine(lines.toArray(new String[lines.size()]));
  }

  /**
   * Junta linhas
   * 
   * @param lines
   * @return join
   */
  public static String joinLine(String[] lines) {
    StringBuilder sb = new StringBuilder();
    for (int n = 0; n < lines.length; n++) {
      sb.append(lines[n]);
      if (n != lines.length - 1) {
        sb.append("\n");
      }
    }
    return sb.toString();
  }

  /**
   * Remove espaço
   * 
   * @param string
   * @return string sem espaço
   */
  public static String noSpace(String string) {
    StringBuilder sb = new StringBuilder(string);
    for (int n = sb.length() - 1; n >= 0; n--) {
      if (Character.isWhitespace(sb.charAt(n))) {
        sb.deleteCharAt(n);
      }
    }
    return sb.toString();
  }

  /**
   * Limpa o texto
   * 
   * @param text
   * @return texto limpo
   */
  public static String clean(String text) {
    StringBuilder sb = new StringBuilder(text.replace("_", " "));
    {
      char[] befor =
        new char[] { 'á', 'à', 'â', 'ã', 'é', 'è', 'ê', 'í', 'ì', 'î', 'ó',
            'ò', 'ô', 'õ', 'ú', 'ù', 'û' };
      char[] after =
        new char[] { 'a', 'a', 'a', 'a', 'e', 'e', 'e', 'i', 'i', 'i', 'o',
            'o', 'o', 'o', 'u', 'u', 'u' };
      for (int n = 0; n < befor.length; n++) {
        String beforStr = "" + befor[n];
        int index = sb.indexOf(beforStr);
        while (index >= 0) {
          sb.setCharAt(index, after[n]);
          index = sb.indexOf(beforStr);
        }
        beforStr = beforStr.toUpperCase();
        index = sb.indexOf(beforStr);
        while (index >= 0) {
          sb.setCharAt(index, after[n]);
          index = sb.indexOf(beforStr);
        }
      }
    }
    {
      for (int n = 0; n < sb.length(); n++) {
        char charAt = sb.charAt(n);
        if (!Character.isLetter(charAt) && charAt != ' ' && charAt != '-'
          && charAt != '&') {
          sb.deleteCharAt(n--);
        }
      }
    }
    {
      int index = sb.indexOf("(");
      if (index >= 0) {
        int end = sb.indexOf(")", index);
        if (end >= 0) {
          sb.delete(index, end + 1);
        }
        else {
          sb.delete(index, sb.length());
        }
      }
    }
    {
      int indexOf = sb.indexOf("  ");
      while (indexOf >= 0) {
        sb.replace(indexOf, indexOf + 2, " ");
        indexOf = sb.indexOf("  ");
      }
    }
    return sb.toString().trim();
  }

  /**
   * Transforma o texto em Html
   * 
   * @param text
   * @return html
   */
  public static String text2html(String text) {
    text = text.replace("\n", "<br/>");
    text =
      text.replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
    return "<html>" + text + "</html>";
  }

  /**
   * Compila um código fonte
   * 
   * @param source
   * @return código
   */
  public static String text2div(String source) {
    source = source.trim();
    StringBuilder sb = new StringBuilder();
    String[] lines = source.split("\n");
    for (String line : lines) {
      line = text2divLine(line);
      sb.append(line);
    }
    return sb.toString();
  }

  /**
   * Compila a linha
   * 
   * @param line
   * @return linha
   */
  private static String text2divLine(String line) {
    String[] hFlag = new String[] { "h1", "h2", "h3", "h4", "h5", "h6", "hc" };
    boolean hFlagFound = false;
    int hFlagIndex = 4;
    boolean boldFlag = false;
    boolean italicFlag = false;
    line = line.trim();
    if (line.length() == 0) {
      return "<div class='text-empty'>&nbsp;</div>";
    }
    StringBuilder sb = new StringBuilder();
    for (int n = 0; n < hFlag.length; n++) {
      if (line.startsWith(hFlag[n] + ".")) {
        hFlagFound = true;
        hFlagIndex = n;
        sb.append("<div class='text-h" + (n + 1) + "'>");
        line = line.substring(hFlag[n].length() + 1).trim();
        break;
      }
    }
    if (!hFlagFound) {
      sb.append("<div class='text-" + hFlag[hFlagIndex] + "'>");
    }
    for (int n = 0; n < line.length(); n++) {
      char c = line.charAt(n);
      if (c == '*') {
        if (!boldFlag) {
          if (line.indexOf('*', n + 1) >= 0) {
            sb.append("<span class='text-bold'>");
          }
        }
        else {
          sb.append("</span>");
        }
        boldFlag = !boldFlag;
      }
      else if (c == '_') {
        if (!italicFlag) {
          if (line.indexOf('_', n + 1) >= 0) {
            sb.append("<span class='text-italic'>");
          }
        }
        else {
          sb.append("</span>");
        }
        italicFlag = !italicFlag;
      }
      else {
        sb.append(c);
      }
    }
    if (hFlagFound) {
      for (int n = 0; n < hFlag.length; n++) {
        if (n == hFlagIndex) {
          sb.append("</div>");
        }
      }
    }
    else {
      sb.append("</div>");
    }
    return sb.toString();
  }

  /**
   * Validador de email
   * 
   * @param email
   * @return valida o email
   */
  public static boolean isEmail(String email) {
    String regex =
      "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,4})$";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(email);
    return matcher.matches();
  }

  /**
   * Validador de email
   * 
   * @param email
   * @return valida o email
   */
  public static boolean isName(String email) {
    String regex = "^[A-Za-z0-9]+$";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(email);
    return matcher.matches();
  }

  /**
   * Validador de email
   * 
   * @param email
   * @return valida o email
   */
  public static boolean isNames(String email) {
    String regex = "^[A-Za-z0-9]+( [A-Za-z0-9]+)*$";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(email);
    return matcher.matches();
  }

  /**
   * Testador
   * 
   * @param args
   */
  public static void main(String[] args) {
  }

}