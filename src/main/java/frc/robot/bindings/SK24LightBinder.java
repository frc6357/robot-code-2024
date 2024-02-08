package frc.robot.bindings;

import static frc.robot.Ports.OperatorPorts.kRotateGrid;
import static frc.robot.Ports.OperatorPorts.kRotateLeft;

import java.util.Optional;

import com.ctre.phoenix.led.ColorFlowAnimation.Direction;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.SK24Light;

public class SK24LightBinder implements CommandBinder{
    Optional<SK24Light> subsystem;
    private final Trigger lightButton;
    private final Trigger animateButton;

    public SK24LightBinder(Optional<SK24Light> subsystem){
        this.subsystem = subsystem;
        lightButton = kRotateGrid.button;
        animateButton = kRotateLeft.button;
    }

    public void bindButtons()
    {
        // If subsystem is present then this method will bind the buttons
        if (subsystem.isPresent())
        {
            SK24Light m_light = subsystem.get();
            
            lightButton.onTrue(new InstantCommand(() -> m_light.setOrange(1000)));
            animateButton.onTrue(new InstantCommand(() -> m_light.setGreen(1000)));
            animateButton.onFalse(new InstantCommand(() -> m_light.clearAnimate()));

        }
    }
}
