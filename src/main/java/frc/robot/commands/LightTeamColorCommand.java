package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.utils.SKCANLight;

public class LightTeamColorCommand extends Command{
    private SKCANLight light;
    public LightTeamColorCommand(SKCANLight light)
    {
        this.light = light;   
    }

    @Override
    public void initialize()
    {}

    @Override
    public void execute()
    {}

    @Override
    public void end(boolean interrupted)
    {
        light.setTeamColor();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished()
    {
        return true;
    } 
}
