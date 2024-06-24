package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SK24Intake;


public class StopIntakingCommand extends Command{
    private SK24Intake intake;
    public StopIntakingCommand(SK24Intake intake)
    {
        this.intake = intake;
        
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(intake);
    }

    @Override
    public void initialize()
    {}

    @Override
    public void execute()
    {
        intake.stopIntake();
        intake.stopTransfer();
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
