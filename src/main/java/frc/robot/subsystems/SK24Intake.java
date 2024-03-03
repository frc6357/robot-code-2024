package frc.robot.subsystems;

import static frc.robot.Constants.IntakeConstants.noteMeasurement;
import static frc.robot.Ports.intakePorts.kBottomIntakeMotor;
import static frc.robot.Ports.intakePorts.kLaserCanIntake;
import static frc.robot.Ports.intakePorts.kTopIntakeMotor;

import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel.MotorType;

import au.grapplerobotics.LaserCan;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SK24Intake extends SubsystemBase
{
    CANSparkFlex topIntakeMotor;
    CANSparkFlex bottomIntakeMotor;
    private boolean pastIntakeState;
    private boolean currIntakeState;
    private LaserCan laserCan;

    public SK24Intake()
    {

        //Initialize motor objects, assuming intake has 2 motors.
        topIntakeMotor = new CANSparkFlex(kTopIntakeMotor.ID, MotorType.kBrushless);
        topIntakeMotor.setInverted(true);
        bottomIntakeMotor = new CANSparkFlex(kBottomIntakeMotor.ID, MotorType.kBrushless);
        bottomIntakeMotor.follow(topIntakeMotor, true);

        laserCan = new LaserCan(kLaserCanIntake.ID);

        currIntakeState = false;
        pastIntakeState = false;
        SmartDashboard.putBoolean("Intaking", currIntakeState);
    }

    /**
     * Sets the top intake motors to a specified speed
     * @param speed The speed to set. Value should be between -1.0 and 1.0
     **/
    public void setIntakeSpeed (double speed)
    {
        topIntakeMotor.set(speed);

    }
        
    /**
     * Gets the top intake motor speed.
     * @return The current set speed. Value is between -1.0 and 1.0.
     **/
    public double getMotorSpeed ()
    {
        return topIntakeMotor.get();
    }
    
    /**
     * Stop the top intake motor
     **/
    public void stopIntake()
    {
        topIntakeMotor.stopMotor();
    }
    /**
     * Check if we have a note stored in the intake
     * @return True when note detected, false when not detected
     **/
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
     * Periodically check if we have a note and check if we are intaking, then put these statuses in smartdashboard 
     **/
    public void periodic()
    {
        SmartDashboard.putBoolean("HaveIntakeNote", haveNote());
        if(getMotorSpeed() != 0.0)
        {
            currIntakeState = true;
        }
        if(currIntakeState != pastIntakeState){
            pastIntakeState = currIntakeState;
            SmartDashboard.putBoolean("Intaking", currIntakeState);
        }
    }
}
