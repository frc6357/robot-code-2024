package frc.robot.commands;

import static frc.robot.Constants.ClimbConstants.balancePID;

import java.util.Optional;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SK24Climb;
import frc.robot.subsystems.SK24Drive;
import static frc.robot.Constants.ClimbConstants.*;

public class ClimbBalanceCommand extends Command
{
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final SK24Climb climb;
  private final SK24Drive drive;
  PIDController pid;
  private Optional<Boolean> isLeftArm;



  /**
   * Creates a new ExampleCommand.
   *
   * @param climb The subsystem used by this command.
   */
  public ClimbBalanceCommand(SK24Climb climb, SK24Drive drive) 
  {
    this.climb = climb;
    this.drive = drive;
    pid = new PIDController(balancePID.kP, balancePID.kI, balancePID.kD);
    pid.setSetpoint(0.0);
    pid.setTolerance(kClimbBalanceTolerance);
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(climb, drive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() 
  {
    if(drive.leftTilted()){ //Meaning robot tilted to left, need to raise right arm
      isLeftArm = Optional.of(false);
    }else if(drive.rightTilted()){//Meaning robot tilted to right, need to raise left arm
      isLeftArm = Optional.of(true);
    }
      
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() 
  {
    if(isLeftArm.isPresent())
    {
      boolean leftArm = isLeftArm.get();
      double speed = leftArm ? pid.calculate(climb.getLeftPosition()) : pid.calculate(climb.getRightPosition());
      if (leftArm) {climb.runLeftHook(speed);};
      if (!leftArm) {climb.runRightHook(speed);};
    }
    

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) 
  {
    
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return pid.atSetpoint();
  }
}
