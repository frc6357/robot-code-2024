// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.mechanisms.swerve.SwerveDrivetrainConstants;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.ClosedLoopOutputType;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModuleConstants;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModuleConstants.SteerFeedbackType;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModuleConstantsFactory;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;

// import com.pathplanner.lib.PathConstraints;
// import com.pathplanner.lib.auto.PIDConstants;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
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

    public static class OperatorConstants
    {

        // Joystick Deadband
        public static final double LEFT_X_DEADBAND = 0.01;
        public static final double LEFT_Y_DEADBAND = 0.01;
        public static final double RIGHT_X_DEADBAND = 0.01;
        public static final double TURN_CONSTANT = 0.75;
    }
    
    /** Constants that define the drivetrain as a whole */
    public class DriveConstants {
        //Deadband for climb balance
        public static final double deadband = 5.0;
        // Both sets of gains need to be tuned to your individual robot.
        //List of autos we want to show up in the sendable chooser for shuffleboard
        public static List<String> autoList = new ArrayList<String>(Arrays.asList("LeftScore1"));
        // The steer motor uses any SwerveModule.SteerRequestType control request with the
        // output type specified by SwerveModuleConstants.SteerMotorClosedLoopOutput
        // Both sets of gains need to be tuned to your individual robot.
        
        // The steer motor uses any SwerveModule.SteerRequestType control request with the
        // output type specified by SwerveModuleConstants.SteerMotorClosedLoopOutput
        private static final Slot0Configs steerGains = new Slot0Configs()
        .withKP(100).withKI(0).withKD(0.2)
        .withKS(0).withKV(1.5).withKA(0);
        // When using closed-loop control, the drive motor uses the control
        // output type specified by SwerveModuleConstants.DriveMotorClosedLoopOutput
        private static final Slot0Configs driveGains = new Slot0Configs()
            .withKP(3).withKI(0).withKD(0)
            .withKS(0).withKV(0).withKA(0);                     

        // The closed-loop output type to use for the steer motors;
        // This affects the PID/FF gains for the steer motors
        private static final ClosedLoopOutputType steerClosedLoopOutput = ClosedLoopOutputType.Voltage;
        // The closed-loop output type to use for the drive motors;
        // This affects the PID/FF gains for the drive motors
        private static final ClosedLoopOutputType driveClosedLoopOutput = ClosedLoopOutputType.Voltage;

        // The stator current at which the wheels start to slip;
        // This needs to be tuned to your individual robot
        private static final double kSlipCurrentA = 300.0;

        // Theoretical free speed (m/s) at 12v applied output;
        // This needs to be tuned to your individual robot
        public static final double kSpeedAt12VoltsMps = 4.73;

        // Every 1 rotation of the azimuth results in kCoupleRatio drive motor turns;
        // This may need to be tuned to your individual robot
        private static final double kCoupleRatio = 3.5714285714285716;

        private static final double kDriveGearRatio = 6.746031746031747;
        private static final double kSteerGearRatio = 21.428571428571427;
        private static final double kWheelRadiusInches = 2;

        private static final boolean kSteerMotorReversed = true;
        private static final boolean kInvertLeftSide = false;
        private static final boolean kInvertRightSide = true;

        private static final String kCANbusName = "DriveCAN";
        private static final int kPigeonId = 25;


        // These are only used for simulation
        private static final double kSteerInertia = 0.00001;
        private static final double kDriveInertia = 0.001;
        // Simulated voltage necessary to overcome friction
        private static final double kSteerFrictionVoltage = 0.25;
        private static final double kDriveFrictionVoltage = 0.25;

        public static final SwerveDrivetrainConstants DrivetrainConstants = new SwerveDrivetrainConstants()
                .withPigeon2Id(kPigeonId)
                .withCANbusName(kCANbusName);

        private static final SwerveModuleConstantsFactory ConstantCreator = new SwerveModuleConstantsFactory()
                .withDriveMotorGearRatio(kDriveGearRatio)
                .withSteerMotorGearRatio(kSteerGearRatio)
                .withWheelRadius(kWheelRadiusInches)
                .withSlipCurrent(kSlipCurrentA)
                .withSteerMotorGains(steerGains)
                .withDriveMotorGains(driveGains)
                .withSteerMotorClosedLoopOutput(steerClosedLoopOutput)
                .withDriveMotorClosedLoopOutput(driveClosedLoopOutput)
                .withSpeedAt12VoltsMps(kSpeedAt12VoltsMps)
                .withSteerInertia(kSteerInertia)
                .withDriveInertia(kDriveInertia)
                .withSteerFrictionVoltage(kSteerFrictionVoltage)
                .withDriveFrictionVoltage(kDriveFrictionVoltage)
                .withFeedbackSource(SteerFeedbackType.RemoteCANcoder)
                .withCouplingGearRatio(kCoupleRatio)
                .withSteerMotorInverted(kSteerMotorReversed);

        // Front Left
        private static final int kFrontLeftDriveMotorId = 10;
        private static final int kFrontLeftSteerMotorId = 20;
        private static final int kFrontLeftEncoderId = 30;
        private static final double kFrontLeftEncoderOffset = -0.092041015625;

        private static final double kFrontLeftXPosInches = 11.25;
        private static final double kFrontLeftYPosInches = 11.25;

        // Front Right
        private static final int kFrontRightDriveMotorId = 12;
        private static final int kFrontRightSteerMotorId = 22;
        private static final int kFrontRightEncoderId = 32;
        private static final double kFrontRightEncoderOffset = -0.29150390625;

        private static final double kFrontRightXPosInches = 11.25;
        private static final double kFrontRightYPosInches = -11.25;

        // Back Left
        private static final int kBackLeftDriveMotorId = 11;
        private static final int kBackLeftSteerMotorId = 21;
        private static final int kBackLeftEncoderId = 31;
        private static final double kBackLeftEncoderOffset = 0.141845703125;

        private static final double kBackLeftXPosInches = -11.25;
        private static final double kBackLeftYPosInches = 11.25;

        // Back Right
        private static final int kBackRightDriveMotorId = 13;
        private static final int kBackRightSteerMotorId = 23;
        private static final int kBackRightEncoderId = 33;
        private static final double kBackRightEncoderOffset = 0.205322265625;

        private static final double kBackRightXPosInches = -11.25;
        private static final double kBackRightYPosInches = -11.25;

        public static final SwerveModuleConstants FrontLeft = ConstantCreator.createModuleConstants(
                kFrontLeftSteerMotorId, kFrontLeftDriveMotorId, kFrontLeftEncoderId, kFrontLeftEncoderOffset, Units.inchesToMeters(kFrontLeftXPosInches), Units.inchesToMeters(kFrontLeftYPosInches), kInvertLeftSide);
        public static final SwerveModuleConstants FrontRight = ConstantCreator.createModuleConstants(
                kFrontRightSteerMotorId, kFrontRightDriveMotorId, kFrontRightEncoderId, kFrontRightEncoderOffset, Units.inchesToMeters(kFrontRightXPosInches), Units.inchesToMeters(kFrontRightYPosInches), kInvertRightSide);
        public static final SwerveModuleConstants BackLeft = ConstantCreator.createModuleConstants(
                kBackLeftSteerMotorId, kBackLeftDriveMotorId, kBackLeftEncoderId, kBackLeftEncoderOffset, Units.inchesToMeters(kBackLeftXPosInches), Units.inchesToMeters(kBackLeftYPosInches), kInvertLeftSide);
        public static final SwerveModuleConstants BackRight = ConstantCreator.createModuleConstants(
                kBackRightSteerMotorId, kBackRightDriveMotorId, kBackRightEncoderId, kBackRightEncoderOffset, Units.inchesToMeters(kBackRightXPosInches), Units.inchesToMeters(kBackRightYPosInches), kInvertRightSide);


        public static SwerveDriveKinematics swerveKinematics = new SwerveDriveKinematics(
            new Translation2d(FrontLeft.LocationX, FrontLeft.LocationY),
            new Translation2d(FrontRight.LocationX, FrontRight.LocationY), 
            new Translation2d(BackLeft.LocationX, BackLeft.LocationY), 
            new Translation2d(BackRight.LocationX, BackRight.LocationY));


    

        
        /** Distance between centers of right and left wheels on robot */
        public static final double kTrackWidth = 0.5588;
        /** Distance between front and back wheels on robot */
        public static final double kWheelBase  = 0.5588;
        
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
            Math.hypot(DriveConstants.kWheelBase / 2, DriveConstants.kTrackWidth / 2), 
            new ReplanningConfig()
        );

    }

    /** The file that is used for system instantiation at runtime */
    public static final String SUBSYSTEMFILE = "Subsystems.json";
}
