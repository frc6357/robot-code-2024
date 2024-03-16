package frc.robot.subsystems;

import static frc.robot.Constants.IntakeConstants.kIntakeAngle;
import static frc.robot.Constants.IntakeConstants.kIntakeSpeed;
import static frc.robot.Ports.intakePorts.kBottomIntakeMotor;
import static frc.robot.Ports.intakePorts.kTopIntakeMotor;

import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SK24Intake extends SubsystemBase
{
    CANSparkFlex topIntakeMotor;
    CANSparkFlex bottomIntakeMotor;
    private boolean pastIntakeState;
    private boolean currIntakeState;
    double shuffleSpeed;
    boolean isTest = false;

    public SK24Intake()
    {

        //Initialize motor objects, assuming intake has 2 motors.
        topIntakeMotor = new CANSparkFlex(kTopIntakeMotor.ID, MotorType.kBrushless);
        topIntakeMotor.setInverted(false);
        bottomIntakeMotor = new CANSparkFlex(kBottomIntakeMotor.ID, MotorType.kBrushless);
        bottomIntakeMotor.follow(topIntakeMotor, true);

        currIntakeState = false;
        pastIntakeState = false;
    }

    //Set motor speeds
    public void setIntakeSpeed (double speed)
    {
        if(!isTest)
        {
            topIntakeMotor.set(speed);
        }else{
            topIntakeMotor.set(shuffleSpeed);
        }
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
    }

    public void testInit()
    {
        isTest = true;
        Preferences.initDouble("Intake Speed", kIntakeSpeed);
    }
    
    public void testPeriodic()
    {
        shuffleSpeed = Preferences.getDouble("Intake Speed", kIntakeSpeed);
    }
}
