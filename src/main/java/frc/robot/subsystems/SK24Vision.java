package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DriverStation;

public class SK24Vision extends SubsystemBase
{
     NetworkTableInstance instance = NetworkTableInstance.getDefault();
     private NetworkTable limelight;
     public double[] robotPosition;
     public double[] targetPosition;
     public var alliance = DriverStation.getAlliance();

     // Creates a vision class that interacts with the limelight AprilTag data using Networktables
     public SK24Vision()
     {
        limelight = instance.getTable("limelight");

        limelight.getEntry("camMode").setNumber(0);
        limelight.getEntry("pipeline").setNumber(0);
     }

     // Returns a boolean value to determine if a valid AprilTag is present
     public boolean tagPresent()
     {
        if (limelight.getEntry("tv").getDouble() == 1) 
            return true;
        else
            return false;
     }

     // Returns the tag ID of the primary in view AprilTag
     public double getTagID()
     {
        return limelight.getEntry("tid").getDouble();
     }

     // Returns a double array containing {X,Y,Z,Roll,Pitch,Yaw} of the robot in the field space
     public double[] getPose()
     {
        robotPosition[] = limelight.getEntry("botpose").getDoubleArray(new double[6]]);
        return robotPosition[];
     }

     // Returns a double array containing {X,Y,Z,Roll,Pitch,Yaw} of the target in relation to the limelight
     public double[] getTargetPose()
     {
        targetPosition[] = limelight.getEntry("targetpose_cameraspace").getDoubleArray(new double[6]);
        return targetPosition[];
     }

     // Sets the pipeline to 0 which is used to recognize all field AprilTags
     public void setAllTagMode()
     {
         limelight.getEntry("pipeline").setNumber(0);
     }

     // Sets the pipeline to 1 which is used to recognize the shooting location on the amp using AprilTags 5 or 6
     public void setAmpMode()
     {
        limelight.getEntry("pipeline").setNumber(1);
     }

     // Sets the pipeline to 2 which is used to recognize the shooting location on the red speaker using AprilTags 3 and 4
     public void setRedSpeakerMode()
     {
        limelight.getEntry("pipeline").setNumber(2);
     }

     // Sets the pipeline to 3 which is used to recognize the shooting location on the blue speaker using AprilTags 7 and 8
     public void setBlueSpeakerMode()
     {
        limelight.getEntry("pipeline").setNumber(3);
     }

     // Sets the speaker pipeline to red or blue based on the driverstation alliance color
     public void setSpekerMode()
     {
         if(alliance.isPresent())
         {
             if (alliance.get() == DriverStation.Alliance.Red)
             {
                 setRedSpeakerMode();
             }
             else
             {
                 setBlueSpeakerMode();
             }
         }
         else
         {
             setAllTagMode();
         }
     }

     // Sets the pipeline to 4 which is used to recognize the shooting location for the trap on the stage using AprilTags 11 through 16
     public void setStageMode()
     {
        limelight.getEntry("pipeline").setNumber(4);
     }

     // Returns the angle between the target and the pivot point of the launcher
     public double returnTargetAngle()
     {
        return 0.0;
     }
}