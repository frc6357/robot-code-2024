package frc.robot.utils;

 public final class ContinuousInput {
    
    public static double wrapValue(double wrappedValue, double minValue, double maxValue){
        if(wrappedValue > maxValue){
            double changeValue = wrappedValue - maxValue;
            if(changeValue > maxValue)
            {
                return wrapValue(changeValue, minValue, maxValue);
            }
            return minValue + (changeValue);
        }else if(wrappedValue < minValue)
        {
            double changeValue = minValue - wrappedValue;
            if(changeValue < minValue)
            {
                return wrapValue(changeValue, minValue, maxValue);
            }
            return maxValue - (changeValue);
        }else
        {
            return wrappedValue;
        }
    }
}
