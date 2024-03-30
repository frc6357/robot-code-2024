package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import frc.robot.utils.SKTrigger;
import frc.robot.utils.filters.FilteredXboxController;
import static edu.wpi.first.wpilibj.XboxController.Button.*;
import static edu.wpi.first.wpilibj.XboxController.Axis.*;
import static frc.robot.utils.SKTrigger.INPUT_TYPE.AXIS;
import static frc.robot.utils.SKTrigger.INPUT_TYPE.BUTTON;
import static frc.robot.utils.SKTrigger.INPUT_TYPE.POV;

import frc.robot.utils.CANPort;
import frc.robot.utils.filters.FilteredAxis;

public class Ports
{
    public static class DriverPorts
    {
        // Driver Controller set to Xbox Controller
        public static final GenericHID kDriver = new FilteredXboxController(0).getHID();
        
        // Filtered axis (translation & rotation)
        public static final FilteredAxis kTranslationXPort     = new FilteredAxis(() -> kDriver.getRawAxis(kLeftY.value));
        public static final FilteredAxis kTranslationYPort     = new FilteredAxis(() -> kDriver.getRawAxis(kLeftX.value));
        public static final FilteredAxis kVelocityOmegaPort = new FilteredAxis(() -> kDriver.getRawAxis(kRightX.value)); 
        
        // Switch modes
        public static final SKTrigger kRobotCentricMode = new SKTrigger(kDriver, kRightBumper.value, BUTTON);
        public static final SKTrigger kSlowMode = new SKTrigger(kDriver, kLeftBumper.value, BUTTON);

        // Rotate to specified position
        public static final SKTrigger kRotateSpeaker = new SKTrigger(kDriver, kA.value, BUTTON);
        public static final SKTrigger kRotateLeft = new SKTrigger(kDriver, kX.value, BUTTON);
        public static final SKTrigger kRotateRight = new SKTrigger(kDriver, kB.value, BUTTON);
        public static final SKTrigger kRotateSource = new SKTrigger(kDriver, kY.value, BUTTON);

        // Reset gyro
        public static final SKTrigger kResetGyroPos = new SKTrigger(kDriver, kLeftStick.value, BUTTON);

        // Intake and Eject
        public static final SKTrigger kIntake = new SKTrigger(kDriver, kRightTrigger.value, AXIS);
        public static final SKTrigger kEject  = new SKTrigger(kDriver, kLeftTrigger.value, AXIS); 

        // Climb
        public static final SKTrigger kClimbUp = new SKTrigger(kDriver, 0, POV);
        public static final SKTrigger kClimbDown = new SKTrigger(kDriver, 180, POV);
        public static final SKTrigger kStop = new SKTrigger(kDriver, 270, POV);
        public static final SKTrigger kClimbOverride = new SKTrigger(kDriver, kRightStick.value, BUTTON);

        // Launcher Zero Position
        public static final SKTrigger kAngleFloor = new SKTrigger(kDriver, kStart.value, BUTTON); 
        
        // Party mode
        public static final SKTrigger kPartyMode = new SKTrigger(kDriver, kBack.value, BUTTON);
        public static final SKTrigger kLightsOff = new SKTrigger(kDriver, 90, POV);
    }
    /**
     * Defines the button, controller, and axis IDs needed to get input from an external
     * controller
     */
    
    public static class OperatorPorts
    {
        // Operator Controller set to Xbox Controller
        public static final GenericHID kOperator = new FilteredXboxController(1).getHID();

        // Intake and Eject
        public static final SKTrigger kIntake = new SKTrigger(kOperator, kRightTrigger.value, AXIS);
        public static final SKTrigger kEject = new SKTrigger(kOperator, kLeftTrigger.value, AXIS);

        // Transfer
        public static final SKTrigger kTransfer  = new SKTrigger(kOperator, kLeftBumper.value, BUTTON);

        // Launch at Target
        public static final SKTrigger kLaunchSpeaker = new SKTrigger(kOperator, kRightBumper.value, BUTTON);
        public static final SKTrigger kLaunchAmp = new SKTrigger(kOperator, kA.value, BUTTON);

        // Launcher Angle 
        public static final SKTrigger kAngleSpeaker = new SKTrigger(kOperator, 0, POV);
        public static final SKTrigger kAngleAmp = new SKTrigger(kOperator, 270, POV);
        public static final SKTrigger kAngleFloor = new SKTrigger(kOperator, 180, POV);
        public static final SKTrigger kAngleWing = new SKTrigger(kOperator, 90, POV);
        public static final FilteredAxis kLauncherAxis = new FilteredAxis(() -> kOperator.getRawAxis(kLeftY.value));
        public static final SKTrigger kVisionAngle = new SKTrigger(kOperator, kB.value, BUTTON);

        // Reset launcher encoder
        public static final SKTrigger kResetLauncherEncoder = new SKTrigger(kOperator, kStart.value, BUTTON);
        public static final SKTrigger kLauncherOverride = new SKTrigger(kOperator, kLeftStick.value, BUTTON);

        // Churro 
        //public static final FilteredAxis kChurroAxis = new FilteredAxis(() -> kOperator.getRawAxis(kRightY.value));

        // Climb
        public static final SKTrigger kClimbUp = new SKTrigger(kOperator, kY.value, BUTTON);
        public static final SKTrigger kClimbDown = new SKTrigger(kOperator, kX.value, BUTTON);
        public static final FilteredAxis kClimbAxis = new FilteredAxis(() -> kOperator.getRawAxis(kRightY.value));

        // Party mode
        public static final SKTrigger kPartyMode = new SKTrigger(kOperator, kBack.value, BUTTON);
    }
    

    public static class launcherPorts
    {
        private static final String busName = "";
        public static final CANPort kLeftLauncherMotor = new CANPort(40, busName);
        public static final CANPort kRightLauncherMotor = new CANPort(41, busName);
        public static final CANPort kTransferMotor = new CANPort(42, busName);
        public static final CANPort kLauncherAngleMotor = new CANPort(43, busName);
        public static final CANPort kLauncherAngleFollowerMotor = new CANPort(44, busName);
        public static final CANPort kLaserCanLauncherLower = new CANPort(46, busName);
        public static final CANPort kLaserCanLauncherHigher = new CANPort(47, busName);
    }

    //Assign CAN ports to climb motors
    public static class climbPorts
    {
        private static final String busName = "";
        public static final CANPort kRightClimbMotor = new CANPort(60, busName);
        public static final CANPort kLeftClimbMotor = new CANPort(61, busName);
    }

    /**
     * Defines all the ports needed to create sensors and actuators for the drivetrain.
     */

    public static class DrivePorts
    {
        private static final String busName = "DriveCAN";

        // CAN IDs for the drive motors on the swerve module
        public static final CANPort kFrontLeftDriveMotorPort  = new CANPort(13, busName);
        public static final CANPort kRearLeftDriveMotorPort   = new CANPort(12, busName);
        public static final CANPort kFrontRightDriveMotorPort = new CANPort(11, busName);
        public static final CANPort kRearRightDriveMotorPort  = new CANPort(10, busName);

        // CAN IDs for the turning motors on the swerve module
        public static final CANPort kFrontLeftTurningMotorPort  = new CANPort(23, busName);
        public static final CANPort kRearLeftTurningMotorPort   = new CANPort(22, busName);
        public static final CANPort kFrontRightTurningMotorPort = new CANPort(21, busName);
        public static final CANPort kRearRightTurningMotorPort  = new CANPort(20, busName);

        // CAN IDs for the CANCoders
        public static final CANPort kFrontLeftTurningEncoderPort  = new CANPort(33, busName);
        public static final CANPort kRearLeftTurningEncoderPort   = new CANPort(32, busName);
        public static final CANPort kFrontRightTurningEncoderPort = new CANPort(31, busName);
        public static final CANPort kRearRightTurningEncoderPort  = new CANPort(30, busName);
        
        // CAN ID for IMU
        public static final CANPort kPigeonPort = new CANPort(25, busName);
    }

    public static class intakePorts 
    {
        private static final String busName = "";
        public static final CANPort kBottomIntakeMotor = new CANPort(50, busName);
        public static final CANPort kTopIntakeMotor = new CANPort(51, busName);
        public static final CANPort kCandle = new CANPort(48, busName);

        
    }

    public static class churroPorts
    {
        private static final String busName = "";

        public static final CANPort kChurroMotor = new CANPort(45, busName); 
    }
}
