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
import com.revrobotics.SparkFlexExternalEncoder.Type;
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
        encoder = motor.getExternalEncoder(Type.kQuadrature, 8192);
        
        encoder.setPositionConversionFactor(kConversionFactor);
        encoder.setInverted(true);

        encoder.setPosition(kMinAngle);

    }

    public void setTargetAngle(double angle)
    {
        targetAngle = angle;
        PID.setSetpoint(targetAngle);
    }

    public void setTargetAngle(Supplier<Double> angle)
    {
        targetAngle = angle.get();
        PID.setSetpoint(targetAngle);
    }

    
    /**
     * {@inheritDoc}
     */
    public boolean isAtTargetAngle()
    {
        return Math.abs(getCurrentAngle() - getTargetAngle()) < kAngleTolerance;
    }

    /**
     * {@inheritDoc}
     */
    public double getCurrentAngle()
    {
        return encoder.getPosition();
    }

    /**
     * {@inheritDoc}
     */
    public double getTargetAngle()
    {
        return targetAngle;
    }

    /**
     * {@inheritDoc}
     */
    public void resetAngle()
    {
        encoder.setPosition(kMinAngle);
    }

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
