package frc.robot.subsystems;

import static frc.robot.Constants.ClimbConstants.climbConversion;
import static frc.robot.Constants.ClimbConstants.kClimbMotorMaxOutput;
import static frc.robot.Constants.ClimbConstants.kClimbMotorMinOutput;
import static frc.robot.Constants.ClimbConstants.kPositionTolerance;
import static frc.robot.Constants.ClimbConstants.rightClimb;
import static frc.robot.Ports.climbPorts.kRightClimbMotor;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Ports.climbPorts.*;

public class SK24Climb extends SubsystemBase
{
    //Create memory motor objects
    CANSparkFlex motorR;
    CANSparkFlex motorL;
    //Create memory PID object
    PIDController rPID;
    // PIDController lPID;
    // double LtargetPosition;
    // double LcurrentPosition;

    double RtargetPosition;
    double RcurrentPosition;

    // RelativeEncoder encoderL;
    RelativeEncoder encoderR;

    //Constructor for public command access
    public SK24Climb()
    {
        //Initialize motor objects
        rPID = new PIDController(rightClimb.kP, rightClimb.kI, rightClimb.kD);
        rPID.setSetpoint(0.0);

        // lPID = new PIDController(leftClimb.kP, leftClimb.kI, leftClimb.kD);
        // lPID.setSetpoint(0.0);

        // accelLimit = new SlewRateLimiter(kPositiveAccelLimit, kNegativeAccelLimit, 0.0);

        motorR = new CANSparkFlex(kRightClimbMotor.ID, MotorType.kBrushless);
        // motorL = new CANSparkFlex(kLeftClimbMotor.ID, MotorType.kBrushless);

        RelativeEncoder encoderR = motorR.getEncoder();
        encoderR.setPositionConversionFactor(climbConversion);

        // RelativeEncoder encoderL = motorL.getEncoder();
        // encoderL.setPositionConversionFactor(climbConversion);

        motorR.restoreFactoryDefaults();
        motorR.setIdleMode(IdleMode.kBrake); 

        // motorL.restoreFactoryDefaults();
        // motorL.setIdleMode(IdleMode.kBrake); 
        

        RtargetPosition = 0.0;
        RcurrentPosition = 0.0;

        // LtargetPosition = 0.0;
        // LcurrentPosition = 0.0;


    }
    /**
     * Set the location of the right hook
     * @param location The location you want the right hook
     **/
    public void setRightHook(double location)
    {
        RtargetPosition = location;
        rPID.setSetpoint(location);
    }
    
    // public void setLeftHook(double location)
    // {
    //     LtargetPosition = location;
    //     lPID.setSetpoint(location);
    // }

    /**
     * Make the left hook run at a specific speed
     * @param speed The speed to set. Value should be between -1.0 and 1.0.
     **/
    public void runLeftHook(double speed)
    {
        motorL.set(speed);
    }
    /**
     * Make the right hook run at a specific speed
     * @param speed The speed to set. Value should be between -1.0 and 1.0.
     **/
    public void runRightHook(double speed)
    {
        motorR.set(speed);
    }
    // public double getLeftPosition()
    // {
    //     return encoderL.getPosition();
    // }
    
    /**
     * Get the current position of the right hook
     * @return The number of rotations of the motor
     **/
    public double getRightPosition()
    {
        return encoderR.getPosition();
    }

    // public double getLeftCurrentPosition(){
    //     return LcurrentPosition;
    // }
    
    /**
     * Get the target position of the right hook
     * @return The current position between 0.0 and 1.0
     **/
    public double getRightTargetPosition(){
        return RtargetPosition;
    }

    // public double getLeftTargetPosition(){
    //     return LcurrentPosition;
    // }

    /**
     * Check if the right hook is at the target position
     * @return True if it is at the target position, false if it is not at the target position
     **/
    public boolean isRightAtTargetPosition()
    {
        return Math.abs(getRightPosition() - getRightTargetPosition()) < kPositionTolerance;
    }

    // public boolean isLeftAtTargetPosition()
    // {
    //     return Math.abs(getLeftCurrentPosition() - getLeftTargetPosition()) < kPositionTolerance;
    // }

    // public void resetLeftPosition(){
    //     encoderL.setPosition(0.0);
    // }

    /**
     * Resets the right position to a specified position
     */
    public void resetRightPosition(){
        encoderR.setPosition(0.0);
    }

    /**
     * Stops the hooks (climb mechanism) from moving
     */
    public void stopHooks()
    {
        // motorL.stopMotor();
        motorR.stopMotor();
    }
    /**
     * Periodically check on the current position, target position, and run necessary calculations.
     */
    @Override
    public void periodic(){
        
        double r_current_position = getRightPosition();
        double r_target_position = getRightTargetPosition();

        // double l_current_position = getLeftCurrentPosition();
        // double l_target_position = getLeftTargetPosition();

        // Calculates motor speed and puts it within operating range
        double rSpeed = MathUtil.clamp(rPID.calculate(r_current_position), kClimbMotorMinOutput, kClimbMotorMaxOutput);
        // speed = accelLimit.calculate(speed);
        motorR.set(rSpeed); 

        // Calculates motor speed and puts it within operating range
        // double lSpeed = MathUtil.clamp(lPID.calculate(l_current_position), kClimbMotorMinOutput, kClimbMotorMaxOutput);
        // speed = accelLimit.calculate(speed);
        // motorL.set(lSpeed); 

        SmartDashboard.putNumber("Right Current Position", r_current_position);
        SmartDashboard.putNumber("Right Target Position", r_target_position);
        SmartDashboard.putBoolean("Right Arm at Setpoint", isRightAtTargetPosition());

        // SmartDashboard.putNumber("Left Current Position", l_current_position);
        // SmartDashboard.putNumber("Left Target Position", l_target_position);
        // SmartDashboard.putBoolean("Left Arm at Setpoint", isLeftAtTargetPosition());
    }
}
