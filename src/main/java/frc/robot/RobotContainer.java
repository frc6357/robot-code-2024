// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static frc.robot.Constants.DriveConstants.BackLeft;
import static frc.robot.Constants.DriveConstants.BackRight;
import static frc.robot.Constants.DriveConstants.DrivetrainConstants;
import static frc.robot.Constants.DriveConstants.FrontLeft;
import static frc.robot.Constants.DriveConstants.FrontRight;
import static frc.robot.Constants.DriveConstants.autoList;
import static frc.robot.Constants.DriveConstants.kMaxSpeedMetersPerSecond;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ctre.phoenix6.Utils;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.bindings.CommandBinder;
import frc.robot.bindings.SK24ChurroBinder;
import frc.robot.bindings.SK24DriveBinder;
import frc.robot.subsystems.SK24Drive;
import frc.robot.utils.SK24AutoBuilder;
import frc.robot.bindings.SK24IntakeBinder;
import frc.robot.bindings.SK24LauncherBinder;
import frc.robot.bindings.SK24LightBinder;
import frc.robot.subsystems.SK24Churro;
import frc.robot.subsystems.SK24Intake;
import frc.robot.subsystems.SK24Launcher;
import frc.robot.subsystems.SK24LauncherAngle;
import frc.robot.subsystems.SK24Light;
import frc.robot.utils.SubsystemControls;
import frc.robot.utils.filters.FilteredJoystick;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private Optional<SK24Drive>  m_drive  = Optional.empty();
  private Optional<SK24Launcher>  m_launcher  = Optional.empty();
  private Optional<SK24Light>  m_light  = Optional.empty();
  private Optional<SK24Intake>  m_intake  = Optional.empty();
  private Optional<SK24LauncherAngle>  m_launcher_angle  = Optional.empty();
  private Optional<SK24Churro>  m_churro  = Optional.empty();

  // The list containing all the command binding classes
  private List<CommandBinder> buttonBinders = new ArrayList<CommandBinder>();

  // The class used to create all PathPlanner Autos
  // private SK23AutoGenerator autoGenerator;
  // An option box on shuffleboard to choose the auto path
  SendableChooser<Command> autoCommandSelector = new SendableChooser<Command>();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {

    // Creates all subsystems that are on the robot
    configureSubsystems();

    // Configure the trigger bindings
    configureButtonBindings();
  }

  /**
     * Will create all the optional subsystems using the json file in the deploy directory
     */
    private void configureSubsystems()
    {
        File deployDirectory = Filesystem.getDeployDirectory();

        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = new JsonFactory();

        try
        {
            // Looking for the Subsystems.json file in the deploy directory
            JsonParser parser =
                    factory.createParser(new File(deployDirectory, Constants.SUBSYSTEMFILE));
            SubsystemControls subsystems = mapper.readValue(parser, SubsystemControls.class);

            // Instantiating subsystems if they are present
            // This is decided by looking at Subsystems.json
            if(subsystems.isLauncherPresent())
            {
                m_launcher = Optional.of(new SK24Launcher());
            }
            if(subsystems.isLightsPresent())
            {
                m_light = Optional.of(new SK24Light());
            }
            if(subsystems.isIntakePresent())
            {
              m_intake = Optional.of(new SK24Intake());
            }
            if (subsystems.isDrivePresent())
            {
                m_drive = Optional.of(new SK24Drive(DrivetrainConstants, FrontLeft,
                FrontRight, BackLeft, BackRight));

                Telemetry log = new Telemetry(Constants.AutoConstants.kMaxSpeedMetersPerSecond);

                if (Utils.isSimulation()) {
                    m_drive.get().seedFieldRelative(new Pose2d(new Translation2d(), Rotation2d.fromDegrees(90)));
                }
                m_drive.get().registerTelemetry(log::telemeterize);

                // Configures the autonomous paths and smartdashboard chooser
                // autoCommandSelector = AutoBuilder.buildAutoChooser();
                SK24AutoBuilder.setAutoNames(autoList);
                autoCommandSelector = SK24AutoBuilder.buildAutoChooser("LeftScore1");
                SmartDashboard.putData("Auto Chooser", autoCommandSelector);
            }
            if(subsystems.isChurroPresent())
            {
                m_churro = Optional.of(new SK24Churro());
            }
            if(subsystems.isLauncherArmPresent())
            {
                m_launcher_angle = Optional.of(new SK24LauncherAngle());
            }
        }
        catch (IOException e)
        {
            DriverStation.reportError("Failure to read Subsystem Control File!", e.getStackTrace());
        }
    }

  /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a {@link edu.wpi.first.wpilibj.GenericHID} or one of its subclasses
     * ({@link edu.wpi.first.wpilibj.Joystick} or {@link FilteredJoystick}), and then
     * calling passing it to a {@link JoystickButton}.
     */
    private void configureButtonBindings()
    {

        // Adding all the binding classes to the list
        buttonBinders.add(new SK24LauncherBinder(m_launcher, m_launcher_angle));
        buttonBinders.add(new SK24DriveBinder(m_drive));
        buttonBinders.add(new SK24LightBinder(m_light));
        buttonBinders.add(new SK24IntakeBinder(m_intake));
        buttonBinders.add(new SK24ChurroBinder(m_churro));

        // Traversing through all the binding classes to actually bind the buttons
        for (CommandBinder subsystemGroup : buttonBinders)
        {
            subsystemGroup.bindButtons();
        }

    }

  /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand()
    {
        return autoCommandSelector.getSelected();
    }

    public void testPeriodic(){
        
    }
    public void testInit(){
        
    }

    public void matchInit()
    {
    
    }
}
