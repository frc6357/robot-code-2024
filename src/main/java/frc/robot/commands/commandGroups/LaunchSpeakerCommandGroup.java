package frc.robot.commands.commandGroups;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

import static frc.robot.Constants.LauncherConstants.kSpeakerDefaultLeftSpeed;
import static frc.robot.Constants.LauncherConstants.kSpeakerDefaultLeftSpeedKey;
import static frc.robot.Constants.LauncherConstants.kSpeakerDefaultRightSpeed;
import static frc.robot.Constants.LauncherConstants.kSpeakerDefaultRightSpeedKey;
import static frc.robot.Constants.LauncherConstants.speakerSpeedLeft;
import static frc.robot.Constants.LauncherConstants.speakerSpeedRight;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.preferences.Pref;
import frc.robot.preferences.SKPreferences;
import frc.robot.subsystems.SK24Launcher;
import frc.robot.utils.SKCANLight;

public class LaunchSpeakerCommandGroup extends SequentialCommandGroup{
    Pref<Double> speakerSpeedLeft = SKPreferences.attach(kSpeakerDefaultLeftSpeedKey, kSpeakerDefaultLeftSpeed);
    Pref<Double> speakerSpeedRight = SKPreferences.attach(kSpeakerDefaultRightSpeedKey, kSpeakerDefaultRightSpeed);
    double LauncherRightSpeed;
    double LauncherLeftSpeed;

    public LaunchSpeakerCommandGroup(SK24Launcher launcher, SKCANLight light)
    {
        addCommands(
            new InstantCommand(() -> launcher.setSpeakerRampRate(), launcher),
            new InstantCommand(() -> light.setPurple()),
            new InstantCommand(() -> launcher.setLauncherSpeed(speakerSpeedLeft.get(), speakerSpeedRight.get()), launcher),
            new WaitCommand(launcher.getCurrentRampRate()),
            new InstantCommand(() -> light.setGreen())
        );
    }
    
}
