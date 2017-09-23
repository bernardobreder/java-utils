package breder.util.util;

import java.awt.Graphics;

public class GraphicUtil {

  public static int stringWidth(Graphics g, String text) {
    char[] chars = text.toCharArray();
    int len = 0;
    for (char c : chars) {
      len += g.getFontMetrics().charWidth(c);
    }
    return len;
  }

  public static void drawString(Graphics g, String text, int x, int y) {
    char[] chars = text.toCharArray();
    int offx = x;
    for (int n = 0; n < chars.length; n++) {
      g.drawChars(chars, n, 1, offx, y);
      offx += g.getFontMetrics().charWidth(chars[n]);
    }
  }

}
