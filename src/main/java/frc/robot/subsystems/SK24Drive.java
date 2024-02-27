// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.AutoConstants.kAutoPathConfig;
import static frc.robot.Constants.DriveConstants.deadband;

import com.ctre.phoenix6.Utils;
import com.ctre.phoenix6.mechanisms.swerve.SwerveDrivetrain;
import com.ctre.phoenix6.mechanisms.swerve.SwerveDrivetrainConstants;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModuleConstants;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructArrayPublisher;
import edu.wpi.first.networktables.StructPublisher;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants.DriveConstants;
import frc.robot.utils.SK24AutoBuilder;


public class SK24Drive extends SwerveDrivetrain implements Subsystem
{

    private static final double kSimLoopPeriod = 0.005; // 5 ms
    private Notifier m_simNotifier = null;
    private double m_lastSimTime;
    private boolean hasAppliedOperatorPerspective = false;
    /* Blue alliance sees forward as 0 degrees (toward red alliance wall) */
    private final Rotation2d BlueAlliancePerspectiveRotation = Rotation2d.fromDegrees(0);
    /* Red alliance sees forward as 180 degrees (toward blue alliance wall) */
    private final Rotation2d RedAlliancePerspectiveRotation = Rotation2d.fromDegrees(180);


    StructArrayPublisher<SwerveModuleState> currentPublisher = NetworkTableInstance.getDefault()
  .getStructArrayTopic("MyCurrentStates", SwerveModuleState.struct).publish();
  
    StructArrayPublisher<SwerveModuleState> targetPublisher = NetworkTableInstance.getDefault()
  .getStructArrayTopic("MyTargetStates", SwerveModuleState.struct).publish();
    
  StructPublisher<Rotation2d> odomPublisher = NetworkTableInstance.getDefault().getStructTopic("Rotation", Rotation2d.struct).publish();
    public SK24Drive(SwerveDrivetrainConstants driveTrainConstants, SwerveModuleConstants... modules) {
        super(driveTrainConstants, modules);

        this.m_pigeon2.reset();
        setupPathPlanner();

        if (Utils.isSimulation()) {
          startSimThread();
      }

    }
  
    private void startSimThread() {
        m_lastSimTime = Utils.getCurrentTimeSeconds();

        /* Run simulation at a faster rate so PID gains behave more reasonably */
        m_simNotifier = new Notifier(() -> {
            final double currentTime = Utils.getCurrentTimeSeconds();
            double deltaTime = currentTime - m_lastSimTime;
            m_lastSimTime = currentTime;

            /* use the measured time delta, get battery voltage from WPILib */
            updateSimState(deltaTime, RobotController.getBatteryVoltage());
        });
        m_simNotifier.startPeriodic(kSimLoopPeriod);
    }
  /**
   * Setup AutoBuilder for PathPlanner.
   */
  public void setupPathPlanner()
  {
    SK24AutoBuilder.configureHolonomic(
        this::getPose, // Robot pose supplier
        this::resetOdometry, // Method to reset odometry (will be called if your auto has a starting pose)
        this::getRobotVelocity, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
        this::setChassisSpeeds, // Method that will drive the robot given ROBOT RELATIVE ChassisSpeeds
        kAutoPathConfig,
        () -> {
                    // Boolean supplier that controls when the path will be mirrored for the red alliance
                    // This will flip the path being followed to the red side of the field.
                    // THE ORIGIN WILL REMAIN ON THE BLUE SIDE
                    var alliance = DriverStation.getAlliance();
                    return alliance.isPresent() ? alliance.get() == DriverStation.Alliance.Red : false;
                },
        this // Reference to this subsystem to set requirements
                                  );
  }


  /**
   * The primary method for controlling the drivebase.  Takes a xSpeed, ySpeed and a rotation rate, and
   * calculates and commands module states accordingly. Also has field- and robot-relative modes, 
   * which affect how the translation vector is used.
   *
   * @param xSpeed  Velocity of x in m/s
   * @param ySpeed  Velocity of y in m/s
   * @param rot     Robot angular rate, in radians per second. CCW positive.  Unaffected by field/robot relativity.
   * @param fieldRelative Drive mode. True for field-relative, false for robot-relative.
   */
  public void drive(double xSpeed, double ySpeed, double rot, boolean fieldRelative)
  {
    // if((xSpeed == 0.0) && (ySpeed == 0.0) && (rot == 0.0)){
    //   this.lock();
    // }
    // else
     if(fieldRelative){
      SwerveRequest.FieldCentric fieldCentric = new SwerveRequest.FieldCentric()
      .withDriveRequestType(DriveRequestType.OpenLoopVoltage).withDeadband(0).withRotationalDeadband(0);
      
      this.setControl(fieldCentric.withVelocityX(xSpeed).withVelocityY(ySpeed).withRotationalRate(rot));
    }else{
      SwerveRequest.RobotCentric robotCentric = new SwerveRequest.RobotCentric()
      .withDriveRequestType(DriveRequestType.OpenLoopVoltage).withDeadband(0).withRotationalDeadband(0);
      

      this.setControl(robotCentric.withVelocityX(xSpeed).withVelocityY(ySpeed).withRotationalRate(rot));
    }
   }
   public void setFieldRelativeHeading(Rotation2d rotation){
      this.m_fieldRelativeOffset = rotation;
   }
   public void pointWheels(Rotation2d rotation){
      SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt().withModuleDirection(rotation);
      this.setControl(point);
   }
  @Override
  public void periodic()
  {
    this.checkIsRed();
    SmartDashboard.putNumber("Pigeon", getPigeonHeading().getDegrees());
    currentPublisher.set(this.getState().ModuleStates);
    targetPublisher.set(this.getState().ModuleTargets);
    odomPublisher.set(getOdomHeading());
  }




  /**
   * Resets odometry to the given pose.
   *
   * @param initalHolonomicPose The pose to set the odometry to
   */
  public void resetOdometry(Pose2d initalHolonomicPose)
  {
    this.seedFieldRelative(initalHolonomicPose);
  }

  public void setFront()
  {
    resetOdometry(new Pose2d());
    this.seedFieldRelative();
  }
  
  
  /**
   * Gets the current pose (position and rotation) of the robot, as reported by odometry.
   *
   * @return The robot's pose
   */
  public Pose2d getPose()
  {
    return this.m_odometry.getEstimatedPosition();
  }
  
  /**
   * Set chassis speeds of robot
   * @param chassisSpeeds Chassis Speeds to set.
   */
  public void setChassisSpeeds(ChassisSpeeds chassisSpeeds)
  {
    SwerveRequest chassisSpeed = new SwerveRequest.ApplyChassisSpeeds().withSpeeds(chassisSpeeds);
    this.setControl(chassisSpeed);
  }

  
  
  /**
   * Gets the current yaw angle of the robot, as reported by the imu.  CCW positive, not wrapped.
   *
   * @return The yaw angle
   */
  public Rotation2d getPigeonHeading()
  {
    return this.m_pigeon2.getRotation2d();
  }

  public Rotation2d getOdomHeading()
  {
    return getPose().getRotation();
  }

  public boolean leftTilted()
  {
    double roll = this.getPigeon2().getRoll().getValueAsDouble();
    if(roll > deadband){
      return true;
    }
    return false;
  }

  public boolean rightTilted()
  {
    double roll = this.getPigeon2().getRoll().getValueAsDouble();
    if(roll < -deadband){
      return true;
    }
    return false;
  }
  /**
   * Sets the heading of the robot using a {@link Rotation2d}. CCW positive, not wrapped.
   * 
   * @param angle {@link Rotation2d} to set the robot heading to
   */
  public void setHeading(Rotation2d angle){
    resetOdometry(new Pose2d(this.m_odometry.getEstimatedPosition().getTranslation(), angle));
  }
  
  /**
   * Gets the current velocity (x, y and omega) of the robot
   *
   * @return A {@link ChassisSpeeds} object of the current velocity
   */
  public ChassisSpeeds getRobotVelocity()
  {
    return DriveConstants.swerveKinematics.toChassisSpeeds(this.getState().ModuleStates);
  }


  
  /**
   * Lock the swerve drive to prevent it from moving.
   */
  public void lock()
  {
    this.setControl(new SwerveRequest.SwerveDriveBrake());
  }

  /**
   * Gets the current pitch angle of the robot, as reported by the imu.
   *
   * @return The heading as a {@link Rotation2d} angle
   */
  public Rotation2d getPitch()
  {
    return new Rotation2d(this.getPigeon2().getPitch().getValueAsDouble());
  }

  /**
   * Add a fake vision reading for testing purposes.
   */
  public void addFakeVisionReading()
  {
    this.addVisionMeasurement(new Pose2d(3, 3, Rotation2d.fromDegrees(65)), Timer.getFPGATimestamp());
  }
  
  /**
   * Check if the driverstation is set to blue or red
   * @return Return true if the driver station to red and false if the driver station is set to true
   */
  public boolean checkIsRed(){

    var alliance = DriverStation.getAlliance();
    boolean isRed = alliance.isPresent() ? alliance.get() == DriverStation.Alliance.Red : false;
    if (!hasAppliedOperatorPerspective || DriverStation.isDisabled()) {
      this.setFieldRelativeHeading(isRed ? RedAlliancePerspectiveRotation : BlueAlliancePerspectiveRotation);
      hasAppliedOperatorPerspective = true;
    }
    return isRed;
  }
  

  /**
   * Add a vision measurement
   * @param vision The pose of the robot as measured by the vision camera.
   * @param timestampSeconds The timestamp of the vision measurement in seconds. 
   * Note that if you don't use your own time source by calling SwerveDrivePoseEstimator.updateWithTime(double, Rotation2d, SwerveModulePosition []) 
   * then you must use a timestamp with an epoch since FPGA startup (i.e., the epoch of this timestamp is the same epoch as edu.wpi.first.wpilibj.Timer.getFPGATimestamp().) 
   * This means that you should use edu.wpi.first.wpilibj.Timer.getFPGATimestamp() as your time source or sync the epochs.
   */
  public void addVisionMeasurement(Pose2d vision, double timestampSeconds){
    this.addVisionMeasurement(vision, timestampSeconds);
  }
  
}