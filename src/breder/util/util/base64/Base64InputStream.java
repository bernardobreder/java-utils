package breder.util.util.base64;

import java.io.IOException;
import java.io.InputStream;

/**
 * Realiza uma leitura de uma stream em base64 e retorna os próprios bytes da
 * base64. Caso chegue num caracter inválido, será finalizado a stream.
 * 
 * @author Tecgraf
 */
public class Base64InputStream extends InputStream {

  /** Input Stream */
  private InputStream input;
  /** Finaliza a stream */
  private boolean quit;

  /**
   * @param input
   */
  public Base64InputStream(InputStream input) {
    this.input = input;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int read() throws IOException {
    if (this.quit) {
      return -1;
    }
    int c = this.input.read();
    if (c <= ' '
      || !((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')
        || (c >= '0' && c <= '9') || c == '+' || c == '/' || c == '=')) {
      this.quit = true;
      return -1;
    }
    return c;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void close() throws IOException {
    this.input.close();
  }

}
