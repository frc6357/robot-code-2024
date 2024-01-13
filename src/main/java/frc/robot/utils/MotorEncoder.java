package frc.robot.utils;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

/**
 * This is a wrapper class used to make the Falcom500 (TalonFX) CAN encoders provide the
 * outputs we need to drive the Ramsete controller used for path following during
 * autonomous mode.
 */
public class MotorEncoder
{
    /**
     * The underlying motor for this MotorEncoder.
     */
    private WPI_TalonFX underlyingMotor;

    /**
     * Indicates the number of meters per pulse provided by the encoder.
     */
    private double metersPerPulse;

    /**
     * Indicates the last position at which the resetEncoder() method was called.
     */
    private double lastPositionResetValue = 0.0;

    /**
     * The factor used to calculate the pulse count (and invert the count if necessary).
     */
    private double inversionFactor;

    /**
     * Sets up the encoder wrapper with the required motor and other informaiton to
     * calculate distance and velocities
     * 
     * @param selectedMotorEncoder
     *            The motor used to provide the base information for this MotorEncoder
     * @param metersPerPulse
     *            The distance that the wheel moves with each pulse from the motor
     * @param isInverted
     *            Indicates whether or not the motor is inverted (and if so will invert
     *            the pulse count as needed for the calculations)
     */
    public MotorEncoder(WPI_TalonFX selectedMotorEncoder, double metersPerPulse, boolean isInverted)
    {
        underlyingMotor = selectedMotorEncoder;
        this.metersPerPulse = metersPerPulse;
        inversionFactor = isInverted ? -1 : 1;
    }

    /**
     * Calculates the position of the motor in pulses using the current position,
     * inversion, and the last motor position reset.
     * 
     * @return The current position of the motor in pulses
     */
    public double getPositionPulses()
    {
        return (underlyingMotor.getSelectedSensorPosition() - lastPositionResetValue)
            * inversionFactor;
    }

    /**
     * Calculates the velocity of the motor in pulses using the inversion, and the last
     * motor position reset.
     * 
     * @return The current velocity of the motor in pulses per 100ms
     */
    public double getVelocityPulses()
    {
        return underlyingMotor.getSelectedSensorVelocity() * inversionFactor;
    }

    /**
     * Calculates the position of the motor in meters using the stored meters per pulse
     * and the current position.
     * 
     * @return The current position of the motor in meters
     */
    public double getPositionMeters()
    {
        return getPositionPulses() * metersPerPulse;
    }

    /**
     * Calculates the velocity of the motor in meters per second using the stored meters
     * per pulse and the current velocity.
     * 
     * @return The current velocity of the motor in meters per second
     */
    public double getVelocityMetersPerSecond()
    {
        // Multiplies by ten to convert from m/100ms to m/sec
        return getVelocityPulses() * metersPerPulse * 10;
    }

    /**
     * Resets the position of the motor encoder to zero.
     */
    public void resetEncoder()
    {
        lastPositionResetValue = underlyingMotor.getSelectedSensorPosition();
    }

    /**
     * Sets the distance in meters that is given for each pulse to calculate position and
     * velocity speed in meters.
     * 
     * @param metersPerPulse
     *            The given meters that should be used per pulse
     */
    public void setMetersPerPulse(double metersPerPulse)
    {
        this.metersPerPulse = metersPerPulse;
    }
}
