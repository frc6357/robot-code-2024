package frc.robot.commands;


import static frc.robot.Constants.IntakeConstants.kSlowIntakeSpeed;
import static frc.robot.Constants.LauncherConstants.kSlowTransferSpeed;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SK24Intake;
import frc.robot.subsystems.SK24Launcher;
import frc.robot.utils.SKCANLight;


public class IntakeTransferCommand extends Command
{
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final SK24Intake intake;
  private final SK24Launcher launcher;
  private final SKCANLight light;
  double intakeSpeed;
  double transferSpeed;

  /**
   * Command to intake the note using intake and transfer
   *
   * @param intake The intake subsystem used by this command.
   * @param launcher The launcher subsystem used by this command.
   */
  public IntakeTransferCommand(double intakeSpeed, double transferSpeed, SK24Intake intake, SK24Launcher launcher, SKCANLight light) 
  {
    this.intake = intake;
    this.launcher = launcher;
    this.light = light;
    this.intakeSpeed = intakeSpeed;
    this.transferSpeed = transferSpeed;


    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(intake, launcher);
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
    if (launcher.haveLowerNote())
    {
      light.setOrange();
      intake.setIntakeSpeed(kSlowIntakeSpeed);
      launcher.setTransferSpeed(kSlowTransferSpeed);
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
    return launcher.haveHigherNote();
  }
}
