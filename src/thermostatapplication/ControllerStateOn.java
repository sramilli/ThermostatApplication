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
class ControllerStateOn implements ControllerState {
    
    Controller iController;

    public ControllerStateOn(Controller aController) {
        iController = aController;
        /*
            System.out.println("Switching Thermostat to On");
            iHeaterStatusLed.turnOn();
            iHeaterRelay.turnOn();
            iLedGreen.turnOn();
            iLedYellow.turnOff();
            iLedRed.turnOff();
        */
        System.out.println("Switching Thermostat to On");
        iController.getHeaterStatusLed().turnOn();
        iController.getHeaterRelay().turnOn();
        iController.getLedGreen().turnOn();
        iController.getLedYellow().turnOff();
        iController.getLedRed().turnOff();
    }

    @Override
    public void turnON() {
        System.out.println("Doing nothing, already ON");
    }

    @Override
    public void turnOFF() {
        System.out.println("Turning off");
        iController.setControllerState(ControllerStateFactory.createControllerState(iController, State.OFF));
    }

    @Override
    public void setToManual() {
        System.out.println("Setting to manual");
        iController.setControllerState(ControllerStateFactory.createControllerState(iController, State.MANUAL));
    }

    @Override
    public void switchState() {
        System.out.println("Switching state to manual");
        iController.setControllerState(ControllerStateFactory.createControllerState(iController, State.MANUAL));
    }
    
    @Override
    public void activateManualThermostat() {
        System.out.println("Switching on manual thermostat. Doing nothing because thermostat ON");
    }

    @Override
    public void deActivateManualThermostat() {
        System.out.println("Switching off manual thermostat. Doing nothing because ON");
    }
    
    @Override
    public State getState() {
        return State.ON;
    }
    
    @Override
    public void turnONConditionally() {
        turnON();
    }

    @Override
    public void turnOFFConditionally() {
        turnOFF();
    }
}
