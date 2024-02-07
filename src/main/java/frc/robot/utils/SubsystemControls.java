package frc.robot.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class holds the subsystem control values as imported from the subsystem control
 * JSON file. This is made for the 2022 season
 */
public class SubsystemControls
{

    private final boolean example;
    private final boolean launcher;

    /**
     * 
     * @param example
     *            indicates if the example subsystem is present and should be enabled
     */
    public SubsystemControls(
        @JsonProperty(required = true, value = "example")     boolean example,
        @JsonProperty(required = true, value = "launcher")    boolean launcher)
    {
        this.example = example;
        this.launcher = launcher;
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

    public boolean isLauncherPresent()
    {
        return launcher;
    }
}
