package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SK24LauncherAngle;
import frc.robot.subsystems.SK24Vision;


public class ReadyScoreCommandAngle extends Command{

    private SK24LauncherAngle arm;
    
    private SK24Vision              vision;
    
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
    public ReadyScoreCommandAngle(SK24LauncherAngle arm, SK24Vision vision)
    {
        this.arm = arm;
        this.vision = vision;

        vision.setSpeakerMode();
        
        addRequirements(arm, vision);
    }
    
    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute()
    {
        double launcherAngle = vision.returnTargetAngle(vision.getTargetPose());
        SmartDashboard.putNumber("Vison angle", launcherAngle);
        arm.setTargetAngle(launcherAngle);
        
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted)
    {
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
