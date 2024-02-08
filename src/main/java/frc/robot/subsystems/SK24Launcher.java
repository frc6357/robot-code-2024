package frc.robot.subsystems;

import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Ports.launcherPorts.*;


public class SK24Launcher extends SubsystemBase
{
    // Create memory objects for both motors for public use
    CANSparkFlex topMotor;
    CANSparkFlex bottomMotor;

    //Constructor for public command access
    public SK24Launcher()
    {
        //Initialize motor objects
        topMotor = new CANSparkFlex(kTopLauncherMotor.ID, MotorType.kBrushless);
        bottomMotor = new CANSparkFlex(kBottomLauncherMotor.ID, MotorType.kBrushless);

        addFollower(bottomMotor, topMotor);

    }

    /**
     * Make a follower motor follow a leader motor
     * @param followerMotor The motor controller that is following
     * @param leaderMotor The motor controller that is being followed
     **/
    public void addFollower(CANSparkFlex followerMotor, CANSparkFlex leaderMotor)
    {
        followerMotor.follow(leaderMotor);
    }

    //Set motor speeds
    public void setLauncherSpeed (double speed)
    {
        topMotor.set(speed);
    }
        
    //Return motor speeds
    public double getLauncherMotorSpeed()
    {
        return topMotor.get();
    }
    
    //Stop motors
    public void stopLauncher()
    {
        topMotor.stopMotor();
    }

}
