package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import static frc.robot.Constants.ClimbConstants.*;

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
    PIDController lPID;
    double LtargetAngle;
    double LcurrentAngle;

    double RtargetAngle;
    double RcurrentAngle;

    RelativeEncoder encoderL;
    RelativeEncoder encoderR;

    //Constructor for public command access
    public SK24Climb()
    {
        //Initialize motor objects
        rPID = new PIDController(rightClimb.kP, rightClimb.kI, rightClimb.kD);
        rPID.setSetpoint(0.0);

        lPID = new PIDController(leftClimb.kP, leftClimb.kI, leftClimb.kD);
        lPID.setSetpoint(0.0);

        // accelLimit = new SlewRateLimiter(kPositiveAccelLimit, kNegativeAccelLimit, 0.0);

        motorR = new CANSparkFlex(kRightClimbMotor.ID, MotorType.kBrushless);
        motorL = new CANSparkFlex(kLeftClimbMotor.ID, MotorType.kBrushless);

        RelativeEncoder encoderR = motorR.getEncoder();
        encoderR.setPositionConversionFactor(climbConversion);

        RelativeEncoder encoderL = motorL.getEncoder();
        encoderL.setPositionConversionFactor(climbConversion);

        motorR.restoreFactoryDefaults();
        motorR.setIdleMode(IdleMode.kBrake); 

        motorL.restoreFactoryDefaults();
        motorL.setIdleMode(IdleMode.kBrake); 
        

        RtargetAngle = 0.0;
        RcurrentAngle = 0.0;

        LtargetAngle = 0.0;
        LcurrentAngle = 0.0;


    }

    public void setRightHook(double location)
    {
        RtargetAngle = location;
        rPID.setSetpoint(location);
    }
    
    public void setLeftHook(double location)
    {
        LtargetAngle = location;
        lPID.setSetpoint(location);
    }

    public void runLeftHook(double speed)
    {
        motorL.set(speed);
    }

    public void runRightHook(double speed)
    {
        motorR.set(speed);
    }
    public double getLeftPosition()
    {
        return encoderL.getPosition();
    }
    
    public double getRightPosition()
    {
        return encoderR.getPosition();
    }

    public double getRightCurrentAngle(){
        return RcurrentAngle;
    }

    public double getLeftCurrentAngle(){
        return LcurrentAngle;
    }

    public double getRightTargetAngle(){
        return RcurrentAngle;
    }

    public double getLeftTargetAngle(){
        return LcurrentAngle;
    }


    public boolean isRightAtTargetAngle()
    {
        return Math.abs(getRightCurrentAngle() - getRightTargetAngle()) < kAngleTolerance;
    }

    public boolean isLeftAtTargetAngle()
    {
        return Math.abs(getLeftCurrentAngle() - getLeftTargetAngle()) < kAngleTolerance;
    }

    public void resetLeftAngle(){
        encoderL.setPosition(0.0);
    }

    public void resetRightAngle(){
        encoderR.setPosition(0.0);
    }

    public void stopHooks()
    {
        motorL.stopMotor();
        motorR.stopMotor();
    }

    @Override
    public void periodic(){
        
        double r_current_angle = getRightCurrentAngle();
        double r_target_angle = getRightTargetAngle();

        double l_current_angle = getLeftCurrentAngle();
        double l_target_angle = getLeftTargetAngle();

        // Calculates motor speed and puts it within operating range
        double rSpeed = MathUtil.clamp(rPID.calculate(r_current_angle), kClimbMotorMinOutput, kClimbMotorMaxOutput);
        // speed = accelLimit.calculate(speed);
        motorR.set(rSpeed); 

        // Calculates motor speed and puts it within operating range
        double lSpeed = MathUtil.clamp(lPID.calculate(l_current_angle), kClimbMotorMinOutput, kClimbMotorMaxOutput);
        // speed = accelLimit.calculate(speed);
        motorL.set(lSpeed); 

        SmartDashboard.putNumber("Right Current Angle", r_current_angle);
        SmartDashboard.putNumber("Right Target Angle", r_target_angle);
        SmartDashboard.putBoolean("Right Arm at Setpoint", isRightAtTargetAngle());

        SmartDashboard.putNumber("Left Current Angle", l_current_angle);
        SmartDashboard.putNumber("Left Target Angle", l_target_angle);
        SmartDashboard.putBoolean("Left Arm at Setpoint", isLeftAtTargetAngle());
    }
}
