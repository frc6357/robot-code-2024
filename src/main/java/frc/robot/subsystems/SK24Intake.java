package frc.robot.subsystems;

//import static frc.robot.Constants.IntakeConstants.noteMeasurement;
import static frc.robot.Ports.intakePorts.kBottomIntakeMotor;
import static frc.robot.Ports.intakePorts.kTopIntakeMotor;
//import static frc.robot.Ports.launcherPorts.kLaserCanLauncherHigher;
//import static frc.robot.Ports.launcherPorts.kLaserCanLauncherLower;
//import static frc.robot.Ports.launcherPorts.kTransferMotor;

import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel.MotorType;

//import au.grapplerobotics.LaserCan;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SK24Intake extends SubsystemBase
{
    CANSparkFlex topIntakeMotor;
    CANSparkFlex bottomIntakeMotor;
    CANSparkFlex transferMotor;

    //private LaserCan laserCanLower;
    //private LaserCan laserCanHigher;
    DigitalInput beamBreakLeft;
    DigitalInput beamBreakRight;


    public SK24Intake()
    {

        //Initialize motor objects, assuming intake has 2 motors.
        topIntakeMotor = new CANSparkFlex(kTopIntakeMotor.ID, MotorType.kBrushless);
        topIntakeMotor.setInverted(false);
        bottomIntakeMotor = new CANSparkFlex(kBottomIntakeMotor.ID, MotorType.kBrushless);
        bottomIntakeMotor.follow(topIntakeMotor, true);

        //transferMotor = new CANSparkFlex(kTransferMotor.ID, MotorType.kBrushless);
        //transferMotor.setInverted(true);

       // laserCanLower = new LaserCan(kLaserCanLauncherLower.ID);
       // laserCanHigher = new LaserCan(kLaserCanLauncherHigher.ID);

       beamBreakLeft = new DigitalInput(0);
       beamBreakRight = new DigitalInput(1);



    }
    /*
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
    }*/


    /**
     * Sets the speed of the transfer
     * @param speed The speed to set for left. Value should be between -1.0 and 1.0.
     */    
    /**public void setTransferSpeed (double speed)
    {
        transferMotor.set(speed);
    }*/

    //Return motor speeds
    /**public double getTransferMotorSpeed()
    {
        return transferMotor.get();
    }
    public void stopTransfer(){
        transferMotor.stopMotor();
    }*/

    //Set motor speeds

    public Boolean beamBreak()
    {
        //interrupted(input);
        if(beamBreakLeft.get() || beamBreakRight.get())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void setIntakeSpeed (double speed)
    {
        topIntakeMotor.set(speed);
    }
        
    //Return motor speeds
    public double getMotorSpeed ()
    {
        return topIntakeMotor.get();
    }
    
    //Stop motors
    public void stopIntake()
    {
        topIntakeMotor.stopMotor();
    }

    public void periodic()
    {
        //SmartDashboard.putBoolean("HaveLauncherNote", haveHigherNote());
        //SmartDashboard.putBoolean("HaveLauncherLowerNote", haveLowerNote());
        SmartDashboard.putBoolean("Beam broken",beamBreak());
    }

    public void testInit()
    {
    }
    
    public void testPeriodic()
    {
    }
}
