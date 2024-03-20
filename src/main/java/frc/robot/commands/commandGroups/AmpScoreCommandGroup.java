package frc.robot.commands.commandGroups;

import static frc.robot.Constants.LauncherConstants.kAmpDefaultLeftSpeed;
import static frc.robot.Constants.LauncherConstants.kAmpDefaultRightSpeed;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.ChurroLowerCommand;
import frc.robot.commands.ChurroRaiseCommand;
import frc.robot.commands.IntakeAutoCommand;
import frc.robot.commands.LaunchCommandAuto;
import frc.robot.commands.StopCommand;
import frc.robot.subsystems.SK24Churro;
import frc.robot.subsystems.SK24Intake;
import frc.robot.subsystems.SK24Launcher;

public class AmpScoreCommandGroup extends SequentialCommandGroup{

  /**
   * Command to score in the amp, moving the launcher angle to the right angle, 
   * launching a note, and then turning off motors and returning to zero position
   * @param arm Launcher angle subsystem to use
   * @param launcher Launcher subsystem to use
   */
  public AmpScoreCommandGroup(SK24Launcher launcher, SK24Churro churro, SK24Intake intake) {
    addCommands(
          new ParallelDeadlineGroup(
            new WaitCommand(1.5),
            new ChurroRaiseCommand(churro),
            new LaunchCommandAuto(kAmpDefaultLeftSpeed, kAmpDefaultRightSpeed, launcher)
          ),
          new IntakeAutoCommand(intake, launcher),
          new WaitCommand(1.0),
          new ParallelCommandGroup(
            new StopCommand(intake, launcher),
            new ChurroLowerCommand(churro))
        );
  }

}

