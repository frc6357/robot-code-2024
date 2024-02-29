// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.commandGroups;

import static frc.robot.Constants.LauncherAngleConstants.kLauncherBottomSpeed;
import static frc.robot.Constants.LauncherAngleConstants.kLauncherTopSpeed;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.AutoLaunchAngle;
import frc.robot.commands.DriveTurnCommand;
import frc.robot.commands.LaunchCommand;
import frc.robot.commands.ZeroPositionCommand;
import frc.robot.subsystems.SK24Drive;
import frc.robot.subsystems.SK24Launcher;
import frc.robot.subsystems.SK24LauncherAngle;
import frc.robot.subsystems.SK24Vision;
/** An example command that uses an example subsystem. */
public class AutoLaunchCommandGroup extends SequentialCommandGroup {
   
    public AutoLaunchCommandGroup(SK24Launcher launcher, SK24Drive drive, SK24LauncherAngle arm, SK24Vision vision)
    {
        addCommands(
            new AutoLaunchAngle(arm, vision),
            new LaunchCommand(kLauncherTopSpeed, kLauncherBottomSpeed, launcher),
            new WaitCommand(0.2),
            new ZeroPositionCommand(arm, launcher)
        );
    }

}
