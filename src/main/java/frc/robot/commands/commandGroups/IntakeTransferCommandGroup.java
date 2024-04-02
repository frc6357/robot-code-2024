// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.commandGroups;

import static frc.robot.Constants.IntakeConstants.kIntakeSpeed;
import static frc.robot.Constants.LauncherAngleConstants.kSpeakerAngle;
import static frc.robot.Constants.LauncherConstants.kTransferSpeed;
import static frc.robot.Constants.LauncherConstants.speakerRestingSpeedLeft;
import static frc.robot.Constants.LauncherConstants.speakerRestingSpeedRight;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.AngleCommand;
import frc.robot.commands.IntakeTransferCommand;
import frc.robot.subsystems.SK24Intake;
import frc.robot.subsystems.SK24Launcher;
import frc.robot.subsystems.SK24LauncherAngle;
import frc.robot.utils.SKCANLight;

public class IntakeTransferCommandGroup extends SequentialCommandGroup {
    
    public IntakeTransferCommandGroup(SK24LauncherAngle arm, SK24Intake intake, SK24Launcher launcher, SKCANLight light)
    {
        addCommands(  
                new AngleCommand(kSpeakerAngle, arm),
                // new InstantCommand(() -> launcher.setRestingRampRate()),
                new IntakeTransferCommand(kIntakeSpeed, kTransferSpeed, intake, light),
                new WaitCommand(0.2),
                new InstantCommand(() -> launcher.setLauncherSpeed(speakerRestingSpeedLeft.get(), speakerRestingSpeedRight.get()))
        );
    }
}
