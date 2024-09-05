// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SK24Launcher;
import frc.robot.utils.SKCANLight;
import frc.robot.Constants;
import frc.robot.subsystems.SK24Intake;
/** An example command that uses an example subsystem. */
public class LaunchCommand extends Command {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

    private final SK24Launcher launcher;
    private final SKCANLight light;
    private double LeftSpeed;
    private double RightSpeed;
    private final SK24Intake intake;
    double intakeSpeed;

    

    /**
     * Command that will launch a note at speeds top and bottom speeds
     * @param LeftSpeed
     *            Speed for the left side of the launchers
     * @param RightSpeed
     *            Speed for the right side of the launchers
     * @param launcher
     *            Launcher angle used for this command
     * @param intake The intake subsystem used by this command.
     */
    public LaunchCommand(double LeftSpeed, double RightSpeed, SK24Launcher launcher, SK24Intake intake, SKCANLight light)
    {
        this.LeftSpeed = LeftSpeed;
        this.RightSpeed = RightSpeed;
        this.launcher = launcher;
        this.light = light;
        this.intake = intake;
        this.intakeSpeed = Constants.IntakeConstants.kIntakeSpeed;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(launcher, intake);
    }

    @Override
    public void initialize()
    {
        intake.setIntakeSpeed(intakeSpeed);
        launcher.setLauncherSpeed(LeftSpeed, RightSpeed);
    }

    @Override
    public void execute()
    {
        if (launcher.isFullSpeed()) {light.setGreen();}
        else{
            light.setTeamColor();
        }
    }

    @Override
    public void end(boolean interrupted)
    {
        launcher.stopLauncher();
        light.setTeamColor();
        intake.stopIntake();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished()
    {
        return false;
    }
}
