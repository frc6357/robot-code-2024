// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import static frc.robot.Constants.LauncherAngleConstants.kMinAngle;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SK24Launcher;
import frc.robot.subsystems.SK24LauncherAngle;
/** An example command that uses an example subsystem. */
public class ZeroPositionCommand extends Command {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

    private final SK24LauncherAngle arm;
    private final SK24Launcher launcher;

    /**
     *         
     * @param arm
     *            Launcher Angle Subsystem used for this command
     * @param launcher
     *            Launcher Subsystem used for this command
     */
    public ZeroPositionCommand(SK24LauncherAngle arm, SK24Launcher launcher)
    {
        this.arm = arm;
        this.launcher = launcher;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(arm, launcher);
    }

    @Override
    public void initialize()
    {}

    @Override
    public void execute()
    {
        arm.setTargetAngle(kMinAngle);
        launcher.setLauncherSpeed(0.0, 0.0);
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