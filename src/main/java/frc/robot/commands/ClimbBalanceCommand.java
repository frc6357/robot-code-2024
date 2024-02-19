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
  PIDController pid;



  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public ClimbBalanceCommand(SK24Climb subsystem) 
  {
    this.subsystem = subsystem;
    pid = new PIDController(balancePID.kP, balancePID.kI, balancePID.kD);
    pid.setSetpoint(0.0);

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() 
  {
      
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() 
  {
    // if(gyro.leftTilted()){ //Meaning robot tilted to left, need to raise right arm
    //     subsystem.setRightHook(pid.calculate(subsystem.getRightPosition()));
    // }else if(gyro.rightTilted()){
    //     subsystem.setLeftHook(pid.calculate(subsystem.getLeftPosition()));
    // }

    //TODO - Add in drive subsystem to get pigeon data
    

  }

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
