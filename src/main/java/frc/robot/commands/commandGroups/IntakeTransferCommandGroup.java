// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.commandGroups;

import static frc.robot.Constants.IntakeConstants.kIntakeSeconds;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.IntakeTransferCommand;
import frc.robot.commands.LaunchCommand;
import frc.robot.subsystems.SK24Intake;
import frc.robot.subsystems.SK24Launcher;

public class IntakeTransferCommandGroup extends ParallelCommandGroup {
    
    public IntakeTransferCommandGroup(SK24Launcher launcher, SK24Intake intake)
    {
        addCommands(
            //new AngleCommand(kIntakeAngle, arm),
            new ParallelDeadlineGroup(
                new IntakeTransferCommand(intake, launcher),
                new WaitCommand(kIntakeSeconds)
            )
        );
    }

}
