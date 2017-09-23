package breder.util.util.base64;

import java.io.IOException;
import java.io.InputStream;

/**
 * Stream de Base64, converte bytes da inputstream para uma stream de Base64
 * 
 * 
 * @author bbreder
 */
public class Base64ToByteInputStream extends InputStream {

  /** Quantidade de bytes a ser lido */
  private static final int BYTE_COUNT = 4;
  /** Saida de stream */
  private final InputStream input;
  /** Estado */
  private final byte[] bytes;
  /** Estado */
  private final int[] outs;
  /** Indice de bytes */
  private int index;
  /** Indice de bytes */
  private int step;
  /** Indice de bytes */
  private int size;

  /**
   * @param input
   */
  public Base64ToByteInputStream(InputStream input) {
    this.input = input;
    this.bytes = new byte[BYTE_COUNT];
    this.outs = new int[3];
    if (bytes.length / 4 != (float) bytes.length / 4) {
      throw new IllegalStateException();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int read() throws IOException {
    if (this.step < 0) {
      return -1;
    }
    if (this.step == 0) {
      if (this.index == this.size) {
        size = this.input.read(bytes);
        this.index = 0;
      }
      int i0 = index >= size ? -1 : byte2int(bytes[index++]);
      if (i0 <= ' ') {
        return this.step = -1;
      }
      int i1 = index >= size ? -1 : byte2int(bytes[index++]);
      int i2 = index >= size ? 'A' : byte2int(bytes[index++]);
      int i3 = index >= size ? 'A' : byte2int(bytes[index++]);
      int b1 = map2[i1];
      int b2 = map2[i2];
      outs[0] = ((map2[i0]) << 2) | (b1 >>> 4);
      outs[1] = ((b1 & 0xf) << 4) | (b2 >>> 2);
      outs[2] = ((b2 & 3) << 6) | (map2[i3]);
    }
    int c = outs[this.step];
    if (c > 255) {
      return this.index = -1;
    }
    this.step = (this.step + 1) % 3;
    return c;
  }

  /** Mapping table from 6-bit nibbles to Base64 characters. **/
  private static final char[] map1 = new char[64];
  /** Mapping table from Base64 characters to 6-bit nibbles. */
  private static final int[] map2 = new int[128];

  /**
   * Inicializa as constantes
   */
  static {
    {
      int i = 0;
      for (char c = 'A'; c <= 'Z'; c++) {
        map1[i++] = c;
      }
      for (char c = 'a'; c <= 'z'; c++) {
        map1[i++] = c;
      }
      for (char c = '0'; c <= '9'; c++) {
        map1[i++] = c;
      }
      map1[i++] = '+';
      map1[i++] = '/';
    }
    {
      for (int i = 0; i < map2.length; i++) {
        map2[i] = -1;
      }
      for (int i = 0; i < 64; i++) {
        map2[map1[i]] = i;
      }
    }
  }

  /**
   * @param b
   * @return inteiro
   */
  public static int byte2int(byte b) {
    int n = b;
    return n < 0 ? 2 * Byte.MAX_VALUE + 2 + n : n;
  }

}
