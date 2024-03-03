package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SK24Drive;
import frc.robot.subsystems.SK24Vision;

public class AmpCenterCommand extends Command
{
    private SK24Drive  drive;
    private SK24Vision vision;

    private PIDController    transPID;

    // Meters per second
    private double transDeadband = 0.005;

    private double currentTranslation;

    public AmpCenterCommand(SK24Drive drive, SK24Vision vision)
    {
        this.drive = drive;
        this.vision = vision;


        transPID = new PIDController(0.13, 0, 0, 0.02);
        transPID.setSetpoint(0);

        addRequirements(drive, vision);
    }

    @Override
    public void initialize()
    {
        vision.setAmpMode();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute()
    {
        
        if (vision.tagPresent())
        {
            double translation = transPID.calculate(vision.returnXOffset(vision.getTargetPose()));
            translation = Math.abs(translation) < transDeadband ? 0.0 : translation;
            currentTranslation = translation;
            drive.drive(translation, 0.0, 0.0, false);
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted)
    {
        vision.setAllTagMode();
        drive.drive(0.0, 0.0, 0.0, false);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished()
    {
        if(currentTranslation == 0.0){
            return true;
        }
        return false;
    }
}
