package frc.robot.utils;

import com.ctre.phoenix6.hardware.TalonFX;

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
    private TalonFX underlyingMotor;

    /**
     * Indicates the number of meters per rotation.
     */
    private double metersPerRotation;

    /**
     * Indicates the last position at which the resetEncoder() method was called.
     */
    private double lastPositionResetValue = 0.0;

    /**
     * The factor used to invert the count if necessary.
     */
    private double inversionFactor;

    /**
     * Sets up the encoder wrapper with the required motor and other informaiton to
     * calculate distance and velocities
     * 
     * @param selectedMotorEncoder
     *            The motor used to provide the base information for this MotorEncoder
     * @param metersPerRotation
     *            The distance that the wheel moves with each rotation from the motor
     * @param isInverted
     *            Indicates whether or not the motor is inverted
     */
    public MotorEncoder(TalonFX selectedMotorEncoder, double metersPerRotation, boolean isInverted)
    {
        underlyingMotor = selectedMotorEncoder;
        this.metersPerRotation = metersPerRotation;
        inversionFactor = isInverted ? -1 : 1;
    }

    /**
     * Calculates the position of the motor in rotations using the current position,
     * inversion, and the last motor position reset.
     * 
     * @return The current position of the motor in rotations
     */
    public double getPositionRotations()
    {
        return (underlyingMotor.getPosition().getValue() - lastPositionResetValue)
            * inversionFactor;
    }

    /**
     * Calculates the velocity of the motor in rot/sec using the inversion, and the last
     * motor position reset.
     * 
     * @return The current velocity of the motor in rotations per second
     */
    public double getVelocityRotations()
    {
        return underlyingMotor.getVelocity().getValue() * inversionFactor;
    }

    /**
     * Calculates the position of the motor in meters using the stored meters per pulse
     * and the current position.
     * 
     * @return The current position of the motor in meters
     */
    public double getPositionMeters()
    {
        return getPositionRotations() * metersPerRotation;
    }

    /**
     * Calculates the velocity of the motor in meters per second using the stored meters
     * per pulse and the current velocity.
     * 
     * @return The current velocity of the motor in meters per second
     */
    public double getVelocityMetersPerSecond()
    {
        return getVelocityRotations() * metersPerRotation;
    }

    /**
     * Resets the position of the motor encoder to zero.
     */
    public void resetEncoder()
    {
        lastPositionResetValue = underlyingMotor.getPosition().getValue();
    }

    /**
     * Sets the distance in meters that is given for each rotation to calculate position and
     * velocity speed in meters.
     * 
     * @param metersPerRotation
     *            The given meters that should be used per rotation
     */
    public void setMetersPerRotation(double metersPerRotation)
    {
        this.metersPerRotation = metersPerRotation;
    }
}
