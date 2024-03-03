package frc.robot.subsystems;


import com.ctre.phoenix.led.CANdle;
import com.ctre.phoenix.led.CANdle.LEDStripType;
import com.ctre.phoenix.led.CANdleConfiguration;
import com.ctre.phoenix.led.ColorFlowAnimation;
import com.ctre.phoenix.led.ColorFlowAnimation.Direction;
import com.ctre.phoenix.led.FireAnimation;
import com.ctre.phoenix.led.LarsonAnimation;
import com.ctre.phoenix.led.LarsonAnimation.BounceMode;
import com.ctre.phoenix.led.RainbowAnimation;
import com.ctre.phoenix.led.RgbFadeAnimation;
import com.ctre.phoenix.led.SingleFadeAnimation;
import com.ctre.phoenix.led.StrobeAnimation;
import com.ctre.phoenix.led.TwinkleAnimation;
import com.ctre.phoenix.led.TwinkleAnimation.TwinklePercent;

import edu.wpi.first.wpilibj.shuffleboard.SimpleWidget;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SK24Light extends SubsystemBase{
    CANdle candle;
    SimpleWidget colorWidget;
    
    public SK24Light(){

        candle = new CANdle(3); // creates a new CANdle with ID 3
        
        CANdleConfiguration config = new CANdleConfiguration();
        
        
        config.stripType = LEDStripType.RGB; // set the strip type to RGB
        config.disableWhenLOS = true;
        config.statusLedOffWhenActive = true;
    
        config.brightnessScalar = 1.0; // dim the LEDs to half brightness
        config.v5Enabled = false;
    
        candle.configAllSettings(config);

        
    }
    
    /**
     * 
     * @param red The amount of Red to set, range is [0, 255]
     * @param green The amount of Green to set, range is [0, 255]
     * @param blue The amount of Blue to set, range is [0, 255]
     */
    public void setLight(int red, int green, int blue, int numLed){
        candle.setLEDs(red, green, blue, 0, 8, numLed); 
        
    }

    /**
     * @param bright Brightness for all LED's controlled, range is [0.0, 1.0]
     */
    public void setBrightness(double bright){
        
        candle.configBrightnessScalar(bright);
    }

    /**
     * Animates the lights in a full color shifting pattern
     * @param brightness The brightness of LEDs from 0 to 1 as a double value
     * @param speed The speed at which the color shifts among the LEDs from 0 to 1 as a double value
     * @param numLed The number of LEDs controlled by the CANdle as an int value
     */
    public void RainbowAnimate(double brightness, double speed, int numLed){
        RainbowAnimation animation = new RainbowAnimation(brightness, speed, numLed);
        candle.animate(animation);
    }

    /**
     * Turns off the LEDs and stops them from animating
     */
    public void clearAnimate(){
        candle.clearAnimation(0);
    }

    /**
     * Animates the lights in a flowing pattern (one color)
     * @param r R value of RGB color as an int value
     * @param g G value of RGB color as an int value
     * @param b B value of RGB color as an int value
     * @param speed The speed at which the lights flow as a double value
     * @param numLed The number of LEDs controlled by the CANdle as an int value
     * @param direction The direction the flow should move from the starting point using a direction enum (forward or backward)
     * @param offset The offset from the starting point of the flow animation to begin as an int value
     */
    public void FlowAnimate(int r, int g, int b, double speed, int numLed, Direction direction, int offset){

        ColorFlowAnimation animation = new ColorFlowAnimation( r,  g,  b,  0,  speed,  numLed,  direction, offset);
        candle.animate(animation);
    }
    
    /**
     * Animation of lights in a fire-like pattern
     * @param bright Brightness for all LED's controlled, range is 0 to 1 as a double value
     * @param speed The speed at which the lights flow as a double value
     * @param numLed The number of LEDs controlled by the CANdle as an int value
     * @param sparking The rate at which the fire produces a sparking effect as a double from 0 to 1
     * @param cooling The rate at which the fire produces a cooling effect as a double from 0 to 1
     */
    public void FireAnimate(double brightness, double speed, int numLed, double sparking, double cooling){
        FireAnimation animation = new FireAnimation(brightness, speed, numLed, sparking, cooling, false, 8);
        candle.animate(animation);
    }

    /**
     * Animation of lights in a larson pattern (one color)
     * @param r R value of RGB color as an int value
     * @param g G value of RGB color as an int value
     * @param b B value of RGB color as an int value
     * @param numLed The number of LEDs controlled by the CANdle as an int value
     */
    public void LarsonAnimate(int r, int g, int b, int numLed){
        BounceMode mode = BounceMode.Center;
        LarsonAnimation animation = new LarsonAnimation(r, g, b, 0, 0.1, numLed, mode, numLed / 2, 8 );
        candle.animate(animation);
    }

    /**
     * Animation of lights in an Fading pattern (all colors)
     * @param bright Brightness for all LED's controlled, range is 0 to 1 as a double value
     * @param speed The speed at which the lights flow as a double value
     * @param numLed The number of LEDs controlled by the CANdle as an int value
     */
    public void RgbFadeAnimate(double brightness, double speed, int numLed){
        RgbFadeAnimation animation = new RgbFadeAnimation(brightness, speed, numLed, 8);
        candle.animate(animation);
    }

    /**
     * Animation of lights in a fade pattern (one color)
     * @param r R value of RGB color as an int value
     * @param g G value of RGB color as an int value
     * @param b B value of RGB color as an int value
     * @param speed The speed at which the lights flow as a double value
     * @param numLed The number of LEDs controlled by the CANdle as an int value
     */
    public void SingleFadeAnimate(int r, int g, int b, double speed, int numLed){
        SingleFadeAnimation animation = new SingleFadeAnimation(r, g, b, 0, speed, numLed, 8);
        candle.animate(animation);
    }

    /**
     * Animation of lights in a strobe pattern (one color)
     * @param r R value of RGB color as an int value
     * @param g G value of RGB color as an int value
     * @param b B value of RGB color as an int value
     * @param speed The speed at which the lights flow as a double value
     * @param numLed The number of LEDs controlled by the CANdle as an int value
     */
    public void StrobeAnimate(int r, int g, int b, double speed, int numLed){
        StrobeAnimation animation = new StrobeAnimation(r, g, b, 0, speed, numLed, 8);
        candle.animate(animation);
    }

    /**
     * Animation of lights in a twinkling pattern (one color)
     * @param r R value of RGB color as an int value
     * @param g G value of RGB color as an int value
     * @param b B value of RGB color as an int value
     * @param numLed The number of LEDs controlled by the CANdle as an int value
     */
    public void TwinkleAnimate(int r, int g, int b, int numLed){
        TwinklePercent percent = TwinklePercent.Percent100;
        TwinkleAnimation animation = new TwinkleAnimation(r, g, b, 0, 1.0, numLed, percent, 8);
        candle.animate(animation);
    }
    
    /**
     * Sets the color of the LEDs to orange
     * @param numLed The number of LEDs controlled by the CANdle as an int value
     */
    public void setOrange(int numLed){
        setLight(255, 30, 0, numLed);
    }

    /**
     * Sets the color of the LEDs to green
     * @param numLed The number of LEDs controlled by the CANdle as an int value
     */
    public void setGreen(int numLed){
        setLight(0, 255, 0, numLed);
    }
    

}
