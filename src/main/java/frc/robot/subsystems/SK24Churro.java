package frc.robot.subsystems;

import static frc.robot.Ports.churroPorts.kChurroMotor;

import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

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

    public boolean isChurroAtLower(){
        return Math.abs(getChurroPosition() - Constants.ChurroConstants.kChurroLowerPosition) < Constants.ChurroConstants.kAngleTolerance;
    }

    public boolean isChurroAtUpper(){
        return Math.abs(getChurroPosition() - Constants.ChurroConstants.kChurroRaisePosition) < Constants.ChurroConstants.kAngleTolerance;
    }
    //Stop motor
    public void stopChurro()
    {
        cMotor.stopMotor();
    }

    public void periodic()
    {
        SmartDashboard.putNumber("Current Churro Position", getChurroPosition());
        SmartDashboard.putNumber("Current Churro Speed", getChurroSpeed());
        SmartDashboard.putBoolean("Churro at Lower", isChurroAtLower());
        SmartDashboard.putBoolean("Churro at Upper", isChurroAtUpper());
    }
}