// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import static frc.robot.Constants.ClimbConstants.kMaxAngle;
import static frc.robot.Constants.ClimbConstants.kMinAngle;

import java.util.function.Supplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SK24Climb;
/** An example command that uses an example subsystem. */
public class ClimbAngleCommand extends Command {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

    private final SK24Climb climb;
    private final Supplier<Double>  controller;
    private final Supplier<Boolean> override;
    

    /**
     * Sets the angle of the arm based upon input from a joystick, adding or subtracting
     * to the current set point. Default movement will receive joystick input with
     * downward movement on joystick turning motor clockwise and upward movement on
     * joystick turning motor counter clockwise.
     * 
     * @param setpointChange
     *            The method to get the setpoint change in degrees per second
     * @param clampOverride
     *            The method to determine if the angle limits should be overridden
     * @param climb
     *            Subsystem used for this command
     */
    public ClimbAngleCommand(Supplier<Double> setpointChange, Supplier<Boolean> clampOverride, SK24Climb climb)
    {
        this.controller = setpointChange;
        this.override = clampOverride;
        this.climb = climb;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(climb);
    }

    @Override
    public void initialize()
    {}

    @Override
    public void execute()
    {
        double angleChange = controller.get() / 50; // Units per 20ms from 0.0 to 1.0

         // Sets the new angle to the current angle plus or minus the constant change
        double rightSetpoint = climb.getRightTargetPosition() + angleChange;
        double leftSetpoint= climb.getLeftTargetPosition() + angleChange;

        if(!override.get())
        {
            rightSetpoint = MathUtil.clamp(rightSetpoint, kMinAngle, kMaxAngle);
            leftSetpoint = MathUtil.clamp(leftSetpoint, kMinAngle, kMaxAngle);
        }

        climb.setRightHook(rightSetpoint);
        climb.setLeftHook(leftSetpoint);
    }

    @Override
    public void end(boolean interrupted)
    {

    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished()
    {
        return false;
    }
}
