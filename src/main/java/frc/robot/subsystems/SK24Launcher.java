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

    /**
     * Set the speed of the launcher
     * @param speedLeft The speed to set the left launcher motor to as a double value
     * @param speedRight The speed to set the right launcher motor to as a double value
     */
    public void setLauncherSpeed (double speedLeft, double speedRight)
    {
        leftMotor.set(speedLeft);
        rightMotor.set(speedRight);
    }
    /**
     * Set the speed of the launcher
     * @param speed The speed to set the transfer motor to as a double value
     */
    public void setTransferSpeed (double speed)
    {
        transferMotor.set(speed);
    }

    /**
     * Gets the speed of the left launcher motor
     * @return double value of speed for the left launcher motor
     */
    public double getLeftMotorSpeed()
    {
        return leftMotor.get();
    }

    /**
     * Gets the speed of the right launcher motor
     * @return double value of speed for the right launcher motor
     */
    public double getRightMotorSpeed()
    {
        return rightMotor.get();
    }

    /**
     * Gets the speed of the transfer motor
     * @return double value of speed for the transfer motor
     */
    public double getTransferMotorSpeed()
    {
        return transferMotor.get();
    }
    
    /**
     * Determines if the launcher currently has a note
     * @return trure if LaserCAN detects a note, false if no note detected
     */
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

    /**
     * Stops the launcher motors
     */
    public void stopLauncher()
    {
        leftMotor.stopMotor();
        rightMotor.stopMotor();
    }

    /**
     * Stops the transfer motor
     */
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
