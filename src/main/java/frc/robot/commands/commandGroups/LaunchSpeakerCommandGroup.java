package frc.robot.commands.commandGroups;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import frc.robot.subsystems.SK24Launcher;
import frc.robot.utils.SKCANLight;

public class LaunchSpeakerCommandGroup extends SequentialCommandGroup{
    double LauncherRightSpeed;
    double LauncherLeftSpeed;

    public LaunchSpeakerCommandGroup(double LauncherLeftSpeed, double LauncherRightSpeed, SK24Launcher launcher, SKCANLight light)
    {
        this.LauncherRightSpeed = LauncherRightSpeed;
        this.LauncherLeftSpeed = LauncherLeftSpeed;
        addCommands(
            new InstantCommand(() -> launcher.setSpeakerRampRate()),
            new InstantCommand(() -> light.setRed()),
            new InstantCommand(() -> launcher.setLauncherSpeed(LauncherLeftSpeed, LauncherRightSpeed)),
            new WaitCommand(launcher.getCurrentRampRate()),
            new InstantCommand(() -> light.setGreen())
        );
    }
    
}
