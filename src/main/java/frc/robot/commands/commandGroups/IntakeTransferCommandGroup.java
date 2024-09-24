// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.commandGroups;

import static frc.robot.Constants.IntakeConstants.kIntakeSpeed;
import static frc.robot.Constants.LauncherAngleConstants.kSpeakerAngle;
import static frc.robot.Constants.IntakeConstants.kTransferSpeed;
import static frc.robot.Constants.LauncherConstants.speakerRestingSpeedLeft;
import static frc.robot.Constants.LauncherConstants.speakerRestingSpeedRight;

import java.util.List;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
//import frc.robot.commands.AngleCommand;
import frc.robot.commands.IntakeTransferCommand;
import frc.robot.subsystems.SK24Intake;
import frc.robot.subsystems.SK24Launcher;
//import frc.robot.subsystems.SK24LauncherAngle;
import frc.robot.utils.SKCANLight;

public class IntakeTransferCommandGroup extends SequentialCommandGroup {
    
    public IntakeTransferCommandGroup(SK24Intake intake, SK24Launcher launcher, SKCANLight light) //previously had SK24LauncherAngle arm
    {
        List<Command> myCommandGroup = List.of(new IntakeTransferCommand(kIntakeSpeed, intake, light),
         new WaitCommand(2));
        if (intake.haveNote())
        {
            myCommandGroup.add(new WaitCommand(10.0));
            myCommandGroup.add(new InstantCommand(() -> light.setOrange()));
            myCommandGroup.add(new InstantCommand(() -> intake.setIntakeSpeed(0)));
        }
        else
        {
            myCommandGroup.add(new InstantCommand(() -> light.setTeamColor()));
            myCommandGroup.add(new InstantCommand(() -> intake.setIntakeSpeed(kIntakeSpeed)));
        } 
        addCommands(myCommandGroup.toArray(new Command[0]));
        s
    }
}
