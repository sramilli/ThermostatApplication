/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermostatapplication;

import java.util.Date;
import java.util.TimerTask;

/**
 *
 * @author Ste
 */
public class ThermostatIgnitionShutdownTimerTask extends TimerTask{
    
    Controller iController;
    CommandType iCommandType;
    
    public ThermostatIgnitionShutdownTimerTask(Controller aController, CommandType aCommandType){
        iController = aController;
        iCommandType = aCommandType;
    }

    @Override
    public void run() {
        iController.executeCommand(iCommandType, null);
    }
    
}
