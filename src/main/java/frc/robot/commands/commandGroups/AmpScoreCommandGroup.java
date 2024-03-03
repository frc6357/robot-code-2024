package frc.robot.commands.commandGroups;

import static frc.robot.Constants.LauncherAngleConstants.kAmpAngle;
import static frc.robot.Constants.LauncherConstants.kAmpDefaultLeftSpeed;
import static frc.robot.Constants.LauncherConstants.kAmpDefaultRightSpeed;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.AngleCommand;
import frc.robot.commands.LaunchCommand;
import frc.robot.commands.ZeroPositionCommand;
import static frc.robot.Constants.LauncherConstants.kAmpDefaultRightSpeed;
import static frc.robot.Constants.LauncherConstants.kAmpDefaultLeftSpeed;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.AngleCommand;
import frc.robot.commands.ChurroRaiseCommand;
import frc.robot.commands.LaunchCommand;
import frc.robot.commands.ZeroPositionCommand;
import frc.robot.subsystems.SK24Churro;
import frc.robot.subsystems.SK24Launcher;
import frc.robot.subsystems.SK24LauncherAngle;

public class AmpScoreCommandGroup extends SequentialCommandGroup{

  public AmpScoreCommandGroup(SK24Churro churro, SK24LauncherAngle arm, SK24Launcher launcher) {
    addCommands(
        new ParallelCommandGroup(

            new ChurroRaiseCommand(churro, kChurroSpeed),

            new AngleCommand(kAmpAngle, arm)),
        new LaunchCommand(kAmpDefaultLeftSpeed, kAmpDefaultRightSpeed, launcher),
        new WaitCommand(0.5),
        new ZeroPositionCommand(arm, launcher)
    );
  }

}

