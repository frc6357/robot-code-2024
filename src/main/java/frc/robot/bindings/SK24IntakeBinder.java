package frc.robot.bindings;

import static frc.robot.Constants.IntakeConstants.kIntakeSpeed;
import static frc.robot.Constants.LauncherConstants.kTransferSpeed;

import java.util.Optional;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.SK24Intake;
import frc.robot.subsystems.SK24Launcher;
import frc.robot.Ports;

public class SK24IntakeBinder implements CommandBinder{
    Optional<SK24Intake> m_intake;
    Optional<SK24Launcher> m_launcher;
    Trigger intakeDriverButton;
    Trigger intakeOperatorButton;
    Trigger ejectButton;

    public SK24IntakeBinder(Optional<SK24Intake> intake, Optional<SK24Launcher> launcher){
        this.m_intake = intake;
        this.m_launcher = launcher;
        this.intakeDriverButton = Ports.DriverPorts.kIntake.button;
        this.intakeOperatorButton = Ports.OperatorPorts.kIntake.button;
        this.ejectButton = Ports.DriverPorts.kEject.button;
    }

    public void bindButtons()
    {
        // If subsystem is present then this method will bind the buttons
        if (m_intake.isPresent() && m_launcher.isPresent())
        {
            SK24Intake intake = m_intake.get();
            SK24Launcher launcher = m_launcher.get();

            intakeDriverButton.or(intakeOperatorButton).onTrue(new InstantCommand(() -> intake.setIntakeSpeed(kIntakeSpeed)));
            ejectButton.onTrue(new InstantCommand(() -> intake.setIntakeSpeed(-kIntakeSpeed)));
            intakeDriverButton.or(intakeOperatorButton).onTrue(new InstantCommand(() -> launcher.setTransferSpeed(kTransferSpeed)));

            intakeDriverButton.or(intakeOperatorButton).onFalse(new InstantCommand(() -> intake.stopIntake()));
            ejectButton.onFalse(new InstantCommand(() -> intake.stopIntake()));
            intakeDriverButton.or(intakeOperatorButton).onFalse(new InstantCommand(() -> launcher.stopTransfer()));

            //intakeButton.whileTrue(new IntakeTransferCommand(intake, launcher)); //TODO - test intake transfer command
        }
    }
}

