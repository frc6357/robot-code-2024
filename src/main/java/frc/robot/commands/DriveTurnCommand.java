package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SK24Drive;

/**
 * A command used to turn the robot to a certain angle. This allows the driver to set the
 * robot angle to a set position while maintaining translation driving abilities.
 */
public class DriveTurnCommand extends Command
{
    private Supplier<Double> xSpeed;
    private Supplier<Double> ySpeed;

    private Supplier<Boolean>      robotCentric;
    private SK24Drive              subsystem;
    private PIDController          PID;
    private Supplier<Double>      setpoint;

    // In radians per second
    private double maxRot = 4;

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
    public DriveTurnCommand(Supplier<Double> xSpeed, Supplier<Double> ySpeed,
        Supplier<Boolean> robotCentric, double setpoint, SK24Drive drive)
    {
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.setpoint = () -> {return setpoint;};

        this.robotCentric = robotCentric;
        this.subsystem = drive;

        PID = new PIDController(0.1, 0, 0, 0.02);
        PID.enableContinuousInput(-180, 180);

        addRequirements(subsystem);
    }

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
    public DriveTurnCommand(Supplier<Double> xSpeed, Supplier<Double> ySpeed,
        Supplier<Boolean> robotCentric, Supplier<Double> setpoint, SK24Drive drive)
    {
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.setpoint = setpoint;

        this.robotCentric = robotCentric;
        this.subsystem = drive;

        PID = new PIDController(0.1, 0, 0, 0.02);
        PID.enableContinuousInput(-180, 180);
        
        addRequirements(subsystem);
    }
    
    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute()
    {
        PID.setSetpoint(MathUtil.inputModulus(setpoint.get(), -180, 180));
        double rot = PID.calculate(subsystem.getPose().getRotation().getDegrees());
        rot = Math.abs(rot) > maxRot ? Math.copySign(maxRot, rot) : rot;
        subsystem.drive(xSpeed.get(), ySpeed.get(), rot, !robotCentric.get());
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted)
    {
        subsystem.drive(0, 0, 0, false);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished()
    {
        return false;
    }
}
