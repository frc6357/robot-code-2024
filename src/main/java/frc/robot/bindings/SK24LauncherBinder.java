package frc.robot.bindings;

import static frc.robot.Constants.ChurroConstants.kChurroLowerPosition;
import static frc.robot.Constants.ChurroConstants.kChurroRaisePosition;
import static frc.robot.Constants.LauncherAngleConstants.kAmpAngle;
import static frc.robot.Constants.LauncherAngleConstants.kFloorAngle;
import static frc.robot.Constants.LauncherAngleConstants.kJoystickChange;
import static frc.robot.Constants.LauncherAngleConstants.kJoystickReversed;
import static frc.robot.Constants.LauncherAngleConstants.kLauncherAngleWingKey;
import static frc.robot.Constants.LauncherAngleConstants.kSpeakerAngle;
import static frc.robot.Constants.LauncherAngleConstants.kWingAngle;
import static frc.robot.Constants.LauncherConstants.ampSpeedLeft;
import static frc.robot.Constants.LauncherConstants.ampSpeedRight;
import static frc.robot.Constants.LauncherConstants.kSpeakerDefaultLeftSpeed;
import static frc.robot.Constants.LauncherConstants.kSpeakerDefaultLeftSpeedKey;
import static frc.robot.Constants.LauncherConstants.kSpeakerDefaultRightSpeed;
import static frc.robot.Constants.LauncherConstants.kSpeakerDefaultRightSpeedKey;
import static frc.robot.Constants.LauncherConstants.speakerSpeedLeft;
import static frc.robot.Constants.LauncherConstants.speakerSpeedRight;
import static frc.robot.Constants.OIConstants.kJoystickDeadband;
import static frc.robot.Ports.DriverPorts.kRotateSpeaker;
import static frc.robot.Ports.OperatorPorts.kAngleSpeaker;
import static frc.robot.Ports.OperatorPorts.kLaunchAmp;
import static frc.robot.Ports.OperatorPorts.kLaunchSpeaker;
import static frc.robot.Ports.OperatorPorts.kLauncherAxis;
import static frc.robot.Ports.OperatorPorts.kLauncherOverride;
import static frc.robot.Ports.OperatorPorts.kVisionAngle;

import java.util.Optional;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Ports;
import frc.robot.commands.LaunchAngleCommand;
import frc.robot.commands.ReadyScoreCommandAngle;
import frc.robot.commands.ZeroPositionCommand;
import frc.robot.commands.commandGroups.ChurroLowerCommandGroup;
import frc.robot.commands.commandGroups.ChurroRaiseCommandGroup;
import frc.robot.commands.commandGroups.LaunchSpeakerCommandGroup;
import frc.robot.preferences.Pref;
import frc.robot.preferences.SKPreferences;
import frc.robot.subsystems.SK24Churro;
import frc.robot.subsystems.SK24Launcher;
import frc.robot.subsystems.SK24LauncherAngle;
import frc.robot.subsystems.SK24Vision;
import frc.robot.utils.SKCANLight;
import frc.robot.utils.filters.DeadbandFilter;
public class SK24LauncherBinder implements CommandBinder
{
    Optional<SK24Launcher> launcher;
    Optional<SK24LauncherAngle> launcherAngle;
    Optional<SK24Vision> vision;
    Optional<SK24Churro> churro;
    SKCANLight light;

    Trigger intakeDriverButton;
    Trigger intakeOperatorButton;
    private Trigger angleOverrideButton;
    Trigger angleUpDriverButton;
    private Trigger defaultLauncherAngleButton;
    private Trigger floorAngleDriver;
    private Trigger floorAngleOperator;
    private Trigger defaultFloorAngleButton;
    private Trigger launchAmpButton;
    private Trigger ampAngleButton;
    private Trigger launchSpeakerButton;
    // private Trigger resetAngleButton;
    private Trigger visionAngleButton;
    private Trigger wingAngleButton;
    private final Trigger readyShoot;

    Pref<Double> launcherAngleWing = SKPreferences.attach(kLauncherAngleWingKey, kWingAngle);


    /**
     * The class that is used to bind all the commands for the arm subsystem
     * 
     * @param controller
     *            The contoller that the commands are being bound to
     * @param launcher
     *            The required drive subsystem for the commands
     * @return 
     */
    public  SK24LauncherBinder(Optional<SK24Launcher> launcher, Optional<SK24LauncherAngle> launcherAngle, Optional<SK24Vision> vision, Optional<SK24Churro> churro, SKCANLight light)
    {
        this.launcher = launcher;
        this.launcherAngle = launcherAngle;
        this.vision = vision;
        this.churro = churro;
        this.light = light;

        this.intakeDriverButton = Ports.DriverPorts.kIntake.button;
        this.intakeOperatorButton = Ports.OperatorPorts.kIntake.button;
        angleUpDriverButton = Ports.DriverPorts.kRobotCentricMode.button;

        launchSpeakerButton = kLaunchSpeaker.button;
        launchAmpButton = kLaunchAmp.button;
        visionAngleButton = kVisionAngle.button;

        readyShoot = kRotateSpeaker.button;

        angleOverrideButton = kLauncherOverride.button;

        floorAngleDriver = Ports.OperatorPorts.kAngleFloor.button;
        floorAngleOperator = Ports.DriverPorts.kAngleFloor.button;
        defaultFloorAngleButton = Ports.OperatorPorts.kAngleFloor.button;
        // resetAngleButton = Ports.OperatorPorts.kResetLauncherEncoder.button;

        ampAngleButton = Ports.OperatorPorts.kAngleAmp.button;
        wingAngleButton = Ports.OperatorPorts.kAngleWing.button;

        defaultLauncherAngleButton = kAngleSpeaker.button;
    }

    public void bindButtons()
    {
        // If subsystem is present then this method will bind the buttons
        if (launcher.isPresent())
        {

            SK24Launcher m_launcher = launcher.get();

            // Launch Speaker Button
            
            launchSpeakerButton.onTrue(new LaunchSpeakerCommandGroup(m_launcher, light));
            launchSpeakerButton.onFalse(new InstantCommand(() -> {
                m_launcher.rampDown();
                m_launcher.setLauncherSpeed(0.0, 0.0);
                light.setTeamColor();
            }, m_launcher));
            
            
            if(churro.isPresent())
            {
                SK24Churro m_churro = churro.get();

                // Launch Amp Button
                if(launcherAngle.isPresent()){
                    SK24LauncherAngle m_launcherAngle = launcherAngle.get();
                    launchAmpButton.onTrue(new InstantCommand(() -> {
                    m_launcher.setAmpRampRate();
                    m_launcher.setLauncherSpeed(ampSpeedLeft.get(), ampSpeedRight.get());
                }, m_launcher));
                    launchAmpButton.onTrue(new ChurroRaiseCommandGroup(m_launcherAngle, m_churro));
                    launchAmpButton.onFalse(new ChurroLowerCommandGroup(m_launcherAngle, m_churro));
                    launchAmpButton.onFalse(new InstantCommand(() -> {
                    m_launcher.rampDown();
                    m_launcher.stopLauncher();
                    light.setTeamColor();
                }, m_launcher));

                }else{
                    launchAmpButton.onTrue(new InstantCommand(() -> {
                    m_launcher.setAmpRampRate();
                    m_launcher.setLauncherSpeed(ampSpeedLeft.get(), ampSpeedRight.get());
                }, m_launcher));
                    launchAmpButton.onTrue(new InstantCommand(() -> m_churro.setChurroPosition(kChurroRaisePosition)));
                    launchAmpButton.onFalse(new InstantCommand(() -> m_churro.setChurroPosition(kChurroLowerPosition)));
                    launchAmpButton.onFalse(new InstantCommand(() -> {
                    m_launcher.rampDown();
                    m_launcher.stopLauncher();
                    light.setTeamColor();
                }, m_launcher));
                }
                

                //SmartDashboard.putNumber("Churro Angle", m_churro.getChurroPosition());
            }
            if(launcherAngle.isPresent())
            {
                SK24LauncherAngle m_launcherAngle = launcherAngle.get();
                if(vision.isPresent())
                {
                    SK24Vision m_vision = vision.get();

                    // Vision Angle Change Button
                    visionAngleButton.onTrue(new InstantCommand(() -> m_vision.setSpeakerMode()));
                    visionAngleButton.onFalse(new InstantCommand(() -> m_vision.setAllTagMode()));
                    visionAngleButton.whileTrue(new ReadyScoreCommandAngle(m_launcherAngle, vision.get()));
                }

                //intakeDriverButton.or(intakeOperatorButton).onTrue(new InstantCommand(() -> m_launcherAngle.setTargetAngle(kSpeakerAngle)));
                intakeDriverButton.and(launchSpeakerButton.negate()).onFalse(new InstantCommand(() -> m_launcherAngle.setTargetAngle(kFloorAngle), m_launcherAngle));

                readyShoot.onTrue(new InstantCommand(() -> m_launcherAngle.setTargetAngle(kSpeakerAngle)));
                launchSpeakerButton.onFalse(new InstantCommand(() -> m_launcherAngle.setTargetAngle(kFloorAngle)));

                double joystickGain = kJoystickReversed ? -kJoystickChange : kJoystickChange;
                    kLauncherAxis.setFilter(new DeadbandFilter(kJoystickDeadband, joystickGain));

                // Launch Angle Buttons
                defaultLauncherAngleButton.onTrue(new InstantCommand(() -> m_launcherAngle.setTargetAngle(kSpeakerAngle), m_launcherAngle)); // 42 deg
                angleUpDriverButton.onTrue(new InstantCommand(() -> m_launcherAngle.setTargetAngle(kSpeakerAngle), m_launcherAngle)); // 42 deg
                defaultFloorAngleButton.onTrue(new InstantCommand(() -> m_launcherAngle.setTargetAngle(kFloorAngle))); // 14 deg
                ampAngleButton.onTrue(new InstantCommand(() -> m_launcherAngle.setTargetAngle(kAmpAngle))); // 44 deg
                wingAngleButton.onTrue(new InstantCommand(() -> m_launcherAngle.setTargetAngle(launcherAngleWing.get()))); // 23 deg

                // resetAngleButton.onTrue(new InstantCommand(() -> m_launcherAngle.resetAngle()));

                
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