package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;

public final class OnTheFly {
    // Load the path we want to pathfind to and follow
    public static final PathPlannerPath scoreAmp = PathPlannerPath.fromPathFile("AmpScore");
    public static final PathPlannerPath centerStage = PathPlannerPath.fromPathFile("StageCenter");
    public static final PathPlannerPath leftStage = PathPlannerPath.fromPathFile("StageLeft");
    public static final PathPlannerPath rightStage = PathPlannerPath.fromPathFile("StageRight");

    // Create the constraints to use while pathfinding. The constraints defined in the path will only be used for the path.
    public static final PathConstraints constraints = new PathConstraints(
            3.0, 4.0,
            Units.degreesToRadians(540), Units.degreesToRadians(720));

    public static final Command scoreAmpCommand = AutoBuilder.pathfindThenFollowPath(
            scoreAmp,
            constraints,
            0.0 // Rotation delay distance in meters. This is how far the robot should travel before attempting to rotate.
    );
    public static final Command centerStageCommand = AutoBuilder.pathfindThenFollowPath(
            centerStage,
            constraints,
            0.0 // Rotation delay distance in meters. This is how far the robot should travel before attempting to rotate.
    );
    public static final Command LeftStageCommand = AutoBuilder.pathfindThenFollowPath(
            leftStage,
            constraints,
            0.0 // Rotation delay distance in meters. This is how far the robot should travel before attempting to rotate.
    );
    public static final Command rightStageCommand = AutoBuilder.pathfindThenFollowPath(
            rightStage,
            constraints,
            0.0 // Rotation delay distance in meters. This is how far the robot should travel before attempting to rotate.
    );
}
