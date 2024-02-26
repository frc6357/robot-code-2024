package frc.robot.subsystems;

import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel.MotorType;

import au.grapplerobotics.ConfigurationFailedException;
import au.grapplerobotics.LaserCan;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Ports.launcherPorts.*;


public class SK24Launcher extends SubsystemBase
{
    // Create memory objects for both motors and lidars for public use
    CANSparkFlex leftMotor;
    CANSparkFlex rightMotor;
    CANSparkFlex transferMotor;
    LaserCan leftLaser;
    LaserCan rightLaser;

    LaserCan.Measurement measurement;

    //Constructor for public command access
    public SK24Launcher()
    {
        //Initialize motor objects
        leftMotor = new CANSparkFlex(kLeftLauncherMotor.ID, MotorType.kBrushless);
        rightMotor = new CANSparkFlex(kRightLauncherMotor.ID, MotorType.kBrushless);
        transferMotor = new CANSparkFlex(kTransferMotor.ID, MotorType.kBrushless);
        leftLaser = new LaserCan(kLeftLaser.ID);
        setLaserConfig(leftLaser);
        rightLaser = new LaserCan(kRightLaser.ID); 
        setLaserConfig(rightLaser);
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
    //Return motor speed
    public double getTransferMotorSpeed()
    {
        return transferMotor.get();
    }
    
    //Stops launcher motors
    public void stopLauncher()
    {
        leftMotor.stopMotor();
        rightMotor.stopMotor();
    }

    //Stops transfer motor
    public void stopTransfer()
    {
        transferMotor.stopMotor();
    }

    //Sets the configuration for the LaserCan
    public void setLaserConfig(LaserCan laser)
    {
        try
        {
            laser.setRangingMode(LaserCan.RangingMode.SHORT);
            laser.setRegionOfInterest(new LaserCan.RegionOfInterest(8, 8, 10, 10));
            laser.setTimingBudget(LaserCan.TimingBudget.TIMING_BUDGET_33MS);
        }
        catch (ConfigurationFailedException e)
        {
            System.out.println("Configuration failed! " + e);
        }
    }

    //Returns the distance in mm of the laser to a contact point
    public double getDistance(LaserCan laser)
    {
        measurement = laser.getMeasurement();
        if(measurement != null && measurement.status == LaserCan.LASERCAN_STATUS_VALID_MEASUREMENT)
        {return measurement.distance_mm;}
        else  {return 0.0;}
    }

    //Returns a boolean to determine the status of a note in the launcher
    public boolean notePresent(double distance)
    {
        if((distance <= 127))  {return true;}
        else  {return false;}
    }


}
