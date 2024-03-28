package frc.robot.commands;

import static frc.robot.Constants.DriveConstants.kDriveAngleTolerance;

import java.util.function.Supplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SK24Drive;
import frc.robot.subsystems.SK24LauncherAngle;
import frc.robot.subsystems.SK24Vision;


public class ReadyScoreCommand extends Command{

    private SK24LauncherAngle arm;
    private Supplier<Double> ySpeed;
    private Supplier<Double> xSpeed;
    
    private SK24Drive              drive;
    private SK24Vision              vision;
    private PIDController          PID_NoVision;
    private PIDController          PID_Vision;
    
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
     * @param vision
     *            The subsystem to control vision
     */
    public ReadyScoreCommand(Supplier<Double> xSpeed, Supplier<Double> ySpeed, SK24Drive drive, SK24LauncherAngle arm, SK24Vision vision)
    {
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.arm = arm;
        this.vision = vision;

        this.drive = drive;

        PID_NoVision = new PIDController(0.1, 0, 0, 0.02);
        PID_NoVision.enableContinuousInput(-180, 180);
        PID_NoVision.setTolerance(kDriveAngleTolerance);
        
        PID_Vision = new PIDController(0.0008, 0, 0, 0.02); //TODO - tune PID for turning
        PID_Vision.enableContinuousInput(-180, 180);
        PID_Vision.setTolerance(kDriveAngleTolerance);

        vision.setSpeakerMode();
        
        addRequirements(drive, arm, vision);
    }
    
    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute()
    {
        double rot = 0.0;
        if(vision.tagPresent()){
            PID_NoVision.setSetpoint(MathUtil.inputModulus(0.0, -180, 180));
            rot = PID_NoVision.calculate(vision.returnXOffset(vision.getTargetPose()));
            rot = Math.abs(rot) > maxRot ? Math.copySign(maxRot, rot) : rot;
        }else{
            PID_NoVision.setSetpoint(MathUtil.inputModulus(drive.getSpeakerAngle(), -180, 180));
            rot = PID_NoVision.calculate(drive.getPose().getRotation().getDegrees());
            rot = Math.abs(rot) > maxRot ? Math.copySign(maxRot, rot) : rot;
        }
        drive.drive(xSpeed.get(), ySpeed.get(), rot, true);

        double launcherAngle = vision.returnTargetAngle(vision.getTargetPose());
        SmartDashboard.putNumber("Vison angle", launcherAngle);
        arm.setTargetAngle(launcherAngle);
        
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted)
    {
        drive.drive(0, 0, 0, false);
        arm.zeroPosition();
        vision.setAllTagMode();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished()
    {
        return false;
    }

}
