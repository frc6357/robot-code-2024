package frc.robot.subsystems;

import static frc.robot.Ports.launcherPorts.kTopLauncherMotor;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.LauncherAngleConstants.*;


public class SK24LauncherAngle extends SubsystemBase
{
    // Create memory objects for both motors for public use
    CANSparkFlex    motor;
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
        motor = new CANSparkFlex(kTopLauncherMotor.ID, MotorType.kBrushless);

        PID = new PIDController(kAnglePID.kP, kAnglePID.kI, kAnglePID.kD);
        PID.setIntegratorRange(-kAnglePID.iZone, kAnglePID.iZone);
        PID.setSetpoint(0.0);

        motor.restoreFactoryDefaults();
        motor.setIdleMode(IdleMode.kBrake); 

        targetAngle = 0.0;
        currentAngle = 0.0;
        encoder = motor.getEncoder();
        
        encoder.setPositionConversionFactor(kConversionFactor);

        encoder.setPosition(0.0);

    }

    public void setTargetAngle(double angle)
    {
        targetAngle = angle;
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
        encoder.setPosition(0.0);
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

        SmartDashboard.putNumber("Current Angle", current_angle);
        SmartDashboard.putNumber("Target Angle", target_angle);
        SmartDashboard.putBoolean("Arm at Setpoint", isAtTargetAngle());
    }

}
