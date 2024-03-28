package frc.robot.bindings;

import static frc.robot.Constants.ClimbConstants.*;
import static frc.robot.Ports.OperatorPorts.kClimbAxis;

import java.util.Optional;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.ChurroRaiseCommand;
import frc.robot.commands.ClimbAngleCommand;
import frc.robot.subsystems.SK24Climb;
import frc.robot.subsystems.SK24Churro;
import frc.robot.Ports;
import frc.robot.utils.filters.DeadbandFilter;

public class SK24ClimbBinder implements CommandBinder{
    Optional<SK24Climb> subsystem;
    Optional<SK24Churro> churro;
    Trigger climbUpDriverButton;
    Trigger climbDownDriverButton;
    Trigger climbUpOperatorButton;
    Trigger climbDownOperatorButton;
    Trigger climbOverride;


    public SK24ClimbBinder(Optional<SK24Climb> subsystem, Optional<SK24Churro> churro){
        this.subsystem = subsystem;
        this.churro = churro;
        this.climbUpDriverButton = Ports.DriverPorts.kClimbUp.button;
        this.climbDownDriverButton = Ports.DriverPorts.kClimbDown.button;
        this.climbUpOperatorButton = Ports.OperatorPorts.kClimbUp.button;
        this.climbDownOperatorButton = Ports.OperatorPorts.kClimbDown.button;
        this.climbOverride = Ports.DriverPorts.kClimbOverride.button;
    }

    public void bindButtons()
    {
        // If subsystem is present then this method will bind the buttons
        if (subsystem.isPresent())
        {
            SK24Climb climb = subsystem.get();

            double joystickGain = kJoystickReversed ? -kJoystickChange : kJoystickChange;
            kClimbAxis.setFilter(new DeadbandFilter(kJoystickDeadband, joystickGain));

            if (churro.isPresent())
            {
                SK24Churro m_Churro = churro.get();

                climbUpDriverButton.or(climbUpOperatorButton).onTrue(new ChurroRaiseCommand(m_Churro));
                climbDownDriverButton.or(climbDownOperatorButton).onTrue(new ChurroRaiseCommand(m_Churro));
            }

            // Climb Up Buttons 
            climbUpDriverButton.or(climbUpOperatorButton).onTrue(new InstantCommand(() -> climb.runRightHook(kClimbUpSpeed))); 
            climbUpDriverButton.or(climbUpOperatorButton).onTrue(new InstantCommand(() -> climb.runLeftHook(kClimbUpSpeed))); 
            
            climbUpDriverButton.or(climbUpOperatorButton).onFalse(new InstantCommand(() -> climb.runRightHook(0.0))); 
            climbUpDriverButton.or(climbUpOperatorButton).onFalse(new InstantCommand(() -> climb.runLeftHook(0.0)));

            // Climb Down Buttons
            climbDownDriverButton.or(climbDownOperatorButton).onTrue(new InstantCommand(() -> climb.runRightHook(kClimbDownSpeed))); 
            climbDownDriverButton.or(climbDownOperatorButton).onTrue(new InstantCommand(() -> climb.runLeftHook(kClimbDownSpeed))); 
            
            climbDownDriverButton.or(climbDownOperatorButton).onFalse(new InstantCommand(() -> climb.runLeftHook(0.0)));
            climbDownDriverButton.or(climbDownOperatorButton).onFalse(new InstantCommand(() -> climb.runRightHook(0.0)));
            
            climbUpDriverButton.and(climbOverride).onTrue(new InstantCommand(() -> climb.resetPosition(1.0)));

            //climbButton.onTrue(new ClimbBalanceCommand(climb)); //TODO - Possibly add climb balancing command later

              climb.setDefaultCommand(
                         // Vertical movement of the arm is controlled by the Y axis of the right stick.
                         // Up on joystick moving arm up and down on stick moving arm down.
                         new ClimbAngleCommand(
                             () -> {return kClimbAxis.getFilteredAxis();},
                             climbOverride::getAsBoolean,
                             climb));
        }
    }
}

