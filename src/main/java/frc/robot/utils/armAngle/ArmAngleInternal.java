package frc.robot.utils.armAngle;

/**
 * Generic class to set and read the angle of an arm in degrees, with the lower point
 * starting at 0 and increasing towards the upper point. It does this using a motor that
 * contains an internal encoder, and can determine when it is at it's max point and zero
 * point using upper and lower sensors.
 */
public class ArmAngleInternal
{
    GenericArmMotor motor;

    /**
     * Enumerated Value that determines the motor type that is used for the arm
     */

    public static enum AngleMotorType
    {
        /**
         * CAN Spark Max motor
         */
        SparkMax,
        SparkFlex
    }

    /**
     * Creates a new arm motor of the specified type
     * 
     * @param motorType
     *            Type of motor the arm is using
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
     * @param MinOutput
     *            Value for minimum output of the PID controller
     * @param MaxOutput
     *            Value for maximum output of the PID controller
     * @param lowerSensorID
     *            ID for digital input sensor that determines lower limit of arm or -1 to
     *            indicate no switch is present
     * @param upperSensorID
     *            ID for digital input sensor that determines upper limit of arm or -1 to
     *            indicate no switch is present
     */
    public ArmAngleInternal(AngleMotorType motorType, int CanID, double gearRatio, double Kp,
        double Ki, double Kd, double Kiz, double MinOutput, double MaxOutput, int lowerSensorID, int upperSensorID)
    {
        switch (motorType)
        {
            case SparkMax:
                motor = new SparkMaxArm(CanID, gearRatio, Kp, Ki, Kd, Kiz, MinOutput, MaxOutput, lowerSensorID,
                    upperSensorID);
                break;
            case SparkFlex:
                motor = new SparkFlexArm(CanID, gearRatio, Kp, Ki, Kd, Kiz, MinOutput, MaxOutput, lowerSensorID,
                    upperSensorID);
                break;
        }
    }

    /**
     * Creates a new arm motor of the specified type
     * 
     * @param motorType
     *            Type of motor the arm is using
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
     * @param MinOutput
     *            Value for minimum output of the PID controller
     * @param MaxOutput
     *            Value for maximum output of the PID controller
     * @param lowerSensorID
     *            ID for digital input sensor that determines lower limit of arm or -1 to
     *            indicate no switch is present
     */
    public ArmAngleInternal(AngleMotorType motorType, int CanID, double gearRatio, double Kp,
        double Ki, double Kd, double Kiz, double MinOutput, double MaxOutput, int lowerSensorID)
    {
        switch (motorType)
        {
            case SparkMax:
                motor = new SparkMaxArm(CanID, gearRatio, Kp, Ki, Kd, Kiz, MinOutput, MaxOutput, lowerSensorID);
                break;
            case SparkFlex:
                motor = new SparkFlexArm(CanID, gearRatio, Kp, Ki, Kd, Kiz, MinOutput, MaxOutput, lowerSensorID);
                break;
        }
    }

    /**
     * Creates a new arm motor of the specified type
     * 
     * @param motorType
     *            Type of motor the arm is using
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
     * @param MinOutput
     *            Value for minimum output of the PID controller
     * @param MaxOutput
     *            Value for maximum output of the PID controller
     */
    public ArmAngleInternal(AngleMotorType motorType, int CanID, double gearRatio, double Kp,
        double Ki, double Kd, double Kiz, double MinOutput, double MaxOutput)
    {
        switch (motorType)
        {
            case SparkMax:
                motor = new SparkMaxArm(CanID, gearRatio, Kp, Ki, Kd, Kiz, MinOutput, MaxOutput);
                break;
            case SparkFlex:
                motor = new SparkFlexArm(CanID, gearRatio, Kp, Ki, Kd, Kiz, MinOutput, MaxOutput);
                break;
        }
    }

    /**
     * Resets position of encoder to 0.0.
     */
    public void resetEncoder()
    {
        motor.resetEncoder();
    }

    /**
     * Resets position of encoder to given angle
     * 
     * @param angle
     *          The desired angle to reset the position to
     */
    public void resetEncoder(double angle)
    {
        motor.resetEncoder(angle);
    }
    

    /**
     * Adds a new motor that follows the actions of the lead motor
     * 
     * @param CanID
     *            CanID for the follower motor
     */

    public void addFollowerMotor(int CanID)
    {
        motor.addFollowerMotor(CanID);
    }

    /**
     * @return Returns the value of digital input sensor that is used for location the
     *         lower limit of the arm.
     */
    public boolean isLowerReached()
    {
        return motor.isLowerReached();
    }

    /**
     * @return Returns true if the lower sensor is present
     */
    public boolean isLowerAvailable()
    {
        return motor.isLowerAvailable();
    }

    /**
     * @return Returns the value of digital input sensor that is used for locating the
     *         upper limit of the arm.
     */
    public boolean isUpperReached()
    {
        return motor.isUpperReached();
    }

    /**
     * @return Returns true if the upper sensor is present
     */
    public boolean isUpperAvailable()
    {
        return motor.isUpperAvailable();
    }

    /**
     * Stops motor movement. Motor can be moved again by calling set without having to
     * re-enable the motor.
     */
    public void stop()
    {
        motor.stop();
    }

    /**
     * @return Returns the current setpoint that the arm is attempting to reach
     */
    public double getTargetAngle()
    {
        return motor.getTargetAngle();
    }

    /**
     * @return Returns the angle that the arm is currently at
     */
    public double getCurrentAngle()
    {
        return motor.getCurrentAngle();
    }

    /**
     * Sets the angle of the arm to specified degrees, starting at 0 at the lower point
     * and increasing towards upper point
     * 
     * @param degrees
     *            Degree to which the arm should be set
     */
    public void setTargetAngle(double degrees)
    {
        motor.setTargetAngle(degrees);
    }

    /**
     * 
     * Checks both the upper and lower sensor, if they are present, to determine if they
     * have been reached. If so it will stop the motor, and reset the encoder if it has
     * reached the bottom sensor.
     */
    public void checkLimitSensors()
    {
        if (isLowerAvailable() && isLowerReached())
        {
            resetEncoder();
            stop();
        }

        if (isUpperAvailable() && isUpperReached())
        {
            stop();
        }
    }

    public void testInit(){
        motor.testInit();
    }
    public void periodic()
    {
        motor.periodic();
    }

    public GenericArmMotor getMotor()
    {
        return motor;
    }

}
