// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.commandGroups;

import static frc.robot.Constants.LauncherAngleConstants.kLauncherRightSpeed;
import static frc.robot.Constants.LauncherAngleConstants.kLauncherLeftSpeed;
import static frc.robot.Constants.LauncherAngleConstants.kPos3Angle;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.AngleCommand;
import frc.robot.commands.LaunchCommand;
import frc.robot.subsystems.SK24Launcher;
import frc.robot.subsystems.SK24LauncherAngle;

public class Pos3CommandGroup extends SequentialCommandGroup {
   /**
     * Shoot note into speaker from starting position 3
     * @param launcher Launcher subsystem to use
     * @param arm Launcher angle subsystem to use
     */
    public Pos3CommandGroup(SK24Launcher launcher, SK24LauncherAngle arm)
    {
        addCommands(
            new AngleCommand(kPos3Angle, arm),
            new LaunchCommand(kLauncherLeftSpeed, kLauncherRightSpeed, launcher)
        );
    }

}
