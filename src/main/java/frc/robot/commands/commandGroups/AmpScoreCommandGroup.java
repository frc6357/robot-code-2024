package frc.robot.commands.commandGroups;

import static frc.robot.Constants.LauncherAngleConstants.kAmpAngle;
import static frc.robot.Constants.LauncherConstants.kAmpDefaultLeftSpeed;
import static frc.robot.Constants.LauncherConstants.kAmpDefaultRightSpeed;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.AngleCommand;
import frc.robot.commands.LaunchCommand;
import frc.robot.commands.ZeroPositionCommand;
import frc.robot.subsystems.SK24Launcher;
import frc.robot.subsystems.SK24LauncherAngle;

public class AmpScoreCommandGroup extends SequentialCommandGroup{

  /**
   * Command to score in the amp, moving the launcher angle to the right angle, 
   * launching a note, and then turning off motors and returning to zero position
   * @param arm Launcher angle subsystem to use
   * @param launcher Launcher subsystem to use
   */
  public AmpScoreCommandGroup(SK24LauncherAngle arm, SK24Launcher launcher) {
    addCommands(
        // new ParallelCommandGroup(

            // new ChurroRaiseCommand(churro, kChurroSpeed),

            new AngleCommand(kAmpAngle, arm),
        new LaunchCommand(kAmpDefaultLeftSpeed, kAmpDefaultRightSpeed, launcher),
        new WaitCommand(0.5),
        new ZeroPositionCommand(arm, launcher)
    );
  }

}

