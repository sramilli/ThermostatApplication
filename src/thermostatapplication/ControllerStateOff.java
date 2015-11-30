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
class ControllerStateOff implements ControllerState {
    
    Controller iController;

    public ControllerStateOff(Controller aController) {
        iController = aController;
        /*
            System.out.println("Switching Thermostat to Off");
            iHeaterStatusLed.turnOff();
            iHeaterRelay.turnOff();
            iLedGreen.turnOff();
            iLedYellow.turnOff();
            iLedRed.turnOn();
        */
        System.out.println("Switching Thermostat to Off");
        iController.getHeaterStatusLed().turnOff();
        iController.getHeaterRelay().turnOff();
        iController.getLedGreen().turnOff();
        iController.getLedYellow().turnOff();
        iController.getLedRed().turnOn();
        
    }

    @Override
    public void turnON() {
        System.out.println("Turning on");
        iController.setControllerState(ControllerStateFactory.createControllerState(iController, State.ON));
    }

    @Override
    public void turnOFF() {
        System.out.println("Doing nothing, already OFF");
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
        System.out.println("Switching on manual thermostat. Doing nothing because thermostat OFF");
    }

    @Override
    public void deActivateManualThermostat() {
        System.out.println("Switching off manual thermostat. Doing nothing because OFF");
    }
    
    @Override
    public State getState() {
        return State.OFF;
    }
}
