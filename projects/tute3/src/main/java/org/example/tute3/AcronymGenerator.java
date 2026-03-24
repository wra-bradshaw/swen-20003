package org.example.tute3;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;

public class AcronymGenerator {
  private final Reader r;
  private int lookahead = -1;
  private int pos = 0;

  public static void main(String args) {
    AcronymGenerator a;
    try {
      a = new AcronymGenerator(new InputStreamReader(System.in));
    } catch (IOException e) {
      System.err.println("Couldn't create AcronymGenerator");
      return;
    }

    try {
      System.out.println(a.readAcronymFromLine());
    } catch (ParseException e) {
      System.err.println(e);
      return;
    }
  }

  public AcronymGenerator(Reader r) throws IOException {
    this.r = r;
    advance();
  }

  private void advance() throws IOException {
    lookahead = this.r.read();
    pos++;
  }

  private int peek() {
    return lookahead;
  }

  private boolean isAtEnd() {
    return lookahead == -1;
  }

  private int getPos() {
    return pos;
  }

  private void skipSpaceChars() throws IOException {
    while (Character.isSpaceChar(peek()) && !isAtEnd()) {
      advance();
    }
  }

  private void skipUntilSpace() throws IOException {
    while (!Character.isSpaceChar(peek()) && !isAtEnd()) {
      advance();
    }
  }

  public String readAcronymFromLine() throws ParseException {
    StringBuilder s = new StringBuilder();

    while (true) {
      try {
        skipSpaceChars();
      } catch (IOException e) {
        break;
      }

      if (Character.isAlphabetic(peek())) {
        s.append((char) Character.toUpperCase(peek()));

        try {
          skipUntilSpace();
        } catch (IOException e) {
          break;
        }
      } else if (peek() == '\n' || peek() == '\r') {
        break;
      } else if (isAtEnd()) {
        break;
      } else {
        throw new ParseException("Unknown character '" + peek() + "'", getPos());
      }
    }

    return s.toString();
  }
}
