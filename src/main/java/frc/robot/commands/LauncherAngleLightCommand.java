/**
package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SK24LauncherAngle;
import frc.robot.utils.SKCANLight;

public class LauncherAngleLightCommand extends Command{
 
    private SK24LauncherAngle arm;
    private SKCANLight light;

    public LauncherAngleLightCommand(SK24LauncherAngle arm, SKCANLight light)
    {
        this.arm = arm;
        this.light = light;
        
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(arm);
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
        if(!interrupted) {light.setPurple();}
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished()
    {
        return arm.isAtTargetAngle();
    }
}
*/