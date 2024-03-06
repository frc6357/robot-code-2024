package frc.robot.subsystems;

import static frc.robot.Constants.LauncherConstants.ENCODER_RPM_TO_MPS;
import static frc.robot.Constants.LauncherConstants.kLeftLauncherFF;
import static frc.robot.Constants.LauncherConstants.kLeftLauncherP;
import static frc.robot.Constants.LauncherConstants.kRightLauncherFF;
import static frc.robot.Constants.LauncherConstants.kRightLauncherP;
import static frc.robot.Constants.LauncherConstants.kVelocityTolerance;
import static frc.robot.Constants.LauncherConstants.noteMeasurement;
import static frc.robot.Ports.launcherPorts.*;

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
    private boolean currLauncherState;
    private boolean pastLauncherState;
    private LaserCan laserCan1;
    private LaserCan laserCan2;

    double leftTargetVelocity;
    double rightTargetVelocity;


    //Constructor for public command access
    public SK24Launcher()
    {
        //Initialize motor objects
        leftMotor = new CANSparkFlex(kLeftLauncherMotor.ID, MotorType.kBrushless);
        rightMotor = new CANSparkFlex(kRightLauncherMotor.ID, MotorType.kBrushless);
        transferMotor = new CANSparkFlex(kTransferMotor.ID, MotorType.kBrushless);

        leftMotor.restoreFactoryDefaults();
        rightMotor.restoreFactoryDefaults();

        leftMotor.setInverted(true);
        rightMotor.setInverted(false);
        transferMotor.setInverted(true);
        
        laserCan1 = new LaserCan(kLaserCanLauncher1.ID);
        laserCan2 = new LaserCan(kLaserCanLauncher2.ID);

        currLauncherState = false;
        pastLauncherState = false;
        SmartDashboard.putBoolean("Launching", currLauncherState);
        
        encoderL = leftMotor.getEncoder();
        encoderR = rightMotor.getEncoder();

         //Pid setup
        // leftPidController = leftMotor.getPIDController();
        // rightPidController = rightMotor.getPIDController();

        // leftPidController.setP(kLeftLauncherP);
        // rightPidController.setP(kRightLauncherP);

        // leftPidController.setFF(kLeftLauncherFF);
        // rightPidController.setFF(kRightLauncherFF);
        
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

    public double getLeftVelocity()
    {
        return encoderL.getVelocity();
    }
    
    public double getRightVelocity()
    {
        return encoderR.getVelocity();
    }

    public double getLeftTargetVelocity()
    {
        return leftTargetVelocity;
    }

    public double getRightTargetVelocity()
    {
        return rightTargetVelocity;
    }


    public boolean isRightAtTargetPosition()
    {
        return Math.abs(getRightVelocity() - getRightTargetVelocity()) < kVelocityTolerance;
    }

    public boolean isLeftAtTargetPosition()
    {
        return Math.abs(getLeftVelocity() - getLeftTargetVelocity()) < kVelocityTolerance;
    }

    /**
     * Set launcher speeds in m/s
     * @param speedLeft speed of left launcher wheels in m/s
     * @param speedRight speed of right launcher wheels in m/s
     */
    public void setLauncherSpeed (double speedLeft, double speedRight)
    {
        // leftTargetVelocity = speedLeft;
        // rightTargetVelocity = speedRight;
        // leftPidController.setReference(speedLeft, CANSparkBase.ControlType.kVelocity);
        // rightPidController.setReference(speedRight, CANSparkBase.ControlType.kVelocity);

        rightMotor.set(speedRight);
        leftMotor.set(speedLeft);
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
        SmartDashboard.putNumber("Left Velocity", getLeftMotorSpeed());
        SmartDashboard.putNumber("Right Velocity", getRightMotorSpeed());

        SmartDashboard.putNumber("Left Target Velocity", getLeftTargetVelocity());
        SmartDashboard.putNumber("Right Target Velocity", getRightTargetVelocity());


        if(getLeftVelocity() != 0.0 || getRightVelocity() != 0.0)
        {
            currLauncherState = true;
        }
        if(currLauncherState != pastLauncherState){
            pastLauncherState = currLauncherState;
            SmartDashboard.putBoolean("Launching", currLauncherState);
        }
    }

}
