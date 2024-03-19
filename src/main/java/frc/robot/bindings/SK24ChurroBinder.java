package frc.robot.bindings;

import java.util.Optional;

import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.ChurroRaiseCommand;
import frc.robot.commands.ChurroLowerCommand;
import frc.robot.subsystems.SK24Churro;
import static frc.robot.Ports.OperatorPorts.*;
import static frc.robot.Constants.ChurroConstants.*;

public class SK24ChurroBinder implements CommandBinder
{
    Optional<SK24Churro> subsystem;
    Trigger churroButton;
    /**
     * The class that is used to bind all the commands for the churro subsystem
     * 
     * @param controller
     *            The contoller that the commands are being bound to
     * @param subsystem
     *            The required drive subsystem for the commands
     * @return 
     */
    public SK24ChurroBinder(Optional<SK24Churro> subsystem)
    {
        this.subsystem = subsystem;
        this.churroButton = kChurro.button;
    }

    public void bindButtons()
    {
        // If subsystem is present then this method will bind the buttons
        if (subsystem.isPresent())
        {

            SK24Churro m_churro = subsystem.get();
            
            churroButton.onTrue(new ChurroLowerCommand(m_churro, kChurroSpeed));
            churroButton.onFalse(new ChurroRaiseCommand(m_churro, -kChurroSpeed));

        }
    }

}