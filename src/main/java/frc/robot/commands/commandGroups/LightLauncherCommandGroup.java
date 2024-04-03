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
import frc.robot.utils.SKCANLight;

public class LightLauncherCommandGroup extends SequentialCommandGroup{

    public LightLauncherCommandGroup(SKCANLight light)
    {
        addCommands(
            new InstantCommand(() -> light.setRed()),
            new WaitCommand(1.0),
            new InstantCommand(() -> light.setGreen())
        );
    }
    
}