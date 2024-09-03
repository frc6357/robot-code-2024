package frc.robot.commands.commandGroups;

import static frc.robot.Constants.IntakeConstants.kTransferSpeed;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.SK24Intake;

public class TransferCommandGroup extends SequentialCommandGroup {
    public TransferCommandGroup(SK24Intake intake)
    {
        addCommands(
           //new InstantCommand(() -> intake.setTransferSpeed(kTransferSpeed)),
           // new WaitCommand(1.0),
           // new InstantCommand(() -> intake.stopTransfer())
            );
    }
}
