package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.utils.SKCANLight;

public class LightLauncherCommand extends Command{
    private SKCANLight light;
    private Supplier <Boolean> fullSpeed;
    private boolean isDone;
    public LightLauncherCommand(boolean isDone, Supplier<Boolean> fullSpeed, SKCANLight light)
    {
        this.light = light;   
        this.fullSpeed = fullSpeed;
        this.isDone = isDone;
    }

    @Override
    public void initialize()
    {}

    @Override
    public void execute()
    {
        if (fullSpeed.get()) {light.setPurple();}
        else{
            light.setTeamColor();
        }
    }

    @Override
    public void end(boolean interrupted)
    {
        light.setTeamColor();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished()
    {
        return isDone;
    } 
}
