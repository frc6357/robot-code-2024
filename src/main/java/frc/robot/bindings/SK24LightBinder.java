package frc.robot.bindings;

import static frc.robot.Ports.OperatorPorts.kPartyMode;

import java.util.Optional;

import com.ctre.phoenix.led.ColorFlowAnimation.Direction;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.SK24Light;
import frc.robot.Ports.DriverPorts;
import frc.robot.Ports.OperatorPorts;

public class SK24LightBinder implements CommandBinder{
    Optional<SK24Light> subsystem;
    private final Trigger driverLightButton;
    private final Trigger operatorLightButton;

    public SK24LightBinder(Optional<SK24Light> subsystem){
        this.subsystem = subsystem;
        this.driverLightButton = DriverPorts.kPartyMode.button;
        this.operatorLightButton = OperatorPorts.kPartyMode.button;
    }

    public void bindButtons()
    {
        // If subsystem is present then this method will bind the buttons
        Direction direction = Direction.Forward;
        if (subsystem.isPresent())
        {
            SK24Light m_light = subsystem.get();
            
            driverLightButton.onTrue(new InstantCommand(() -> m_light.FlowAnimate(3, 168, 181, 0.5, 64, direction,8)));
            operatorLightButton.onTrue(new InstantCommand(() -> m_light.FlowAnimate(3, 168, 181, 0.5, 64, direction,8)));
        }
    }
}
