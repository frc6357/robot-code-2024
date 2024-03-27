// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.commandGroups;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.DriveTurnCommand;
import frc.robot.subsystems.SK24Drive;
import frc.robot.subsystems.SK24LauncherAngle;

public class ReadyScoreCommandGroup extends SequentialCommandGroup {
    
    /**
     * Command group that will turn robot to speaker and change the launcher angle to shoot in the speaker
     * @param launcher Launcher subsystem to use
     * @param arm Launcher angle subsystem to use
     */
    public ReadyScoreCommandGroup(Supplier<Double> xSpeed, Supplier<Double> ySpeed, SK24Drive drive, SK24LauncherAngle arm)
    {
        addCommands(
            new ParallelCommandGroup(
                new DriveTurnCommand(
                        xSpeed,
                        ySpeed,
                        () -> {return false;}, drive::getSpeakerAngle, drive),
                new InstantCommand(() ->arm.setTargetAngle(drive::getSpeakerLauncherAngle))
            ));
    }

}
