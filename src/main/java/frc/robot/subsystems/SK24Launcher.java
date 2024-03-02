package frc.robot.subsystems;

import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel.MotorType;

import au.grapplerobotics.LaserCan;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Ports.launcherPorts.*;
import static frc.robot.Constants.LauncherConstants.*;


public class SK24Launcher extends SubsystemBase
{
    // Create memory objects for both motors for public use
    CANSparkFlex leftMotor;
    CANSparkFlex rightMotor;
    CANSparkFlex transferMotor;
    private boolean currLauncherState;
    private boolean pastLauncherState;
    private LaserCan laserCan;


    //Constructor for public command access
    public SK24Launcher()
    {
        //Initialize motor objects
        leftMotor = new CANSparkFlex(kLeftLauncherMotor.ID, MotorType.kBrushless);
        rightMotor = new CANSparkFlex(kRightLauncherMotor.ID, MotorType.kBrushless);
        transferMotor = new CANSparkFlex(kTransferMotor.ID, MotorType.kBrushless);

        laserCan = new LaserCan(kLaserCanLauncher.ID);

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
    public void setLauncherSpeed (double speedLeft, double speedRight)
    {
        leftMotor.set(speedLeft);
        rightMotor.set(speedRight);
    }
        
    public void setTransferSpeed (double speed)
    {
        transferMotor.set(speed);
    }

    //Return motor speeds
    public double getLeftMotorSpeed()
    {
        return leftMotor.get();
    }

    //Return motor speeds
    public double getRightMotorSpeed()
    {
        return rightMotor.get();
    }
    //Return motor speeds
    public double getTransferMotorSpeed()
    {
        return transferMotor.get();
    }
    
    public boolean haveNote()
    {
        LaserCan.Measurement measurement = laserCan.getMeasurement();
        if (measurement != null && measurement.status == LaserCan.LASERCAN_STATUS_VALID_MEASUREMENT) {
          if(measurement.distance_mm < noteMeasurement)
          {
            return true;
          }
        } 
        return false;
    }

    //Stop motors
    public void stopLauncher()
    {
        leftMotor.stopMotor();
        rightMotor.stopMotor();
    }

    public void stopTransfer(){
        transferMotor.stopMotor();
    }

    public void periodic()
    {
        SmartDashboard.putBoolean("HaveLauncherNote", haveNote());
        if(getLeftMotorSpeed() != 0.0 || getRightMotorSpeed() != 0.0)
        {
            currLauncherState = true;
        }
        if(currLauncherState != pastLauncherState){
            pastLauncherState = currLauncherState;
            SmartDashboard.putBoolean("Launching", currLauncherState);
        }
    }

}
