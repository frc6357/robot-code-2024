// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.commandGroups;

import static frc.robot.Constants.LauncherAngleConstants.kLauncherBottomSpeed;
import static frc.robot.Constants.LauncherAngleConstants.kLauncherTopSpeed;
import static frc.robot.Constants.LauncherAngleConstants.kPos1Angle;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.AngleCommand;
import frc.robot.commands.LaunchCommand;
import frc.robot.subsystems.SK24Launcher;
import frc.robot.subsystems.SK24LauncherAngle;
/** An example command that uses an example subsystem. */
public class Pos1CommandGroup extends SequentialCommandGroup {
   
    public Pos1CommandGroup(SK24Launcher launcher, SK24LauncherAngle arm)
    {
        addCommands(
            new AngleCommand(kPos1Angle, arm),
            new LaunchCommand(kLauncherTopSpeed, kLauncherBottomSpeed, launcher)
        );
    }

}
