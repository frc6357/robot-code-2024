package frc.robot.commands.commandGroups;

import static frc.robot.Constants.LauncherConstants.kAmpDefaultLeftSpeed;
import static frc.robot.Constants.LauncherConstants.kAmpDefaultRightSpeed;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.AmpCenterCommand;
import frc.robot.commands.ChurroRaiseCommand;
import frc.robot.commands.LaunchCommandAuto;
import frc.robot.subsystems.SK24Churro;
import frc.robot.subsystems.SK24Drive;
import frc.robot.subsystems.SK24Launcher;
import frc.robot.subsystems.SK24Vision;

public class AmpScoreCommandGroup extends ParallelCommandGroup{

  /**
   * Command to score in the amp, moving the launcher angle to the right angle, 
   * launching a note, and then turning off motors and returning to zero position
   * @param arm Launcher angle subsystem to use
   * @param launcher Launcher subsystem to use
   */
  public AmpScoreCommandGroup(Supplier<Double> forwardSpeed, SK24Launcher launcher, SK24Churro churro, SK24Drive drive, SK24Vision vision) {
    addCommands(
          new ChurroRaiseCommand(churro),
          new LaunchCommandAuto(kAmpDefaultLeftSpeed, kAmpDefaultRightSpeed, launcher),
          new AmpCenterCommand(forwardSpeed, drive, vision)
        );
  }

}

