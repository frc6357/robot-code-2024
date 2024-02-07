package frc.robot.subsystems;

import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Ports.launcherPorts.*;


public class SK24Launcher extends SubsystemBase
{
    CANSparkFlex topMotor;
    CANSparkFlex bottomMotor;

    public SK24Launcher()
    {
        //Initialize motor objects
        topMotor = new CANSparkFlex(kTopLauncherMotor.ID, MotorType.kBrushless);
        bottomMotor = new CANSparkFlex(kBottomLauncherMotor.ID, MotorType.kBrushless);

    }
    //Make motorR follow motorL
    public void addFollower(CANSparkFlex bottomMotor)
    {
        bottomMotor.follow(topMotor);
    }

    //Set motor speeds
    public void setMotorSpeed (double speed)
    {
        topMotor.set(speed);
    }
        
    //Return motor speeds
    public double getMotorSpeed ()
    {
        return topMotor.get();
    }
    
    //Stop motors
    public void stopMotor()
    {
        topMotor.stopMotor();
    }

}
