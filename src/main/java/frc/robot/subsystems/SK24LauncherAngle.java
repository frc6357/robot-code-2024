package frc.robot.subsystems;

import static frc.robot.Constants.LauncherAngleConstants.kAnglePID;
import static frc.robot.Constants.LauncherAngleConstants.kAngleTolerance;
import static frc.robot.Constants.LauncherAngleConstants.kArmMotorMaxOutput;
import static frc.robot.Constants.LauncherAngleConstants.kArmMotorMinOutput;
import static frc.robot.Constants.LauncherAngleConstants.kLauncherAngleFF;
import static frc.robot.Constants.LauncherAngleConstants.kMinAngle;
import static frc.robot.Ports.launcherPorts.kLauncherAngleFollowerMotor;
import static frc.robot.Ports.launcherPorts.kLauncherAngleMotor;

import java.util.function.Supplier;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class SK24LauncherAngle extends SubsystemBase
{
    // Create memory objects for both motors for public use
    CANSparkFlex    motor;
    CANSparkFlex    followerMotor;
    int             joystickCount;
    double          targetAngle;
    DutyCycleEncoder lEncoder;
    DigitalInput lLimitSwitch;
    PIDController   PID;
    SlewRateLimiter accelLimit;
    double FeedForward;
    

    //Constructor for public command access
    public SK24LauncherAngle()
    {
        //Initialize motor objects
        motor = new CANSparkFlex(kLauncherAngleMotor.ID, MotorType.kBrushless);
        followerMotor = new CANSparkFlex(kLauncherAngleFollowerMotor.ID, MotorType.kBrushless);
        followerMotor.follow(motor, true);
        //lLimitSwitch = new DigitalInput(); //TODO - find actual limit switch channel

        PID = new PIDController(kAnglePID.kP, kAnglePID.kI, kAnglePID.kD);
        PID.setSetpoint(kMinAngle);
        FeedForward = kLauncherAngleFF;

        motor.setIdleMode(IdleMode.kBrake); 

        motor.setInverted(true);
        

        targetAngle = kMinAngle;
        lEncoder = new DutyCycleEncoder(0);
        lEncoder.setDistancePerRotation(360.0);

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
        return lEncoder.getDistance(); //TODO - determine if launcher up is negative
        // return Math.abs(encoder.getAbsolutePosition());
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
        lEncoder.setPositionOffset(kMinAngle);
    }

    public void resetEncoderAngle()
    {
        lEncoder.reset();
    }

    public void zeroPosition()
    {
        setTargetAngle(kMinAngle);
    }

   // public boolean hitZeroPos()
   // {
   //     return lLimitSwitch.get();
   // }

    /**
     * Calculates feedforward value of arm by multiplying cosine of angle degrees by feed forward constant
     * @param angle Target angle of the launcher arm in degrees
     * @return feed forward value to be used in PID control loop
     * 
     */
    public double calculateFF(double angle)
    {
        return Math.cos(Math.toRadians(angle)) * FeedForward;
    }

    @Override
    public void periodic()
    {

        double current_angle = getCurrentAngle();
        double target_angle = getTargetAngle();

        // Calculates motor speed and puts it within operating range
        double speed = MathUtil.clamp(PID.calculate(current_angle) + calculateFF(target_angle), kArmMotorMinOutput, kArmMotorMaxOutput);
        // speed = accelLimit.calculate(speed);
        //motor.set(speed); TODO- add back when we want to move launcher angle

        SmartDashboard.putNumber("Current Launcher Angle", getCurrentAngle());
        SmartDashboard.putNumber("Target Launcher Angle", target_angle);
        SmartDashboard.putBoolean("Arm at Setpoint", isAtTargetAngle());
    }

}
