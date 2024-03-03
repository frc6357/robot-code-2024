package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SK24Drive;
import frc.robot.subsystems.SK24LauncherAngle;
import static frc.robot.Constants.DriveConstants.*;


public class ReadyScoreCommand extends Command{

    private SK24LauncherAngle arm;
    private Supplier<Double> ySpeed;
    private Supplier<Double> xSpeed;
    
    private SK24Drive              drive;
    private PIDController          PID;
    
    // In radians per second
    private double maxRot = 4;
    
    /**
     * Creates a command that turns the robot to the speaker using the field
     * coordinate system and brings the launcher angle to score in speaker
     * 
     * @param xSpeed
     *            The supplier for the robot x axis speed
     * @param ySpeed
     *            The supplier for the robot y axis speed
     * @param drive
     *            The subsystem required to control the drivetrain
     * @param arm
     *            The subsystem to control launcher angle
     */
    public ReadyScoreCommand(Supplier<Double> xSpeed, Supplier<Double> ySpeed, SK24Drive drive, SK24LauncherAngle arm)
    {
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.arm = arm;

        this.drive = drive;

        PID = new PIDController(0.1, 0, 0, 0.02);
        PID.enableContinuousInput(-180, 180);
        PID.setTolerance(kDriveAngleTolerance);
        
        addRequirements(drive, arm);
    }
    
    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute()
    {

        PID.setSetpoint(MathUtil.inputModulus(drive.getSpeakerAngle(), -180, 180));
        double rot = PID.calculate(drive.getPose().getRotation().getDegrees());
        rot = Math.abs(rot) > maxRot ? Math.copySign(maxRot, rot) : rot;
        drive.drive(xSpeed.get(), ySpeed.get(), rot, true);

        arm.setTargetAngle(drive::getSpeakerLauncherAngle);
        
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted)
    {
        drive.drive(0, 0, 0, false);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished()
    {
        return PID.atSetpoint() && arm.isAtTargetAngle();
    }

}
