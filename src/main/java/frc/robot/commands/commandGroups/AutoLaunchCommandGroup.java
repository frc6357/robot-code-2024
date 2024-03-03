// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.commandGroups;

import static frc.robot.Constants.LauncherAngleConstants.kLauncherBottomSpeed;
import static frc.robot.Constants.LauncherAngleConstants.kLauncherTopSpeed;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.AutoLaunchAngle;
import frc.robot.commands.LaunchCommand;
import frc.robot.commands.ZeroPositionCommand;
import frc.robot.subsystems.SK24Launcher;
import frc.robot.subsystems.SK24LauncherAngle;
import frc.robot.subsystems.SK24Vision;

public class AutoLaunchCommandGroup extends SequentialCommandGroup {
   /**
    * Command group that will determine launcher angle based on the vision targeting, set the launcher to that angle,
    * and then launch a note in the speaker. Finally, turns everything off and to zero position.
    * @param launcher Launcher subsystem to use
    * @param arm Launcher angle subsystem to use
    * @param vision Vision subsystem to use
    */
    public AutoLaunchCommandGroup(SK24Launcher launcher, SK24LauncherAngle arm, SK24Vision vision)
    {
        addCommands(
            new AutoLaunchAngle(arm, vision),
            new LaunchCommand(kLauncherTopSpeed, kLauncherBottomSpeed, launcher),
            new WaitCommand(0.2),
            new ZeroPositionCommand(arm, launcher)
        );
    }

}
