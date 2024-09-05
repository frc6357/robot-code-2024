package frc.robot.bindings;

import static frc.robot.Constants.IntakeConstants.kIntakeSpeed;
//import static frc.robot.Constants.IntakeConstants.kTransferSpeed;
import static frc.robot.Constants.LightConstants.kLightsOffBrightness;
import static frc.robot.Constants.LightConstants.kLightsOnBrightness;
import static frc.robot.Ports.OperatorPorts.kLaunchAmp;

import java.util.Optional;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Ports;
import frc.robot.commands.IntakeTransferCommand;
import frc.robot.commands.commandGroups.LaunchSpeakerCommandGroup;
//import frc.robot.commands.IntakeAutoCommand;
//import frc.robot.commands.StopIntakingCommand;
//import frc.robot.commands.commandGroups.IntakeTransferCommandGroup;
import frc.robot.subsystems.SK24Intake;
import frc.robot.subsystems.SK24Launcher;
//import frc.robot.subsystems.SK24LauncherAngle;
import frc.robot.utils.SKCANLight;

public class SK24IntakeBinder implements CommandBinder{
    Optional<SK24Intake> m_intake;
    Optional<SK24Launcher> m_launcher;
    //Optional<SK24LauncherAngle> m_launcherAngle;
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


    public SK24IntakeBinder(Optional<SK24Intake> intake, Optional<SK24Launcher> launcher, SKCANLight light){  //previously Optional<SK24LauncherAngle> arm
        this.m_intake = intake;
        this.m_launcher = launcher;
        this.light = light;
        //this.m_launcherAngle = arm;

        this.launchAmpButton = kLaunchAmp.button;
        this.intakeDriverButton = Ports.DriverPorts.kIntake.button;
        this.stopButton = Ports.DriverPorts.kStop.button;
        this.intakeOperatorButton = Ports.OperatorPorts.kIntake.button;
        this.ejectDriverButton = Ports.DriverPorts.kEject.button;
        this.ejectOperatorButton = Ports.OperatorPorts.kEject.button;
        this.driverPartyButton = Ports.DriverPorts.kPartyMode.button;
        this.operatorPartyButton = Ports.OperatorPorts.kPartyMode.button;
        //operatorTransferButton = Ports.OperatorPorts.kTransfer.button;
        lightsOffButton = Ports.DriverPorts.kLightsOff.button;
    }

    public void bindButtons()
    {
        // If subsystem is present then this method will bind the buttons
        if (m_intake.isPresent() && m_launcher.isPresent()) //previously m_launcherAngle.isPresent() in addition to all others to be true
        {
            SK24Intake intake = m_intake.get();
            SK24Launcher launcher = m_launcher.get();
            //SK24LauncherAngle arm = m_launcherAngle.get();

            operatorPartyButton.onFalse(new InstantCommand(() -> light.setBrightness(kLightsOnBrightness)));
            operatorPartyButton.onFalse(new InstantCommand(() -> light.setPartyMode()));
            
            driverPartyButton.onTrue(new InstantCommand(() -> light.clearAnimate()));
            driverPartyButton.onFalse(new InstantCommand(() -> light.setTeamColor()));
            driverPartyButton.onFalse(new InstantCommand(() -> light.setBrightness(kLightsOnBrightness)));

            lightsOffButton.onTrue(new InstantCommand(() -> light.setBrightness(kLightsOffBrightness)));

            // Eject Buttons
            ejectDriverButton.or(ejectOperatorButton).onTrue(new InstantCommand(() -> intake.setIntakeSpeed(-kIntakeSpeed)));
            //ejectDriverButton.or(ejectOperatorButton).onTrue(new InstantCommand(() -> intake.setTransferSpeed(-kTransferSpeed)));

            ejectDriverButton.or(ejectOperatorButton).onFalse(new InstantCommand(() -> intake.stopIntake()));
            //ejectDriverButton.or(ejectOperatorButton).onFalse(new InstantCommand(() -> intake.stopTransfer()));
            ejectDriverButton.or(ejectOperatorButton).onFalse(new InstantCommand(() -> light.setTeamColor()));

            // Transfer Button
            
            //operatorTransferButton.and(launchAmpButton.negate()).onTrue(new InstantCommand(() -> intake.setTransferSpeed(kTransferSpeed)));

            //operatorTransferButton.and(launchAmpButton).onTrue(new InstantCommand(() -> intake.setTransferSpeed(0.25)));
            //operatorTransferButton.and(launchAmpButton).onTrue(new InstantCommand(() -> intake.setIntakeSpeed(kIntakeSpeed)));
            intakeOperatorButton.onTrue(new InstantCommand(() -> intake.setIntakeSpeed(kIntakeSpeed)));  //perviously operatorIntakeButton
            intakeOperatorButton.onFalse(new InstantCommand(() -> intake.setIntakeSpeed(0.0)));
            
            //not originaly here
            intakeDriverButton.onTrue(new InstantCommand(() -> intake.setIntakeSpeed(kIntakeSpeed)));
            intakeDriverButton.onFalse(new InstantCommand(() -> intake.setIntakeSpeed(0.0)));
            //operatorTransferButton.onFalse(new StopIntakingCommand(intake));
            //stopButton.onTrue(new StopIntakingCommand(intake, launcher));



            
            //currently tseting below


            //intakeDriverButton.or(intakeOperatorButton).whileTrue(new IntakeTransferCommand(kIntakeSpeed, launcher, light)); //previously arm
            
            //temporary intake command until above one fixed
            //intakeDriverButton.or(intakeOperatorButton).whileTrue(new InstantCommmand(() -> intake.setIntakeSpeed(kIntakeSpeed)));

        }
    }
}

