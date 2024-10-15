package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.SK24Intake;


public class IntakeAutoCommand extends Command
{
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final SK24Intake intake;
  double intakeSpeed;
  //double transferSpeed;

  /**
   * Command to intake the note using intake and transfer
   *
   * @param intake The intake subsystem used by this command.
   * @param launcher The launcher subsystem used by this command.
   */
  public IntakeAutoCommand(SK24Intake intake) 
  {
    this.intake = intake;
    this.intakeSpeed = Constants.IntakeConstants.kIntakeSpeed;
    //this.transferSpeed = Constants.IntakeConstants.kTransferSpeed;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() 
  {
      intake.setIntakeSpeed(intakeSpeed);
      //intake.setTransferSpeed(transferSpeed);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) 
  {
    
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
