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
class ControllerStateManual implements ControllerState {
    
    Controller iController;

    public ControllerStateManual(Controller aController) {
        iController = aController;
        /*
            System.out.println("Switching Thermostat to Manual");
            iHeaterStatusLed.turnOff();
            iHeaterRelay.turnOff();
            iLedGreen.turnOff();
            iLedYellow.turnOn();
            iLedRed.turnOff();
        */
        System.out.println("Switching Thermostat to Manual");
        //TODO . Correct the initial status
        iController.getHeaterStatusLed().turnOff();
        iController.getHeaterRelay().turnOff();
        
        iController.getLedGreen().turnOff();
        iController.getLedYellow().turnOn();
        iController.getLedRed().turnOff();
    }

    @Override
    public void turnON() {
        System.out.println("Turning on");
        iController.setControllerState(ControllerStateFactory.createControllerState(iController, State.ON));
    }

    @Override
    public void turnOFF() {
        System.out.println("Turning off");
        iController.setControllerState(ControllerStateFactory.createControllerState(iController, State.OFF));
    }

    @Override
    public void setToManual() {
        System.out.println("Doing nothing, already in manual mode");
    }
    
    @Override
    public void switchState() {
        System.out.println("Switching state to OFF");
        iController.setControllerState(ControllerStateFactory.createControllerState(iController, State.OFF));
    }
    
    @Override
    public void activateManualThermostat() {
        System.out.println("Switching on manual thermostat");
        iController.getHeaterStatusLed().turnOn();
        iController.getHeaterRelay().turnOn();
    }

    @Override
    public void deActivateManualThermostat() {
        System.out.println("Switching off manual thermostat");
        iController.getHeaterStatusLed().turnOff();
        iController.getHeaterRelay().turnOff();
    }

    @Override
    public State getState() {
        return State.MANUAL;
    }

    @Override
    public void turnONConditionally() {
        System.out.println("Doing nothing, turnONConditionally received while in manual mode");
    }

    @Override
    public void turnOFFConditionally() {
        System.out.println("Doing nothing, turnOFFConditionally received while in manual mode");
    }
    
}
