package frc.robot.commands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SK24Drive;

/**
 * A command used to turn the robot to a certain angle. This allows the driver to set the
 * robot angle to a set position while maintaining translation driving abilities.
 */
public class TurnWheelsCommand extends Command
{
    private Rotation2d rotation;

    private SK24Drive              subsystem;


    /**
     * Creates a command that turns the robot to a specified angle using the field
     * coordinate system
     * 
     * @param xSpeed
     *            The supplier for the robot x axis speed
     * @param ySpeed
     *            The supplier for the robot y axis speed
     * @param robotCentric
     *            Whether or not the drive mode is in robot or field centric mode
     * @param setpoint
     *            The desired angle [0º, 360º] using the field coordinate system
     * @param drive
     *            The subsystem required to control the drivetrain
     */
    public TurnWheelsCommand(Rotation2d setpoint, SK24Drive drive)
    {
        this.rotation = setpoint;
        this.subsystem = drive;
        addRequirements(subsystem);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute()
    {
       subsystem.pointWheels(rotation);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted)
    {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished()
    {
        return false;
    }
}
