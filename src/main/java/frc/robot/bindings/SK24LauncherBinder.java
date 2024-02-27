package frc.robot.bindings;

import static frc.robot.Constants.LauncherAngleConstants.kJoystickChange;
import static frc.robot.Constants.LauncherAngleConstants.kJoystickReversed;
import static frc.robot.Constants.LauncherAngleConstants.kSpeakerAngle;
import static frc.robot.Constants.OIConstants.kJoystickDeadband;
import static frc.robot.Ports.OperatorPorts.kAngleSpeaker;
import static frc.robot.Ports.OperatorPorts.kLaunchSpeaker;
import static frc.robot.Ports.OperatorPorts.kLauncherAxis;
import static frc.robot.Ports.OperatorPorts.kLauncherOverride;
import static frc.robot.Ports.OperatorPorts.kManualAmp;
import static frc.robot.Ports.OperatorPorts.kManualLauncher;
import static frc.robot.Ports.OperatorPorts.kManualTrap;
import static frc.robot.Constants.LauncherConstants.*;

import java.util.Optional;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Ports;
import frc.robot.commands.AutoLaunchCommand;
import frc.robot.commands.LaunchCommand;
import frc.robot.commands.LaunchOffCommand;
import frc.robot.subsystems.SK24Launcher;
import frc.robot.subsystems.SK24LauncherAngle;
import frc.robot.utils.filters.DeadbandFilter;

public class SK24LauncherBinder implements CommandBinder
{
    Optional<SK24Launcher> launcher;
    Optional<SK24LauncherAngle> launcherAngle;

    private Trigger manualLauncherButton = null;
    private Trigger angleOverrideButton = null;
    private Trigger zeroPosDriver = null;
    private Trigger zeroPosOperator = null;
    private Trigger defaultLauncherAngleButton = null;
    private Trigger manualAmpButton = null;

    /**
     * The class that is used to bind all the commands for the arm subsystem
     * 
     * @param controller
     *            The contoller that the commands are being bound to
     * @param launcher
     *            The required drive subsystem for the commands
     * @return 
     */
    public  SK24LauncherBinder(Optional<SK24Launcher> launcher, Optional<SK24LauncherAngle> launcherAngle)
    {
        this.launcher = launcher;
        this.launcherAngle = launcherAngle;
        manualLauncherButton = kManualLauncher.button;
        manualAmpButton = kManualAmp.button;
        angleOverrideButton = kLauncherOverride.button;
        zeroPosDriver = Ports.OperatorPorts.kZeroPos.button;
        zeroPosOperator = Ports.DriverPorts.kZeroPos.button;
        defaultLauncherAngleButton = kAngleSpeaker.button;
    }

    public void bindButtons()
    {
        // If subsystem is present then this method will bind the buttons
        if (launcher.isPresent())
        {

            SK24Launcher m_launcher = launcher.get();
            
            manualLauncherButton.onTrue(new LaunchCommand(m_launcher, kSpeakerDefaultTopSpeed, kSpeakerDefaultBottomSpeed));
            manualLauncherButton.onFalse(new LaunchOffCommand(m_launcher));

            manualAmpButton.onTrue(new LaunchCommand(m_launcher,kAmpDefaultTopSpeed, kAmpDefaultBottomSpeed));
            manualAmpButton.onFalse(new LaunchOffCommand(m_launcher));

        }
        if(launcherAngle.isPresent())
        {
            SK24LauncherAngle m_launcherAngle = launcherAngle.get();
            double joystickGain = kJoystickReversed ? -kJoystickChange : kJoystickChange;
                kLauncherAxis.setFilter(new DeadbandFilter(kJoystickDeadband, joystickGain));
            defaultLauncherAngleButton.onTrue(new InstantCommand(() -> m_launcherAngle.setTargetAngle(kSpeakerAngle)));
            

            zeroPosDriver.or(zeroPosOperator).onTrue(new InstantCommand(() -> m_launcherAngle.setTargetAngle(0.0)));
            m_launcherAngle.setDefaultCommand(
                    // Vertical movement of the arm is controlled by the Y axis of the right stick.
                    // Up on joystick moving arm up and down on stick moving arm down.
                    new AutoLaunchCommand(
                        () -> {return kLauncherAxis.getFilteredAxis();},
                        angleOverrideButton::getAsBoolean,
                        m_launcherAngle));
        }
    }

}