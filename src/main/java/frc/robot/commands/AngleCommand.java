// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.SK24LauncherAngle;
import static frc.robot.Constants.LauncherAngleConstants.*;
/** An example command that uses an example subsystem. */
public class AngleCommand extends Command {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

    private final SK24LauncherAngle arm;
    private double angle;

    /**
     *         
     * @param arm
     *            Subsystem used for this command
     */
    public AngleCommand(double angle, SK24LauncherAngle arm)
    {
        this.arm = arm;
        this.angle = angle;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(arm);
    }

    @Override
    public void initialize()
    {}

    @Override
    public void execute()
    {
        arm.setTargetAngle(angle);
    }

    @Override
    public void end(boolean interrupted)
    {

    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished()
    {
        return arm.isAtTargetAngle();
    }
}