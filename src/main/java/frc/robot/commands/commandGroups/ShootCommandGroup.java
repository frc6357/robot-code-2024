package frc.robot.commands.commandGroups;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.IntakeAutoCommand;
import frc.robot.subsystems.SK24Intake;

public class ShootCommandGroup extends SequentialCommandGroup {
    
    public ShootCommandGroup(SK24Intake intake) 
    {
        addCommands(
            new WaitCommand(0.5),
            new IntakeAutoCommand(intake),
            new WaitCommand(0.5),
            new InstantCommand(() -> intake.stopIntake(), intake),
            new InstantCommand(() -> intake.stopTransfer(), intake)
        );
    }
}
