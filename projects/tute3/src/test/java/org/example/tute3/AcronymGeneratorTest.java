package org.example.tute3;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.StringReader;
import org.junit.jupiter.api.Test;

public class AcronymGeneratorTest {
  public void testString(String testString, String testAcronym) {
    StringReader r = new StringReader(testString);

    AcronymGenerator a =
        assertDoesNotThrow(
            () -> {
              return new AcronymGenerator(r);
            });

    String acronym = assertDoesNotThrow(() -> a.readAcronymFromLine());

    assertEquals(testAcronym, acronym);
  }

  @Test
  public void shouldParse() {
    testString("Portable Network Graphics\n", "PNG");
    testString("situational task and yodelling hat open mustache extension\n", "STAYHOME");
    testString("LeAgUe oF LeGeNdS\n", "LOL");
  }
}
