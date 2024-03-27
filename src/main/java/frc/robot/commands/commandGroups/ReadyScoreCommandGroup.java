// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.commandGroups;

import static frc.robot.Ports.DriverPorts.kRobotCentricMode;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.DriveTurnCommand;
import frc.robot.subsystems.SK24Drive;
import frc.robot.subsystems.SK24LauncherAngle;
/** An example command that uses an example subsystem. */
public class ReadyScoreCommandGroup extends SequentialCommandGroup {
    
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
