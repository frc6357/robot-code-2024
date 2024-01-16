// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;

// import com.pathplanner.lib.PathConstraints;
// import com.pathplanner.lib.auto.PIDConstants;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical
 * or boolean constants. This class should not be used for any other purpose. All
 * constants should be declared globally (i.e. public static). Do not put anything
 * functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes) wherever
 * the constants are needed, to reduce verbosity.
 */
public final class Constants
{
    

    /** Constants that define the drivetrain as a whole */
    public static final class DriveConstants
    {
        // Whether or not the turning motor needs to be reversed. Positive input should
        // cause CCW+ angle change
        public static final boolean kFrontLeftTurnMotorReversed  = true;
        public static final boolean kRearLeftTurnMotorReversed   = true;
        public static final boolean kFrontRightTurnMotorReversed = true;
        public static final boolean kRearRightTurnMotorReversed  = true;

        // Whether or not the drive motors need to be reversed. Positive input should
        // cause the robot to go into the positive x or y direction as outlined by the
        // FIELD coordinate system
        public static final boolean kFrontLeftDriveMotorReversed    = false;
        public static final boolean kRearLeftDriveEncoderReversed   = false;
        public static final boolean kFrontRightDriveEncoderReversed = true;
        public static final boolean kRearRightDriveMotorReversed    = true;

        // Offset for the CANCoders in Degrees
        public static final double kFrontLeftAngleOffset  = -326.162;
        public static final double kRearLeftAngleOffset   = -247.412;
        public static final double kFrontRightAngleOffset = -1.934;
        public static final double kRearRightAngleOffset  = -108.896;

        /** Distance between centers of right and left wheels on robot */
        public static final double kTrackWidth = 0.5588;
        /** Distance between front and back wheels on robot */
        public static final double kWheelBase  = 0.5588;

        public static final double kLengthFromGrid = 1.9;
        /** Contains position of the swerve modules relative to the robot center */
        public static final SwerveDriveKinematics kDriveKinematics =
                new SwerveDriveKinematics(new Translation2d(kWheelBase / 2, kTrackWidth / 2),
                    new Translation2d(kWheelBase / 2, -kTrackWidth / 2),
                    new Translation2d(-kWheelBase / 2, kTrackWidth / 2),
                    new Translation2d(-kWheelBase / 2, -kTrackWidth / 2));

        /** Whether or not the gyro needs to be reversed to be CCW+ */
        public static final boolean kGyroReversed = false;

        /** The default state of the swerve drive to allow for maximum defense */
        public static final SwerveModuleState[] kDefenseState =
                {new SwerveModuleState(0, Rotation2d.fromDegrees(315)),
                    new SwerveModuleState(0, Rotation2d.fromDegrees(45)),
                    new SwerveModuleState(0, Rotation2d.fromDegrees(45)),
                    new SwerveModuleState(0, Rotation2d.fromDegrees(315))};

        /** The max speed the drive wheels should be allowed to go */
        public static final double kMaxSpeedMetersPerSecond = 5;
        public static final double kMaxRotationDegreesPerSecond = 360.0;
        public static final double kStartAutoLength = 4;
    }

    /** Constants that define each swerve module as an individual */
    public static final class ModuleConstants
    {
        // Turning Constraints
        public static final double kMaxModuleAngularSpeedDegreesPerSecond               = 360;
        public static final double kMaxModuleAngularAccelerationDegreesPerSecondSquared = 360;

        // PID Constants
        public static final double kPModuleTurningController = 0.25;
        public static final double kPModuleDriveController   = 0.01;
        public static final double kFModuleDriveController   = 0.0465;
        public static final double kPIDAngleDeadband         = 0.01;

        // Module Characteristics
        public static final int    kEncoderCPR                   = 2048;
        public static final int    kDegreesPerRevolution         = 360;
        public static final double kWheelDiameterMeters          = Units.inchesToMeters(4.0);
        public static final double kDriveGearRatio               = 6.75;
        public static final double kTurnGearRatio                = 150.0 / 7.0;
        public static final double kDriveEncoderDistancePerRotation =
                (kWheelDiameterMeters * Math.PI) / (kDriveGearRatio * (double) kEncoderCPR); //Meters per rotation
        public static final double kTurnEncoderRotationsToMechanism  =
                kDegreesPerRevolution / (kTurnGearRatio * (double) kEncoderCPR);

    }

    /** Constants that are used when defining filters for controllers */
    public static final class OIConstants
    {
        // Controller constraints
        public static final double kDriveCoeff       = 0.95;
        public static final double kRotationCoeff    = 0.95;
        public static final double kJoystickDeadband = 0.15;
        public static final double kSlowModePercent  = 0.2;

        public static final double kAccelLimit = 2;
    }

    /** Defines constraints and information for autonomous development */
    public static final class AutoConstants
    {
       
        // Autonomous translation constraints
        public static final double          kMaxSpeedMetersPerSecond               = 3;
        public static final double          kMaxAccelerationMetersPerSecondSquared = 2;
        // public static final PathConstraints kPathConstraints                       =
        //         new PathConstraints(kMaxSpeedMetersPerSecond,
        //             kMaxAccelerationMetersPerSecondSquared);

        // public static final PathConstraints kFastConstraints =
        //     new PathConstraints(4.5, 3.5);

        // PID Constants
        public static final PIDConstants kTranslationPIDConstants = new PIDConstants(6, 0, 0);
        public static final PIDConstants kRotationPIDConstants    = new PIDConstants(1.75, 0, 0);
        public static final HolonomicPathFollowerConfig kAutoPathConfig = new HolonomicPathFollowerConfig(
            kTranslationPIDConstants,
            kRotationPIDConstants,
            kMaxSpeedMetersPerSecond,
            Math.hypot(DriveConstants.kWheelBase / 2, DriveConstants.kTrackWidth / 2), //TODO: Create drive base radius
            new ReplanningConfig()
        );
    }

    /** The file that is used for system instantiation at runtime */
    public static final String SUBSYSTEMFILE = "Subsystems.json";
}
