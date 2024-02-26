package frc.robot.bindings;

import static frc.robot.Ports.OperatorPorts.kPartyMode;

import java.util.Optional;

import com.ctre.phoenix.led.ColorFlowAnimation.Direction;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.SK24Light;

public class SK24LightBinder implements CommandBinder{
    Optional<SK24Light> subsystem;
    private final Trigger lightButton;

    public SK24LightBinder(Optional<SK24Light> subsystem){
        this.subsystem = subsystem;
        this.lightButton = kPartyMode.button;
    }

    public void bindButtons()
    {
        // If subsystem is present then this method will bind the buttons
        Direction direction = Direction.Forward;
        if (subsystem.isPresent())
        {
            SK24Light m_light = subsystem.get();
            
            lightButton.onTrue(new InstantCommand(() -> m_light.FlowAnimate(3, 168, 181, 0.5, 64, direction,8)));
        }
    }
}
