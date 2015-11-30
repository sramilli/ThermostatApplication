/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermostatapplication;

/**
 *
 * @author Ste
 */
public class ControllerStateFactory {
    
    public static ControllerState createControllerState(Controller aController, State aState){
        
        switch (aState){
            case ON: 
                return new ControllerStateOn(aController);
            case OFF:
                return new ControllerStateOff(aController);
            case MANUAL:
                return new ControllerStateManual(aController);
            default:
                return null;
        }
    }
}
