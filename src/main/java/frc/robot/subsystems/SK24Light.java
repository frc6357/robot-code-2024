package frc.robot.subsystems;

import com.ctre.phoenix.led.CANdle;
import com.ctre.phoenix.led.CANdle.LEDStripType;
import com.ctre.phoenix.led.CANdleConfiguration;
import com.ctre.phoenix.led.ColorFlowAnimation;
import com.ctre.phoenix.led.ColorFlowAnimation.Direction;
import com.ctre.phoenix.led.FireAnimation;
import com.ctre.phoenix.led.LarsonAnimation;
import com.ctre.phoenix.led.LarsonAnimation.BounceMode;
import com.ctre.phoenix.led.TwinkleAnimation.TwinklePercent;
import com.ctre.phoenix.led.RainbowAnimation;
import com.ctre.phoenix.led.RgbFadeAnimation;
import com.ctre.phoenix.led.SingleFadeAnimation;
import com.ctre.phoenix.led.StrobeAnimation;
import com.ctre.phoenix.led.TwinkleAnimation;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SK24Light extends SubsystemBase{
    CANdle candle;
    
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
        SmartDashboard.putNumber("RED", red);
        candle.setLEDs(red, green, blue, 0, 8, numLed); 
        

        
    }

    /**
     * 
     * @param bright Brightness for all LED's controlled, range is [0.0, 1.0]
     */
    public void setBrightness(double bright){
        
        candle.configBrightnessScalar(bright);
    }

    public void RainbowAnimate(double brightness, double speed, int numLed){
        RainbowAnimation animation = new RainbowAnimation(brightness, speed, numLed);
        candle.animate(animation);
    }

    public void clearAnimate(){
        candle.clearAnimation(0);
    }

    public void FlowAnimate(int r, int g, int b, double speed, int numLed, Direction direction, int offset){

        ColorFlowAnimation animation = new ColorFlowAnimation( r,  g,  b,  0,  speed,  numLed,  direction, offset);
        candle.animate(animation);
    }

    public void FireAnimate(double brightness, double speed, int numLed, double sparking, double cooling){
        FireAnimation animation = new FireAnimation(brightness, speed, numLed, sparking, cooling, false, 8);
        candle.animate(animation);
    }

    public void LarsonAnimate(int r, int g, int b, int numLed){
        BounceMode mode = BounceMode.Center;
        LarsonAnimation animation = new LarsonAnimation(r, g, b, 0, 0.1, numLed, mode, numLed / 2, 8 );
        candle.animate(animation);
    }

    public void RgbFadeAnimate(double brightness, double speed, int numLed){
        RgbFadeAnimation animation = new RgbFadeAnimation(brightness, speed, numLed, 8);
        candle.animate(animation);
    }

    public void SingleFadeAnimate(int r, int g, int b, double speed, int numLed){
        SingleFadeAnimation animation = new SingleFadeAnimation(r, g, b, 0, speed, numLed, 8);
        candle.animate(animation);
    }

    public void StrobeAnimate(int r, int g, int b, double speed, int numLed){
        StrobeAnimation animation = new StrobeAnimation(r, g, b, 0, speed, numLed, 8);
        candle.animate(animation);
    }

    public void TwinkleAnimate(int r, int g, int b, int numLed){
        TwinklePercent percent = TwinklePercent.Percent100;
        TwinkleAnimation animation = new TwinkleAnimation(r, g, b, 0, 1.0, numLed, percent, 8);
        candle.animate(animation);
    }
    
    public void setOrange(int numLed){
        setLight(255, 30, 0, numLed);
    }

    public void setGreen(int numLed){
        setLight(0, 255, 0, numLed);
    }
    

}
