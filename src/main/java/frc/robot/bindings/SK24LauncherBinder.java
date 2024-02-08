package frc.robot.bindings;

import java.util.Optional;

import edu.wpi.first.wpilibj2.command.button.Trigger;
import static frc.robot.Ports.OperatorPorts.*;
import frc.robot.commands.LaunchCommand;
import frc.robot.commands.LaunchOffCommand;
import frc.robot.subsystems.SK24Launcher;

public class SK24LauncherBinder implements CommandBinder
{
    Optional<SK24Launcher> subsystem;

    private Trigger launcherButton = null;

    /**
     * The class that is used to bind all the commands for the arm subsystem
     * 
     * @param controller
     *            The contoller that the commands are being bound to
     * @param subsystem
     *            The required drive subsystem for the commands
     * @return 
     */
    public  SK24LauncherBinder(Optional<SK24Launcher> subsystem)
    {
        this.subsystem = subsystem;
        launcherButton = kLauncher.button;
    }

    public void bindButtons()
    {
        // If subsystem is present then this method will bind the buttons
        if (subsystem.isPresent())
        {

            SK24Launcher m_example = subsystem.get();
            
            launcherButton.onTrue(new LaunchCommand(m_example, 0.5));
            launcherButton.onFalse(new LaunchOffCommand(m_example));

            //exampleButton.onTrue(new InstantCommand(() -> m_example.setLauncherSpeed(0.5)));
            //exampleButton.onFalse(new InstantCommand(m_example::stopLauncher));
        }
    }

}