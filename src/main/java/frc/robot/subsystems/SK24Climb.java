package frc.robot.subsystems;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.Slot1Configs;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.ClimbConstants.*;

import static frc.robot.Ports.climbPorts.*;

public class SK24Climb extends SubsystemBase
{
    //Create memory motor objects
    TalonFX motorR;
    TalonFX motorL;
    //Create memory PID object
    PIDController PID;

    //Constructor for public command access
    public SK24Climb()
    {
        //Initialize motor objects
        motorR = new TalonFX(kRightClimbMotor.ID);
        motorL = new TalonFX(kLeftClimbMotor.ID);
        Slot0Configs configsLeft = new Slot0Configs();
        configsLeft.kP = leftClimb.kP;
        configsLeft.kI = leftClimb.kI;
        configsLeft.kD = leftClimb.kD;
        motorL.getConfigurator().apply(configsLeft);

        Slot1Configs configsRight = new Slot1Configs();
        configsRight.kP = leftClimb.kP;
        configsRight.kI = leftClimb.kI;
        configsRight.kD = leftClimb.kD;
        motorL.getConfigurator().apply(configsRight);

    }

    public void setRightHook(double location)
    {
        double rotationLocation = location * climbHeight;
        rotationLocation /= climbConversion;
        PositionVoltage voltage = new PositionVoltage(rotationLocation);
        motorR.setControl(voltage);
    }
    
    public void setLeftHook(double location)
    {
        double rotationLocation = location * climbHeight;
        rotationLocation /= climbConversion;
        PositionVoltage voltage = new PositionVoltage(rotationLocation);
        motorR.setControl(voltage);
    }

    public double getLeftPosition()
    {
        return (motorL.getPosition().getValueAsDouble() * climbConversion) / climbHeight;
    }
    
    public double getRightPosition()
    {
        return (motorR.getPosition().getValueAsDouble() * climbConversion) / climbHeight;
    }

    public void stopHooks()
    {
        motorL.stopMotor();
        motorR.stopMotor();
    }
}
