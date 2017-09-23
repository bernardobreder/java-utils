package breder.util.util.input;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * Array de bytes
 * 
 * 
 * @author Bernardo Breder
 */
public class UTFReaderStream extends Reader {

  /** Stream */
  private final InputStream input;

  /**
   * @param content
   */
  public UTFReaderStream(InputStream input) {
    this.input = input;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int read() throws IOException {
    int c = this.input.read();
    if (c <= 0x7F) {
      return c;
    }
    else if ((c >> 5) == 0x6) {
      int i2 = this.input.read();
      return ((c & 0x1F) << 6) + (i2 & 0x3F);
    }
    else {
      int i2 = this.input.read();
      int i3 = this.input.read();
      return ((c & 0xF) << 12) + ((i2 & 0x3F) << 6) + (i3 & 0x3F);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void close() throws IOException {
    this.input.close();
  }

  @Override
  public int read(char[] cbuf, int off, int len) throws IOException {
    for (int n = 0; n < len; n++) {
      int c = this.read();
      if (c < 0) {
        return n;
      }
      cbuf[n] = (char) c;
    }
    return len;
  }

}
