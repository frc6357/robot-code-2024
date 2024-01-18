package frc.robot.bindings;

import java.util.Optional;

import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.ExampleCommand;
import frc.robot.subsystems.SK24Example;
import static frc.robot.Ports.OperatorPorts.*;

public class SK24ExampleBinder implements CommandBinder
{
    Optional<SK24Example> subsystem;

    // Arm button commands
    private final Trigger exampleButton;

    /**
     * The class that is used to bind all the commands for the arm subsystem
     * 
     * @param controller
     *            The contoller that the commands are being bound to
     * @param subsystem
     *            The required drive subsystem for the commands
     */
    public SK24ExampleBinder(Optional<SK24Example> subsystem)
    {
        this.subsystem = subsystem;
        exampleButton           = kExample.button;
    }

    public void bindButtons()
    {
        // If subsystem is present then this method will bind the buttons
        if (subsystem.isPresent())
        {

            SK24Example m_example = subsystem.get();
            
            exampleButton.onTrue(new ExampleCommand(m_example));
        }
    }

}
