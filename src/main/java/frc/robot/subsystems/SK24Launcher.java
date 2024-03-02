package frc.robot.subsystems;

import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Ports.launcherPorts.*;


public class SK24Launcher extends SubsystemBase
{
    // Create memory objects for both motors for public use
    CANSparkFlex topMotor;
    CANSparkFlex bottomMotor;
    CANSparkFlex transferMotor;
    private boolean currLauncherState;
    private boolean pastLauncherState;

    //Constructor for public command access
    public SK24Launcher()
    {
        //Initialize motor objects
        topMotor = new CANSparkFlex(kTopLauncherMotor.ID, MotorType.kBrushless);
        bottomMotor = new CANSparkFlex(kBottomLauncherMotor.ID, MotorType.kBrushless);
        transferMotor = new CANSparkFlex(kTransferMotor.ID, MotorType.kBrushless);
        currLauncherState = false;
        pastLauncherState = false;
        SmartDashboard.putBoolean("Launching", currLauncherState);
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
    public void setLauncherSpeed (double speedTop, double speedBottom)
    {
        topMotor.set(speedTop);
        bottomMotor.set(speedBottom);
    }
        
    public void setTransferSpeed (double speed)
    {
        transferMotor.set(speed);
    }

    //Return motor speeds
    public double getTopMotorSpeed()
    {
        return topMotor.get();
    }

    //Return motor speeds
    public double getBottomMotorSpeed()
    {
        return bottomMotor.get();
    }
    //Return motor speeds
    public double getTransferMotorSpeed()
    {
        return transferMotor.get();
    }
    
    //Stop motors
    public void stopLauncher()
    {
        topMotor.stopMotor();
        bottomMotor.stopMotor();
    }

    public void stopTransfer(){
        transferMotor.stopMotor();
    }

    public void periodic()
    {
        if(getTopMotorSpeed() != 0.0 || getBottomMotorSpeed() != 0.0)
        {
            currLauncherState = true;
        }
        if(currLauncherState != pastLauncherState){
            pastLauncherState = currLauncherState;
            SmartDashboard.putBoolean("Launching", currLauncherState);
        }
    }

}
