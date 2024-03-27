package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SK24Intake;
import frc.robot.subsystems.SK24Launcher;


public class StopCommand extends Command{
    private SK24Intake intake;
    private SK24Launcher launcher;
    public StopCommand(SK24Intake intake, SK24Launcher launcher)
    {
        this.intake = intake;
        this.launcher = launcher;
        
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(intake, launcher);
    }

    @Override
    public void initialize()
    {}

    @Override
    public void execute()
    {
        intake.stopIntake();
        launcher.stopTransfer();
        launcher.stopLauncher();
    }

    @Override
    public void end(boolean interrupted)
    {

    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished()
    {
        return true;
    }
}
