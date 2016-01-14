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
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataListener;
import static java.lang.Thread.sleep;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Ste
 */
public class Thermostat implements GpioPinListenerDigital, SerialDataListener {

    private Led iStatusLED;
    private Relay iHeaterRelay;
    private Led iGreenLED;
    private Led iYellowLED;
    private Led iRedLED;
    private Led iBlueLED;
    private Button iModeButton;
    private Button iManualTherostat;
    private Controller iController;
    private SMSGateway iSMSGateway;

    public static boolean ON = true;
    public static boolean OFF = false;
    Timer timer;

    public Thermostat(int aModeButtonPinID, int aManualThermostatPinID, int aStatusLEDPinNumber, int aGreenLEDPinNumber, int aYellowLEDPinNumber, int aRedLEDPinNumber, int aBlueLEDPinNumber, int aHeaterRELAYPinNumber) {
        try {
            iStatusLED = new Led(aStatusLEDPinNumber);
            iGreenLED = new Led(aGreenLEDPinNumber);
            iYellowLED = new Led(aYellowLEDPinNumber);
            iRedLED = new Led(aRedLEDPinNumber);
            iBlueLED = new Led(aBlueLEDPinNumber);
            iHeaterRelay = new Relay(aHeaterRELAYPinNumber);
            iModeButton = new Button(aModeButtonPinID);
            iModeButton.setInputListener(this);
            iController = new Controller(iStatusLED, iGreenLED, iYellowLED, iRedLED, iBlueLED, iHeaterRelay);
            iManualTherostat = new Button(aManualThermostatPinID);
            iManualTherostat.setInputListener(this);
            iSMSGateway = new SMSGateway();
            iSMSGateway.initialize(this);
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
        iSMSGateway.sendTextMessageToUser("+46700447531", "This is anooother test");
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

    /*   Keep if i want to eliminate the listener
    public void startPollingIncomingCommands(boolean aDeleteReadMessages, int aSeconds) {
        timer = new Timer();
        //every 30 seconds
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                List<SMS> tSMSs = iSMSGateway.getAllMessages();
                Collections.sort(tSMSs);
                Collections.reverse(tSMSs);
                //print the list
                System.out.println("List of all messages on the modem ordered by date:");
                for (SMS tSMS : tSMSs) {
                    System.out.println(tSMS);
                }
                //check for valid commands
                for (SMS tSMS : tSMSs) {
                    if (tSMS.isDateValid() && tSMS.senderAuthorized() && (CommandParser.parse(tSMS)).isValid()) {
                        System.out.println("Date Valid & User Authorized & Command is valid. Executing: -------> " + tSMS);
                        iController.executeCommand(CommandParser.parse(tSMS));
                        break; //execute only last command
                    } else {
                        System.out.println("SMS discarded: " + tSMS);
                    }
                }
                if (aDeleteReadMessages) {
                    //TODO delete all messages
                    for (SMS tSMS : tSMSs) {
                        System.out.println("Delete message "+tSMS);
                        String tResp = iSMSGateway.deleteMsgAtCertainPosition(tSMS.getPosition());
                        System.out.println(tResp);
                    }
                }
            }
        }, 0, aSeconds * 1000);
    }*/
    
    @Override
     public void dataReceived(SerialDataEvent event) {
         // print out the data received to the console
         //http://www.developershome.com/sms/resultCodes3.asp
         System.out.println("Incoming event arrived from the GSM module!");
         String response = event.getData();
         iSMSGateway.removeListener(this);
         //String response = iSMSGateway.readAnswer();
         System.out.println("");
         if (GSMDataInterpreter.getCommand(response).equals(GSMCommand.MESSAGE_ARRIVED)){
            System.out.println("A new message has arrived!");
            System.out.print("AAAAAThermostat-dataReceived: ---->"+response+"<----");
            waitABit(3000);
            List<SMS> tSMSs = iSMSGateway.getAllMessages();
            iSMSGateway.printAllMessages(tSMSs);
            //check for valid commands
            for (SMS tSMS : tSMSs) {
                CommandType tCommand = CommandParser.parse(tSMS);
                if (tSMS.isDateValid() && tSMS.senderAuthorized() && (tCommand).isActive()) {
                    System.out.println("Date Valid & User Authorized & Command is active. Executing: -------> " + tSMS);
                    if (CommandType.ON.equals(tCommand) || CommandType.OFF.equals(tCommand) || CommandType.MANUAL.equals(tCommand)){
                        iController.executeCommand(tCommand, null);
                    } else if (CommandType.STATUS.equals(tCommand)){
                        iSMSGateway.sendStatusToUser(tSMS.getSender(), this.getStatus());
                    } else if (CommandType.HELP.equals(tCommand)){
                        iSMSGateway.sendHelpMessageToUser(tSMS.getSender());
                    } else if (CommandType.REGISTER_NUMBER.equals(tCommand)){
                        String[] tSplittedStringt = tSMS.getText().split(" ");
                        if (tSplittedStringt.length >= 2){
                            System.out.println("REGISTER_NUMBER splitted string: ["+tSplittedStringt[0]+"] ["+tSplittedStringt[1]+"] ");
                            AuthorizedUsers.addAuthorizedUser(tSplittedStringt[1]);
                        }else {
                            System.out.println("REGISTER_NUMBER command not formatted correctly");
                        }
                    } else if (CommandType.PROGRAM_DAILY.equals(tCommand)){
                       iController.executeCommand(tCommand, tSMS.getText());
                    }
                    break; //execute only last command
                } else {
                    System.out.println("SMS discarded: " + tSMS);
                }
            }
            //delete all messages
            iSMSGateway.deleteAllMessages(tSMSs);
         }
         iSMSGateway.addListener(this);
     }
    
    public void dataArrivingFromGSMModule(String aData){
        System.out.println(aData);
    }

    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
        GpioPin tPin = event.getPin();
        if (tPin == iModeButton.getPin()) {
            if (PinState.HIGH.equals(event.getState())) {
                System.out.println("Thermostat: Mode Button pressed!");
                iController.switchMode();
                System.out.println("Thermostat: Switched to MODE " + iController.getControllerState().getState());
            }

        } else if (tPin == iManualTherostat.getPin()) {
            if (PinState.HIGH.equals(event.getState())) {
                System.out.println("Thermostat: Manual thermostat change ON");
                iController.activateManualThermostat();
            } else if (PinState.LOW.equals(event.getState())) {
                System.out.println("Thermostat: Manual thermostat change OFF");
                iController.deActivateManualThermostat();
            }
        } else {
            System.out.println("Thermostat ERROR! Detected a non registered input change");
        }

    }

    public String getStatus() {
        StringBuffer tResponse = new StringBuffer();
        tResponse.append("Running since: " + Helper.calToString(ThermostatApplication.iRunningSince) + "\n");
        tResponse.append("State: " + iController.getControllerState().getState() + "\n");
        tResponse.append("ProgramDaily: " + iController.getProgramTimes() + "\n");
        tResponse.append("Last Temp read: " + ThermostatApplication.lastTemperatureRead);
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
                waitABit(3000);
                iSMSGateway.stop();
                iSMSGateway = null;
            }
            if (iController != null) {
                iController.close();
                iController = null;
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
    
    private void waitABit(int a) {
        try {
            Thread.sleep(a);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
