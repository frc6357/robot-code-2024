// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.commandGroups;

import static frc.robot.Constants.LauncherAngleConstants.kAngleScore2;
import static frc.robot.Constants.LauncherAngleConstants.kPos3Angle;
import static frc.robot.Constants.LauncherConstants.kLauncherLeftSpeed;
import static frc.robot.Constants.LauncherConstants.kLauncherRightSpeed;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.AngleCommandAuto;
import frc.robot.commands.LaunchCommandAuto;
import frc.robot.subsystems.SK24Launcher;
import frc.robot.subsystems.SK24LauncherAngle;

public class Score2AngleCommand extends ParallelCommandGroup {
    
   /**
     * Shoot note into speaker from starting position 3
     * @param launcher Launcher subsystem to use
     * @param arm Launcher angle subsystem to use
     */
    public Score2AngleCommand(SK24Launcher launcher, SK24LauncherAngle arm)
    {
        addCommands(
            new AngleCommandAuto(kAngleScore2, arm)
        );
    }

}
