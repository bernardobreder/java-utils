package breder.util.util.base64;

import java.io.IOException;
import java.io.InputStream;

/**
 * Stream de Base64, converte bytes da inputstream para uma stream de Base64
 * 
 * 
 * @author bbreder
 */
public class ByteToBase64InputStream extends InputStream {

  /** Quantidade de bytes a ser lido */
  private static final int BYTE_COUNT = 1023;
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
  public ByteToBase64InputStream(InputStream input) {
    this.input = input;
    this.bytes = new byte[BYTE_COUNT];
    this.outs = new int[4];
    if (bytes.length / 3 != (float) bytes.length / 3) {
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
      if (i0 < 0) {
        return this.step = -1;
      }
      int i1 = index >= size ? -1 : byte2int(bytes[index++]);
      int p1 = i1 < 0 ? 0 : i1;
      int i2 = index >= size ? -1 : byte2int(bytes[index++]);
      int p2 = i2 < 0 ? 0 : i2;
      int o0 = i0 >>> 2;
      int o1 = ((i0 & 3) << 4) | (p1 >>> 4);
      int o2 = ((p1 & 0xf) << 2) | (p2 >>> 6);
      int o3 = p2 & 0x3F;
      outs[0] = map1[o0];
      outs[1] = map1[o1];
      outs[2] = (i1 < 0 ? '=' : map1[o2]);
      outs[3] = (i2 < 0 ? '=' : map1[o3]);
    }
    int c = outs[this.step];
    this.step = (this.step + 1) % 4;
    return c;
  }

  /** Mapping table from 6-bit nibbles to Base64 characters. **/
  private static final char[] map1 = new char[64];

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
