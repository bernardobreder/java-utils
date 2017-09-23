package breder.util.net.ws.shared;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import breder.util.util.input.InputStreamUtil;

/**
 * InputStream aceito pelo webservice
 * 
 * 
 * @author Bernardo Breder
 */
public class WSInputStream implements Serializable {

  /** Bytes */
  private byte[] bytes;

  /**
   * Construtor
   * 
   * @param input
   * @throws IOException
   */
  public WSInputStream(InputStream input) throws IOException {
    this.bytes = InputStreamUtil.getBytes(input);
  }

  /**
   * Retorna
   * 
   * @return bytes
   */
  public byte[] getBytes() {
    return bytes;
  }

}
