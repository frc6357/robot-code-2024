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

    private PIDController    transPID;
    private PIDController    rotPID;

    // Meters per second
    private double transDeadband = 0.005;

    private double currentRotation;
    public double maxRot = 4.0;
    public Supplier<Double> manualSpeed;

    /**
     * Command to center robot on amp
     * @param drive Drive subsystem to use
     * @param vision Vision subsystem to use
     */
    public AmpCenterCommand(Supplier<Double> forwardSpeed, SK24Drive drive, SK24Vision vision)
    {
        this.drive = drive;
        this.vision = vision;

        manualSpeed = forwardSpeed;
        rotPID = new PIDController(0.18, 0, 0, 0.02);
        rotPID.enableContinuousInput(-180, 180);
        double ampRotation = drive.checkIsRed() ? -90 : 90;
        rotPID.setSetpoint(ampRotation);

        transPID = new PIDController(1.3, 0, 0, 0.02);
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
        double rot = rotPID.calculate(drive.getPose().getRotation().getDegrees());
        rot = Math.abs(rot) > maxRot ? Math.copySign(maxRot, rot) : rot;
        currentRotation = rot;
        if (vision.tagPresent())
        {
            double translation = transPID.calculate(vision.returnXOffset(vision.getTargetPose()));
            translation = Math.abs(translation) < transDeadband ? 0.0 : translation;
            drive.drive(manualSpeed.get(), translation, currentRotation, false);
        }
        else
        {
            drive.drive(manualSpeed.get(), 0.0, currentRotation, false);
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
        return false;
    }
}
