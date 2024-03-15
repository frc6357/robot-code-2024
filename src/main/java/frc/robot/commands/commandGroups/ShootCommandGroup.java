package frc.robot.commands.commandGroups;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.IntakeAutoCommand;
import frc.robot.subsystems.SK24Intake;
import frc.robot.subsystems.SK24Launcher;

public class ShootCommandGroup extends SequentialCommandGroup {
    
    public ShootCommandGroup(SK24Intake intake, SK24Launcher launcher) 
    {
        addCommands(
            new WaitCommand(0.5),
            new IntakeAutoCommand(intake, launcher),
            new WaitCommand(0.5),
            new InstantCommand(() -> intake.stopIntake()),
            new InstantCommand(() -> launcher.stopTransfer())
        );
    }
}
