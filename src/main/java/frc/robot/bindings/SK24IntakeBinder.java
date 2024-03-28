package frc.robot.bindings;

import static frc.robot.Constants.IntakeConstants.kIntakeSpeed;
import static frc.robot.Constants.LauncherConstants.kTransferSpeed;
import static frc.robot.Ports.OperatorPorts.kLaunchAmp;
import static frc.robot.Constants.LightConstants.*;

import java.util.Optional;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Ports;
import frc.robot.commands.IntakeAutoCommand;
import frc.robot.commands.StopIntakingCommand;
import frc.robot.commands.commandGroups.IntakeTransferCommandGroup;
import frc.robot.subsystems.SK24Intake;
import frc.robot.subsystems.SK24Launcher;
import frc.robot.utils.SKCANLight;

public class SK24IntakeBinder implements CommandBinder{
    Optional<SK24Intake> m_intake;
    Optional<SK24Launcher> m_launcher;
    SKCANLight light;
    Trigger intakeDriverButton;
    Trigger launchAmpButton;
    Trigger intakeOperatorButton;
    Trigger ejectDriverButton;
    Trigger ejectOperatorButton;
    Trigger driverPartyButton;
    Trigger operatorPartyButton;
    Trigger operatorTransferButton;
    Trigger lightsOffButton;
    Trigger stopButton;


    public SK24IntakeBinder(Optional<SK24Intake> intake, Optional<SK24Launcher> launcher, SKCANLight light){
        this.m_intake = intake;
        this.m_launcher = launcher;
        this.light = light;

        this.launchAmpButton = kLaunchAmp.button;
        this.intakeDriverButton = Ports.DriverPorts.kIntake.button;
        this.stopButton = Ports.DriverPorts.kStop.button;
        this.intakeOperatorButton = Ports.OperatorPorts.kIntake.button;
        this.ejectDriverButton = Ports.DriverPorts.kEject.button;
        this.ejectOperatorButton = Ports.OperatorPorts.kEject.button;
        this.driverPartyButton = Ports.DriverPorts.kPartyMode.button;
        this.operatorPartyButton = Ports.OperatorPorts.kPartyMode.button;
        operatorTransferButton = Ports.OperatorPorts.kTransfer.button;
        lightsOffButton = Ports.DriverPorts.kLightsOff.button;
    }

    public void bindButtons()
    {
        // If subsystem is present then this method will bind the buttons
        if (m_intake.isPresent() && m_launcher.isPresent())
        {
            SK24Intake intake = m_intake.get();
            SK24Launcher launcher = m_launcher.get();

            operatorPartyButton.onFalse(new InstantCommand(() -> light.setBrightness(kLightsOnBrightness)));
            operatorPartyButton.onFalse(new InstantCommand(() -> light.setPartyMode()));
            
            driverPartyButton.onTrue(new InstantCommand(() -> light.clearAnimate()));
            driverPartyButton.onFalse(new InstantCommand(() -> light.setTeamColor()));
            driverPartyButton.onFalse(new InstantCommand(() -> light.setBrightness(kLightsOnBrightness)));

            lightsOffButton.onTrue(new InstantCommand(() -> light.setBrightness(kLightsOffBrightness)));

            // Eject Buttons
            ejectDriverButton.or(ejectOperatorButton).onTrue(new InstantCommand(() -> intake.setIntakeSpeed(-kIntakeSpeed)));
            ejectDriverButton.or(ejectOperatorButton).onTrue(new InstantCommand(() -> launcher.setTransferSpeed(-kTransferSpeed)));

            ejectDriverButton.or(ejectOperatorButton).onFalse(new InstantCommand(() -> intake.stopIntake()));
            ejectDriverButton.or(ejectOperatorButton).onFalse(new InstantCommand(() -> launcher.stopTransfer()));
            ejectDriverButton.or(ejectOperatorButton).onFalse(new InstantCommand(() -> light.setTeamColor()));

            // Transfer Button
            operatorTransferButton.and(launchAmpButton.negate()).onTrue(new IntakeAutoCommand(intake, launcher));

            operatorTransferButton.and(launchAmpButton).onTrue(new InstantCommand(() -> launcher.setTransferSpeed(0.25)));
            operatorTransferButton.and(launchAmpButton).onTrue(new InstantCommand(() -> intake.setIntakeSpeed(kIntakeSpeed)));
            
            operatorTransferButton.onFalse(new StopIntakingCommand(intake, launcher));

            intakeDriverButton.or(intakeOperatorButton).whileTrue(new IntakeTransferCommandGroup(launcher, intake, light));
           
            //stopButton.onTrue(new StopIntakingCommand(intake, launcher));
        }
    }
}

