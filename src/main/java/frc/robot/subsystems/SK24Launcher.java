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

    SparkPIDController leftPidController;
    SparkPIDController rightPidController;
    RelativeEncoder encoderL;
    RelativeEncoder encoderR;
    

    double leftTargetSpeed;
    double rightTargetSpeed;

    private SKInterpolator<N3, LaunchConfig> interpolator = new SKInterpolator<>(LaunchConfig::new);


    //Constructor for public command access
    public SK24Launcher()
    {
        //Initialize motor objects
        leftMotor = new CANSparkFlex(kLeftLauncherMotor.ID, MotorType.kBrushless);
        rightMotor = new CANSparkFlex(kRightLauncherMotor.ID, MotorType.kBrushless);

        leftMotor.setInverted(true);
        rightMotor.setInverted(false);

        
        encoderL = leftMotor.getEncoder();
        encoderR = rightMotor.getEncoder();

        setSpeakerRampRate();
        
        SmartDashboard.putNumber("Left launcher", kSpeakerDefaultLeftSpeed);
        SmartDashboard.putNumber("Right launcher", kSpeakerDefaultRightSpeed);

        interpolator.put(0.1f, new LaunchConfig(0.0, 0.4, 0.1));
        interpolator.put(0.5f, new LaunchConfig(25.0, 0.5, 0.2));
        interpolator.put(0.9f, new LaunchConfig(50.0, 0.6, 0.3));
        LaunchConfig config = interpolator.get(0.123f);
        SmartDashboard.putNumber("Interpolator Angle", config.angle);
        SmartDashboard.putNumber("Interpolator Left Speed", config.speedLeft);
        SmartDashboard.putNumber("Interpolator Right Speed", config.speedRight);
        

    }

    public LaunchConfig getInterpolatedValues(double distance){
        return interpolator.get(distance);
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
    
   
    //Stop motors
    public void stopLauncher()
    {
        leftMotor.stopMotor();
        rightMotor.stopMotor();
    }

    

    public void periodic()
    {
        
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

    public static record LaunchConfig(double angle, double speedLeft, double speedRight) implements SKInterpolatable<N3, LaunchConfig> {

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
