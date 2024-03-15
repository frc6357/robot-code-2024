package frc.robot.commands.commandGroups;

import static frc.robot.Constants.LauncherConstants.kLauncherLeftSpeed;
import static frc.robot.Constants.LauncherConstants.kLauncherRightSpeed;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.IntakeAutoCommand;
import frc.robot.commands.LaunchCommand;
import frc.robot.commands.LaunchCommandAuto;
import frc.robot.commands.StopCommand;
import frc.robot.subsystems.SK24Intake;
import frc.robot.subsystems.SK24Launcher;

public class ShootCommandGroup extends SequentialCommandGroup {
    
    public ShootCommandGroup(SK24Intake intake, SK24Launcher launcher) 
    {
        addCommands(
            new LaunchCommandAuto(kLauncherLeftSpeed, kLauncherRightSpeed, launcher),
            new WaitCommand(1.5),
            new IntakeAutoCommand(intake, launcher),
            new WaitCommand(0.5),
            new StopCommand(intake, launcher)
        );
    }
}
