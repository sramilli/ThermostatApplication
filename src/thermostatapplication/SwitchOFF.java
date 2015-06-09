/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package thermostatapplication;

import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 *
 * @author Ste
 */
public class SwitchOFF extends Button implements GpioPinListenerDigital{
    
    private boolean iTerminateApp = false;
    
    public SwitchOFF(int aPin){
        super(aPin);
        super.getPin().addListener(this);
    }
    
    public boolean terminateApp(){
        return iTerminateApp;
    }
    
    //TODO do i need these
    //and getPin() ?
    public void close(){
        super.getPin().removeAllListeners();
        super.close();
    }

    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
        if (PinState.HIGH.equals(event.getState())){
            System.out.println("Switch OFF detected!! ");
            iTerminateApp = true;
        }
    }
    
}
