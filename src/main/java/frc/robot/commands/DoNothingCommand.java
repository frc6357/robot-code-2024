// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;

/**
 * A generic command with no subsystem depdendency, which merely does absolutely nothing
 * other than to tell the scheduler that it is immediately finished.
 */
public class DoNothingCommand extends Command
{
    // Returns true when the command should end.
    @Override
    public boolean isFinished()
    {
        return true;
    }
}
