package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SK24Drive;
import frc.robot.subsystems.SK24Vision;

public class CenterCommand extends Command
{
    private SK24Drive  drive;
    private SK24Vision vision;

    private PIDController    transPID;

    // Meters per second
    private double transDeadband = 0.005;

    public double maxRot = 4.0;
    public Supplier<Double> manualSpeed;

    /**
     * Command to center robot on amp
     * @param drive Drive subsystem to use
     * @param vision Vision subsystem to use
     */
    public CenterCommand(Supplier<Double> forwardSpeed, SK24Drive drive, SK24Vision vision)
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
        vision.setAllTagMode();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute()
    {
        if (vision.tagPresent())
        {
            double translation = transPID.calculate(vision.returnXOffset(vision.getTargetPose()));
            translation = Math.abs(translation) < transDeadband ? 0.0 : translation;
            drive.drive(translation, manualSpeed.get(), 0.0, false);
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted)
    {
        drive.drive(0.0, 0.0, 0.0, false);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished()
    {
        return false;
    }
}
