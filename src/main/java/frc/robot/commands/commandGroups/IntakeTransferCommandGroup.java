// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.commandGroups;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.AngleCommand;
import frc.robot.commands.IntakeTransferCommand;
import frc.robot.commands.ZeroPositionCommand;
import frc.robot.subsystems.SK24Intake;
import frc.robot.subsystems.SK24Launcher;
import frc.robot.subsystems.SK24LauncherAngle;
import static frc.robot.Constants.IntakeConstants.*;

public class IntakeTransferCommandGroup extends ParallelCommandGroup {
    
    public IntakeTransferCommandGroup(SK24Launcher launcher, SK24Intake intake, SK24LauncherAngle arm)
    {
        addCommands(
            new AngleCommand(kIntakeAngle, arm),
            new ParallelDeadlineGroup(
                new IntakeTransferCommand(intake, launcher),
                new WaitCommand(kIntakeSeconds)
            ),
            new ZeroPositionCommand(arm, launcher)
        );
    }

}
