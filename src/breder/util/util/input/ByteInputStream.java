package breder.util.util.input;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/**
 * Array de bytes
 * 
 * 
 * @author Bernardo Breder
 */
public class ByteInputStream extends ByteArrayInputStream implements
  Serializable {

  /**
   * @param buf
   * @param offset
   * @param length
   */
  public ByteInputStream(byte[] buf, int offset, int length) {
    super(buf, offset, length);
  }

  /**
   * @param buf
   */
  public ByteInputStream(byte[] buf) {
    super(buf);
  }

  /**
   * @param input
   * @throws IOException
   */
  public ByteInputStream(InputStream input) throws IOException {
    this(InputStreamUtil.getBytes(input));
  }

}
