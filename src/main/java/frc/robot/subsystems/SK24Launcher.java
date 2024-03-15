package frc.robot.subsystems;

import static frc.robot.Constants.LauncherConstants.kLeftLauncherP;
import static frc.robot.Constants.LauncherConstants.kRightLauncherP;
import static frc.robot.Constants.LauncherConstants.kSpeedTolerance;
import static frc.robot.Constants.LauncherConstants.noteMeasurement;
import static frc.robot.Ports.launcherPorts.kLaserCanLauncher1;
import static frc.robot.Ports.launcherPorts.kLaserCanLauncher2;
import static frc.robot.Ports.launcherPorts.kLeftLauncherMotor;
import static frc.robot.Ports.launcherPorts.kRightLauncherMotor;
import static frc.robot.Ports.launcherPorts.kTransferMotor;

import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;

import au.grapplerobotics.LaserCan;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class SK24Launcher extends SubsystemBase
{
    // Create memory objects for both motors for public use
    CANSparkFlex leftMotor;
    CANSparkFlex rightMotor;
    CANSparkFlex transferMotor;

    SparkPIDController leftPidController;
    SparkPIDController rightPidController;
    RelativeEncoder encoderL;
    RelativeEncoder encoderR;
    private LaserCan laserCan1;
    private LaserCan laserCan2;

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
        
        laserCan1 = new LaserCan(kLaserCanLauncher1.ID);
        laserCan2 = new LaserCan(kLaserCanLauncher2.ID);

        
        encoderL = leftMotor.getEncoder();
        encoderR = rightMotor.getEncoder();

        //  //Pid setup
        // leftPidController = leftMotor.getPIDController();
        // rightPidController = rightMotor.getPIDController();

        // leftPidController.setP(kLeftLauncherP);
        // rightPidController.setP(kRightLauncherP);
        
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
        // leftPidController.setReference(speedLeft, CANSparkBase.ControlType.kDutyCycle);
        // rightPidController.setReference(speedRight, CANSparkBase.ControlType.kDutyCycle);

        rightMotor.set(speedRight);
        leftMotor.set(speedLeft);
    }


    /**
     * Sets the speed of the transfer
     * @param speed The speed to set for left. Value should be between -1.0 and 1.0.
     */    
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
        LaserCan.Measurement measurement1 = laserCan1.getMeasurement();
        LaserCan.Measurement measurement2 = laserCan2.getMeasurement();

        if ((measurement1 != null && measurement1.status == LaserCan.LASERCAN_STATUS_VALID_MEASUREMENT)) {
            SmartDashboard.putNumber("LaserCan distance 1", measurement1.distance_mm);
          if(measurement1.distance_mm < noteMeasurement)
          {
            return true;
          }
        } 

        if ((measurement2 != null && measurement2.status == LaserCan.LASERCAN_STATUS_VALID_MEASUREMENT)) {
            SmartDashboard.putNumber("LaserCan distance 2", measurement2.distance_mm);
          if(measurement2.distance_mm < noteMeasurement)
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
        SmartDashboard.putNumber("Left Launcher Speed", getLeftMotorSpeed());
        SmartDashboard.putNumber("Right Launcher Speed", getRightMotorSpeed());

        SmartDashboard.putNumber("Left Launcher Target Speed", getLeftTargetSpeed());
        SmartDashboard.putNumber("Right Launcher Target Speed", getRightTargetSpeed());
        SmartDashboard.putBoolean("Launcher Full Speed", isFullSpeed());
    }

}
