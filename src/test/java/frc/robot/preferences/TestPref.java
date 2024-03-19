package frc.robot.preferences;

import java.math.BigInteger;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Preferences;

import static frc.robot.TestUtil.assertEqualsWithMessage;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestPref {

  NetworkTableInstance testNetworkTableInstance;

  @BeforeEach
  void setUp() {
    testNetworkTableInstance = NetworkTableInstance.create();
    Preferences.setNetworkTableInstance(testNetworkTableInstance);
  }

  @AfterEach
  void reset() {
    Preferences.removeAll();
    testNetworkTableInstance.close();
  }

  @Test
  void testThrowsForInvalidType() {

    assertThrows(RuntimeException.class, () -> {
      // Preferences doesn't support BigInteger, so this is invalid.
      Pref.create("key", new BigInteger("0"), true);
    });
  }

  @ParameterizedTest
  @MethodSource("testArgs")
  void testResetsFromCodeDefaultWhenResetIsTrue(String key, Object defaultValue, Object nonDefaultValue) {
    // Setup
    // Initialize to non-default values.
    initializePrefs(false);

    // Execute
    Pref<?> pref = Pref.create(key, defaultValue, true);

    // Assert
    assertEqualsWithMessage(defaultValue, pref.get());
  }

  @ParameterizedTest
  @MethodSource("testArgs")
  void testResetsFromCodeDefaultWhenResetIsFalseButPriorValueIsMissing(String key, Object defaultValue,
      Object nonDefaultValue) {
    // Execute
    Pref<?> pref = Pref.create(key, defaultValue, false);

    // Assert
    assertEqualsWithMessage(defaultValue, pref.get());
  }

  @ParameterizedTest
  @MethodSource("testArgs")
  void testRetainsPreconfiguredValueWhenResetIsFalse(String key, Object defaultValue, Object nonDefaultValue) {
    // Setup
    // Initialize to non-default values.
    initializePrefs(false);

    // Execute
    Pref<?> pref = Pref.create(key, defaultValue, false);

    // Assert
    assertEqualsWithMessage(nonDefaultValue, pref.get());
  }

  @ParameterizedTest
  @MethodSource("testArgs")
  void testUpdateNotReceivedWithoutPoll(String key, Object defaultValue, Object nonDefaultValue) {
    // Setup
    var capture = new ValueCapture();
    Pref<?> pref = Pref.create(key, defaultValue, false).onChange(capture::capture);

    // Execute
    updatePref(key, nonDefaultValue);

    // Assert
    assertEqualsWithMessage(defaultValue, pref.get());
  }

  @ParameterizedTest
  @MethodSource("testArgs")
  void testUpdatingPrefBroadcastsNewValueToAllListeners(String key, Object defaultValue, Object nonDefaultValue) {
    // Setup
    var captureA = new ValueCapture();
    var captureB = new ValueCapture();
    Pref<?> pref = Pref.create(key, defaultValue, false)
        .onChange(captureA::capture)
        .onChange(captureB::capture);

    // Phase 1 -- Make sure updates are received by all listeners.
    // Execute 1
    updatePref(key, nonDefaultValue);
    pref.poll();

    // Assert 1
    assertEqualsWithMessage(nonDefaultValue, pref.get());
    assertEqualsWithMessage(nonDefaultValue, captureA.captured);
    assertEqualsWithMessage(nonDefaultValue, captureB.captured);

    // Phase 2 -- Make further updates are still received by all listeners.
    // Execute 2
    updatePref(key, defaultValue);
    pref.poll();

    // Assert 2
    assertEqualsWithMessage(defaultValue, pref.get());
    assertEqualsWithMessage(defaultValue, captureA.captured);
    assertEqualsWithMessage(defaultValue, captureB.captured);
  }

  @ParameterizedTest
  @MethodSource("testArgs")
  void testDuplicatePollsDoNotCauseOnChangeToTrigger(String key, Object defaultValue, Object nonDefaultValue) {
    // Setup
    var capture = new ValueCapture();
    Pref<?> pref = Pref.create(key, defaultValue, false)
        .onChange(capture::capture);

    // Execute
    for (int i = 0; i < 5; i++) {
      updatePref(key, nonDefaultValue);
      pref.poll();
    }

    // Assert
    assertEqualsWithMessage(nonDefaultValue, pref.get());
    assertEqualsWithMessage(1, capture.count);
  }

  static Stream<Arguments> testArgs() {
    return Stream.of(
        Arguments.of(Keys.INT, Defaults.INT, NonDefaults.INT),
        Arguments.of(Keys.LONG, Defaults.LONG, NonDefaults.LONG),
        Arguments.of(Keys.FLOAT, Defaults.FLOAT, NonDefaults.FLOAT),
        Arguments.of(Keys.DOUBLE, Defaults.DOUBLE, NonDefaults.DOUBLE),
        Arguments.of(Keys.BOOLEAN, Defaults.BOOLEAN, NonDefaults.BOOLEAN),
        Arguments.of(Keys.STRING, Defaults.STRING, NonDefaults.STRING));
  }

  private static void updatePref(String key, Object value) {
    switch (key) {
      case Keys.INT:
        Preferences.setInt(key, (int) value);
        break;
      case Keys.LONG:
        Preferences.setLong(key, (long) value);
        break;
      case Keys.FLOAT:
        Preferences.setFloat(key, (float) value);
        break;
      case Keys.DOUBLE:
        Preferences.setDouble(key, (double) value);
        break;
      case Keys.BOOLEAN:
        Preferences.setBoolean(key, (boolean) value);
        break;
      case Keys.STRING:
        Preferences.setString(key, (String) value);
        break;
      default:
        throw new RuntimeException(String.format("Invalid preference value: %s", value));
    }
  }

  private static void initializePrefs(boolean useDefault) {
    testArgs().forEach((args) -> {
      final String key = (String) args.get()[0];
      final var defaultValue = args.get()[1];
      final var nonDefaultValue = args.get()[2];
      final var value = useDefault ? defaultValue : nonDefaultValue;
      updatePref(key, value);
    });
  }

  private static class Keys {
    static final String INT = "int";
    static final String LONG = "long";
    static final String FLOAT = "float";
    static final String DOUBLE = "double";
    static final String BOOLEAN = "boolean";
    static final String STRING = "string";
  }

  private static class Defaults {
    static final int INT = 123;
    static final long LONG = 123l;
    static final float FLOAT = 123f;
    static final double DOUBLE = 123.0;
    static final boolean BOOLEAN = true;
    static final String STRING = "default";
  }

  private static class NonDefaults {
    static final int INT = -1;
    static final long LONG = -1l;
    static final float FLOAT = -1f;
    static final double DOUBLE = -1.0;
    static final boolean BOOLEAN = false;
    static final String STRING = "non-default";
  }

  private static class ValueCapture {

    private Object captured;
    private int count = 0;

    void capture(Object obj) {
      this.captured = obj;
      this.count += 1;
    }
  }
}
