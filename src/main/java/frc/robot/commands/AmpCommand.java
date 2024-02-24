package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SK24Launcher;

public class AmpCommand extends Command
{
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final SK24Launcher subsystem;
  double speedTop;
  double speedBottom;

  /**
   * @param subsystem The subsystem used by this command.
   */
    public AmpCommand(SK24Launcher subsystem, double speedTop, double speedBottom)
    {
        this.subsystem = subsystem;
        this.speedTop = speedTop;
        this.speedBottom = speedBottom;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(this.subsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() 
    {
        subsystem.setLauncherSpeed(speedTop, speedBottom);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {}

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) 
    {
        subsystem.stopLauncher();
    }
    // Returns true when the command should end.
    @Override
    public boolean isFinished()
    {
        return false;
    }
}

