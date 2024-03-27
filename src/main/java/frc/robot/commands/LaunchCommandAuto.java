// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SK24Launcher;
/** An example command that uses an example subsystem. */
public class LaunchCommandAuto extends Command {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

    private final SK24Launcher launcher;
    private double LeftSpeed;
    private double RightSpeed;

    

    /**
     * Command that will launch a note at speeds top and bottom speeds
     * @param LeftSpeed
     *            Speed for the left side of the launchers
     * @param RightSpeed
     *            Speed for the right side of the launchers
     * @param launcher
     *            Launcher angle used for this command
     */
    public LaunchCommandAuto(double LeftSpeed, double RightSpeed, SK24Launcher launcher)
    {
        this.LeftSpeed = LeftSpeed;
        this.RightSpeed = RightSpeed;
        this.launcher = launcher;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(launcher);
    }

    @Override
    public void initialize()
    {
        launcher.setLauncherSpeed(LeftSpeed, RightSpeed);
    }

    @Override
    public void execute()
    {
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
