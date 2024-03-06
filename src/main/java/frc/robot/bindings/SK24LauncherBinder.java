package frc.robot.bindings;

import static frc.robot.Constants.LauncherAngleConstants.kJoystickChange;
import static frc.robot.Constants.LauncherAngleConstants.kJoystickReversed;
import static frc.robot.Constants.LauncherAngleConstants.kSpeakerAngle;
import static frc.robot.Constants.LauncherConstants.kAmpDefaultLeftSpeed;
import static frc.robot.Constants.LauncherConstants.kAmpDefaultRightSpeed;
import static frc.robot.Constants.LauncherConstants.kSpeakerDefaultLeftSpeed;
import static frc.robot.Constants.LauncherConstants.kSpeakerDefaultRightSpeed;
import static frc.robot.Constants.LauncherConstants.kTransferSpeed;
import static frc.robot.Constants.OIConstants.kJoystickDeadband;
import static frc.robot.Ports.OperatorPorts.kAngleSpeaker;
import static frc.robot.Ports.OperatorPorts.kLaunchAmp;
import static frc.robot.Ports.OperatorPorts.kLaunchSpeaker;
import static frc.robot.Ports.OperatorPorts.kLauncherAxis;
import static frc.robot.Ports.OperatorPorts.kLauncherOverride;
import static frc.robot.Ports.OperatorPorts.kManualAmp;
import static frc.robot.Ports.OperatorPorts.kManualLauncher;

import java.util.Optional;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Ports;
import frc.robot.commands.LaunchAngleCommand;
import frc.robot.commands.ZeroPositionCommand;
import frc.robot.commands.commandGroups.AmpScoreCommandGroup;
import frc.robot.subsystems.SK24Churro;
import frc.robot.subsystems.SK24Launcher;
import frc.robot.subsystems.SK24LauncherAngle;
import frc.robot.utils.filters.DeadbandFilter;

public class SK24LauncherBinder implements CommandBinder
{
    Optional<SK24Launcher> launcher;
    Optional<SK24LauncherAngle> launcherAngle;
    Optional<SK24Churro> churro;

    private Trigger manualLauncherButton;
    private Trigger angleOverrideButton;
    private Trigger zeroPosDriver;
    private Trigger zeroPosOperator;
    private Trigger defaultLauncherAngleButton;
    private Trigger manualAmpButton;
    private Trigger launchSpeaker;
    private Trigger launchAmp;

    /**
     * The class that is used to bind all the commands for the arm subsystem
     * 
     * @param controller
     *            The contoller that the commands are being bound to
     * @param launcher
     *            The required drive subsystem for the commands
     * @return 
     */
    public  SK24LauncherBinder(Optional<SK24Launcher> launcher, Optional<SK24LauncherAngle> launcherAngle, Optional<SK24Churro> churro)
    {
        this.launcher = launcher;
        this.launcherAngle = launcherAngle;
        this.churro = churro;

        manualLauncherButton = kManualLauncher.button;
        launchSpeaker = kLaunchSpeaker.button;
        manualAmpButton = kManualAmp.button;

        angleOverrideButton = kLauncherOverride.button;

        zeroPosDriver = Ports.OperatorPorts.kZeroPos.button;
        zeroPosOperator = Ports.DriverPorts.kZeroPos.button;

        defaultLauncherAngleButton = kAngleSpeaker.button;

        // operatorTransferButton = Ports.OperatorPorts.kTransfer.button;
        
        launchAmp = kLaunchAmp.button;
    }

    public void bindButtons()
    {
        // If subsystem is present then this method will bind the buttons
        if (launcher.isPresent())
        {

            SK24Launcher m_launcher = launcher.get();
            
            manualLauncherButton.onTrue(new InstantCommand(() -> m_launcher.setLauncherSpeed(kSpeakerDefaultLeftSpeed, kSpeakerDefaultRightSpeed)));
            manualLauncherButton.onFalse(new InstantCommand(() -> m_launcher.stopLauncher()));

            launchSpeaker.onTrue(new InstantCommand(() -> m_launcher.setLauncherSpeed(kSpeakerDefaultLeftSpeed, kSpeakerDefaultRightSpeed)));
            launchSpeaker.onFalse(new InstantCommand(() -> m_launcher.stopLauncher()));

            // manualAmpButton.onTrue(new InstantCommand(() -> m_launcher.setLauncherSpeed(kAmpDefaultLeftSpeed, kAmpDefaultRightSpeed)));
            // manualAmpButton.onFalse(new InstantCommand(() -> m_launcher.stopLauncher()));
        
            if(launcherAngle.isPresent())
            {
                SK24LauncherAngle m_launcherAngle = launcherAngle.get();
                double joystickGain = kJoystickReversed ? -kJoystickChange : kJoystickChange;
                    kLauncherAxis.setFilter(new DeadbandFilter(kJoystickDeadband, joystickGain));

                defaultLauncherAngleButton.onTrue(new InstantCommand(() -> m_launcherAngle.setTargetAngle(kSpeakerAngle)));
                
                manualLauncherButton.onFalse(new ZeroPositionCommand(m_launcherAngle, launcher.get()));
                manualAmpButton.onFalse(new ZeroPositionCommand(m_launcherAngle, launcher.get()));

                zeroPosDriver.or(zeroPosOperator).onTrue(new ZeroPositionCommand(m_launcherAngle, launcher.get()));
                
                m_launcherAngle.setDefaultCommand(
                        // Vertical movement of the arm is controlled by the Y axis of the right stick.
                        // Up on joystick moving arm up and down on stick moving arm down.
                        new LaunchAngleCommand(
                            () -> {return kLauncherAxis.getFilteredAxis();},
                            angleOverrideButton::getAsBoolean,
                            m_launcherAngle));
                
                launchAmp.onTrue(new AmpScoreCommandGroup(m_launcherAngle, m_launcher));
                
            }
        }
    }
}