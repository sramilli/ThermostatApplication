/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermostatapplication;

import java.io.IOException;

/**
 *
 * @author Ste
 */
class Controller {

    private Status iStatus;  // on, off, manual
    private Led iHeaterStatus;
    private Relay iHeaterRelay;
    private Led iLedGreen;
    private Led iLedYellow;
    private Led iLedRed;

    public static boolean ON = true;
    public static boolean OFF = false;

    public Controller(Led aHeaterStatus, Led aGreen, Led aYellow, Led aRed, Relay aRelay) {
        iStatus = Status.OFF;
        iHeaterStatus = aHeaterStatus;
        iHeaterRelay = aRelay;
        iLedGreen = aGreen;
        iLedYellow = aYellow;
        iLedRed = aRed;
        activateOutput();
    }

    public void switchMode() {
        //Controlled manually by pushing Mode button
        System.out.print("Switching mode manually: ");
        switch (iStatus){
            case ON:
                iStatus = Status.MANUAL;
                break;
            case MANUAL:
                iStatus = Status.OFF;
                break;
            case OFF:
                iStatus = Status.ON;
        }
        System.out.println(" Mode"+iStatus);
        activateOutput();
    }

    private Status setMode(Status aMode) {
        //Used via SMS
        if (aMode != Status.ON && aMode != Status.MANUAL && aMode != Status.OFF) {
            System.out.println("Controller: setMode error: "+aMode);
            return aMode;
        }
        iStatus = aMode;
        activateOutput();
        return iStatus;
    }

    public Status getState() {
        return iStatus;
    }

    private void activateOutput() {
        try {
            switch (iStatus) {
                case ON:
                    System.out.println("Switching Thermostat to On");
                    iHeaterStatus.turnOn();
                    iHeaterRelay.turnOn();
                    iLedGreen.turnOn();
                    iLedYellow.turnOff();
                    iLedRed.turnOff();
                    break;
                case MANUAL:
                    System.out.println("Switching Thermostat to Manual");
                    iHeaterStatus.turnOff();
                    iHeaterRelay.turnOff();
                    iLedGreen.turnOff();
                    iLedYellow.turnOn();
                    iLedRed.turnOff();
                    break;
                case OFF:
                    System.out.println("Switching Thermostat to Off");
                    iHeaterStatus.turnOff();
                    iHeaterRelay.turnOff();
                    iLedGreen.turnOff();
                    iLedYellow.turnOff();
                    iLedRed.turnOn();
                    break;
                default:
                    System.out.println("Controller switching: This should never happen");
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public void activateManualThermostat() {
        if (Status.MANUAL.equals(iStatus)) {
            System.out.println("Manual Thermostate: On");
            iHeaterStatus.turnOn();
            iHeaterRelay.turnOn();
        }
    }

    public void deActivateManualThermostat() {
        if (Status.MANUAL.equals(iStatus)) {
            System.out.println("Manual Thermostat: Off");
            iHeaterStatus.turnOff();
            iHeaterRelay.turnOff();
        }
    }
    
    public void executeCommand(CommandType aCmd) {
        //used via SMS
        if (aCmd == null) 
            return;
        if (aCmd.equals(CommandType.ON)) {
            if (!iStatus.equals(Status.ON)){
                System.out.println("SMS received: Turn On Heater");
                this.setMode(Status.ON);
            }else {
                System.out.println("SMS received: command not executed, already On");
            }
        }
        else if (aCmd.equals(CommandType.MANUAL)) {
            if (!iStatus.equals(Status.MANUAL)){
                System.out.println("SMS received: Turn to Manual");
                this.setMode(Status.MANUAL);
            }else {
                System.out.println("SMS received: command not executed, already on Manual");
            }
        }
        else if (aCmd.equals(CommandType.OFF)) {
            if (!iStatus.equals(Status.OFF)){
                System.out.println("SMS received: Off");
                this.setMode(Status.OFF);
            }else {
                System.out.println("SMS received: command not executed, already Off");
            }
        }
        else{
            System.out.println("Controller: Command via SMS not supported! ");
        }

    }

}
