package frc.robot.subsystems;

import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Ports.churroPorts.kChurroMotor;

public class SK24Churro extends SubsystemBase
{
    //memory variable for motor
    CANSparkFlex cMotor;

    public SK24Churro()
    {
        //Initialize motor object
        cMotor = new CANSparkFlex(kChurroMotor.ID, MotorType.kBrushless);
    }

    //Run motor
    public void setChurroSpeed(double speed)
    {
        cMotor.set(speed);
    }

    //Return motor speed
    public double getChurroSpeed()
    {
        return cMotor.get();
    }
    
    //Stop motor
    public void stopChurro()
    {
        cMotor.stopMotor();
    }
}