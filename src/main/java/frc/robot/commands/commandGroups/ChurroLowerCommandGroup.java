package frc.robot.commands.commandGroups;

import static frc.robot.Constants.LauncherAngleConstants.kFloorAngle;
import static frc.robot.Constants.LauncherAngleConstants.kSpeakerAngle;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.AngleCommandAuto;
import frc.robot.commands.ChurroLowerCommand;
import frc.robot.subsystems.SK24Churro;
import frc.robot.subsystems.SK24LauncherAngle;

public class ChurroLowerCommandGroup extends SequentialCommandGroup{

  /**
   * Command to score in the amp, moving the launcher angle to the right angle, 
   * launching a note, and then turning off motors and returning to zero position
   * @param arm Launcher angle subsystem to use
   * @param launcher Launcher subsystem to use
   */
  public ChurroLowerCommandGroup(SK24LauncherAngle arm, SK24Churro churro) {
    addCommands(
      new ChurroLowerCommand(churro),
      new WaitCommand(1.0),
      new AngleCommandAuto(kFloorAngle, arm));
  }

}

