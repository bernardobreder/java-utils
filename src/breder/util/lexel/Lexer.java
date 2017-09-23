package breder.util.lexel;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import breder.util.util.lexical.LexicalException;

public class Lexer {

  /** Stream */
  private InputStream input;
  /** Próximo caracter */
  protected int[] look;
  /** Máximo de look a head */
  protected int max = 5;
  /** Indice da fila */
  protected int index;
  /** Indice da fila */
  private int count;

  /**
   * Construtor
   * 
   * @param input
   * @throws IOException
   */
  public Lexer(InputStream input) throws IOException {
    this.input = input;
    this.look = new int[max];
  }

  /**
   * Realiza a leitura de um Token
   * 
   * @return Token
   * @throws IOException
   */
  public Token readToken() throws IOException {
    if (this.look[index] == -1) {
      return null;
    }
    while (this.isDocument()) {
      this.readDocument();
    }
    if (this.isWord()) {
      return this.readWord();
    }
    else if (this.isNumber()) {
      return this.readNumber();
    }
    else if (this.isString()) {
      return this.readString();
    }
    else {
      return this.readSymbol();
    }
  }

  /**
   * Realiza a leitura de um número
   * 
   * @return token
   * @throws IOException
   */
  public StringToken readDocument() throws IOException {
    int c = this.look();
    while (c <= 32) {
      if (c < 0) {
        return null;
      }
      c = this.next();
    }
    return this.readDocument(c);
  }

  /**
   * Realiza a leitura de um número
   * 
   * @return token
   * @throws IOException
   * @throws
   */
  public StringToken readString() throws IOException {
    int c = this.look();
    while (c <= 32) {
      if (c < 0) {
        return null;
      }
      c = this.next();
    }
    return this.readString(c);
  }

  /**
   * Realiza a leitura de um número
   * 
   * @return token
   * @throws IOException
   * @throws
   */
  public NumberToken readNumber() throws IOException {
    int c = this.look();
    while (c <= 32) {
      if (c < 0) {
        return null;
      }
      c = this.next();
    }
    return this.readNumber(c);
  }

  /**
   * Realiza a leitura de um número
   * 
   * @return token
   * @throws IOException
   * @throws
   */
  public BrederWordToken readWord() throws IOException {
    int c = this.look();
    while (c <= 32) {
      if (c < 0) {
        return null;
      }
      c = this.next();
    }
    return this.readWord(c);
  }

  /**
   * Realiza a leitura de um número
   * 
   * @return token
   * @throws IOException
   * @throws
   */
  public Token readSymbol() throws IOException {
    int c = this.look();
    while (c <= 32) {
      if (c < 0) {
        return null;
      }
      c = this.next();
    }
    return this.readSymbol(c);
  }

  /**
   * Verifica se o próximo token é um token do tipo número
   * 
   * @return indica se é do tipo número
   * @throws IOException
   */
  public boolean isDocument() throws IOException {
    int c = this.look();
    while (c <= 32) {
      if (c < 0) {
        return false;
      }
      this.next();
      c = this.look();
    }
    return isDocument(c);
  }

  /**
   * Verifica se o próximo token é um token do tipo número
   * 
   * @return indica se é do tipo número
   * @throws IOException
   */
  public boolean isNumber() throws IOException {
    int c = this.look();
    while (c <= 32) {
      if (c < 0) {
        return false;
      }
      this.next();
      c = this.look();
    }
    return isNumber(c);
  }

  /**
   * Verifica se o próximo token é um token do tipo string
   * 
   * @return indica se é do tipo string
   * @throws IOException
   */
  public boolean isString() throws IOException {
    int c = this.look();
    while (c <= 32) {
      if (c < 0) {
        return false;
      }
      this.next();
      c = this.look();
    }
    return isString(c);
  }

  /**
   * Verifica se o próximo token é um token do tipo palavra
   * 
   * @return indica se é do tipo palavra
   * @throws IOException
   */
  public boolean isWord() throws IOException {
    int c = this.look();
    while (c <= 32) {
      if (c < 0) {
        return false;
      }
      this.next();
      c = this.look();
    }
    return isWordStart(c);
  }

  /**
   * {@inheritDoc}
   */
  public StringToken readDocument(int c) throws IOException {
    c = this.next();
    this.next();
    StringBuilder sb = new StringBuilder();
    if (c == '*') {
      c = this.look();
      for (; c != '*' || this.look(1) != '/';) {
        sb.append((char) c);
        c = this.next();
      }
      this.next();
      this.next();
    }
    else {
      c = this.look();
      for (; c != '\n';) {
        if (c != '\r') {
          sb.append((char) c);
        }
        c = this.next();
      }
      this.next();
    }
    return new StringToken(sb.toString());
  }

  /**
   * {@inheritDoc}
   */
  public NumberToken readNumber(int c) throws IOException {
    double value = 0.;
    int dot = 10;
    if (!this.isNumber(c)) {
      throw new LexicalException();
    }
    if (c == '0' && this.look(1) == 'x') {
      c = this.next();
      if (c != 'x') {
        throw new LexicalException();
      }
      c = Character.toLowerCase(this.next());
      value = c >= '0' && c <= '9' ? c - '0' : c - 'a' + 10;
      c = Character.toLowerCase(this.next());
      while ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f')) {
        value = 16 * value + (c >= '0' && c <= '9' ? c - '0' : c - 'a' + 10);
        c = Character.toLowerCase(this.next());
      }
      return new NumberToken(value);
    }
    else {
      value = c - '0';
      c = this.next();
      while (this.isNumber(c)) {
        value = 10 * value + (c - '0');
        c = this.next();
      }
      if (c == '.') {
        c = this.next();
        while (this.isNumber(c)) {
          value += (double) (c - '0') / dot;
          dot *= 10;
          c = this.next();
        }
      }
      return new NumberToken(value);
    }
  }

  /**
   * {@inheritDoc}
   */
  public StringToken readString(int c) throws IOException {
    StringBuilder sb = new StringBuilder();
    if (!this.isString(c)) {
      throw new LexicalException();
    }
    c = this.next();
    while (!this.isString(c)) {
      if (c == '\\') {
        switch (this.next()) {
          case 'n': {
            sb.append('\n');
            break;
          }
          case 'r': {
            sb.append('\r');
            break;
          }
          case 't': {
            sb.append('\t');
            break;
          }
          case 'f': {
            sb.append('\f');
            break;
          }
          case 'b': {
            sb.append('\b');
            break;
          }
          case '\\': {
            sb.append('\\');
            break;
          }
          default: {
            throw new LexicalException();
          }
        }
      }
      else {
        sb.append((char) c);
      }
      c = this.next();
    }
    this.next();
    return new StringToken(sb.toString());
  }

  /**
   * {@inheritDoc}
   */
  public BrederWordToken readWord(int c) throws IOException {
    StringBuilder sb = new StringBuilder();
    if (this.isWordStart(c)) {
      sb.append((char) c);
    }
    c = this.next();
    while (this.isWordPart(c)) {
      sb.append((char) c);
      c = this.next();
    }
    String value = sb.toString();
    BrederWordToken word = BrederWordToken.build(value);
    if (word != null) {
      return word;
    }
    return new IdentifyToken(value);
  }

  /**
   * {@inheritDoc}
   */

  public Token readSymbol(int c) throws IOException {
    switch (c) {
      case '!': {
        c = this.next();
        if (c == '=') {
          this.next();
          return BrederWordToken.NOT_EQUAL_TOKEN;
        }
        else {
          return new Token('!');
        }
      }
      case '=': {
        c = this.next();
        if (c == '=') {
          this.next();
          return BrederWordToken.EQ_TOKEN;
        }
        else {
          return new Token('=');
        }
      }
      case '-': {
        c = this.next();
        if (c == '-') {
          this.next();
          return BrederWordToken.DEC_TOKEN;
        }
        else {
          return new Token('-');
        }
      }
      case '+': {
        c = this.next();
        if (c == '+') {
          this.next();
          return BrederWordToken.INC_TOKEN;
        }
        else {
          return new Token('+');
        }
      }
      case '>': {
        c = this.next();
        if (c == '=') {
          this.next();
          return BrederWordToken.GE_TOKEN;
        }
        else {
          return new Token('>');
        }
      }
      case '<': {
        c = this.next();
        if (c == '=') {
          this.next();
          return BrederWordToken.LE_TOKEN;
        }
        else {
          return new Token('<');
        }
      }
      default: {
        this.next();
        return new Token(c);
      }
    }
  }

  /**
   * {@inheritDoc}
   */

  public boolean isDocument(int c) throws IOException {
    if (c != '/') {
      return false;
    }
    c = this.look(1);
    return c == '*' || c == '/';
  }

  /**
   * {@inheritDoc}
   */

  public boolean isNumber(int c) {
    return c >= '0' && c <= '9';
  }

  /**
   * {@inheritDoc}
   */

  public boolean isString(int c) {
    return c == '\"';
  }

  /**
   * {@inheritDoc}
   */

  public boolean isWordStart(int c) {
    return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_'
      || c == '$';
  }

  /**
   * {@inheritDoc}
   */

  public boolean isWordPart(int c) {
    return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')
      || (c >= '0' && c <= '9') || c == '_' || c == '$';
  }

  /**
   * Realiza a leitura de somente um byte
   * 
   * @return leitura de um byte
   * @throws IOException
   */
  protected int look() throws IOException {
    if (count == 0) {
      this.look[index] = this.read();
      this.count = 1;
    }
    return this.look[index];
  }

  /**
   * Realiza a leitura de somente um byte
   * 
   * @param next
   * @return leitura de um byte
   * @throws IOException
   */
  protected int look(int next) throws IOException {
    if (next >= max) {
      int[] data = new int[max * 2];
      for (int n = 0; n < max; n++) {
        data[index + n] = this.look[(index + n) % max];
      }
      this.look = data;
      max *= 2;
    }
    if (next >= count) {
      int size = next + 1 - count;
      for (int n = 0; n < size; n++) {
        this.look[(index + n + count) % max] = this.read();
      }
      this.count += size;
    }
    return this.look[(index + next) % max];
  }

  /**
   * Anda para o próximo byte
   * 
   * @return o byte seguinte
   * 
   * @throws IOException
   */
  public int next() throws IOException {
    if (count > 0) {
      this.count--;
    }
    index = (index + 1) % max;
    if (count <= 0) {
      count = 1;
      this.look[index] = this.read();
    }
    return this.look[index];
  }

  /**
   * Realiza a leitura
   * 
   * @return leitura
   * @throws IOException
   */
  private int read() throws IOException {
    int c = this.input.read();
    if (c <= 0x7F) {
      return c;
    }
    else if ((c >> 5) == 0x6) {
      int i2 = this.input.read();
      if (i2 < 0) {
        throw new EOFException();
      }
      return (((c & 0x1F) << 6) + (i2 & 0x3F));
    }
    else {
      int i2 = this.input.read();
      if (i2 < 0) {
        throw new EOFException();
      }
      int i3 = this.input.read();
      if (i3 < 0) {
        throw new EOFException();
      }
      return (((c & 0xF) << 12) + ((i2 & 0x3F) << 6) + (i3 & 0x3F));
    }
  }

  public static class Token {

    public final int tag;

    public Token(int t) {
      tag = t;
    }

    @Override
    public String toString() {
      return "" + (char) tag;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + tag;
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      Token other = (Token) obj;
      if (tag != other.tag) {
        return false;
      }
      return true;
    }

  }

  public static class StringToken extends Token {

    public final String value;

    public StringToken(String v) {
      super(BrederWordToken.STR);
      this.value = v;
    }

    @Override
    public String toString() {
      return value;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((value == null) ? 0 : value.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (!super.equals(obj)) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      StringToken other = (StringToken) obj;
      if (value == null) {
        if (other.value != null) {
          return false;
        }
      }
      else if (!value.equals(other.value)) {
        return false;
      }
      return true;
    }

  }

  public static class NumberToken extends Token {

    public final double value;

    public NumberToken(double v) {
      super(BrederWordToken.NUM);
      value = v;
    }

    @Override
    public String toString() {
      return "" + value;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      long temp;
      temp = Double.doubleToLongBits(value);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (!super.equals(obj)) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      NumberToken other = (NumberToken) obj;
      if (Double.doubleToLongBits(value) != Double
        .doubleToLongBits(other.value)) {
        return false;
      }
      return true;
    }

  }

  public static class IdentifyToken extends BrederWordToken {

    public IdentifyToken(String s) {
      super(s, BrederWordToken.ID);
    }

  }

  public static class BrederWordToken extends Token {

    public String lexeme = "";

    private static final Map<String, BrederWordToken> words =
      new HashMap<String, BrederWordToken>();

    public final static int AND = 256, CONTINUE = 257, BREAK = 258, DO = 259,
      ELSE = 260, EQ = 261, FALSE = 262, GE = 263, ID = 264, IF = 265,
      LE = 267, NE = 269, NUM = 270, OR = 271, TRUE = 274, WHILE = 295,
      END = 276, REPEAT = 277, FOR = 278, STR = 290, THIS = 291, DEC = 292,
      INC = 293;

    public static final BrederWordToken EQ_TOKEN =
      new BrederWordToken("==", EQ);

    public static final BrederWordToken NOT_EQUAL_TOKEN = new BrederWordToken(
      "!=", NE);

    public static final BrederWordToken LE_TOKEN =
      new BrederWordToken("<=", LE);

    public static final BrederWordToken GE_TOKEN =
      new BrederWordToken(">=", GE);

    public static final BrederWordToken INC_TOKEN = new BrederWordToken("++",
      INC);

    public static final BrederWordToken DEC_TOKEN = new BrederWordToken("--",
      DEC);

    static {
      words.put("--", DEC_TOKEN);
      words.put("++", INC_TOKEN);
      words.put(">=", GE_TOKEN);
      words.put("<=", LE_TOKEN);
      words.put("!=", NOT_EQUAL_TOKEN);
      words.put("==", EQ_TOKEN);
      words.put("and", new BrederWordToken("and", AND));
      words.put("break", new BrederWordToken("break", BREAK));
      words.put("continue", new BrederWordToken("continue", CONTINUE));
      words.put("do", new BrederWordToken("do", DO));
      words.put("else", new BrederWordToken("else", ELSE));
      words.put("end", new BrederWordToken("end", END));
      words.put("false", new BrederWordToken("false", FALSE));
      words.put("for", new BrederWordToken("for", FOR));
      words.put("if", new BrederWordToken("if", IF));
      words.put("or", new BrederWordToken("or", OR));
      words.put("repeat", new BrederWordToken("repeat", REPEAT));
      words.put("this", new BrederWordToken("this", THIS));
      words.put("true", new BrederWordToken("true", TRUE));
      words.put("while", new BrederWordToken("while", WHILE));
    }

    protected BrederWordToken(String s, int tag) {
      super(tag);
      lexeme = s;
    }

    public static BrederWordToken build(String token) {
      return words.get(token);
    }

    public static BrederWordToken build(int tag) {
      for (BrederWordToken token : words.values()) {
        if (token.tag == tag) {
          return token;
        }
      }
      throw new IllegalArgumentException();
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((lexeme == null) ? 0 : lexeme.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      BrederWordToken other = (BrederWordToken) obj;
      if (lexeme == null) {
        if (other.lexeme != null) {
          return false;
        }
      }
      else if (!lexeme.equals(other.lexeme)) {
        return false;
      }
      return true;
    }

    @Override
    public String toString() {
      return lexeme;
    }

  }

}
