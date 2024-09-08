package frc.robot.commands;

import static frc.robot.Constants.LauncherConstants.kLauncherLeftSpeed;
import static frc.robot.Constants.LauncherConstants.kLauncherRightSpeed;

import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.SK24Launcher;



public class RevLauncher extends Command{
   
    private SK24Launcher launcher;
    public RevLauncher(SK24Launcher launcher)
    {
       
        this.launcher = launcher;
        
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(launcher);
    }

    @Override
    public void initialize()
    {}

    @Override
    public void execute()
    {
        launcher.setLauncherSpeed(kLauncherLeftSpeed, kLauncherRightSpeed);
    }

    @Override
    public void end(boolean interrupted)
    {
        launcher.stopLauncher();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished()
    {
        return true;
    }
}