package frc.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUtil {

  public static <T> void assertEqualsWithMessage(T expected, T result) {
    assertEquals(expected, result, String.format("%s != %s", String.valueOf(result), String.valueOf(expected)));
  }
}
