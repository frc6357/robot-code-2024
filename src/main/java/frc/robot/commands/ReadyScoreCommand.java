package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.preferences.Pref;
import frc.robot.preferences.SKPreferences;
import frc.robot.subsystems.SK24Drive;
import frc.robot.subsystems.SK24Launcher;
import frc.robot.subsystems.SK24Launcher.LaunchConfig;
import frc.robot.subsystems.SK24LauncherAngle;
import frc.robot.subsystems.SK24Vision;
import static frc.robot.Constants.DriveConstants.*;
import static frc.robot.Constants.LauncherAngleConstants.kSpeakerAngle;


public class ReadyScoreCommand extends Command{

    private SK24LauncherAngle arm;
    private Supplier<Double> ySpeed;
    private Supplier<Double> xSpeed;
    
    private SK24Drive              drive;
    private SK24Vision              vision;
    private SK24Launcher            launcher;
    private PIDController          PID;
    private Supplier<Double>              rotation;

    Pref<Double> driveRotationP; 
    Pref<Double> driveRotationI;
    Pref<Double> driveRotationD;
    
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
     * @param rotation
     *            The supplier for the robot rotational speed
     * @param drive
     *            The subsystem required to control the drivetrain
     * @param arm
     *            The subsystem to control launcher angle
     * @param launcher
     *            The subsystem to control launcher
     * @param vision
     *            The subsystem to control vision
     */
    public ReadyScoreCommand(Supplier<Double> xSpeed, Supplier<Double> ySpeed, Supplier<Double> rotation, SK24Drive drive, SK24LauncherAngle arm, SK24Launcher launcher, SK24Vision vision)
    {
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.arm = arm;
        this.vision = vision;
        this.rotation = rotation;

        this.drive = drive;
        this.launcher = launcher;
        
        PID = new PIDController(kDriveRotationP, kDriveRotationI, kDriveRotationD, 0.02);

        driveRotationP = SKPreferences.attach(kDriveRotationPKey, kDriveRotationP).onChange(PID::setP);
        driveRotationI = SKPreferences.attach(kDriveRotationIKey, kDriveRotationI).onChange(PID::setI);
        driveRotationD = SKPreferences.attach(kDriveRotationDKey, kDriveRotationD).onChange(PID::setD);

        PID.setTolerance(0.1);
        PID.setIZone(5.0);
        
        addRequirements(drive, arm, vision, launcher);
    }
    
    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void initialize()
    {
        vision.setSpeakerMode();

        launcher.setRestingRampRate();
        arm.setTargetAngle(kSpeakerAngle);

        PID.setP(driveRotationP.get());
        PID.setI(driveRotationI.get());
        PID.setD(driveRotationD.get());
        
    }
    @Override
    public void execute()
    {

        if(vision.tagPresent()){
            PID.setSetpoint(0.0);
            double rot = PID.calculate(vision.getTx());
            SmartDashboard.putNumber("Tx", vision.getTx());
            rot = Math.abs(rot) > maxRot ? Math.copySign(maxRot, rot) : rot;
            drive.drive(xSpeed.get(), ySpeed.get(), rot, true);

            double distance = Math.hypot(
                    vision.returnZOffset(vision.getPoseTargetSpace()), 
                    vision.returnXOffset(vision.getPoseTargetSpace()));

            LaunchConfig config = launcher.getInterpolatedValues(distance);
                
            SmartDashboard.putNumber("Distance", distance);
    
            SmartDashboard.putNumber("Vison angle", config.angle());
            arm.setTargetAngle(config.angle());
    
            SmartDashboard.putNumber("Vision Launcher Speed Left", config.speedLeft());
            SmartDashboard.putNumber("Vision Launcher Speed Right", config.speedRight());
    
            launcher.setLauncherSpeed(config.speedLeft(), config.speedRight());
        }else{
            drive.drive(xSpeed.get(), ySpeed.get(), rotation.get(), true);
        }

        
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted)
    {
        drive.drive(0, 0, 0, false);
        arm.zeroPosition();
        launcher.rampDown();
        launcher.setLauncherSpeed(0.0, 0.0);
        vision.setAllTagMode();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished()
    {
        return false;
    }

}
