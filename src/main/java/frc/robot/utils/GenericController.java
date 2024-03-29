package frc.robot.utils;

import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.utils.SKTrigger.INPUT_TYPE;

public interface GenericController {

    final int a = 1;
    final int b = 2;
    final int x = 3;
    final int y = 4;
    final int leftBumper = 5;
    final int rightBumper = 6;
    final int back = 7;
    final int start = 8;
    final int leftStick = 9;
    final int rightStick = 10;

    /**
     * Method to return the mapped port that corresponds to the parameter
     * @param port The current port used for the default controller
     * @return The new mapped value that corresponds with the new controller
     */
    public Trigger getMap(int port, INPUT_TYPE type);
}
