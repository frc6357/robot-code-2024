package frc.robot.commands.commandGroups;

import static frc.robot.Constants.LauncherAngleConstants.kAmpAngle;
import static frc.robot.Constants.LauncherConstants.kAmpDefaultLeftSpeed;
import static frc.robot.Constants.LauncherConstants.kAmpDefaultRightSpeed;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.AngleCommand;
import frc.robot.commands.ChurroRaiseCommand;
import frc.robot.commands.LaunchCommandAuto;
import frc.robot.subsystems.SK24Churro;
import frc.robot.subsystems.SK24Launcher;
import frc.robot.subsystems.SK24LauncherAngle;

public class AmpScoreCommandGroup extends SequentialCommandGroup{

  /**
   * Command to score in the amp, moving the launcher angle to the right angle, 
   * launching a note, and then turning off motors and returning to zero position
   * @param arm Launcher angle subsystem to use
   * @param launcher Launcher subsystem to use
   */
  public AmpScoreCommandGroup(SK24LauncherAngle arm, SK24Launcher launcher, SK24Churro churro) {
    addCommands(
        new AngleCommand(kAmpAngle, arm),
        new ParallelCommandGroup(
          new ChurroRaiseCommand(churro),
          new LaunchCommandAuto(kAmpDefaultLeftSpeed, kAmpDefaultRightSpeed, launcher)
        )
    );
  }

}

