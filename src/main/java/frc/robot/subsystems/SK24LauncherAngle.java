package frc.robot.subsystems;

import static frc.robot.Constants.LauncherAngleConstants.*;
import static frc.robot.Ports.launcherPorts.kLauncherAngleFollowerMotor;
import static frc.robot.Ports.launcherPorts.kLauncherAngleMotor;

import java.util.function.Supplier;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.preferences.Pref;
import frc.robot.preferences.SKPreferences;


public class SK24LauncherAngle extends SubsystemBase
{
    // Create memory objects for both motors for public use
    CANSparkFlex    motor;
    CANSparkFlex    followerMotor;
    int             joystickCount;
    double          targetAngle;
    DutyCycleEncoder lEncoder;
    double FeedForward;
    PIDController PID = new PIDController(kLauncherAngleP, kLauncherAngleI, kLauncherAngleD);
    
    Pref<Double> launcherAngleP = SKPreferences.attach(kLauncherAnglePKey, kLauncherAngleP).onChange(PID::setP);
    Pref<Double> launcherAngleI = SKPreferences.attach(kLauncherAngleIKey, kLauncherAngleI).onChange(PID::setI);
    Pref<Double> launcherAngleD = SKPreferences.attach(kLauncherAngleDKey, kLauncherAngleD).onChange(PID::setD);
    Pref<Double> launcherAngleFF = SKPreferences.attach(kLauncherAngleFFKey, kLauncherAngleFF);

    

    //Constructor for public command access
    public SK24LauncherAngle()
    {
        //Initialize motor objects
        motor = new CANSparkFlex(kLauncherAngleMotor.ID, MotorType.kBrushless);
        followerMotor = new CANSparkFlex(kLauncherAngleFollowerMotor.ID, MotorType.kBrushless);

        motor.restoreFactoryDefaults();
        followerMotor.restoreFactoryDefaults();
        followerMotor.follow(motor, true);
        followerMotor.setIdleMode(IdleMode.kBrake);
        //lLimitSwitch = new DigitalInput(); //TODO - find actual limit switch channel


        FeedForward = kLauncherAngleFF;
        
        motor.setIdleMode(IdleMode.kBrake); 
        
        motor.setInverted(true);
        
        motor.setSmartCurrentLimit(30);
        followerMotor.setSmartCurrentLimit(30);
        
        motor.burnFlash();
        followerMotor.burnFlash();
        
        targetAngle = kSpeakerAngle;
        PID.setSetpoint(targetAngle);
        lEncoder = new DutyCycleEncoder(1);

    }

    public void resetPID()
    {
        PID.reset();
    }
    public void setTargetAngle(double angle)
    {
        if (angle == targetAngle) return;
        PID.reset();
        targetAngle = angle;
        PID.setSetpoint(targetAngle);
    }

    public void setTargetAngle(Supplier<Double> angle)
    {
        if (angle.get() == targetAngle) return;
        PID.reset();

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
     * Gets the current angle of the encoder in degrees. 
     * Wrapped in between -180 and 180, and limited to minimum and maximum angle constants. 
     * Upward movement of the launcheris positive.
     * @return Current angle of the encoder in degrees.
     */
    public double getCurrentAngle()
    {
        //Gets absolute position of encoder an converts it into degrees
        double absolutePosition = lEncoder.getAbsolutePosition() * 360.0;
        //Converts the encoder position so upward movement from zero increases with offset, wrapping value from -180 to 180 degrees
        double wrappedPosition = MathUtil.inputModulus(360.0 - (absolutePosition - kAngleOffset), -180.0, 180.0);
        //Returns the angle with it clamped between minimum angle and maximum angle to avoid running through the hard stops.
        return MathUtil.clamp(wrappedPosition, kMinAngle, kMaxAngle);
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
        lEncoder.reset();
    }

    public void resetEncoderAngle()
    {
        lEncoder.reset();
    }

    public void zeroPosition()
    {
        setTargetAngle(kZeroAngle);
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
        return Math.cos(Math.toRadians(angle)) * launcherAngleFF.get();
    }

    @Override
    public void periodic()
    {

        double current_angle = getCurrentAngle();
        double target_angle = getTargetAngle();

        // Calculates motor speed and puts it within operating range
        double closedLoopOutput = PID.calculate(current_angle);
        double openLoopOutput = calculateFF(target_angle);
        
        double speed = MathUtil.clamp(closedLoopOutput + openLoopOutput, kArmMotorMinOutput, kArmMotorMaxOutput);
        motor.set(speed);

        SmartDashboard.putNumber("Current Launcher Angle", getCurrentAngle());
        SmartDashboard.putNumber("Target Launcher Angle", target_angle);
        SmartDashboard.putNumber("Angle Open Loop Out", openLoopOutput);
        SmartDashboard.putNumber("Angle Closed Loop Out", closedLoopOutput);
        SmartDashboard.putBoolean("Arm at Setpoint", isAtTargetAngle());
    }

}
