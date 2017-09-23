package breder.util.util.input;

import java.io.IOException;
import java.io.OutputStream;

public class StringOutputStream extends OutputStream {

  private final OutputStream output;

  public StringOutputStream(OutputStream output) {
    this.output = output;
  }

  public void append(String text) throws IOException {
    int size = text.length();
    for (int n = 0; n < size; n++) {
      char c = text.charAt(n);
      if (c <= 0x7F) {
        output.write(c);
      }
      else if (c <= 0x7FF) {
        output.write(((c >> 6) & 0x1F) + 0xC0);
        output.write((c & 0x3F) + 0x80);
      }
      else {
        output.write(((c >> 12) & 0xF) + 0xE0);
        output.write(((c >> 6) & 0x3F) + 0x80);
        output.write((c & 0x3F) + 0x80);
      }
    }
  }

  @Override
  public void write(int n) throws IOException {
    output.write(n);
  }

}
