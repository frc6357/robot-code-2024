package frc.robot.bindings;

import static frc.robot.Constants.OIConstants.kDriveCoeff;
import static frc.robot.Constants.OIConstants.kJoystickDeadband;
import static frc.robot.Constants.OIConstants.kRotationCoeff;
import static frc.robot.Constants.OIConstants.kSlowModePercent;
import static frc.robot.Ports.DriverPorts.*;


import java.util.Optional;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.ModuleConstants;
import frc.robot.commands.DefaultSwerveCommand;
import frc.robot.commands.DriveTurnCommand;
import frc.robot.subsystems.SK24Drive;
import frc.robot.utils.filters.CubicDeadbandFilter;
import frc.robot.utils.filters.Filter;

public class SK24DriveBinder implements CommandBinder
{
    Optional<SK24Drive>  subsystem;
    

    // Driver Buttons
    private final Trigger robotCentric;
    
    private final Trigger slowmode;
    private final Trigger resetButton;
    private final Trigger rotateSpeaker;
    private final Trigger rotateAmp;
    private final Trigger rotateSource;

    /**
     * The class that is used to bind all the commands for the drive subsystem
     * 
     * @param subsystem
     *            The required drive subsystem for the commands
     */
    public SK24DriveBinder(Optional<SK24Drive> subsystem)
    {
        this.subsystem  = subsystem;
        
        robotCentric    = kRobotCentricMode.button;
        slowmode = kSlowMode.button;
        resetButton = kResetGyroPos.button;
        rotateSpeaker = kRotateSpeaker.button;
        rotateAmp = kRotateAmp.button;
        rotateSource = kRotateSource.button;
        
    }
    
    public void bindButtons()
    {
        if (subsystem.isPresent())
        {
            SK24Drive drive = subsystem.get();

            // Sets filters for driving axes
            kTranslationXPort.setFilter(new CubicDeadbandFilter(kDriveCoeff,
                kJoystickDeadband, DriveConstants.kMaxSpeedMetersPerSecond, true));

            kRotationYPort.setFilter(new CubicDeadbandFilter(kDriveCoeff,
                kJoystickDeadband, DriveConstants.kMaxSpeedMetersPerSecond, true));
            
            kVelocityOmegaPort.setFilter(new CubicDeadbandFilter(kRotationCoeff, kJoystickDeadband,
                Math.toRadians(ModuleConstants.kMaxModuleAngularSpeedDegreesPerSecond), true));

            slowmode.
                onTrue(new InstantCommand(() -> {setGainCommand(kSlowModePercent);}, drive))
                .onFalse(new InstantCommand(() -> {setGainCommand(1);}, drive));

            // Resets gyro angles
            resetButton.onTrue(new InstantCommand(drive::setFront));
            
            rotateSpeaker.whileTrue(
                new DriveTurnCommand(
                    () -> kTranslationXPort.getFilteredAxis(),
                    () -> kRotationYPort.getFilteredAxis(),
                    robotCentric::getAsBoolean, 180, drive));
            rotateAmp.and(robotCentric.negate()).whileTrue(
                new DriveTurnCommand(
                    () -> kTranslationXPort.getFilteredAxis(),
                    () -> kRotationYPort.getFilteredAxis(),
                    robotCentric::getAsBoolean, drive.checkIsRed() ? 270 : 90, drive));
            rotateSource.whileTrue(
                new DriveTurnCommand(
                    () -> kTranslationXPort.getFilteredAxis(),
                    () -> kRotationYPort.getFilteredAxis(),
                    robotCentric::getAsBoolean, drive.checkIsRed() ? 45 : 135, drive)); 

            // Default command for driving
            drive.setDefaultCommand(
                new DefaultSwerveCommand(
                    () -> kTranslationXPort.getFilteredAxis(),
                    () -> kRotationYPort.getFilteredAxis(),
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
        kRotationYPort.setFilter(translation);

     
        Filter rotation = new CubicDeadbandFilter(kDriveCoeff, kJoystickDeadband,
            Math.toRadians(DriveConstants.kMaxRotationDegreesPerSecond) * percent, true);
        kVelocityOmegaPort.setFilter(rotation);
  
    }
  
} 
 
