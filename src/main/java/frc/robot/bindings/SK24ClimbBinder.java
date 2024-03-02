package frc.robot.bindings;

import static frc.robot.Ports.DriverPorts.kClimb;

import java.util.Optional;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.SK24Climb;

public class SK24ClimbBinder implements CommandBinder{
    Optional<SK24Climb> subsystem;
    Trigger climbButton;

    public SK24ClimbBinder(Optional<SK24Climb> subsystem){
        this.subsystem = subsystem;
        this.climbButton = kClimb.button;
    }

    public void bindButtons()
    {
        // If subsystem is present then this method will bind the buttons
        if (subsystem.isPresent())
        {
            SK24Climb climb = subsystem.get();
            climbButton.onTrue(new InstantCommand(() -> climb.setRightHook(1.0))); 
            // climbButton.onTrue(new InstantCommand(() -> climb.setLeftHook(1.0))); 
            
            climbButton.onFalse(new InstantCommand(() -> climb.setRightHook(0.0))); 
            // climbButton.onFalse(new InstantCommand(() -> climb.setLeftHook(0.0))); 

            //climbButton.onTrue(new ClimbBalanceCommand(climb)); TODO - add climb balancing command later
        }
    }
}

