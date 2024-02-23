package frc.robot.subsystems;

import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.ClimbConstants.*;

import static frc.robot.Ports.climbPorts.*;

public class SK24Climb extends SubsystemBase
{
    //Create memory motor objects
    CANSparkFlex motorR;
    CANSparkFlex motorL;
    //Create memory PID object
    PIDController PID;

    //Constructor for public command access
    public SK24Climb()
    {
        //Initialize motor objects
        motorR = new CANSparkFlex(kRightClimbMotor.ID, MotorType.kBrushless);
        motorL = new CANSparkFlex(kLeftClimbMotor.ID, MotorType.kBrushless);

    }

    public void setRightHook(double location, double speed)
    {
        double rotationLocation = location * climbHeight;
        rotationLocation /= climbConversion;
        motorR.set(speed);
    }
    
    public void setLeftHook(double location, double speed)
    {
        double rotationLocation = location * climbHeight;
        rotationLocation /= climbConversion;
        motorL.set(speed);
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
        return (motorL.get() * climbConversion) / climbHeight;
    }
    
    public double getRightPosition()
    {
        return (motorR.get() * climbConversion) / climbHeight;
    }

    public void stopHooks()
    {
        motorL.stopMotor();
        motorR.stopMotor();
    }
}
