// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SK24Launcher;
/** An example command that uses an example subsystem. */
public class LaunchCommand extends Command {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

    private final SK24Launcher launcher;
    private double TopSpeed;
    private double BottomSpeed;

    

    /**
     * @param arm
     *            Subsystem used for this command
     */
    public LaunchCommand(double TopSpeed, double BottomSpeed, SK24Launcher launcher)
    {
        this.TopSpeed = TopSpeed;
        this.BottomSpeed = BottomSpeed;
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
        launcher.setLauncherSpeed(TopSpeed, BottomSpeed);
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
