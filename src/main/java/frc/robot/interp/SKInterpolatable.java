package frc.robot.interp;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.Num;
import edu.wpi.first.math.numbers.N1;

public interface SKInterpolatable<VarCount extends Num, SelfType extends SKInterpolatable<VarCount, SelfType>> {

    public abstract Matrix<N1, VarCount> toMatrix();
    
    public abstract SelfType fromMatrix(Matrix<N1, VarCount> sourceData);
}
