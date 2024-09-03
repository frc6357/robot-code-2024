package frc.robot.commands.commandGroups;

import static frc.robot.Constants.LauncherAngleConstants.kAmpAngle;
import static frc.robot.Constants.LauncherAngleConstants.kAmpAnglePref;
import static frc.robot.Constants.LauncherAngleConstants.kSpeakerAngle;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
//import frc.robot.commands.AngleCommandAuto;
import frc.robot.commands.ChurroRaiseCommand;
import frc.robot.subsystems.SK24Churro;
//import frc.robot.subsystems.SK24LauncherAngle;

public class ChurroRaiseCommandGroup extends SequentialCommandGroup{

  /**
   * Command to score in the amp, moving the launcher angle to the right angle, 
   * launching a note, and then turning off motors and returning to zero position
   * @param arm Launcher angle subsystem to use
   * @param launcher Launcher subsystem to use
   */
  public ChurroRaiseCommandGroup(SK24Churro churro) {  //previously had SK24LauncherAngle
    addCommands(
            //new AngleCommandAuto(kAmpAngle, arm),
            new WaitCommand(1.0),
            new ChurroRaiseCommand(churro));
  }

}

