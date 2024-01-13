package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import frc.robot.utils.SKTrigger;
import frc.robot.utils.filters.FilteredXboxController;
import static edu.wpi.first.wpilibj.XboxController.Axis.*;
import static edu.wpi.first.wpilibj.XboxController.Button.*;
import static frc.robot.utils.SKTrigger.INPUT_TYPE.*;


public class Ports {

    public static final GenericHID kOperator = new FilteredXboxController(1).getHID();
    public static final GenericHID kDriver = new FilteredXboxController(0).getHID();
    public static final SKTrigger kExample   = new SKTrigger(kOperator, kY.value, BUTTON);

}
