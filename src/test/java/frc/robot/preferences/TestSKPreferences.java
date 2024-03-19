package frc.robot.preferences;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Preferences;

import static frc.robot.TestUtil.assertEqualsWithMessage;

public class TestSKPreferences {
  private static final String TEST_KEY = "test";

  NetworkTableInstance testNetworkTableInstance;

  @BeforeEach
  void setUp() {
    testNetworkTableInstance = NetworkTableInstance.create();
    Preferences.setNetworkTableInstance(testNetworkTableInstance);
  }

  @AfterEach
  void tearDown() {
    testNetworkTableInstance.close();
    SKPreferences.disableAllPrefs();

    // Reset to default behavior after each test.
    SKPreferences.initializeFromCode(true);
  }

  @Test
  void valueUpdatesWhenUpstreamValueChanges() {
    // Setup
    final var expected = 123.0;
    Pref<Double> pref = SKPreferences.attach(TEST_KEY, 0.0);

    // Execute
    Preferences.setDouble(TEST_KEY, expected);
    SKPreferences.refreshIfNeeded();

    // Assert
    final var result = pref.get();
    assertEqualsWithMessage(expected, result);
  }

  @Test
  void overwritesWithRobotCodeDefaultValueByDefault() {
    // Setup
    final var expected = 123.0;
    final var unexpected = -1.0;
    Preferences.initDouble(TEST_KEY, unexpected);

    // Execute
    SKPreferences.attach(TEST_KEY, expected);

    // Assert
    final var result = Preferences.getDouble(TEST_KEY, unexpected);
    assertEqualsWithMessage(expected, result);
  }

  @Test
  void overwritesWithPredefinedValueWhenNotInitializingFromCode() {
    // Setup
    final var expected = 123.0;
    final var unexpected = -1.0;
    Preferences.initDouble(TEST_KEY, expected);
    SKPreferences.initializeFromCode(false);

    // Execute
    SKPreferences.attach(TEST_KEY, unexpected);

    // Assert
    final var result = Preferences.getDouble(TEST_KEY, unexpected);

    assertEqualsWithMessage(expected, result);
  }
}
