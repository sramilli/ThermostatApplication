package thermostatapplication;

/**
 * States: ON, OFF, MANUAL
 */

public interface ControllerState {
    
    void turnON();
    void turnOFF();
    void setToManual();
    void switchState();
    void activateManualThermostat();
    void deActivateManualThermostat();
    State getState();
    
}
