package frc.robot.utils.armAngle;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.wpilibj.DigitalInput;

/**
 * Specific class to set the angle of an arm using a CAN Spark Max Brushless motor with an
 * internal encoder, and to determine when it is at it's max point and zero point using
 * digital input sensors.
 */
public class SparkFlexArm extends GenericArmMotor
{
    double                Kp;
    double                Ki;
    double                Kd;
    double                Kiz;
    boolean               isLowerPresent;
    boolean               isUpperPresent;
    double                positionSetPoint;
    double                degreeSetPoint;
    CANSparkFlex          motor;
    RelativeEncoder       encoder;
    SparkPIDController         pidController;
    double                gearRatio;
    DigitalInput          UpperSensor;
    DigitalInput          LowerSensor;

    /**
     * Creates a new CAN Spark Max arm
     * 
     * @param CanID
     *            Can ID of the motor used
     * @param gearRatio
     *            Number of motor shaft rotations per output shaft rotations
     * @param Kp
     *            Value for proportional gain constant in PID controller
     * @param Ki
     *            Value for integral gain constant in PID controller
     * @param Kd
     *            Value for derivative gain constant in PID controller
     * @param Kiz
     *            Value for I Zone constant in PID controller
     * @param LowerSensorID
     *            ID for digital input sensor that determines reset point of arm
     * @param UpperSensorID
     *            ID for digital input sensor that determines max limit point of arm or -1
     *            to indicate no switch is present
     */
    public SparkFlexArm(int CanID, double gearRatio, double Kp, double Ki, double Kd, double Kiz, double MinOutput, double MaxOutput,
        int LowerSensorID, int UpperSensorID)
    {
        this(CanID, gearRatio, Kp, Ki, Kd, Kiz, MinOutput, MaxOutput, LowerSensorID);
        this.gearRatio = gearRatio;

        if (UpperSensorID != -1)
        {
            this.UpperSensor = new DigitalInput(UpperSensorID);
            isUpperPresent = true;
        }
        else
        {
            this.UpperSensor = null;
            isUpperPresent = false;
        }

    }

    /**
     * Creates a new CAN Spark Max arm
     * 
     * @param CanID
     *            Can ID of the motor used
     * @param gearRatio
     *            Number of motor shaft rotations per output shaft rotations
     * @param Kp
     *            Value for proportional gain constant in PID controller
     * @param Ki
     *            Value for integral gain constant in PID controller
     * @param Kd
     *            Value for derivative gain constant in PID controller
     * @param Kiz
     *            Value for I Zone constant in PID controller
     * @param LowerSensorID
     *            ID for digital input sensor that determines reset point of arm
     */
    public SparkFlexArm(int CanID, double gearRatio, double Kp, double Ki, double Kd, double Kiz, double MinOutput, double MaxOutput,
        int LowerSensorID)
    {
        this(CanID, gearRatio, Kp, Ki, Kd, Kiz, MinOutput, MaxOutput);

        if (LowerSensorID != -1)
        {
            this.LowerSensor = new DigitalInput(LowerSensorID);
            isLowerPresent = true;
        }
        else
        {
            this.LowerSensor = null;
            isLowerPresent = false;
        }
    }

    /**
     * Creates a new CAN Spark Max arm
     * 
     * @param CanID
     *            Can ID of the motor used
     * @param gearRatio
     *            Number of motor shaft rotations per output shaft rotations
     * @param Kp
     *            Value for proportional gain constant in PID controller
     * @param Ki
     *            Value for integral gain constant in PID controller
     * @param Kd
     *            Value for derivative gain constant in PID controller
     * @param Kiz
     *            Value for I Zone constant in PID controller
     */

    public SparkFlexArm(int CanID, double gearRatio, double Kp, double Ki, double Kd, double Kiz, double MinOutput, double MaxOutput)
    {
        this.gearRatio = gearRatio;
        motor = new CANSparkFlex(CanID, CANSparkLowLevel.MotorType.kBrushless);
        motor.restoreFactoryDefaults();
        encoder = motor.getEncoder();

        pidController = motor.getPIDController();
        pidController.setP(Kp);
        pidController.setI(Ki);
        pidController.setD(Kd);
        pidController.setIZone(Kiz);

        pidController.setOutputRange(MinOutput, MaxOutput);

        isLowerPresent = false;
        isUpperPresent = false;

        encoder.setPositionConversionFactor(1.0);
        motor.setIdleMode(CANSparkFlex.IdleMode.kBrake); 
    }

    public void resetEncoder()
    {
        encoder.setPosition(0.0); // Reset Position of encoder is 0.0
    }

    public void resetEncoder(double angle)
    {
        encoder.setPosition(angle * gearRatio / 360); // Reset position to native unit (rotations)

    }

    public void addFollowerMotor(int CanID)
    {
        try (CANSparkFlex followerMotor =
                new CANSparkFlex(CanID, CANSparkLowLevel.MotorType.kBrushless))
        {
            followerMotor.follow(motor);
        }
    }

    public boolean isLowerAvailable()
    {
        return isLowerPresent;
    }

    public boolean isLowerReached()
    {
        return LowerSensor.get();
    }

    public boolean isUpperAvailable()
    {
        return isUpperPresent;
    }

    public boolean isUpperReached()
    {
        return UpperSensor.get();
    }

    public void stop()
    {
        motor.stopMotor();
    }

    public double getCurrentAngle()
    {
        double current_value = (encoder.getPosition() * 360) / gearRatio; // Convert native encoder unit of rotations to degrees
        return current_value;
    }

    public double getTargetAngle()
    {
        return degreeSetPoint;
    }

    public void setTargetAngle(double degrees)
    {
        degreeSetPoint = degrees;
        positionSetPoint = (degrees * gearRatio) / 360.0;
        pidController.setReference(positionSetPoint, CANSparkFlex.ControlType.kPosition);
    }

    public void testInit(){
        motor.setIdleMode(IdleMode.kCoast);
    }

    public void periodic()
    {
        
    }
}
