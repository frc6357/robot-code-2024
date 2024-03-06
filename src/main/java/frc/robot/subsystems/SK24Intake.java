package frc.robot.subsystems;

import static frc.robot.Ports.intakePorts.kBottomIntakeMotor;
import static frc.robot.Ports.intakePorts.kTopIntakeMotor;

import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SK24Intake extends SubsystemBase
{
    CANSparkFlex topIntakeMotor;
    CANSparkFlex bottomIntakeMotor;
    private boolean pastIntakeState;
    private boolean currIntakeState;

    public SK24Intake()
    {

        //Initialize motor objects, assuming intake has 2 motors.
        topIntakeMotor = new CANSparkFlex(kTopIntakeMotor.ID, MotorType.kBrushless);
        topIntakeMotor.setInverted(false);
        bottomIntakeMotor = new CANSparkFlex(kBottomIntakeMotor.ID, MotorType.kBrushless);
        bottomIntakeMotor.follow(topIntakeMotor, true);

        //laserCan = new LaserCan(kLaserCanIntake.ID);

        currIntakeState = false;
        pastIntakeState = false;
        SmartDashboard.putBoolean("Intaking", currIntakeState);
    }

    //Set motor speeds
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

    // public boolean haveNote()
    // {
    //     LaserCan.Measurement measurement = laserCan.getMeasurement();
    //     if (measurement != null && measurement.status == LaserCan.LASERCAN_STATUS_VALID_MEASUREMENT) {
    //       if(measurement.distance_mm < noteMeasurement)
    //       {
    //         return true;
    //       }
    //     } 
    //     return false;
    // }

    public void periodic()
    {
        // SmartDashboard.putBoolean("HaveIntakeNote", haveNote());
        if(Math.abs(getMotorSpeed()) < 0.05)
        {
            currIntakeState = true;
        }else{
            currIntakeState = false;
        }
        if(currIntakeState != pastIntakeState){
            pastIntakeState = currIntakeState;
            SmartDashboard.putBoolean("Intaking", currIntakeState);
        }
    }
}
