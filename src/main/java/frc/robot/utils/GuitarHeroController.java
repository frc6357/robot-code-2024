package frc.robot.utils;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.utils.SKTrigger.INPUT_TYPE;

public class GuitarHeroController implements GenericController{
    GenericHID controller;

    public GuitarHeroController(GenericHID controller){
        this.controller = controller;
    }
    public Trigger getMap(int port, INPUT_TYPE type){

        final Trigger whammy    = new Trigger(() -> controller.getRawAxis(11) > 0.5);
        final JoystickButton green    = new JoystickButton(controller, 2);
        final JoystickButton red      = new JoystickButton(controller, 3);
        final JoystickButton yellow   = new JoystickButton(controller, 4);
        final JoystickButton blue     = new JoystickButton(controller, 1);
        final JoystickButton orange   = new JoystickButton(controller, 5);

        final JoystickButton colorModifier    = new JoystickButton(controller, 7);

        final JoystickButton plus    = new JoystickButton(controller, 9);
        final JoystickButton minus   = new JoystickButton(controller, 10);
        
        switch(port)
        {
            case a:
                return green.and(colorModifier.negate());

            case b:
                return red.and(colorModifier.negate());

            case x:
                return yellow.and(colorModifier.negate());

            case y:
                return blue.and(colorModifier.negate());

            case leftBumper:
                return orange.and(colorModifier.negate());

            case rightBumper:
                return green.and(colorModifier);

            case back:
                return red.and(colorModifier);

            case start:
                return yellow.and(colorModifier);

            case leftStick:
                return blue.and(colorModifier);

            case rightStick:
                return orange.and(colorModifier);

            default:
                DriverStation.reportError(port + "is not a valid button", false);
                return new Trigger(() -> {return false;});
        }
    }
}
