package frc.robot.subsystems;

import static frc.robot.Constants.ChurroConstants.kChurroConversion;
import static frc.robot.Constants.ChurroConstants.kChurroPID;
import static frc.robot.Ports.churroPorts.kChurroMotor;

import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class SK24Churro extends SubsystemBase
{
    //memory variable for motor
    CANSparkFlex cMotor;
    RelativeEncoder cEncoder;
    SparkPIDController pid;
    DigitalInput limitSwitch;

    public SK24Churro()
    {
        //Initialize motor object
        limitSwitch = new DigitalInput(1);
        cMotor = new CANSparkFlex(kChurroMotor.ID, MotorType.kBrushless);
        cMotor.setInverted(true);
        cEncoder = cMotor.getEncoder();
        cEncoder.setPosition(0.0);
        cEncoder.setPositionConversionFactor(kChurroConversion);

        pid = cMotor.getPIDController();
        pid.setP(kChurroPID.kP);
        pid.setI(kChurroPID.kI);
        pid.setD(kChurroPID.kD);
    }

    /**
     * Set churro speed to specified degrees
     * @param degrees degrees to set the churro to
     */
    public void setChurroPosition(double degrees)
    {
        SmartDashboard.putNumber("Churro target", degrees);
        pid.setReference(degrees, ControlType.kPosition);
    }

    //Return motor speed
    public double getChurroSpeed()
    {
        return cMotor.get();
    }
    
    // Gets churro position from encoder
    public double getChurroPosition(){
        return cEncoder.getPosition();
    }

    //Resets churro encoder
    public void resetChurroEncoder(){
        cEncoder.setPosition(0.0);
    }

    /**
     * Return the value of the limit switch
     * @return true if the limit switch is not pressed
     */
    public boolean hitLimitSwitch()
    {
        return limitSwitch.get();
    }

    //Boolean true if churro at lower pos
    public boolean isChurroAtLower(){
        return Math.abs(getChurroPosition() - Constants.ChurroConstants.kChurroLowerPosition) < Constants.ChurroConstants.kAngleTolerance;
    }

    //Boolean true if churro at higher pos
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

        if(!hitLimitSwitch())
        {
            stopChurro();
            resetChurroEncoder();
        }
    }
}