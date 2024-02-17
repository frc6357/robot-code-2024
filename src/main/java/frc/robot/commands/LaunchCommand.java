// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SK24Launcher;

/** An example command that uses an example subsystem. */
public class LaunchCommand extends Command {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final SK24Launcher subsystem;
  double speedTop;
  double speedBottom;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public LaunchCommand(SK24Launcher subsystem, double speedTop, double speedBottom) {
    this.subsystem = subsystem;
    this.speedTop = speedTop;
    this.speedBottom = speedBottom;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(this.subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    subsystem.setLauncherSpeed(speedTop, speedBottom);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) 
  {
    subsystem.stopLauncher();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
