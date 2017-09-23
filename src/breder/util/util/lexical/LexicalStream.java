package breder.util.util.lexical;

import java.io.IOException;
import java.io.InputStream;

/**
 * Retorna a stream de lexical
 * 
 * 
 * @author Bernardo Breder
 */
public class LexicalStream {

  /** InputStream */
  private InputStream input;

  /**
   * Construtor
   * 
   * @param input
   * @throws LexicalException
   * @throws IOException
   */
  public LexicalStream(InputStream input) throws LexicalException, IOException {
    this.input = input;
  }

  /**
   * Retorna o proximo token
   * 
   * @return token
   */
  public LexicalToken next() {
    return null;
  }

  /**
   * Retorna o proximo token
   * 
   * @return token
   */
  public LexicalToken get() {
    return null;
  }

  /**
   * Retorna o proximo token
   * 
   * @return token
   */
  public LexicalToken back() {
    return null;
  }

  /**
   * Retorna o proximo token
   * 
   * @return token
   */
  public LexicalToken push() {
    return null;
  }

  /**
   * Retorna o proximo token
   * 
   * @return token
   */
  public LexicalToken pop() {
    return null;
  }

}
