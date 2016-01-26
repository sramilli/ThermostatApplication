/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package thermostatapplication.devices;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import thermostatapplication.helper.Pi4jHelper;

/**
 *
 * @author Ste
 */
public class Led {

    private GpioController gpio = GpioFactory.getInstance();
    private GpioPinDigitalOutput iPin;
    
    public Led(int aPin){
        iPin = gpio.provisionDigitalOutputPin(Pi4jHelper.getPin(aPin), "PIN "+aPin, PinState.LOW);
        iPin.setShutdownOptions(true, PinState.LOW);
        System.out.println("Initialized Led on pin "+aPin+".");
    }
    
    public Led(int aPin, boolean aInitialStatusHigh){
        GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(Pi4jHelper.getPin(aPin), "PIN "+aPin, getInitialStatus(aInitialStatusHigh));
        pin.setShutdownOptions(true, getInitialStatus(aInitialStatusHigh));
        System.out.println("Initialized Led on pin "+aPin+".");
    }
    
    public void turnOn(){
        //iPin.high();
        setValue(true);
    }
    
    public void turnOff(){
        //iPin.low();
        setValue(false);
    }
    
    public void setValue(boolean aValue){
        System.out.println("Turn led "+iPin.getName()+ (aValue ? " on." : " off."));
        iPin.setState(aValue);
    }
    
    public void close(){
        if (iPin != null){
            iPin.removeAllListeners();
            //NB!! Removes everything! Every pin!
            if (gpio != null){
                gpio.shutdown();  
            }
        }
    }
    
    private PinState getInitialStatus(boolean aInitialStatusHigh) {
        if (aInitialStatusHigh) return PinState.HIGH;
        else return PinState.LOW;
    }
}
