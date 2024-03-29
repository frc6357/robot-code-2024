package frc.robot.utils;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.utils.SKTrigger.INPUT_TYPE;

public abstract class SKControllerMapper {
    
    public static enum ControllerType{
        XBOX,
        GUITAR_HERO
    }

    public static Trigger mapController(GenericHID controller, int port, ControllerType controllerType, INPUT_TYPE type){
        GenericController myController;
        switch(controllerType){
            case XBOX:
                if(type == INPUT_TYPE.AXIS){
                    return new Trigger(() -> controller.getRawAxis(port) > 0.5);
                }else{
                    return new JoystickButton(controller, port);
                }
            case GUITAR_HERO:
                myController = new GuitarHeroController(controller);
                return myController.getMap(port, type);
            default:
                if(type == INPUT_TYPE.AXIS){
                    return new Trigger(() -> controller.getRawAxis(port) > 0.5);
                }else{
                    return new JoystickButton(controller, port);
                }

        }
    }
}
