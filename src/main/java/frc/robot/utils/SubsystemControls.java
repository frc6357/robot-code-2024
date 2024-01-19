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

    /**
     * 
     * @param example
     *            indicates if the example subsystem is present and should be enabled
     * @param drive 
     *            indicated if the drive subsystem is present and should be enabled
     */
    public SubsystemControls(
        @JsonProperty(required = true, value = "example")     boolean example,
        @JsonProperty(required = true, value = "drive")       boolean drive)
    {
        this.example = example;
        this.drive = drive;
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
}
