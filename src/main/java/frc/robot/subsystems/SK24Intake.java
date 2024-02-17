package frc.robot.subsystems;

import static frc.robot.Ports.intakePorts.kTopMotor;

import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SK24Intake extends SubsystemBase
{
    CANSparkFlex motor;
    public SK24Intake()
    {
        //Initialize motor object
        motor = new CANSparkFlex(kTopMotor.ID, MotorType.kBrushless);
    }
    
    public void setIntakeSpeed (double speed)
    {
        motor.set(speed);
    }
        
    //Return motor speeds
    public double getMotorSpeed ()
    {
        return motor.get();
    }
    
    //Stop motors
    public void stopIntake()
    {
        motor.stopMotor();
    }
}
