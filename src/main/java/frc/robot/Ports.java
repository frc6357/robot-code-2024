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
        public static final GenericHID kDriver = new FilteredXboxController(0).getHID();
        
        // Filtered axis (translation & rotation)
        public static final FilteredAxis kTranslationXPort     = new FilteredAxis(() -> kDriver.getRawAxis(kLeftY.value));
        public static final FilteredAxis kRotationYPort     = new FilteredAxis(() -> kDriver.getRawAxis(kLeftX.value));
        public static final FilteredAxis kVelocityOmegaPort = new FilteredAxis(() -> kDriver.getRawAxis(kRightX.value)); 
        
        // Switch modes
        public static final SKTrigger kRobotCentricMode = new SKTrigger(kDriver, kRightBumper.value, BUTTON);
        public static final SKTrigger kSlowMode = new SKTrigger(kDriver, kLeftBumper.value, BUTTON);

        // Climb/Gyro
        public static final SKTrigger kClimb = new SKTrigger(kDriver, kY.value, BUTTON);

        // Rotate to specified position
        public static final SKTrigger kRotateSpeaker = new SKTrigger(kDriver, kA.value, BUTTON);
        public static final SKTrigger kRotateAmp = new SKTrigger(kDriver, kX.value, BUTTON);
        public static final SKTrigger kRotateSource = new SKTrigger(kDriver, kB.value, BUTTON);

        // Zero position
        public static final SKTrigger kZeroPos = new SKTrigger(kDriver, kStart.value, BUTTON); 

        // Party mode
        public static final SKTrigger kPartyMode = new SKTrigger(kDriver, kBack.value, BUTTON);

        // Align to specified position
        public static final SKTrigger kCenterStage = new SKTrigger(kDriver, 0, POV);
        public static final SKTrigger kRightStage = new SKTrigger(kDriver, 90, POV);
        public static final SKTrigger kLeftStage = new SKTrigger(kDriver, 270, POV);

        // Reset gyro
        public static final SKTrigger kResetGyroPos = new SKTrigger(kDriver, kRightBumper.value, BUTTON);

        // Intake or eject
        public static final SKTrigger kIntake = new SKTrigger(kDriver, kLeftTrigger.value, AXIS);
        public static final SKTrigger kEject  = new SKTrigger(kDriver, kRightTrigger.value, AXIS); 

    }
    /**
     * Defines the button, controller, and axis IDs needed to get input from an external
     * controller
     */

    public static class OperatorPorts
    {
        // Operator controller set to xbox controller
        public static final GenericHID kOperator = new FilteredXboxController(1).getHID();

        // Launch at target
        public static final SKTrigger kLaunchTrap = new SKTrigger(kOperator, kLeftBumper.value, BUTTON);
        public static final SKTrigger kLaunchSpeaker = new SKTrigger(kOperator, kRightBumper.value, BUTTON);
        public static final SKTrigger kLaunchAmp = new SKTrigger(kOperator, kA.value, BUTTON);

        // Go and align to location
        public static final SKTrigger kMoveLocationOne = new SKTrigger(kOperator, kX.value, BUTTON);
        public static final SKTrigger kMoveLocationTwo = new SKTrigger(kOperator, kB.value, BUTTON);
        public static final SKTrigger kMoveLocationThree = new SKTrigger(kOperator, kY.value, BUTTON);

        // Party mode
        public static final SKTrigger kPartyMode = new SKTrigger(kOperator, kBack.value, BUTTON);

        // Zero position
        public static final SKTrigger kZeroPos = new SKTrigger(kOperator, kStart.value, BUTTON); 

        // Intake
        public static final SKTrigger kIntake = new SKTrigger(kOperator, kLeftTrigger.value, BUTTON);
        public static final SKTrigger k = new SKTrigger(kOperator, kLeftTrigger.value, BUTTON);

        // Orient to speaker
        public static final SKTrigger kAngleSpeaker = new SKTrigger(kOperator, 0, BUTTON);

        // Run subsystem manually
        public static final SKTrigger kManualLauncher = new SKTrigger(kOperator, 90, POV);
        public static final SKTrigger kManualAmp = new SKTrigger(kOperator, 270, POV);
        public static final SKTrigger kManualTrap = new SKTrigger(kOperator, 180, POV);
        public static final FilteredAxis kLauncherAxis = new FilteredAxis(() -> kOperator.getRawAxis(kLeftY.value));

        // Reset launcher encoder
        public static final SKTrigger kResetLauncherEncoder = new SKTrigger(kOperator, kRightStick.value, BUTTON);
        public static final SKTrigger kLauncherOverride = new SKTrigger(kOperator, kLeftStick.value, BUTTON);

        // Run Churro 
        public static final SKTrigger kChurro = new SKTrigger(kOperator, kRightStick.value, BUTTON);
    }
    
    public static class launcherPorts
    {
        private static final String busName = "";
        public static final CANPort kTopLauncherMotor = new CANPort(40, busName);
        public static final CANPort kBottomLauncherMotor = new CANPort(41, busName);
        public static final CANPort kTransferMotor = new CANPort(42, busName);
    }
    /**
     * Defines all the ports needed to create sensors and actuators for the drivetrain.
     */

    public static class DrivePorts
    {
        private static final String busName = "DriveCAN";

        // CAN IDs for the drive motors on the swerve module
        public static final CANPort kFrontLeftDriveMotorPort  = new CANPort(10, busName);
        public static final CANPort kRearLeftDriveMotorPort   = new CANPort(11, busName);
        public static final CANPort kFrontRightDriveMotorPort = new CANPort(12, busName);
        public static final CANPort kRearRightDriveMotorPort  = new CANPort(13, busName);

        // CAN IDs for the turning motors on the swerve module
        public static final CANPort kFrontLeftTurningMotorPort  = new CANPort(20, busName);
        public static final CANPort kRearLeftTurningMotorPort   = new CANPort(21, busName);
        public static final CANPort kFrontRightTurningMotorPort = new CANPort(22, busName);
        public static final CANPort kRearRightTurningMotorPort  = new CANPort(23, busName);

        // CAN IDs for the CANCoders
        public static final CANPort kFrontLeftTurningEncoderPort  = new CANPort(30, busName);
        public static final CANPort kRearLeftTurningEncoderPort   = new CANPort(31, busName);
        public static final CANPort kFrontRightTurningEncoderPort = new CANPort(32, busName);
        public static final CANPort kRearRightTurningEncoderPort  = new CANPort(33, busName);
        
        // CAN ID for IMU
        public static final CANPort kPigeonPort = new CANPort(25, busName);
    }

    public static class intakePorts 
    {
        private static final String busName = "";
        public static final CANPort kTopIntakeMotor = new CANPort(50, busName);
        public static final CANPort kBottomIntakeMotor = new CANPort(51, busName);
        
    }

    public static class churroPorts
    {
        private static final String busName = "";

        public static final CANPort kChurroMotor = new CANPort(70, busName);
    }
}
