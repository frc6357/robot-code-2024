package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.SK24Intake;
import frc.robot.subsystems.SK24Launcher;


public class IntakeTransferCommand extends Command
{
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final SK24Intake intake;
  private final SK24Launcher launcher;
  double intakeSpeed;
  double transferSpeed;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public IntakeTransferCommand(SK24Intake intake, SK24Launcher launcher) 
  {
    this.intake = intake;
    this.launcher = launcher;
    this.intakeSpeed = Constants.IntakeConstants.kIntakeSpeed;
    this.transferSpeed = Constants.LauncherConstants.kTransferSpeed;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() 
  {
      intake.setIntakeSpeed(intakeSpeed);
      launcher.setTransferSpeed(transferSpeed);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(!launcher.haveNote())
    {
      if(intake.haveNote())
      {
        intake.setIntakeSpeed(intakeSpeed);
        launcher.setTransferSpeed(transferSpeed);
      }else{
        intake.setIntakeSpeed(intakeSpeed / 2.0);
        launcher.setTransferSpeed(transferSpeed / 2.0);
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) 
  {
    intake.stopIntake();
    launcher.stopTransfer();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return launcher.haveNote();
  }
}
