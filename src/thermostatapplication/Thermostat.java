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
import java.util.List;
import java.util.Calendar;
import java.util.Timer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import static java.lang.Thread.sleep;

/**
 *
 * @author Ste
 */
public class Thermostat implements GpioPinListenerDigital {

    private ThermostatState iThermostatState;
        
    private Led iHeaterStatusLed;
    private Relay iHeaterRelay;
    private Led iGreenLED;
    private Led iYellowLED;
    private Led iRedLED;
    private Led iBlueLED;
    private Button iModeButton;
    private Button iManualTherostat;
    
    private MessageHandler iMessageHandler;

    private Timer iTimer;
    
    ThermostatIgnitionShutdownTimerTask iStartTask;
    ThermostatIgnitionShutdownTimerTask iStopTask;
    
    public static long REPEAT_DAILY = 24 * 60 * 60 * 1000;
    public static boolean ON = true;
    public static boolean OFF = false;
    
    public final static String HELP_TEXT_USAGE = 
                "Examples:\r"
                + "1) on\r"
                + "2) off\r"
                + "3) manual\r"
                + "4) status\r"
                + "5) help\r"
                + "6) register +391234512345\r"
                + "7) ProgramDaily 6:15-7:45\r";

    public Thermostat() {
        try {
            iHeaterStatusLed = new Led(ThermostatProperties.GREEN_HEATER_STATUS_LED);
            iGreenLED = new Led(ThermostatProperties.GREEN_STATE_LED);
            iYellowLED = new Led(ThermostatProperties.YELLOW_STATE_LED);
            iRedLED = new Led(ThermostatProperties.RED_STATE_LED);
            iBlueLED = new Led(ThermostatProperties.BLUE_PROGRAM_LED);
            iHeaterRelay = new Relay(ThermostatProperties.HEATER_RELAY);
            iModeButton = new Button(ThermostatProperties.MODE_BUTTON);
            iModeButton.setInputListener(this);
            iManualTherostat = new Button(ThermostatProperties.MANUAL_THERMOSTAT_INPUT);
            iManualTherostat.setInputListener(this);
            
            iMessageHandler = new MessageHandler(this);
            //iSMSGateway.initialize(this);
            
            iTimer = new Timer(true);
            
            iThermostatState = ThermostatStateFactory.createThermostatState(this, State.OFF);
            
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
    


    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
        GpioPin tPin = event.getPin();
        if (tPin == iModeButton.getPin()) {
            if (PinState.HIGH.equals(event.getState())) {
                System.out.println("Thermostat: Mode Button pressed!");
                this.switchMode();
                System.out.println("Thermostat: Switched to MODE " + this.getThermostatState().getState());
            }

        } else if (tPin == iManualTherostat.getPin()) {
            if (PinState.HIGH.equals(event.getState())) {
                System.out.println("Thermostat: Manual thermostat change ON");
                this.activateManualThermostat();
            } else if (PinState.LOW.equals(event.getState())) {
                System.out.println("Thermostat: Manual thermostat change OFF");
                this.deActivateManualThermostat();
            }
        } else {
            System.out.println("Thermostat ERROR! Detected a non registered input change");
        }

    }

    public String getStatus() {
        
        StringBuffer tResponse = new StringBuffer();
        tResponse.append("Running since: " + Helper.calToString(ThermostatApplication.iRunningSince) + "\n");
        tResponse.append("State: " + this.getThermostatState().getState() + "\n");
        tResponse.append("ProgramDaily: " + this.getProgramTimes() + "\n");
        tResponse.append("Last Temp read: " + TemperatureStore.LastTemperatureReadString);
        return tResponse.toString();
    }

    //////////////////////
    // From Controller
    //////////////////////
    
    public void setThermostatState(ThermostatState aThermostatState){
        iThermostatState = aThermostatState;
    }
    
    public ThermostatState getThermostatState(){
        return iThermostatState;
    }
    
    public String getProgramTimes(){
        if (iStartTask == null) return "not active";
        DateFormat sdf = new SimpleDateFormat("HH:mm");
        Calendar tLastStart = Calendar.getInstance();
        tLastStart.setTimeInMillis(iStartTask.scheduledExecutionTime());
        Calendar tLastStop = Calendar.getInstance();
        tLastStop.setTimeInMillis(iStopTask.scheduledExecutionTime());
        return sdf.format(tLastStart.getTime()) + "-" + sdf.format(tLastStop.getTime());
    }
    
    public void turnOn(){
        iThermostatState.turnON();
    }
    
    public void turnOff(){
        iThermostatState.turnOFF();
    }
    
    public void turnOnConditionally(){
        iThermostatState.turnONConditionally();
    }
    
    public void turnOffConditionally(){
        iThermostatState.turnOFFConditionally();
    }
    
    public void setToManual(){
        iThermostatState.setToManual();
    }
    
    public void switchMode() {
        //Controlled manually by pushing Mode button
        System.out.print("Switching mode manually: ");
        iThermostatState.switchState();
    }

    private State setMode(State aMode) {
        //Used via SMS
        if (aMode != State.ON && aMode != State.MANUAL && aMode != State.OFF) {
            System.out.println("Thermostat: setMode error: "+aMode);
            return aMode;
        }
        
        switch (aMode){
            case ON:
                this.turnOn();
                break;
            case OFF:
                this.turnOff();
                break;
            case MANUAL:
                this.setToManual();
        }

        return this.getThermostatState().getState();
    }

    public void activateManualThermostat() {
        iThermostatState.activateManualThermostat();
    }

    public void deActivateManualThermostat() {
        iThermostatState.deActivateManualThermostat();
    }
    
    public void processReceivedCommand(CommandType tCommand, SMS aSMS){
                if (CommandType.ON.equals(tCommand) || CommandType.OFF.equals(tCommand) || CommandType.MANUAL.equals(tCommand)){
                    executeCommand(tCommand, null);
                } else if (CommandType.STATUS.equals(tCommand)){
                    //TODO change to user instead of aSMS
                    //TODO
                    iMessageHandler.sendMessage(new Message(aSMS.getSender(), null, this.getStatus()));
                } else if (CommandType.HELP.equals(tCommand)){
                    iMessageHandler.sendMessage(new Message(aSMS.getSender(), null, HELP_TEXT_USAGE));
                } else if (CommandType.REGISTER_NUMBER.equals(tCommand)){
                    //TODO !!!!!!!!!!!!!!1
                    //FINISH REFACTOR!!!!!!!!1
                    //
                    String[] tSplittedStringt = aSMS.getText().split(" ");
                    if (tSplittedStringt.length >= 2){
                        System.out.println("REGISTER_NUMBER splitted string: ["+tSplittedStringt[0]+"] ["+tSplittedStringt[1]+"] ");
                        //TODO complete with name and email
                        AuthorizedUsers.addAuthorizedUser(tSplittedStringt[1]);
                    }else {
                        System.out.println("REGISTER_NUMBER command not formatted correctly");
                    }
                } else if (CommandType.PROGRAM_DAILY.equals(tCommand)){
                    executeCommand(tCommand, aSMS.getText());
                }
    }
    
            //TODO TODO TODO 
            //anything to refactor in processReceivedCommand and executeCommand???
    
    public void executeCommand(CommandType aCmd, String aText) {
        //used via SMS
        if (aCmd == null) 
            return;
        if (aCmd.equals(CommandType.ON)) {
            this.turnOn();
        } else if (aCmd.equals(CommandType.ON_CONDITIONAL)) {
            this.turnOnConditionally();
        } else if (aCmd.equals(CommandType.MANUAL)) {
            this.setToManual();
        } else if (aCmd.equals(CommandType.OFF)) {
            this.turnOff();
        } else if (aCmd.equals(CommandType.OFF_CONDITIONAL)) {
            this.turnOffConditionally();
        }else if (aCmd.equals(CommandType.PROGRAM_DAILY)){
            //reset the timer (if no valid parameter is specified it will just clear it)
            iTimer = new Timer(true);
            iStartTask = null;
            iStopTask = null;
            this.getLedBlue().turnOff();
            String[] tSplittedStringt = aText.split(" ");
            if (tSplittedStringt.length == 2){
                String timeInterval = tSplittedStringt[1];
                //ex. 6:00-8:30
                String[] tSplittedTime = timeInterval.split("-");
                if (tSplittedTime.length == 2){
                    try {Calendar tNow = Helper.getThisInstant();
                        Helper.printCal("Now", tNow);

                        Calendar startDateParse = Helper.parseTime(tSplittedTime[0]);
                        Helper.printCal("startDateParse", startDateParse);
            
                        Calendar stopDateParse = Helper.parseTime(tSplittedTime[1]);
                        Helper.printCal("stopDateParse", stopDateParse);

                        if (tNow.before(startDateParse)){
                            //schedule ON from today
                        } else {
                            //schedule ON from tomorrow
                            startDateParse.add(Calendar.DAY_OF_MONTH, 1);
                        }
                        Helper.printCal("Scheduling daily Ignition from: ", startDateParse);
                        iStartTask = new ThermostatIgnitionShutdownTimerTask(this, CommandType.ON_CONDITIONAL);
                        iTimer.scheduleAtFixedRate(iStartTask, startDateParse.getTime(), REPEAT_DAILY);
                        
                        if (tNow.before(stopDateParse)){
                            //schedule OFF from today
                        } else {
                            //schedule OFF from tomorrow
                            stopDateParse.add(Calendar.DAY_OF_MONTH, 1);
                        }
                        Helper.printCal("Scheduling daily shutdown from: ", stopDateParse);
                        iStopTask = new ThermostatIgnitionShutdownTimerTask(this, CommandType.OFF_CONDITIONAL);
                        iTimer.scheduleAtFixedRate(iStopTask, stopDateParse.getTime(), REPEAT_DAILY);
                        
                        this.getLedBlue().turnOn();
                        
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                }else{
                    System.out.println("Program daily bad format!");
                }
            }else {
                System.out.println("Program daily bad format!");
            }
        }
        
        
        else{
            System.out.println("Thermostat: Command via SMS not supported! ");
        }

    }

    public Led getHeaterStatusLed() {
        return iHeaterStatusLed;
    }

    public void setHeaterStatusLed(Led aHeaterStatusLed) {
        this.iHeaterStatusLed = aHeaterStatusLed;
    }

    public Relay getHeaterRelay() {
        return iHeaterRelay;
    }

    public void setHeaterRelay(Relay aHeaterRelay) {
        this.iHeaterRelay = aHeaterRelay;
    }

    public Led getLedGreen() {
        return iGreenLED;
    }
    
    public Led getLedBlue() {
        return iBlueLED;
    }

    public void setLedGreen(Led aLedGreen) {
        this.iGreenLED = aLedGreen;
    }

    public Led getLedYellow() {
        return iYellowLED;
    }

    public void setLedYellow(Led aLedYellow) {
        this.iYellowLED = aLedYellow;
    }

    public Led getLedRed() {
        return iRedLED;
    }

    public void setLedRed(Led aLedRed) {
        this.iRedLED = aLedRed;
    }
    
    public void stop() {
        try {
            if (iTimer != null){
                iTimer.cancel();
            }
            if (iHeaterStatusLed != null) {
                System.out.println("Thermostat: Turning off Status LED");
                iHeaterStatusLed.close();
                iHeaterStatusLed = null;
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
}
