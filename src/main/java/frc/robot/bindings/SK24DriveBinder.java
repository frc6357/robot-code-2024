package frc.robot.bindings;

import static frc.robot.Ports.OperatorPorts.kResetGyroDSS;
import static frc.robot.Ports.OperatorPorts.kResetGyroGrid;
import static frc.robot.Ports.OperatorPorts.kResetGyroLeft;
import static frc.robot.Ports.OperatorPorts.kResetGyroRight;
import static frc.robot.Ports.OperatorPorts.kRobotCentricMode;
import static frc.robot.Ports.OperatorPorts.kRotateDSS;
import static frc.robot.Ports.OperatorPorts.kRotateGrid;
import static frc.robot.Ports.OperatorPorts.kRotateLeft;
import static frc.robot.Ports.OperatorPorts.kRotateRight;
import static frc.robot.Ports.OperatorPorts.kSlowMode;
import static frc.robot.Ports.OperatorPorts.kVelocityOmegaPort;
import static frc.robot.Ports.OperatorPorts.kVelocityXPort;
import static frc.robot.Ports.OperatorPorts.kVelocityYPort;

import java.util.Optional;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.ModuleConstants;
import static frc.robot.Constants.OIConstants.*;
import frc.robot.commands.DefaultSwerveCommand;
import frc.robot.commands.DriveTurnCommand;
import frc.robot.subsystems.SK24Drive;
//import frc.robot.commands.OnTheFlyCommand;
import frc.robot.utils.filters.CubicDeadbandFilter;
import frc.robot.utils.filters.Filter;

public class SK24DriveBinder implements CommandBinder
{
    Optional<SK24Drive>  subsystem;
    

    // Driver Buttons
    private final Trigger robotCentric;
    private final Trigger rotateDSS;
    private final Trigger rotateGrid;
    private final Trigger rotateLeft;
    private final Trigger rotateRight;

    // Gyro Reset Buttons
    private final Trigger resetGyroDSS;
    private final Trigger resetGyroGrid;
    private final Trigger resetGyroLeft;
    private final Trigger resetGyroRight;
    private final Trigger slowmode;

    /**
     * The class that is used to bind all the commands for the drive subsystem
     * 
     * @param subsystem
     *            The required drive subsystem for the commands
     */
    public SK24DriveBinder(Optional<SK24Drive> subsystem)
    {
        this.subsystem  = subsystem;
      

        resetGyroDSS    = kResetGyroDSS.button;
        resetGyroGrid   = kResetGyroGrid.button;
        resetGyroLeft   = kResetGyroLeft.button;
        resetGyroRight  = kResetGyroRight.button;

        robotCentric    = kRobotCentricMode.button;

        rotateDSS       = kRotateDSS.button;
        rotateGrid      = kRotateGrid.button;
        rotateLeft      = kRotateLeft.button;
        rotateRight     = kRotateRight.button;

        slowmode = kSlowMode.button;
    }

    public void bindButtons()
    {
        if (subsystem.isPresent())
        {
            SK24Drive drive = subsystem.get();

            // Sets filters for driving axes
            kVelocityXPort.setFilter(new CubicDeadbandFilter(kDriveCoeff,
                kJoystickDeadband, DriveConstants.kMaxSpeedMetersPerSecond, true));

            kVelocityYPort.setFilter(new CubicDeadbandFilter(kDriveCoeff,
                kJoystickDeadband, DriveConstants.kMaxSpeedMetersPerSecond, true));

            kVelocityOmegaPort.setFilter(new CubicDeadbandFilter(kRotationCoeff, kJoystickDeadband,
                Math.toRadians(ModuleConstants.kMaxModuleAngularSpeedDegreesPerSecond), true));

            slowmode.
                onTrue(new InstantCommand(() -> {setGainCommand(kSlowModePercent);}, drive))
                .onFalse(new InstantCommand(() -> {setGainCommand(1);}, drive));

            // Resets gyro angles
            resetGyroDSS.onTrue(new InstantCommand(() -> {drive.seedFieldRelative();}));
            resetGyroGrid.onTrue(new InstantCommand(() -> {drive.seedFieldRelative(new Pose2d(drive.getPose().getTranslation(), new Rotation2d(Math.toRadians(90))));}));
            resetGyroLeft.onTrue(new InstantCommand(() -> {drive.seedFieldRelative(new Pose2d(drive.getPose().getTranslation(), new Rotation2d(Math.toRadians(180))));}));
            resetGyroRight.onTrue(new InstantCommand(() -> {drive.seedFieldRelative(new Pose2d(drive.getPose().getTranslation(), new Rotation2d(Math.toRadians(270))));}));

            rotateDSS.whileTrue(
                new DriveTurnCommand(
                    () -> kVelocityXPort.getFilteredAxis(),
                    () -> kVelocityYPort.getFilteredAxis(),
                    robotCentric::getAsBoolean, 0, drive));
            rotateGrid.and(robotCentric.negate()).whileTrue(
                new DriveTurnCommand(
                    () -> kVelocityXPort.getFilteredAxis(),
                    () -> kVelocityYPort.getFilteredAxis(),
                    robotCentric::getAsBoolean, 180, drive));
            rotateLeft.whileTrue(
                new DriveTurnCommand(
                    () -> kVelocityXPort.getFilteredAxis(),
                    () -> kVelocityYPort.getFilteredAxis(),
                    robotCentric::getAsBoolean, 90, drive));
            rotateRight.whileTrue(
                new DriveTurnCommand(
                    () -> kVelocityXPort.getFilteredAxis(),
                    () -> kVelocityYPort.getFilteredAxis(),
                    robotCentric::getAsBoolean, 270, drive));

            // Default command for driving
            drive.setDefaultCommand(
                new DefaultSwerveCommand(
                    () -> kVelocityXPort.getFilteredAxis(),
                    () -> kVelocityYPort.getFilteredAxis(),
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
        kVelocityXPort.setFilter(translation);
        kVelocityYPort.setFilter(translation);

        Filter rotation = new CubicDeadbandFilter(kDriveCoeff, kJoystickDeadband,
            Math.toRadians(DriveConstants.kMaxRotationDegreesPerSecond) * percent, true);
        kVelocityOmegaPort.setFilter(rotation);
    }
}
