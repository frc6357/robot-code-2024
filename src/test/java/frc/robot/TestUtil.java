package frc.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestUtil {

  public static <T> void assertEqualsWithMessage(T expected, T result) {
    assertEquals(expected, result, String.format("%s != %s", String.valueOf(result), String.valueOf(expected)));
  }

  public static void assertCloseWithMessage(double expected, double result, double eps) {
    assert eps > 0.0;
    assertTrue(Math.abs(expected - result) < eps, String.format("%f not within %f of %f", result, eps, expected));
  }
}
