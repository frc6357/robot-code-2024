package frc.robot.utils;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;

/**
 * This is a wrapper class used to make the Falcon500 (TalonFX) CAN encoders provide the
 * outputs we need to drive the swerve module to the correct position
 */
public class WrappedMotorEncoder
{
    /**
     * The underlying motor for this MotorEncoder.
     */
    private WPI_TalonFX underlyingMotor;

    /**
     * Indicates the number of degrees per pulse provided by the encoder.
     */
    private double degreesPerPulse;

    /**
     * Indicates the initial offset of the motor in degrees
     */
    private double degreesOffset = 0.0;

    /**
     * Sets up the encoder wrapper with the required motor and other informaiton to
     * calculate distance and velocities
     * 
     * @param selectedMotorEncoder
     *            The motor used to provide the base information for this MotorEncoder
     * @param degreesPerPulse
     *            The distance that the wheel moves with each pulse from the motor
     * @param degreesOffset
     *            Indicates the initial absolute position of the motor
     */
    public WrappedMotorEncoder(WPI_TalonFX selectedMotorEncoder, double degreesPerPulse,
        double degreesOffset)
    {
        selectedMotorEncoder.setSelectedSensorPosition(0);
        underlyingMotor = selectedMotorEncoder;
        this.degreesPerPulse = degreesPerPulse;
        this.degreesOffset = degreesOffset;
    }

    /**
     * Calculates the position of the motor in pulses using the current position,
     * inversion, and the last motor position reset.
     * 
     * @return The current position of the motor in pulses
     */
    private double getPositionPulses()
    {
        return underlyingMotor.getSelectedSensorPosition();
    }

    /**
     * Calculates the velocity of the motor in pulses using the inversion, and the last
     * motor position reset.
     * 
     * @return The current velocity of the motor in pulse units
     */
    private double getVelocityPulses()
    {
        return underlyingMotor.getSelectedSensorVelocity();
    }

    /**
     * Calculates the position of the motor in meters using the stored meters per pulse
     * and the current position.
     * 
     * @return The current position of the motor in meters
     */
    public double getPositionDegrees()
    {
        return MathUtil.inputModulus((getPositionPulses() * degreesPerPulse + degreesOffset), 0,
            360);
    }

    /**
     * Calculates the velocity of the motor in meters per second using the stored meters
     * per pulse and the current velocity.
     * 
     * @return The current velocity of the motor in meters
     */
    public double getVelocityDegrees()
    {
        // Multiplies by ten to convert from m/100ms to m/sec
        return getVelocityPulses() * degreesPerPulse * 10;
    }

    public double getPulsePositionFromDesiredAngle(double angle)
    {
        double offset = MathUtil.inputModulus(angle - getPositionDegrees(), 0, 360);
        double absoluteDistance = Math.abs(offset);
        if(absoluteDistance > 90)
        {
            offset = (360-absoluteDistance) * -Math.signum(offset);
        }
        return getPositionPulses() + offset / degreesPerPulse;
    }

    public Rotation2d getRotation2d()
    {
        return Rotation2d.fromDegrees(getPositionDegrees());
    }
}
