/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermostatapplication;

import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import static java.lang.Thread.sleep;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Ste
 */
public class Thermostat implements GpioPinListenerDigital {

    private Led iStatusLED;
    private Relay iHeaterRelay;
    private Led iGreenLED;
    private Led iYellowLED;
    private Led iRedLED;
    private Button iModeButton;
    private Button iManualTherostat;
    private Controller iController;
    private SMSGateway iSMSGateway;

    public static boolean ON = true;
    public static boolean OFF = false;
    Timer timer;

    public Thermostat(int aModeButtonPinID, int aManualThermostatPinID, int aStatusLEDPinNumber, int aGreenLEDPinNumber, int aYellowLEDPinNumber, int aRedLEDPinNumber, int aHeaterRELAYPinNumber) {
        try {
            iStatusLED = new Led(aStatusLEDPinNumber);
            iGreenLED = new Led(aGreenLEDPinNumber);
            iYellowLED = new Led(aYellowLEDPinNumber);
            iRedLED = new Led(aRedLEDPinNumber);
            iHeaterRelay = new Relay(aHeaterRELAYPinNumber);
            iModeButton = new Button(aModeButtonPinID);
            iModeButton.setInputListener(this);
            iController = new Controller(iStatusLED, iGreenLED, iYellowLED, iRedLED, iHeaterRelay);
            iManualTherostat = new Button(aManualThermostatPinID);
            iManualTherostat.setInputListener(this);
            iSMSGateway = new SMSGateway();
            iSMSGateway.initialize();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    public void testRelay() {
        for (int i = 1; i < 8; i++) {
            try {
                System.out.println("Turning on " + i);
                iHeaterRelay.turnOn();
                sleep(500);
                System.out.println("Turning off " + i);
                iHeaterRelay.turnOff();
                sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void testSendSMS() {
        iSMSGateway.sendTextAndReadWithoutListenerTEST("This is anooother test");
    }

    public void testLoopingAT() {
        iSMSGateway.testLoopingAT();
    }

    public String testReadAllMessagesRaw() {
        return iSMSGateway.readAllMessagesRaw();
    }

    public void testReadAllMessages() {
        for (SMS tSMS : iSMSGateway.getAllMessages()) {
            System.out.println(tSMS);
        }
    }

    public void testReadAllMessagesOneByOne() {
        for (SMS tSMS : iSMSGateway.getAllMessages()) {
            System.out.println(iSMSGateway.readMsgAtCertainPosition(tSMS.getPosition()));
        }
    }

    public void startPollingIncomingCommands(boolean aDeleteReadMessages, int aSeconds) {
        timer = new Timer();
        //every 30 seconds
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                List<SMS> tSMSs = iSMSGateway.getAllMessages();
                Collections.sort(tSMSs);
                Collections.reverse(tSMSs);
                System.out.println("List of all messages on the modem ordered by date:");
                for (SMS tSMS : tSMSs) {
                    System.out.println(tSMS);
                }
                for (SMS tSMS : tSMSs) {
                    if (tSMS.isDateValid() && tSMS.senderAuthorized()) {
                        System.out.println("SMS Valid & Authorized: -------> " + tSMS);
                        iController.executeCommand(tSMS);
                        if (aDeleteReadMessages) {
                            //TODO delete all messages
                        }
                        
                        break; //execute only last command
                    } else {
                        System.out.println("SMS discarded: " + tSMS);
                    }
                }
                //TODO and then erase every SMS
            }
        }, 0, aSeconds * 1000);
    }

    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
        GpioPin tPin = event.getPin();
        if (tPin == iModeButton.getPin()) {
            if (PinState.HIGH.equals(event.getState())){
                System.out.println("Thermostat: Mode Button pressed!");
                iController.switchMode();
                System.out.println("Thermostat: Switched to MODE " + iController.getState());
            }
            


        } else if (tPin == iManualTherostat.getPin()) {
            if (PinState.HIGH.equals(event.getState())){
               System.out.println("Thermostat: Manual thermostat change ON"); 
               iController.activateManualThermostat();
            } else if (PinState.LOW.equals(event.getState())){
               System.out.println("Thermostat: Manual thermostat change OFF"); 
               iController.deActivateManualThermostat();
            }
        } else {
            System.out.println("Thermostat ERROR! Detected a non registered input change");
        }

    }

    public String getStatus() {
        StringBuffer tResponse = new StringBuffer();
        tResponse.append("Running since: " + ThermostatApplication.iRunningSince + "\n");
        tResponse.append("State: " + iController.getState() + "\n");
        return tResponse.toString();
    }

    public void stop() {
        try {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            if (iStatusLED != null) {
                System.out.println("Thermostat: Turning off Status LED");
                iStatusLED.close();
                iStatusLED = null;
            }
            if (iHeaterRelay != null) {
                System.out.println("Thermostat: Turning off Relay");
                iHeaterRelay.close();
                iHeaterRelay = null;
            }
            if (iGreenLED != null) {
                System.out.println("Thermostat: Turning off green LED");
                iGreenLED.close();
                iGreenLED = null;
            }
            if (iYellowLED != null) {
                System.out.println("Thermostat: Turning off yellow LED");
                iYellowLED.close();
                iYellowLED = null;
            }
            if (iRedLED != null) {
                System.out.println("Thermostat: Turning off red LED");
                iRedLED.close();
                iRedLED = null;
            }
            if (iModeButton != null) {
                System.out.println("Thermostat: Turning off Mode Button");
                iModeButton.setInputListener(null);
                iModeButton.close();
                iModeButton = null;
            }
            if (iManualTherostat != null) {
                System.out.println("Thermostat: Turning off manual thermostat input");
                iManualTherostat.setInputListener(null);
                iManualTherostat.close();
                iManualTherostat = null;
            }
            if (iSMSGateway != null) {
                System.out.println("Thermostat: Turning off SMSGateway");
                iSMSGateway.stop();
                iSMSGateway = null;
            }
            if (iController != null) {
                iController = null;
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
}
