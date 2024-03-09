package frc.robot.commands.commandGroups;

import static frc.robot.Constants.ChurroConstants.kChurroSpeed;
import static frc.robot.Constants.LauncherAngleConstants.kAmpAngle;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.AngleCommand;
import frc.robot.commands.ChurroLowerCommand;
import frc.robot.commands.ZeroPositionCommand;
import frc.robot.subsystems.SK24Churro;
import frc.robot.subsystems.SK24Launcher;
import frc.robot.subsystems.SK24LauncherAngle;

public class AmpScoreDownGroup extends SequentialCommandGroup{

  /**
   * Command to score in the amp, moving the launcher angle to the right angle, 
   * launching a note, and then turning off motors and returning to zero position
   * @param arm Launcher angle subsystem to use
   * @param launcher Launcher subsystem to use
   */
  public AmpScoreDownGroup(SK24LauncherAngle arm, SK24Launcher launcher, SK24Churro churro) {
    addCommands(
        new ZeroPositionCommand(arm, launcher),
        new WaitCommand(1.0),
        new ChurroLowerCommand(churro, kChurroSpeed)
    );
  }

}

