package frc.robot.bindings;

import static frc.robot.Constants.IntakeConstants.kIntakeSpeed;
import static frc.robot.Constants.LauncherConstants.kTransferSpeed;

import java.util.Optional;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Ports;
import frc.robot.commands.IntakeAutoCommand;
import frc.robot.commands.StopCommand;
import frc.robot.commands.commandGroups.IntakeTransferCommandGroup;
import frc.robot.subsystems.SK24Intake;
import frc.robot.subsystems.SK24Launcher;
import frc.robot.utils.SKCANLight;

public class SK24IntakeBinder implements CommandBinder{
    Optional<SK24Intake> m_intake;
    Optional<SK24Launcher> m_launcher;
    SKCANLight light;
    Trigger intakeDriverButton;
    Trigger intakeOperatorButton;
    Trigger ejectDriverButton;
    Trigger ejectOperatorButton;
    Trigger operatorTransferButton;


    public SK24IntakeBinder(Optional<SK24Intake> intake, Optional<SK24Launcher> launcher, SKCANLight light){
        this.m_intake = intake;
        this.m_launcher = launcher;
        this.light = light;
        this.intakeDriverButton = Ports.DriverPorts.kIntake.button;
        this.intakeOperatorButton = Ports.OperatorPorts.kIntake.button;
        this.ejectDriverButton = Ports.DriverPorts.kEject.button;
        this.ejectOperatorButton = Ports.OperatorPorts.kEject.button;
        operatorTransferButton = Ports.OperatorPorts.kTransfer.button;
    }

    public void bindButtons()
    {
        // If subsystem is present then this method will bind the buttons
        if (m_intake.isPresent() && m_launcher.isPresent())
        {
            SK24Intake intake = m_intake.get();
            SK24Launcher launcher = m_launcher.get();

            // Eject Buttons
            ejectDriverButton.or(ejectOperatorButton).onTrue(new InstantCommand(() -> intake.setIntakeSpeed(-kIntakeSpeed)));
            ejectDriverButton.or(ejectOperatorButton).onTrue(new InstantCommand(() -> launcher.setTransferSpeed(-kTransferSpeed)));

            ejectDriverButton.or(ejectOperatorButton).onFalse(new InstantCommand(() -> intake.stopIntake()));
            ejectDriverButton.or(ejectOperatorButton).onFalse(new InstantCommand(() -> launcher.stopTransfer()));

            // Transfer Button
            operatorTransferButton.onTrue(new IntakeAutoCommand(intake, launcher));
            operatorTransferButton.onFalse(new StopCommand(intake, launcher));
            
            // Intake Buttons
            intakeDriverButton.or(intakeOperatorButton).onFalse(new InstantCommand(() -> launcher.stopLauncher()));
            intakeDriverButton.or(intakeOperatorButton).onFalse(new InstantCommand(() -> launcher.stopTransfer()));
           

            intakeDriverButton.or(intakeOperatorButton).whileTrue(new IntakeTransferCommandGroup(launcher, intake, light)); //TODO - test intake transfer command
            
        }
    }
}

