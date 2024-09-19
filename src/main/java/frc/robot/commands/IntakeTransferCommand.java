package frc.robot.commands;


import static frc.robot.Constants.IntakeConstants.kIntakeTransferSpeed;
import static frc.robot.Constants.IntakeConstants.kSlowIntakeSpeed;
import static frc.robot.Constants.IntakeConstants.kSlowTransferSpeed;

import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SK24Intake;
import frc.robot.utils.SKCANLight;


public class IntakeTransferCommand extends Command
{
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final SK24Intake intake;
  double intakeSpeed;
 // double transferSpeed;

  /**
   * Command to intake the note using intake and transfer
   *
   * @param intake The intake subsystem used by this command.
   * @param launcher The launcher subsystem used by this command.
   */
  public IntakeTransferCommand(double intakeSpeed, SK24Intake intake, SKCANLight light) //previously had a double transferSpeed parameter
  {
    this.intake = intake;
    this.intakeSpeed = intakeSpeed;
   // this.transferSpeed = transferSpeed;


    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() 
  {
      intake.setIntakeSpeed(intakeSpeed); 
      //intake.setTransferSpeed(kIntakeTransferSpeed);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() 
  {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) 
  {
    intake.stopIntake();
    //intake.stopTransfer();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return intake.haveNote();
  }
}
