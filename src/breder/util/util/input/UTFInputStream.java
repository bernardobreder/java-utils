package breder.util.util.input;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/**
 * Array de bytes
 * 
 * 
 * @author Bernardo Breder
 */
public class UTFInputStream extends InputStream implements Serializable {

  /** Stream */
  private final InputStream input;

  /**
   * @param input
   */
  public UTFInputStream(InputStream input) {
    this.input = input;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int read() throws IOException {
    return read(this);
  }

  /**
   * @param input
   * @return character utf8
   * @throws IOException
   */
  public static int read(InputStream input) throws IOException {
    int c = input.read();
    if (c <= 0x7F) {
      return c;
    }
    else if ((c >> 5) == 0x6) {
      int i2 = input.read();
      return ((c & 0x1F) << 6) + (i2 & 0x3F);
    }
    else {
      int i2 = input.read();
      int i3 = input.read();
      return ((c & 0xF) << 12) + ((i2 & 0x3F) << 6) + (i3 & 0x3F);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int available() throws IOException {
    return this.input.available();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void close() throws IOException {
    this.input.close();
  }

}
