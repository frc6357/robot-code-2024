// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import static frc.robot.Constants.LauncherAngleConstants.kLauncherAmpBottomSpeed;
import static frc.robot.Constants.LauncherAngleConstants.kLauncherAmpTopSpeed;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SK24Launcher;
import frc.robot.subsystems.SK24LauncherAngle;
/** An example command that uses an example subsystem. */
public class AmpLaunchCommand extends Command {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

    private final SK24Launcher launcher;

    

    /**
     * @param arm
     *            Subsystem used for this command
     */
    public AmpLaunchCommand(SK24Launcher launcher)
    {
        this.launcher = launcher;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(launcher);
    }

    @Override
    public void initialize()
    {}

    @Override
    public void execute()
    {
        launcher.setLauncherSpeed(kLauncherAmpTopSpeed, kLauncherAmpBottomSpeed);
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
