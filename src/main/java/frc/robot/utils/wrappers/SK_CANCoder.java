package frc.robot.utils.wrappers;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.CANcoderConfigurator;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.signals.AbsoluteSensorRangeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.utils.CANPort;

/**
 * Java class that wraps WPI_CANCoder to allow offsetDegrees as parameter that changes
 * position to an offset position.
 */
public class SK_CANCoder extends CANcoder
{

    private double offsetDegrees;

    public SK_CANCoder(CANPort deviceNumber, double offsetDegrees)
    {
        super(deviceNumber.ID, deviceNumber.bus);

        this.offsetDegrees = offsetDegrees;
        CANcoderConfigurator configurator = this.getConfigurator();
        CANcoderConfiguration config = new CANcoderConfiguration();

        config.MagnetSensor.AbsoluteSensorRange = AbsoluteSensorRangeValue.Unsigned_0To1;
        config.MagnetSensor.MagnetOffset = this.offsetDegrees / 360.0; //Convert from rotations to degrees
        config.MagnetSensor.SensorDirection = SensorDirectionValue.CounterClockwise_Positive;
        configurator.apply(config);
        
    }

    /**
     * Uses the measured angle to find Rotation2d
     * 
     * @return calculated Rotation2d (CCW+)
     */
    public Rotation2d getRotation2d()
    {
        return Rotation2d.fromRotations(getAbsolutePosition().getValueAsDouble());
    }
}
