// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SK24LauncherAngle;
/** An example command that uses an example subsystem. */
public class AngleCommandAuto extends Command {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

    private final SK24LauncherAngle arm;
    private double angle;

    /**
     * Command to set the launcher angle to the angle parameter
     * @param angle The specified angle to set the launcher to - In degrees, up is positive
     * @param arm Launcher angle subsysystem to use
     */
    public AngleCommandAuto(double angle, SK24LauncherAngle arm)
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
        return true;
    }
}
