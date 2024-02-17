package frc.robot.bindings;

import static frc.robot.Constants.OIConstants.kJoystickDeadband;
import static frc.robot.Ports.OperatorPorts.kLaunchSpeaker;
import static frc.robot.Ports.OperatorPorts.kLauncherAxis;
import static frc.robot.Ports.OperatorPorts.kLauncherOverride;

import java.util.Optional;

import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.LaunchAngleCommand;
import frc.robot.commands.LaunchCommand;
import frc.robot.commands.LaunchOffCommand;
import frc.robot.subsystems.SK24Launcher;
import frc.robot.subsystems.SK24LauncherAngle;
import frc.robot.utils.filters.DeadbandFilter;
import static frc.robot.Constants.LauncherAngleConstants.*;

public class SK24LauncherBinder implements CommandBinder
{
    Optional<SK24Launcher> launcher;
    Optional<SK24LauncherAngle> launcherAngle;

    private Trigger launcherButton = null;
    private Trigger angleOverrideButton = null;

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
        launcherButton = kLaunchSpeaker.button;
        angleOverrideButton = kLauncherOverride.button;
    }

    public void bindButtons()
    {
        // If subsystem is present then this method will bind the buttons
        if (launcher.isPresent())
        {

            SK24Launcher m_launcher = launcher.get();
            
            launcherButton.onTrue(new LaunchCommand(m_launcher, 0.5));
            launcherButton.onFalse(new LaunchOffCommand(m_launcher));
        }
        if(launcherAngle.isPresent())
        {
            SK24LauncherAngle m_launcherAngle = launcherAngle.get();
            double joystickGain = kJoystickReversed ? -kJoystickChange : kJoystickChange;
                kLauncherAxis.setFilter(new DeadbandFilter(kJoystickDeadband, joystickGain));

            m_launcherAngle.setDefaultCommand(
                    // Vertical movement of the arm is controlled by the Y axis of the right stick.
                    // Up on joystick moving arm up and down on stick moving arm down.
                    new LaunchAngleCommand(
                        () -> {return kLauncherAxis.getFilteredAxis();},
                        angleOverrideButton::getAsBoolean,
                        m_launcherAngle));
        }
    }

}