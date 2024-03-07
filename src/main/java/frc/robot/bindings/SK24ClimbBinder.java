package frc.robot.bindings;

import static frc.robot.Constants.ClimbConstants.kJoystickChange;
import static frc.robot.Constants.ClimbConstants.kJoystickDeadband;
import static frc.robot.Constants.ClimbConstants.kJoystickReversed;
import static frc.robot.Ports.OperatorPorts.kClimbDown;
import static frc.robot.Ports.OperatorPorts.kClimbAxis;
import static frc.robot.Ports.DriverPorts.kClimbOverride;
import static frc.robot.Ports.OperatorPorts.kClimbUp;

import java.util.Optional;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.ClimbAngleCommand;
import frc.robot.subsystems.SK24Climb;
import frc.robot.utils.filters.DeadbandFilter;

public class SK24ClimbBinder implements CommandBinder{
    Optional<SK24Climb> subsystem;
    Trigger climbUpButton;
    Trigger climbDownButton;
    Trigger climbOverride;


    public SK24ClimbBinder(Optional<SK24Climb> subsystem){
        this.subsystem = subsystem;
        this.climbUpButton = kClimbUp.button;
        this.climbDownButton = kClimbDown.button;
        this.climbOverride = kClimbOverride.button;
    }

    public void bindButtons()
    {
        // If subsystem is present then this method will bind the buttons
        if (subsystem.isPresent())
        {
            SK24Climb climb = subsystem.get();

            double joystickGain = kJoystickReversed ? -kJoystickChange : kJoystickChange;
            kClimbAxis.setFilter(new DeadbandFilter(kJoystickDeadband, joystickGain));

            climbUpButton.onTrue(new InstantCommand(() -> climb.runRightHook(0.2))); //TODO - change values back to 1.0
            climbUpButton.onTrue(new InstantCommand(() -> climb.runLeftHook(0.2))); //TODO - change values back to 1.0
            
            climbUpButton.onFalse(new InstantCommand(() -> climb.runRightHook(0.0))); 
            climbUpButton.onFalse(new InstantCommand(() -> climb.runLeftHook(0.0)));


            climbDownButton.onTrue(new InstantCommand(() -> climb.runRightHook(-0.2))); //TODO - change values back to 1.0
            climbDownButton.onTrue(new InstantCommand(() -> climb.runLeftHook(-0.2))); //TODO - change values back to 1.0
            
            climbDownButton.onFalse(new InstantCommand(() -> climb.runLeftHook(0.0)));
            climbDownButton.onFalse(new InstantCommand(() -> climb.runRightHook(0.0)));
            climbUpButton.and(climbOverride).onTrue(new InstantCommand(() -> climb.resetPosition(1.0)));

            //climbButton.onTrue(new ClimbBalanceCommand(climb)); //TODO - add climb balancing command later

            //  climb.setDefaultCommand(
            //             // Vertical movement of the arm is controlled by the Y axis of the right stick.
            //             // Up on joystick moving arm up and down on stick moving arm down.
            //             new ClimbAngleCommand(
            //                 () -> {return kClimbAxis.getFilteredAxis();},
            //                 climbOverride::getAsBoolean,
            //                 climb));
        }
    }
}

