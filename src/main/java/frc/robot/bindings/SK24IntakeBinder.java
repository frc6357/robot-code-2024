package frc.robot.bindings;

import static frc.robot.Ports.OperatorPorts.kIntake;

import java.util.Optional;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.IntakeCommand;
import frc.robot.subsystems.SK24Intake;
import static frc.robot.Constants.IntakeConstants.*;

public class SK24IntakeBinder implements CommandBinder{
    Optional<SK24Intake> subsystem;
    Trigger intakeButton;

    public SK24IntakeBinder(Optional<SK24Intake> subsystem){
        this.subsystem = subsystem;
        this.intakeButton = kIntake.button;
    }

    public void bindButtons()
    {
        // If subsystem is present then this method will bind the buttons
        if (subsystem.isPresent())
        {
            SK24Intake intake = subsystem.get();
            intakeButton.onTrue(new IntakeCommand(intake));
            intakeButton.onFalse(new InstantCommand(() -> intake.setIntakeSpeed(0.0))); 
        }
    }
}

