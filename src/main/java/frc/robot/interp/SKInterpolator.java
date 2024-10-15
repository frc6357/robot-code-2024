package frc.robot.interp;

import java.util.function.Supplier;

import edu.wpi.first.math.InterpolatingMatrixTreeMap;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.Num;
import edu.wpi.first.math.numbers.N1;

public class SKInterpolator<Count extends Num, T extends SKInterpolatable<Count, T>> {
    
    private InterpolatingMatrixTreeMap<Double, N1, Count> launcherMap = new InterpolatingMatrixTreeMap<>();
    private final Supplier<T> factory;

    public SKInterpolator(Supplier<T> factory) {
        this.factory = factory;
    }
    
    public void put(double key, T interpolatable) {
        launcherMap.put(key, interpolatable.toMatrix());
    }

    public T get(double key) {
        Matrix<N1, Count> value = launcherMap.get(key);
        T output = factory.get().fromMatrix(value);
        return output;
    }
}
