package frc.robot.bindings;

import static frc.robot.Constants.ClimbConstants.kJoystickChange;
import static frc.robot.Constants.ClimbConstants.kJoystickDeadband;
import static frc.robot.Constants.ClimbConstants.kJoystickReversed;
import static frc.robot.Ports.DriverPorts.kClimb;
import static frc.robot.Ports.OperatorPorts.kClimbAxis;
import static frc.robot.Ports.OperatorPorts.kClimbOverride;

import java.util.Optional;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.ClimbAngleCommand;
import frc.robot.subsystems.SK24Climb;
import frc.robot.utils.filters.DeadbandFilter;
import static frc.robot.Ports.climbPorts.*;

public class SK24ClimbBinder implements CommandBinder{
    Optional<SK24Climb> subsystem;
    Trigger climbButton;
    Trigger climbOverride;


    public SK24ClimbBinder(Optional<SK24Climb> subsystem){
        this.subsystem = subsystem;
        this.climbButton = kClimb.button;
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

            climbButton.onTrue(new InstantCommand(() -> climb.setRightHook(1.0))); 
            // climbButton.onTrue(new InstantCommand(() -> climb.setLeftHook(1.0))); 
            
            climbButton.onFalse(new InstantCommand(() -> climb.setRightHook(0.0))); 
            // climbButton.onFalse(new InstantCommand(() -> climb.setLeftHook(0.0))); 

            //climbButton.onTrue(new ClimbBalanceCommand(climb)); TODO - add climb balancing command later

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

