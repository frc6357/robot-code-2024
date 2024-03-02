package frc.robot.bindings;

import static frc.robot.Ports.OperatorPorts.kIntake;

import java.util.Optional;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.IntakeTransferCommand;
import frc.robot.subsystems.SK24Intake;
import frc.robot.subsystems.SK24Launcher;

public class SK24IntakeBinder implements CommandBinder{
    Optional<SK24Intake> m_intake;
    Optional<SK24Launcher> m_launcher;
    Trigger intakeButton;

    public SK24IntakeBinder(Optional<SK24Intake> intake, Optional<SK24Launcher> launcher){
        this.m_intake = intake;
        this.m_launcher = launcher;
        this.intakeButton = kIntake.button;
    }

    public void bindButtons()
    {
        // If subsystem is present then this method will bind the buttons
        if (m_intake.isPresent() && m_launcher.isPresent())
        {
            SK24Intake intake = m_intake.get();
            SK24Launcher launcher = m_launcher.get();
            intakeButton.whileTrue(new IntakeTransferCommand(intake, launcher));
        }
    }
}

