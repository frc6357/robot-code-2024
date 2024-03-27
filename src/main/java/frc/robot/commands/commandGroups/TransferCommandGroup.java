package frc.robot.commands.commandGroups;

import static frc.robot.Constants.LauncherConstants.kTransferSpeed;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.SK24Launcher;

public class TransferCommandGroup extends SequentialCommandGroup {
    public TransferCommandGroup(SK24Launcher launcher)
    {
        addCommands(
            new InstantCommand(() -> launcher.setTransferSpeed(kTransferSpeed)),
            new WaitCommand(1.0),
            new InstantCommand(() -> launcher.stopTransfer())
            );
    }
}
