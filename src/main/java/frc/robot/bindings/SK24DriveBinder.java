package frc.robot.bindings;

import static frc.robot.Constants.OIConstants.kDriveCoeff;
import static frc.robot.Constants.OIConstants.kJoystickDeadband;
import static frc.robot.Constants.OIConstants.kRotationCoeff;
import static frc.robot.Constants.OIConstants.kSlowModePercent;
import static frc.robot.Ports.DriverPorts.kResetGyroPos;
import static frc.robot.Ports.DriverPorts.kRobotCentricMode;
import static frc.robot.Ports.DriverPorts.kRotateLeft;
import static frc.robot.Ports.DriverPorts.kRotateRight;
import static frc.robot.Ports.DriverPorts.kRotateSource;
import static frc.robot.Ports.DriverPorts.kRotateSpeaker;
import static frc.robot.Ports.DriverPorts.kTranslationYPort;
import static frc.robot.Ports.DriverPorts.kSlowMode;
import static frc.robot.Ports.DriverPorts.kTranslationXPort;
import static frc.robot.Ports.DriverPorts.kVelocityOmegaPort;
import static frc.robot.Ports.OperatorPorts.kLaunchAmp;

import java.util.Optional;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.DriveConstants;
import frc.robot.commands.AmpCenterCommand;
import frc.robot.commands.DefaultSwerveCommand;
import frc.robot.commands.DriveTurnCommand;
import frc.robot.commands.ReadyScoreCommand;
import frc.robot.commands.TurnSpeakerCommand;
import frc.robot.subsystems.SK24Drive;
import frc.robot.subsystems.SK24LauncherAngle;
import frc.robot.subsystems.SK24Vision;
import frc.robot.utils.filters.CubicDeadbandFilter;
import frc.robot.utils.filters.Filter;

public class SK24DriveBinder implements CommandBinder
{
    Optional<SK24Drive>  m_drive;
    Optional<SK24LauncherAngle>  m_arm;
    Optional<SK24Vision>  m_vision;
    

    // Driver Buttons
    private final Trigger robotCentric;
    
    private final Trigger slowmode;
    private final Trigger resetButton;
    private final Trigger rotateSpeaker;
    private final Trigger rotateLeft;
    private final Trigger rotateRight;
    private final Trigger rotateSource;
    private final Trigger launchAmpButton;

    // private final Trigger centerStage;
    // private final Trigger leftStage;
    // private final Trigger rightStage;

    /**
     * The class that is used to bind all the commands for the drive subsystem
     * 
     * @param m_drive
     *            The required drive subsystem for the commands
     */
    public SK24DriveBinder(Optional<SK24Drive> m_drive, Optional<SK24LauncherAngle> m_arm, Optional<SK24Vision> m_vision)
    {
        this.m_drive  = m_drive;
        this.m_arm = m_arm;
        this.m_vision = m_vision;
        
        launchAmpButton = kLaunchAmp.button;

        robotCentric    = kRobotCentricMode.button;
        slowmode = kSlowMode.button;
        resetButton = kResetGyroPos.button;
        rotateSpeaker = kRotateSpeaker.button;
        rotateLeft = kRotateLeft.button;
        rotateRight = kRotateRight.button;
        rotateSource = kRotateSource.button;
        // centerStage = kCenterStage.button;
        // leftStage = kCenterStage.button;
        // rightStage = kCenterStage.button;

        
    }
    
    public void bindButtons()
    {
        if (m_drive.isPresent())
        {
            SK24Drive drive = m_drive.get();

            // Sets filters for driving axes
            kTranslationXPort.setFilter(new CubicDeadbandFilter(kDriveCoeff,
                kJoystickDeadband, DriveConstants.kMaxSpeedMetersPerSecond, true));

            kTranslationYPort.setFilter(new CubicDeadbandFilter(kDriveCoeff,
                kJoystickDeadband, DriveConstants.kMaxSpeedMetersPerSecond, true));
            
            kVelocityOmegaPort.setFilter(new CubicDeadbandFilter(kRotationCoeff, kJoystickDeadband,
                Math.toRadians(DriveConstants.kMaxModuleAngularSpeedDegreesPerSecond), true));

            slowmode.
                onTrue(new InstantCommand(() -> {setGainCommand(kSlowModePercent);}, drive))
                .onFalse(new InstantCommand(() -> {setGainCommand(1);}, drive));

            // Resets gyro angles
            resetButton.onTrue(new InstantCommand(drive::setFront));
            
            if(m_vision.isPresent())
            {

                launchAmpButton.whileTrue(new AmpCenterCommand(() -> kTranslationYPort.getFilteredAxis(), drive, m_vision.get()));
            }

            if(m_arm.isPresent() && m_vision.isPresent())
            {
                SK24LauncherAngle arm = m_arm.get();
                SK24Vision vision = m_vision.get();
                rotateSpeaker.whileTrue(
                    new ReadyScoreCommand(() -> kTranslationXPort.getFilteredAxis(),
                        () -> kTranslationYPort.getFilteredAxis(),
                        drive,arm, vision));
            }else
            {
                rotateSpeaker.whileTrue(
                    new TurnSpeakerCommand(() -> kTranslationXPort.getFilteredAxis(),
                        () -> kTranslationYPort.getFilteredAxis(),
                        drive));
            }
            rotateLeft.and(robotCentric.negate()).whileTrue(
                new DriveTurnCommand(
                    () -> kTranslationXPort.getFilteredAxis(),
                    () -> kTranslationYPort.getFilteredAxis(),
                    robotCentric::getAsBoolean, 90.0, drive));

            rotateRight.and(robotCentric.negate()).whileTrue(
                new DriveTurnCommand(
                    () -> kTranslationXPort.getFilteredAxis(),
                    () -> kTranslationYPort.getFilteredAxis(),
                    robotCentric::getAsBoolean, 270.0, drive));
            rotateSource.whileTrue(
                new DriveTurnCommand(
                    () -> kTranslationXPort.getFilteredAxis(),
                    () -> kTranslationYPort.getFilteredAxis(),
                    robotCentric::getAsBoolean,0.0, drive)); 
                    
            // centerStage.whileTrue(OnTheFly.centerStageCommand);
            // leftStage.whileTrue(OnTheFly.LeftStageCommand);
            // rightStage.whileTrue(OnTheFly.rightStageCommand);

            // Default command for driving
            drive.setDefaultCommand(
                new DefaultSwerveCommand(
                    () -> kTranslationXPort.getFilteredAxis(),
                    () -> kTranslationYPort.getFilteredAxis(),
                    () -> kVelocityOmegaPort.getFilteredAxis(),
                    robotCentric::getAsBoolean, drive));

        }
    } 

    /**
     * Sets the gains on the filters for the joysticks
     * 
     * @param percent
     *            The percent value of the full output that should be allowed (value
     *            should be between 0 and 1)
     */
    public void setGainCommand(double percent)
    {
        Filter translation = new CubicDeadbandFilter(kDriveCoeff, kJoystickDeadband,
            DriveConstants.kMaxSpeedMetersPerSecond * percent, true);
        kTranslationXPort.setFilter(translation);
        kTranslationYPort.setFilter(translation);

     
        Filter rotation = new CubicDeadbandFilter(kDriveCoeff, kJoystickDeadband,
            Math.toRadians(DriveConstants.kMaxRotationDegreesPerSecond) * percent, true);
        kVelocityOmegaPort.setFilter(rotation);
  
    }
  
} 
 
