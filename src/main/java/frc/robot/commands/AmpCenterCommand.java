package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SK24Drive;
import frc.robot.subsystems.SK24Vision;

public class AmpCenterCommand extends Command
{
    private SK24Drive  drive;
    private SK24Vision vision;

    private Supplier<Boolean> override;
    private PIDController    transPID;

    // Meters per second
    private double transDeadband = 0.005;

    private double currentRotation;
    private double currentTranslation;

    public AmpCenterCommand(Supplier<Boolean> override, SK24Drive drive, SK24Vision vision)
    {
        this.drive = drive;
        this.vision = vision;

        this.override = override;

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
        currentRotation = drive.getPose().getRotation().getDegrees();
        if (vision.tagPresent() && !override.get())
        {
            double translation = transPID.calculate(vision.returnXOffset(vision.getTargetPose()));
            translation = Math.abs(translation) < transDeadband ? 0.0 : translation;
            currentTranslation = translation;
            drive.drive(translation, 0.0, currentRotation, false);
        }
        else
        {
            drive.drive(0.0, 0.0, currentRotation, false);
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
