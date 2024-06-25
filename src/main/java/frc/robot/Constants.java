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
import frc.robot.preferences.Pref;
import frc.robot.preferences.SKPreferences;

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
        public static List<String> autoList = new ArrayList<String>(Arrays.asList("P4_Taxi"));
        // The steer motor uses any SwerveModule.SteerRequestType control request with the
        // output type specified by SwerveModuleConstants.SteerMotorClosedLoopOutput
        // Both sets of gains need to be tuned to your individual robot.
        
        // The steer motor uses any SwerveModule.SteerRequestType control request with the
        // output type specified by SwerveModuleConstants.SteerMotorClosedLoopOutput
        private static final Slot0Configs steerGains = new Slot0Configs() //TODO - tune steering gains drive
        .withKP(100).withKI(0).withKD(0.3)
        .withKS(0).withKV(1.5).withKA(0);
        // When using closed-loop control, the drive motor uses the control
        // output type specified by SwerveModuleConstants.DriveMotorClosedLoopOutput
        private static final Slot0Configs driveGains = new Slot0Configs() //TODO - tune driving gains drive
            .withKP(2.3).withKI(0).withKD(0)
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
            private static final double kWheelRadiusInches = 2.0 - 0.1;
            
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
    private static final int kFrontLeftDriveMotorId = 13;
    private static final int kFrontLeftSteerMotorId = 23;
    private static final int kFrontLeftEncoderId = 33;
    private static final double kFrontLeftEncoderOffset = 0.19482421875; //0.219970703125;

    private static final double kFrontLeftXPosInches = 10.875;
    private static final double kFrontLeftYPosInches = 10.875;

    // Front Right
    private static final int kFrontRightDriveMotorId = 11;
    private static final int kFrontRightSteerMotorId = 21;
    private static final int kFrontRightEncoderId = 31;
    private static final double kFrontRightEncoderOffset = 0.141845703125; //0.138916015625;

    private static final double kFrontRightXPosInches = 10.875;
    private static final double kFrontRightYPosInches = -10.875;

    // Back Left
    private static final int kBackLeftDriveMotorId = 12;
    private static final int kBackLeftSteerMotorId = 22;
    private static final int kBackLeftEncoderId = 32;
    private static final double kBackLeftEncoderOffset = -0.277099609375; //-0.28515625;

    private static final double kBackLeftXPosInches = -10.875;
    private static final double kBackLeftYPosInches = 10.875;

    // Back Right
    private static final int kBackRightDriveMotorId = 10;
    private static final int kBackRightSteerMotorId = 20;
    private static final int kBackRightEncoderId = 30;
    private static final double kBackRightEncoderOffset = -0.087646484375; //-0.0859375;

    private static final double kBackRightXPosInches = -10.875;
    private static final double kBackRightYPosInches = -10.875;
        
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

    

        public static final double kMaxModuleAngularSpeedDegreesPerSecond = 360;
        
        /** Distance between centers of right and left wheels on robot */
        public static final double kTrackWidth = 0.5842;
        /** Distance between front and back wheels on robot */
        public static final double kWheelBase  = 0.5842;
        
        /** The max speed the drive wheels should be allowed to go */
        public static final double kMaxSpeedMetersPerSecond = 4.5;
        public static final double kMaxRotationDegreesPerSecond = 360.0;
        public static final double kStartAutoLength = 4;

        public static final double kAmpRedFacing = 270.0;
        public static final double kAmpBlueFacing = 90.0;

        public static final double kDriveAngleTolerance = 1.5;

        public static final double kFieldWidth = 16.58; //Width of the field longwise in meters for use in pointing drive towards speaker
        public static final double kSpeakerLocation = 5.55; //Location of the center speaker in meters from the amp side
        public static final double kSpeakerHeight = 1.6 - 0.0; //Height of the center of the speaker opening in meters minus height of launcher pivot point

        public static final String kDriveRotationPKey = "kDriveRotationPKey";
        public static final String kDriveRotationIKey = "kDriveRotationIKey";
        public static final String kDriveRotationDKey = "kDriveRotationDKey";

        public static final double kDriveRotationP = 0.04;
        public static final double kDriveRotationI = 0.005;
        public static final double kDriveRotationD = 0.0;
    }
    public static final class VisionConstants
    {
        public static final double limelightStartingAngle = 19.0;
    }
    public static final class ClimbConstants
    {
        public static final PIDConstants rightClimb = new PIDConstants(0.01, 0.0, 0.0);
        public static final PIDConstants leftClimb = new PIDConstants(0.01, 0.0, 0.0);
        public static final PIDConstants balancePID = new PIDConstants(0.0, 0.0, 0.0);

        public static final double kClimbBalanceTolerance = 5.0;
        public static final double spoolDiameter = 0.75; //Inches
        public static final double gearRatio = 1.0; //Shaft rotations / 1 motor rotation
        public static final double climbHeight = 11.0; //Inches

        public static final double climbConversion = 1.0 / 87.0; //inches moved per motor rotation
        public static final double kPositionTolerance = 2.0;
        public static final double kClimbMotorMinOutput = -1.0;
        public static final double kClimbMotorMaxOutput = 1.0;

        public static final double kClimbUpSpeed = 1.0;
        public static final double kClimbDownSpeed = -1.0;
        
        public static final double kMinAngle = 0.0;
        public static final double kMaxAngle = 1.0;
    
        public static final double kJoystickChange   = 0.1; // Manual setpoint value for units from 0.0 - 1.0 moved per second - TODO - find good value degrees per second angle launcher
        public static final double kJoystickDeadband = 0.3;  // Manual arm movement axis deadband
    
        public static final boolean kJoystickReversed = true;  // Determines if the joystick movement is reversed
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
        public static final PIDConstants kRotationPIDConstants    = new PIDConstants(6, 0.4, 0);
        public static final HolonomicPathFollowerConfig kAutoPathConfig = new HolonomicPathFollowerConfig(
            kTranslationPIDConstants,
            kRotationPIDConstants,
            kMaxSpeedMetersPerSecond,
            Math.hypot(DriveConstants.kWheelBase / 2, DriveConstants.kTrackWidth / 2), 
            new ReplanningConfig()
        );

    }
    public static final class LauncherAngleConstants
    {
        public static final String kLauncherAngleWingKey = "kLauncherAngleWingKey";
        public static final double kWingAngle = 23.0;

        public static final String kLauncherAnglePKey = "kLauncherAnglePKey";
        public static final String kLauncherAngleIKey = "kLauncherAngleIKey";
        public static final String kLauncherAngleDKey = "kLauncherAngleDKey";
        public static final String kLauncherAngleFFKey = "kLauncherAngleFFKey";
        public static final double kLauncherAngleP = 0.0075; //0.0065
        public static final double kLauncherAngleI = 0.0003; //0.00015
        public static final double kLauncherAngleD = 0.00003; //0.0003
        public static final double kLauncherAngleFF = 0.0155;

        public static final double kConversionFactor = (1.0 / 48.0) * 360.0;
        public static final double kAngleTolerance =  3.0;
        public static final double kArmMotorMinOutput =  -0.2; //TODO - Find motor minimum output
        public static final double kArmMotorMaxOutput =  0.2; //TODO - Find motor maximum output
    
        public static final double kMinAngle = -15.0; //TODO - change to zero
        public static final double kZeroAngle = 0.0; //TODO - change to zero
        public static final double kMaxAngle = 55.0;//TODO - find maximum launcher angle
        public static final double kAngleOffset = 11.14; //Offset of launcher angle in degrees
    
        public static final double kJoystickChange   = 10.0; // Manual setpoint value for degrees moved per second 
        public static final double kJoystickDeadband = 0.3;  // Manual arm movement axis deadband
    
        public static final boolean kJoystickReversed = true;  // Determines if the joystick movement is reversed

        public static final String kAmpAngleKey = "kAmpAngleKey";
        public static final double kAmpAngle = 44.0;

        public static final Pref<Double> kAmpAnglePref = SKPreferences.attach(kAmpAngleKey, kAmpAngle);
        public static final double kFloorAngle = -13.4; 
        public static final double kSpeakerAngle = 42.0; 

        public static final double kPos1Angle = 42.0; //TODO - find launcher angle for position 1
        public static final double kPos2Angle = 42.0; //TODO - find launcher angle for position 2
        public static final double kPos3Angle = 42.0; //TODO - find launcher angle for position 3
        public static final double kAngleFloor = 20.0;

        public static final double GP1Angle = 42.0; //TODO - find launcher angle for gp1
        public static final double GP2Angle = 42.0; //TODO - find launcher angle for gp2
        public static final double GP3Angle = 42.0; //TODO - find launcher angle for gp3
        public static final double GP456Angle = 42.0; //TODO - find launcher angle for gp456
        public static final double GP78Angle = 42.0; //TODO - find launcher angle for gp78
    }

    public static final class LightConstants
    {
        public static final int numLedOnBot = 240;
        public static final double kLightsOffBrightness = 0.0;
        public static final double kLightsOnBrightness = 0.5;
    }
    
    public static final class IntakeConstants
    {
        public static final double kIntakeSpeed = 0.3; 
        public static final double kSlowIntakeSpeed = 0.03;
        public static final double kIntakeAngle = 40.0; 
        public static final double kIntakeLauncherLeftSpeed = -0.1;
        public static final double kIntakeLauncherRightSpeed = -0.1;

        public static final double kTransferSpeed = 0.5; 
        public static final double kIntakeTransferSpeed = 0.15;
        public static final double kSlowTransferSpeed = 0.05;
        public static final double noteMeasurement = 150;                         
    }
    
    public static final class ChurroConstants
    {
        public static final String kChurroPKey = "kChurroPKey";
        public static final String kChurroIKey = "kChurroIKey";
        public static final String kChurroDKey = "kChurroDKey";

        public static final double kChurroP = 0.003;
        public static final double kChurroI = 0.0;
        public static final double kChurroD = 0.0;
        
        public static final PIDConstants kChurroPID = new PIDConstants(0.001, 0.0, 0.0); //TODO - Tune launcher angle PID
        public static final double kAngleTolerance = 10.0; 
        public static final double kChurroLowerPosition = 0.0; 
        public static final double kChurroRaisePosition = 225.0; 
    	public static final double kChurroSpeed = 0.15;
        public static final double kChurroConversion= (1.0 / 49.0) * 360.0;
    }
    
    public static final class LauncherConstants
    {
        
        
        public static final String kSpeakerDefaultLeftSpeedKey = "kSpeakerDefaultLeftSpeedKey";
        public static final String kSpeakerDefaultRightSpeedKey = "kSpeakerDefaultRightSpeedKey";
        public static final double kSpeakerDefaultLeftSpeed = 0.4;
        public static final double kSpeakerDefaultRightSpeed = 0.5;
        public static final Pref<Double> speakerSpeedLeft = SKPreferences.attach(kSpeakerDefaultLeftSpeedKey, kSpeakerDefaultLeftSpeed);
        public static final Pref<Double> speakerSpeedRight = SKPreferences.attach(kSpeakerDefaultRightSpeedKey, kSpeakerDefaultRightSpeed);
        
        public static final String kSpeakerRestingLeftSpeedKey = "kSpeakerRestingLeftSpeedKey";
        public static final String kSpeakerRestingRightSpeedKey = "kSpeakerRestingRightSpeedKey";
        public static final double kSpeakerRestingLeftSpeed = 0.2;
        public static final double kSpeakerRestingRightSpeed = 0.2;
        public static final Pref<Double> speakerRestingSpeedLeft = SKPreferences.attach(kSpeakerRestingLeftSpeedKey, kSpeakerRestingLeftSpeed);
        public static final Pref<Double> speakerRestingSpeedRight = SKPreferences.attach(kSpeakerRestingRightSpeedKey, kSpeakerRestingRightSpeed);
        
        public static final String kAmpDefaultLeftSpeedKey = "kAmpDefaultLeftSpeedKey";
        public static final String kAmpDefaultRightSpeedKey = "kAmpDefaultRightSpeedKey";
        public static final double kAmpDefaultLeftSpeed = 0.22;
        public static final double kAmpDefaultRightSpeed = 0.22;
        public static final Pref<Double> ampSpeedLeft = SKPreferences.attach(kAmpDefaultLeftSpeedKey, kAmpDefaultLeftSpeed);
        public static final Pref<Double> ampSpeedRight = SKPreferences.attach(kAmpDefaultRightSpeedKey, kAmpDefaultRightSpeed);
        
        //TODO - determine how to get launcher speeds for any position on field - Either fixed fast speed or dynamic speeds
        public static final double kLauncherLeftSpeed = 0.4; 
        public static final double kLauncherRightSpeed = 0.5; 
        
        public static final double kSpeedTolerance = 0.03;

        public static final String kSpeakerRampSpeedKey = "kSpeakerRampSpeedKey";
        public static final double kSpeakerRampSpeed = 2.0;
        public static final Pref<Double> speakerRampSpeed = SKPreferences.attach(kSpeakerRampSpeedKey, kSpeakerRampSpeed);

        public static final String kRestingRampSpeedKey = "kRestingRampSpeedKey";
        public static final double kRestingRampSpeed = 1.0;
        public static final Pref<Double> restingRampSpeed = SKPreferences.attach(kRestingRampSpeedKey, kRestingRampSpeed);

        public static final double kAmpRampSpeed = 1.0;
        public static final double kQuickRampSpeed = 0.4;
        public static final double kRampDownSpeed = 12.0;
    }
    
    /** The file that is used for system instantiation at runtime */
    public static final String SUBSYSTEMFILE = "Subsystems.json";
    public static Object LauncherAngleConstants;
}

