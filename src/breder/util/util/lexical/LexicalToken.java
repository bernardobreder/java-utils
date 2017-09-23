package breder.util.util.lexical;

/**
 * Token
 * 
 * 
 * @author Bernardo Breder
 */
public class LexicalToken {

  /** Linha */
  private final int line;
  /** Coluna */
  private final int column;
  /** Texto */
  private final String text;

  /**
   * Construtor
   * 
   * @param line
   * @param column
   * @param text
   */
  public LexicalToken(int line, int column, String text) {
    super();
    this.line = line;
    this.column = column;
    this.text = text;
  }

  /**
   * Retorna
   * 
   * @return line
   */
  public int getLine() {
    return line;
  }

  /**
   * Retorna
   * 
   * @return column
   */
  public int getColumn() {
    return column;
  }

  /**
   * Retorna
   * 
   * @return text
   */
  public String getText() {
    return text;
  }

}
