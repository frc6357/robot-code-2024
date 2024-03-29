package frc.robot.subsystems;


import static frc.robot.Constants.LauncherConstants.*;
import static frc.robot.Ports.launcherPorts.*;

import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;

import au.grapplerobotics.LaserCan;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class SK24Launcher extends SubsystemBase
{
    // Create memory objects for both motors for public use
    CANSparkFlex leftMotor;
    CANSparkFlex rightMotor;
    CANSparkFlex transferMotor;
    double shuffleSpeed = 0.0;
    boolean isTest = false;

    SparkPIDController leftPidController;
    SparkPIDController rightPidController;
    RelativeEncoder encoderL;
    RelativeEncoder encoderR;
    private LaserCan laserCanLower;
    private LaserCan laserCanHigher;

    double leftTargetSpeed;
    double rightTargetSpeed;


    //Constructor for public command access
    public SK24Launcher()
    {
        //Initialize motor objects
        leftMotor = new CANSparkFlex(kLeftLauncherMotor.ID, MotorType.kBrushless);
        rightMotor = new CANSparkFlex(kRightLauncherMotor.ID, MotorType.kBrushless);
        transferMotor = new CANSparkFlex(kTransferMotor.ID, MotorType.kBrushless);

        leftMotor.setInverted(true);
        rightMotor.setInverted(false);
        transferMotor.setInverted(true);
        
        laserCanLower = new LaserCan(kLaserCanLauncherLower.ID);
        laserCanHigher = new LaserCan(kLaserCanLauncherHigher.ID);

        
        encoderL = leftMotor.getEncoder();
        encoderR = rightMotor.getEncoder();

        setSpeakerRampRate();
        
        SmartDashboard.putNumber("Left launcher", kSpeakerDefaultLeftSpeed);
        SmartDashboard.putNumber("Right launcher", kSpeakerDefaultRightSpeed);

    }

    public double getLeftTargetSpeed()
    {
        return leftTargetSpeed;
    }

    public double getRightTargetSpeed()
    {
        return rightTargetSpeed;
    }


    public boolean isFullSpeed()
    {
        return (Math.abs(getRightMotorSpeed() - getRightTargetSpeed()) < kSpeedTolerance) && (Math.abs(getLeftMotorSpeed() - getLeftTargetSpeed()) < kSpeedTolerance);
    }
    

    /**
     * Sets the speed of the launcher
     * @param speedLeft The speed to set for left. Value should be between -1.0 and 1.0.
     * @param speedRight The speed to set for right. Value should be between -1.0 and 1.0.
     */
    public void setLauncherSpeed (double speedLeft, double speedRight)
    {
        leftTargetSpeed = speedLeft;
        rightTargetSpeed = speedRight;

        rightMotor.set(speedRight);
        leftMotor.set(speedLeft);
        // double left = SmartDashboard.getNumber("Left launcher", speedLeft);
        // double right = SmartDashboard.getNumber("Right launcher", speedRight);
    }


    /**
     * Sets the speed of the transfer
     * @param speed The speed to set for left. Value should be between -1.0 and 1.0.
     */    
    public void setTransferSpeed (double speed)
    {
        if(!isTest)
        {
            transferMotor.set(speed);
        }else{
            transferMotor.set(shuffleSpeed);
        }
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

    public void setSpeakerRampRate()
    {
        rightMotor.setOpenLoopRampRate(kSpeakerRampSpeed);
        leftMotor.setOpenLoopRampRate(kSpeakerRampSpeed);
    }

    public void setAmpRampRate()
    {
        rightMotor.setOpenLoopRampRate(kAmpRampSpeed);
        leftMotor.setOpenLoopRampRate(kAmpRampSpeed);
    }

    public void rampDown()
    {
        rightMotor.setOpenLoopRampRate(kRampDownSpeed);
        leftMotor.setOpenLoopRampRate(kRampDownSpeed);
    }

    public double getCurrentRampRate()
    {
        return rightMotor.getOpenLoopRampRate();
    }
    
    public boolean haveLowerNote()
    {
        LaserCan.Measurement measurementLower = laserCanLower.getMeasurement();

        if ((measurementLower != null && measurementLower.status == LaserCan.LASERCAN_STATUS_VALID_MEASUREMENT)) {
            SmartDashboard.putNumber("LaserCan distance lower", measurementLower.distance_mm);
          if(measurementLower.distance_mm < noteMeasurement)
          {
            return true;
          }
        } 

        return false;
    }

    public boolean haveHigherNote()
    {
         LaserCan.Measurement measurementHigher = laserCanHigher.getMeasurement();
        
        if ((measurementHigher != null && measurementHigher.status == LaserCan.LASERCAN_STATUS_VALID_MEASUREMENT)) {
            SmartDashboard.putNumber("LaserCan distance higher", measurementHigher.distance_mm);
          if(measurementHigher.distance_mm < noteMeasurement)
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
        SmartDashboard.putBoolean("HaveLauncherNote", haveHigherNote());
        SmartDashboard.putBoolean("HaveLauncherLowerNote", haveLowerNote());
        SmartDashboard.putNumber("Left Launcher Speed", getLeftMotorSpeed());
        SmartDashboard.putNumber("Right Launcher Speed", getRightMotorSpeed());

        SmartDashboard.putNumber("Left Launcher Target Speed", getLeftTargetSpeed());
        SmartDashboard.putNumber("Right Launcher Target Speed", getRightTargetSpeed());
        SmartDashboard.putBoolean("Launcher Full Speed", isFullSpeed());

    }
    
    public void testPeriodic()
    {
        shuffleSpeed = Preferences.getDouble("Transfer Speed", kTransferSpeed);
    }
    
    public void testInit()
    {
        isTest = true;
        Preferences.initDouble("Transfer Speed", kTransferSpeed);
    }

}
