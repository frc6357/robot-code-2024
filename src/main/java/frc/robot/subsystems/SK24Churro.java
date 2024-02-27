package frc.robot.subsystems;

import com.revrobotics.CANSparkFlex;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Ports.churroPorts.kChurroMotor;

public class SK24Churro extends SubsystemBase
{
    //memory variable for motor
    CANSparkFlex cMotor;
    RelativeEncoder cEncoder;

    public SK24Churro()
    {
        //Initialize motor object
        cMotor = new CANSparkFlex(kChurroMotor.ID, MotorType.kBrushless);
        cEncoder = cMotor.getEncoder();
        cEncoder.setPosition(0.0);
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
    
    public double getChurroPosition(){
        return cEncoder.getPosition();
    }

    public void resetChurroEncoder(){
        cEncoder.setPosition(0.0);
    }
    //Stop motor
    public void stopChurro()
    {
        cMotor.stopMotor();
    }
}