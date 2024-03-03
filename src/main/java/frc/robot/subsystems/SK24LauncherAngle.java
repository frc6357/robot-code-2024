package frc.robot.subsystems;

import static frc.robot.Constants.LauncherAngleConstants.kAnglePID;
import static frc.robot.Constants.LauncherAngleConstants.kAngleTolerance;
import static frc.robot.Constants.LauncherAngleConstants.kArmMotorMaxOutput;
import static frc.robot.Constants.LauncherAngleConstants.kArmMotorMinOutput;
import static frc.robot.Constants.LauncherAngleConstants.kConversionFactor;
import static frc.robot.Constants.LauncherAngleConstants.kMinAngle;
import static frc.robot.Ports.launcherPorts.kLauncherAngleMotor;

import java.util.function.Supplier;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Ports.launcherPorts.*;


public class SK24LauncherAngle extends SubsystemBase
{
    // Create memory objects for both motors for public use
    CANSparkFlex    motor;
    CANSparkFlex    followerMotor;
    int             joystickCount;
    double          targetAngle;
    double          currentAngle;
    RelativeEncoder encoder;
    PIDController   PID;
    SlewRateLimiter accelLimit;

    //Constructor for public command access
    public SK24LauncherAngle()
    {
        //Initialize motor objects
        motor = new CANSparkFlex(kLauncherAngleMotor.ID, MotorType.kBrushless);
        followerMotor = new CANSparkFlex(kLauncherAngleFollowerMotor.ID, MotorType.kBrushless);
        followerMotor.follow(motor, true);

        PID = new PIDController(kAnglePID.kP, kAnglePID.kI, kAnglePID.kD);
        PID.setIntegratorRange(-kAnglePID.iZone, kAnglePID.iZone);
        PID.setSetpoint(kMinAngle);

        motor.restoreFactoryDefaults();
        motor.setIdleMode(IdleMode.kBrake); 
        

        targetAngle = kMinAngle;
        currentAngle = kMinAngle;
        encoder = motor.getEncoder();
        
        encoder.setPositionConversionFactor(kConversionFactor);

        encoder.setPosition(kMinAngle);

    }

    /**
     * Sets the target angle for the launcher to reach
     * @param angle The target angle for the launcher to reach
     */
    public void setTargetAngle(double angle)
    {
        targetAngle = angle;
        PID.setSetpoint(targetAngle);
    }

    /**
     * Sets the target angle for the launcher to reach
     * @param angle The supplier of target angle for the launcher to reach
     */
    public void setTargetAngle(Supplier<Double> angle)
    {
        targetAngle = angle.get();
        PID.setSetpoint(targetAngle);
    }

    
    /**
     * Determines if launcher has reached target angle 
     * @return true if at target angle, false if not at target angle
     */
    public boolean isAtTargetAngle()
    {
        return Math.abs(getCurrentAngle() - getTargetAngle()) < kAngleTolerance;
    }

    /**
     *  Retrives the current angle of the launcher
     * @return double value of encoder position in degrees
     */
    public double getCurrentAngle()
    {
        return encoder.getPosition();
    }

    /**
     *  Gets the target angle of the launcher
     * @return double value of encoder position in degrees
     */
    public double getTargetAngle()
    {
        return targetAngle;
    }

    /**
     * Resets the launcher encoder's angle to the kMinAngle constant
     */
    public void resetEncoderAngle()
    {
        encoder.setPosition(kMinAngle);
    }

    /**
     * Resets the angle of the launcher to the kMinAngle constant
     */
    public void zeroPosition()
    {
        setTargetAngle(kMinAngle);
    }

    @Override
    public void periodic()
    {

        double current_angle = getCurrentAngle();
        double target_angle = getTargetAngle();

        // Calculates motor speed and puts it within operating range
        double speed = MathUtil.clamp(PID.calculate(current_angle), kArmMotorMinOutput, kArmMotorMaxOutput);
        // speed = accelLimit.calculate(speed);
        motor.set(speed); 

        SmartDashboard.putNumber("Current Launcher Angle", current_angle);
        SmartDashboard.putNumber("Target Launcher Angle", target_angle);
        SmartDashboard.putBoolean("Arm at Setpoint", isAtTargetAngle());
    }

}
