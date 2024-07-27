package frc.robot.subsystems;


import static frc.robot.Constants.LauncherConstants.kAmpRampSpeed;
import static frc.robot.Constants.LauncherConstants.kQuickRampSpeed;
import static frc.robot.Constants.LauncherConstants.kRampDownSpeed;
import static frc.robot.Constants.LauncherConstants.kSpeakerDefaultLeftSpeed;
import static frc.robot.Constants.LauncherConstants.kSpeakerDefaultRightSpeed;
import static frc.robot.Constants.LauncherConstants.kSpeedTolerance;
import static frc.robot.Constants.LauncherConstants.restingRampSpeed;
import static frc.robot.Constants.LauncherConstants.speakerRampSpeed;
import static frc.robot.Ports.launcherPorts.kLeftLauncherMotor;
import static frc.robot.Ports.launcherPorts.kRightLauncherMotor;

import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.Nat;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.interp.SKInterpolatable;
import frc.robot.interp.SKInterpolator;



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
    boolean running;

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

        interpolator.put(1.74, new LaunchConfig(42.0, 0.4, 0.5));
        //interpolator.put(2.7, new LaunchConfig(36.0, 0.4, 0.5)); TODO - check value
        interpolator.put(2.8, new LaunchConfig(33.5, 0.4, 0.5));
        interpolator.put(3.6, new LaunchConfig(28.0, 0.4, 0.5)); //27.5
        interpolator.put(4.6, new LaunchConfig(24.7, 0.45, 0.55)); //25.0
        interpolator.put(2.9, new LaunchConfig(36.5, 0.4, 0.5));  //35.0
        // interpolator.put(5.9, new LaunchConfig(22.5, 0.55, 0.65));
        interpolator.put(2.3, new LaunchConfig(39.0, 0.4, 0.5));

        running = false;
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

    public void setRunning(boolean running)
    {
        this.running = running;
    }

    public boolean getRunning(){
        return running;
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
    public void setQuickRampRate()
    {
        rightMotor.setOpenLoopRampRate(kQuickRampSpeed);
        leftMotor.setOpenLoopRampRate(kQuickRampSpeed);
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
