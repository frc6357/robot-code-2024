package frc.robot.subsystems;


import static frc.robot.Constants.LauncherConstants.kAmpRampSpeed;
import static frc.robot.Constants.LauncherConstants.kRampDownSpeed;
import static frc.robot.Constants.LauncherConstants.kSpeakerDefaultLeftSpeed;
import static frc.robot.Constants.LauncherConstants.kSpeakerDefaultLeftSpeedKey;
import static frc.robot.Constants.LauncherConstants.kSpeakerDefaultRightSpeed;
import static frc.robot.Constants.LauncherConstants.kSpeakerDefaultRightSpeedKey;
import static frc.robot.Constants.LauncherConstants.kSpeedTolerance;
import static frc.robot.Constants.LauncherConstants.noteMeasurement;
import static frc.robot.Constants.LauncherConstants.restingRampSpeed;
import static frc.robot.Constants.LauncherConstants.speakerRampSpeed;
import static frc.robot.Ports.launcherPorts.kLaserCanLauncherHigher;
import static frc.robot.Ports.launcherPorts.kLaserCanLauncherLower;
import static frc.robot.Ports.launcherPorts.kLeftLauncherMotor;
import static frc.robot.Ports.launcherPorts.kRightLauncherMotor;
import static frc.robot.Ports.launcherPorts.kTransferMotor;

import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;

import au.grapplerobotics.LaserCan;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.Nat;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.interp.SKInterpolatable;
import frc.robot.interp.SKInterpolator;
import frc.robot.preferences.Pref;
import frc.robot.preferences.SKPreferences;



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
    private LaserCan laserCanLower;
    private LaserCan laserCanHigher;

    double leftTargetSpeed;
    double rightTargetSpeed;

    private SKInterpolator<N3, LaunchConfig> interpolator = new SKInterpolator<>(LaunchConfig::new);


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

        interpolator.put(0.123f, new LaunchConfig(55.0, 0.4, 0.5));
        LaunchConfig config = interpolator.get(0.123f);

        SmartDashboard.putNumber("Interpolator Angle", config.angle);
        SmartDashboard.putNumber("Interpolator Left Speed", config.speedLeft);
        SmartDashboard.putNumber("Interpolator Right Speed", config.speedRight);
        

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

    public void setSpeakerRampRate()
    {
        rightMotor.setOpenLoopRampRate(speakerRampSpeed.get());
        leftMotor.setOpenLoopRampRate(speakerRampSpeed.get());
    }

    public void setRestingRampRate()
    {
        rightMotor.setOpenLoopRampRate(restingRampSpeed.get());
        leftMotor.setOpenLoopRampRate(restingRampSpeed.get());
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
    }
    
    public void testInit()
    {
    }

    static record LaunchConfig(double angle, double speedLeft, double speedRight) implements SKInterpolatable<N3, LaunchConfig> {

        public LaunchConfig() {
            this(0, 0, 0);
        }

        @Override
        public LaunchConfig fromMatrix(Matrix<N1, N3> sourceData) {
            return new LaunchConfig(sourceData.get(0, 0), sourceData.get(0, 1), sourceData.get(0, 2));
        }

        @Override
        public Matrix<N1, N3> toMatrix() {
            return new Matrix<>(Nat.N1(), Nat.N3(), new double[] {angle, speedLeft, speedRight});
        }
    }
}
