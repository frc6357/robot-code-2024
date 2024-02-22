package frc.robot.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class holds the subsystem control values as imported from the subsystem control
 * JSON file. This is made for the 2022 season
 */
public class SubsystemControls
{

    private final boolean example;
    private final boolean drive;
    private final boolean launcher;
    private final boolean lights;
    private final boolean intake;

     /**  
     * @param example
     *            indicates if the example subsystem is present and should be enabled
     * @param launcher
     *            indicates if the launcher subsystem is present and should be enabled
     * @param lights
     *            indicates if the lights subsystem is present and should be enabled
     * @param intake
     *            indicates if the intake subsystem is present and should be enabled
     * @param drive
     *            indicates if the drive subsystem is present and should be enabled
     */
    public SubsystemControls(
        @JsonProperty(required = true, value = "example")     boolean example,
        @JsonProperty(required = true, value = "launcher")    boolean launcher,
        @JsonProperty(required = true, value = "lights")      boolean lights,
        @JsonProperty(required = true, value = "intake")      boolean intake,
        @JsonProperty(required = true, value = "drive")       boolean drive)
    {
        this.drive = drive;
        this.example = example;
        this.launcher = launcher;
        this.lights = lights;
        this.intake = intake;
    }

    /**
     * Returns true if the example is indicated as present and should be enabled.
     * 
     * @return true if the example is indicated as present and should be enabled; false
     *         otherwise
     */
    public boolean isExamplePresent()
    {
        return example;
    }

    /**
     * Returns true if the drive subsystem is indicated as present and should be enabled.
     * 
     * @return true if the drive subsystem is indicated as present and should be enabled; false
     *         otherwise
     */
    public boolean isDrivePresent()
    {
        return drive;
    }
    public boolean isLauncherPresent()
    {
        return launcher;
    }
    public boolean isLightsPresent()
    {
        return lights;
    }
    public boolean isIntakePresent()
    {
        return intake;
    }
}
