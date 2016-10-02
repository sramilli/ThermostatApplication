/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package thermostatapplication.devices;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.event.GpioPinListener;
import com.pi4j.wiringpi.GpioUtil;
import thermostatapplication.helper.Pi4jHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ste
 */
public class Button{
    static Logger logger = LoggerFactory.getLogger(Button.class);

    private GpioController gpio = GpioFactory.getInstance();
    private GpioPinDigitalInput iPin;  //TODO final?
    
    public Button(int aPin){

        iPin = gpio.provisionDigitalInputPin(Pi4jHelper.getPin(aPin));
        System.out.println("-------------------------");
        System.out.println("PullResistance: "+iPin.getPullResistance());
        System.out.println("-------------------------");

        GpioUtil.setEdgeDetection(aPin, GpioUtil.EDGE_RISING);
        iPin.setDebounce(300);
    }
    
    public GpioPinDigitalInput getPin(){
        return iPin;
    }

    public void close() {
        if (iPin != null) {
            iPin.removeAllListeners();
            //NB!! Removes everything! Every pin!
            if (gpio != null){
                //com.pi4j.io.gpio.exception.InvalidPinModeException: Invalid pin mode on pin [GPIO 0]; cannot setState() when pin mode is [input]
                gpio.shutdown();  
            }
        }
    }
    
    public void setInputListener(GpioPinListener aListener){
        iPin.addListener(aListener);
    }

}
