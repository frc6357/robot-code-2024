package frc.robot.utils.armAngle;

/**
 * Generic class to create different types of motors to be used to find arm angles.
 */
public abstract class GenericArmMotor
{
    /**
     * Resets position of encoder to 0.0
     */
    public abstract void resetEncoder();

    /**
     * Resets position of encoder to given angle
     * 
     * @param angle
     *          The desired angle to reset the position to
     */
    public abstract void resetEncoder(double angle);

    /**
     * Adds a new motor that follows the actions of the lead motor
     * 
     * @param CanID
     *            CanID for the follower motor
     */
    public abstract void addFollowerMotor(int CanID);

    /**
     * @return Returns the value of the sensor that is used for locating the lower limit
     *         of the arm.
     */
    public abstract boolean isLowerReached();

    /**
     * @return Returns true if the lower sensor is present
     */
    public abstract boolean isLowerAvailable();

    /**
     * @return Returns the value of the sensor that is used for locating the upper limit
     *         of the arm.
     */
    public abstract boolean isUpperReached();

    /**
     * @return Returns true if the lower sensor is present
     */
    public abstract boolean isUpperAvailable();

    /**
     * Stops motor movement. Motor can be moved again by calling set without having to
     * re-enable the motor.
     */
    public abstract void stop();


    /**
     * 
     * @return Returns the current setpoint the arm is attempting to reach
     */
    public abstract double getTargetAngle();

    /**
     * 
     * @return Returns the current angle the arm is at in this moment of time
     */
    public abstract double getCurrentAngle();

    /**
     * Sets the angle of the arm to specified degrees
     * 
     * @param degrees
     *            Degree to which the arm should be set
     */
    public abstract void setTargetAngle(double degrees);

    public abstract void testInit();

    public abstract void periodic();
    
}
