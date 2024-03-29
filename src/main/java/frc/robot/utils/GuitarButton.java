package frc.robot.utils;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class GuitarButton
{
    public static Trigger get(GenericHID controller, int buttonNumber)
    {
        final JoystickButton green    = new JoystickButton(controller, 2);
        final JoystickButton red      = new JoystickButton(controller, 3);
        final JoystickButton yellow   = new JoystickButton(controller, 4);
        final JoystickButton blue     = new JoystickButton(controller, 1);
        final JoystickButton orange   = new JoystickButton(controller, 5);

        final JoystickButton colorModifier    = new JoystickButton(controller, 7);

        final JoystickButton plus    = new JoystickButton(controller, 9);
        final JoystickButton minus   = new JoystickButton(controller, 10);
        
        switch(buttonNumber)
        {
            case 1:
                return green.and(colorModifier.negate());

            case 2:
                return red.and(colorModifier.negate());

            case 3:
                return yellow.and(colorModifier.negate());

            case 4:
                return blue.and(colorModifier.negate());

            case 5:
                return orange.and(colorModifier.negate());

            case 6:
                return green.and(colorModifier);

            case 7:
                return red.and(colorModifier);

            case 8:
                return yellow.and(colorModifier);

            case 9:
                return blue.and(colorModifier);

            case 10:
                return orange.and(colorModifier);

            case 11:
                return minus;

            case 12:
                return plus;

            default:
                DriverStation.reportError(buttonNumber + "is not a valid button", false);
                return new Trigger(() -> {return false;});
        }
    }
}
