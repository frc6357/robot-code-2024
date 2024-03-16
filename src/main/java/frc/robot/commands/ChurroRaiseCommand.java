package frc.robot.commands;

import frc.robot.subsystems.SK24Churro;

import static frc.robot.Constants.ChurroConstants.kChurroRaisePosition;

import edu.wpi.first.wpilibj2.command.Command;

public class ChurroRaiseCommand extends Command
{
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final SK24Churro subsystem;


  /**
  * Command to raise the churro for retracting back into robot
  *
  * @param churro The churro subsystem used by this command.
  */
  public ChurroRaiseCommand(SK24Churro subsystem) 
  {
    this.subsystem = subsystem;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() 
  {
      subsystem.setChurroPosition(kChurroRaisePosition);
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
