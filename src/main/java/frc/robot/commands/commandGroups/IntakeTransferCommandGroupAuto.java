// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.commandGroups;

import static frc.robot.Constants.IntakeConstants.kIntakeSpeed;
import static frc.robot.Constants.LauncherConstants.kTransferSpeed;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.IntakeTransferCommand;
import frc.robot.subsystems.SK24Intake;
import frc.robot.subsystems.SK24Launcher;

public class IntakeTransferCommandGroupAuto extends SequentialCommandGroup {
    
    public IntakeTransferCommandGroupAuto(SK24Launcher launcher, SK24Intake intake)
    {
        // addCommands(
        //     new ParallelRaceGroup(
        //         new IntakeTransferCommand(intake, launcher),
        //         new WaitCommand(kIntakeSeconds)
        //     ),
        //     new WaitCommand(0.5),
        //     new LaunchCommand(-0.05, -0.05, launcher),
        //     new WaitCommand(0.5),
        //     new InstantCommand(() -> intake.stopIntake()),
        //     new InstantCommand(() -> launcher.stopTransfer()),
            
        //     new InstantCommand(() -> launcher.stopLauncher())
            
        // );

        addCommands(
            new ParallelRaceGroup(
                new IntakeTransferCommand(kIntakeSpeed, kTransferSpeed, intake, launcher),
                new WaitCommand(1.125)
            )
        );
    }

}
