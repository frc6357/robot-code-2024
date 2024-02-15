package frc.robot.commands;

import static frc.robot.Constants.ClimbConstants.balancePID;

import java.util.Optional;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SK24Climb;

public class ClimbBalanceCommand extends Command
{
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final SK24Climb subsystem;
  double speed;
  PIDController pid;
  Optional<Boolean> isRightTilted;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public ClimbBalanceCommand(SK24Climb subsystem, double speed) 
  {
    this.subsystem = subsystem;
    this.speed = speed;
    pid = new PIDController(balancePID.kP, balancePID.kI, balancePID.kD);
    pid.setSetpoint(0.0);

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() 
  {
      if(gyro.leftTilt()){ //Meaning robot tilted to left, need to lower right arm
        isRightTilted = Optional.of(false);
      }else if(gyro.rightTilt()){
        isRightTilted = Optional.of(true);
      }

      if(isRightTilted.isPresent()){
        
      }
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
    return false;
  }
}
