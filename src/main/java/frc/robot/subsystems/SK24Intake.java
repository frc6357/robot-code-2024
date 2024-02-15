package frc.robot.subsystems;

import static frc.robot.Ports.intakePorts.kBottomMotor;
import static frc.robot.Ports.intakePorts.kTopMotor;

import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SK24Intake extends SubsystemBase
{
    CANSparkFlex topMotor;
    CANSparkFlex bottomMotor;

    public SK24Intake()
    {
        //Initialize motor objects, assuming intake has 2 motors.
        topMotor = new CANSparkFlex(kTopMotor.ID, MotorType.kBrushless);
        bottomMotor = new CANSparkFlex(kBottomMotor.ID, MotorType.kBrushless);
    }
    public void addFollower(CANSparkFlex bottomMotor)
    {
        bottomMotor.follow(topMotor);
    }
    public void setIntakeSpeed (double speed)
    {
        topMotor.set(speed);
    }
        
    //Return motor speeds
    public double getMotorSpeed ()
    {
        return topMotor.get();
    }
    
    //Stop motors
    public void stopIntake()
    {
        topMotor.stopMotor();
    }
}
