package frc.robot.bindings;

import static frc.robot.Constants.ClimbConstants.kMinAngle;
import static frc.robot.Constants.LauncherAngleConstants.kAmpAngle;
import static frc.robot.Constants.LauncherAngleConstants.kJoystickChange;
import static frc.robot.Constants.LauncherAngleConstants.kJoystickReversed;
import static frc.robot.Constants.LauncherAngleConstants.kMinAngle;
import static frc.robot.Constants.LauncherAngleConstants.kSpeakerAngle;
import static frc.robot.Constants.LauncherConstants.kAmpDefaultLeftSpeed;
import static frc.robot.Constants.LauncherConstants.kAmpDefaultRightSpeed;
import static frc.robot.Constants.LauncherConstants.kSpeakerDefaultLeftSpeed;
import static frc.robot.Constants.LauncherConstants.kSpeakerDefaultRightSpeed;
import static frc.robot.Constants.LauncherConstants.kTransferSpeed;
import static frc.robot.Constants.OIConstants.kJoystickDeadband;
import static frc.robot.Ports.OperatorPorts.kAngleFloor;
import static frc.robot.Ports.OperatorPorts.kAngleSpeaker;
import static frc.robot.Ports.OperatorPorts.kLaunchAmp;
//import static frc.robot.Ports.OperatorPorts.kLaunchAmp;
import static frc.robot.Ports.OperatorPorts.kLaunchSpeaker;
import static frc.robot.Ports.OperatorPorts.kLauncherAxis;
import static frc.robot.Ports.OperatorPorts.kLauncherOverride;
import static frc.robot.Ports.OperatorPorts.kLaunchAmp;

import java.util.Optional;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.commands.LaunchAngleCommand;
import frc.robot.commands.ZeroPositionCommand;
import frc.robot.commands.commandGroups.AmpScoreCommandGroup;
import frc.robot.subsystems.SK24Churro;
import frc.robot.subsystems.SK24Launcher;
import frc.robot.subsystems.SK24LauncherAngle;
import frc.robot.utils.filters.DeadbandFilter;
import static frc.robot.Constants.LauncherAngleConstants.*;

public class SK24LauncherBinder implements CommandBinder
{
    Optional<SK24Launcher> launcher;
    Optional<SK24LauncherAngle> launcherAngle;
    Optional<SK24Churro> churro;

    private Trigger manualLauncherButton;
    private Trigger angleOverrideButton;
    private Trigger operatorTransferButton;
    private Trigger defaultLauncherAngleButton;
    private Trigger floorAngleDriver;
    private Trigger floorAngleOperator;
    private Trigger defaultFloorAngleButton;
    private Trigger launchAmpButton;
    private Trigger ampAngleButton;
    private Trigger launchSpeakerButton;
    //private Trigger launchAmp;

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

        launchSpeakerButton = kLaunchSpeaker.button;
        launchAmpButton = kLaunchAmp.button;

        angleOverrideButton = kLauncherOverride.button;

        floorAngleDriver = Ports.OperatorPorts.kAngleFloor.button;
        floorAngleOperator = Ports.DriverPorts.kAngleFloor.button;
        defaultFloorAngleButton = Ports.OperatorPorts.kAngleFloor.button;

        ampAngleButton = Ports.OperatorPorts.kAngleAmp.button;

        defaultLauncherAngleButton = kAngleSpeaker.button;

        operatorTransferButton = Ports.OperatorPorts.kTransfer.button;
        
    }

    public void bindButtons()
    {
        // If subsystem is present then this method will bind the buttons
        if (launcher.isPresent())
        {

            SK24Launcher m_launcher = launcher.get();

            operatorTransferButton.onTrue(new InstantCommand(() -> m_launcher.setTransferSpeed(0.6)));
            operatorTransferButton.onFalse(new InstantCommand(() -> m_launcher.stopTransfer()));

            launchSpeakerButton.onTrue(new InstantCommand(() -> m_launcher.setLauncherSpeed(0.6, 0.7))); //used to be .7 .8
            launchSpeakerButton.onFalse(new InstantCommand(() -> m_launcher.stopLauncher()));

            launchAmpButton.onTrue(new InstantCommand(() -> m_launcher.setLauncherSpeed(0.1, 0.1)));
            launchAmpButton.onFalse(new InstantCommand(() -> m_launcher.setLauncherSpeed(0.1, 0.1)));
            

        
            if(launcherAngle.isPresent())
            {
                SK24LauncherAngle m_launcherAngle = launcherAngle.get();
                double joystickGain = kJoystickReversed ? -kJoystickChange : kJoystickChange;
                    kLauncherAxis.setFilter(new DeadbandFilter(kJoystickDeadband, joystickGain));

                defaultLauncherAngleButton.onTrue(new InstantCommand(() -> m_launcherAngle.setTargetAngle(kSpeakerAngle)));
                defaultFloorAngleButton.onTrue(new InstantCommand(() -> m_launcherAngle.setTargetAngle(kFloorAngle)));
                ampAngleButton.onTrue(new InstantCommand(() -> m_launcherAngle.setTargetAngle(kAmpAngle)));

                
                // manualLauncherButton.onFalse(new ZeroPositionCommand(m_launcherAngle, launcher.get()));
                // manualAmpButton.onFalse(new ZeroPositionCommand(m_launcherAngle, launcher.get()));

                floorAngleDriver.or(floorAngleOperator).onTrue(new ZeroPositionCommand(m_launcherAngle, launcher.get()));
                
                m_launcherAngle.setDefaultCommand(
                        // Vertical movement of the arm is controlled by the Y axis of the right stick.
                        // Up on joystick moving arm up and down on stick moving arm down.
                        new LaunchAngleCommand(
                            () -> {return kLauncherAxis.getFilteredAxis();},
                            angleOverrideButton::getAsBoolean,
                            m_launcherAngle));
                
                //launchAmp.onTrue(new AmpScoreCommandGroup(m_launcherAngle, m_launcher));
                
            }
        }
    }
}