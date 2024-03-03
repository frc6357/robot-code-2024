package frc.robot.commands;

import frc.robot.subsystems.SK24Churro;

import edu.wpi.first.wpilibj2.command.Command;

public class ChurroLowerCommand extends Command
{
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final SK24Churro subsystem;
  double speed;

  /**
   * Command to lower the churro for scoring in amp
   *
   * @param churro The churro subsystem used by this command.
   */
  public ChurroLowerCommand(SK24Churro churro, double speed) 
  {
    this.subsystem = churro;
    this.speed = speed;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(churro);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() 
  {
      subsystem.setChurroSpeed(-speed);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) 
  {
    //Intereupted by limit swicth
    subsystem.stopChurro();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return subsystem.isChurroAtUpper();
  }
}