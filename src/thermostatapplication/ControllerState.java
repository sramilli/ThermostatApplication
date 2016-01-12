package thermostatapplication;

/**
 * States: ON, OFF, MANUAL
 */

public interface ControllerState {
    
    void turnON();
    void turnONConditionally();
    void turnOFF();
    void turnOFFConditionally();
    void setToManual();
    void switchState();
    void activateManualThermostat();
    void deActivateManualThermostat();
    State getState();
    
}
