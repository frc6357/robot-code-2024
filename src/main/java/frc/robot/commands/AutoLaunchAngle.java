// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SK24LauncherAngle;
import frc.robot.subsystems.SK24Vision;

/** An example command that uses an example subsystem. */
public class AutoLaunchAngle extends Command {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final SK24LauncherAngle arm;
  private final SK24Vision vision;
  double speedTop;
  double speedBottom;

  /**
     * Command to set the target angle of the launcher to score in speaker based on vision 
     * @param arm Launcher angle subsysystem to use
     * @param vision Vision subsystem to use
     */
  public AutoLaunchAngle(SK24LauncherAngle arm, SK24Vision vision) {
    this.arm = arm;
    this.vision = vision;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(this.arm, this.vision);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    double launcherAngle = vision.returnTargetAngle(vision.getTargetPose());
    arm.setTargetAngle(launcherAngle);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) 
  {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return arm.isAtTargetAngle();
  }
}
