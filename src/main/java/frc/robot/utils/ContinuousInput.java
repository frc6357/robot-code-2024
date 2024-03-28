package frc.robot.utils;

 public final class ContinuousInput {
    
    public static double wrapValue(double wrappedValue, double minValue, double maxValue){
        double distance = Math.abs(maxValue - minValue);
        double remainder = wrappedValue % distance;

        if(remainder > maxValue){
            return minValue + (remainder - maxValue);
        }
        else if(remainder < minValue){
            return maxValue - (minValue - remainder);
        }
        else{
            return wrappedValue;
        }
    }
}
