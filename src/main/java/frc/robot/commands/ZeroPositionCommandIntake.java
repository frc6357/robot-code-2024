// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SK24Intake;
import frc.robot.subsystems.SK24Launcher;
import frc.robot.subsystems.SK24LauncherAngle;
/** An example command that uses an example subsystem. */
public class ZeroPositionCommandIntake extends Command {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

    private final SK24LauncherAngle arm;
    private final SK24Launcher launcher;
    private final SK24Intake intake;

    /**
     * Command to zero the position of the launcher angle and turn off the launcher motors
     * @param arm
     *            Launcher Angle Subsystem used for this command
     * @param launcher
     *            Launcher Subsystem used for this command
     */
    public ZeroPositionCommandIntake(SK24LauncherAngle arm, SK24Launcher launcher, SK24Intake intake)
    {
        this.intake = intake;
        this.arm = arm;
        this.launcher = launcher;
        
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(arm, launcher);
    }

    @Override
    public void initialize()
    {

        arm.zeroPosition();
        launcher.stopTransfer();
        launcher.stopLauncher();
        intake.stopIntake();
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
