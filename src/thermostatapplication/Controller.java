/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermostatapplication;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

/**
 *
 * @author Ste
 */
class Controller {
    
    private ControllerState iControllerState;
    //private State iState;  // on, off, manual
    private Led iHeaterStatusLed;
    private Relay iHeaterRelay;
    private Led iLedGreen;
    private Led iLedYellow;
    private Led iLedRed;
    private Led iLedBlue;
    private Timer iTimer;
    
    ThermostatIgnitionShutdownTimerTask iStartTask;
    ThermostatIgnitionShutdownTimerTask iStopTask;

    public static boolean ON = true;
    public static boolean OFF = false;
    
    public static long REPEAT_DAILY = 24 * 60 * 60 * 1000;

    public Controller(Led aHeaterStatus, Led aGreen, Led aYellow, Led aRed, Led aBlue, Relay aRelay) {

        //iState = State.OFF;
        iHeaterStatusLed = aHeaterStatus;
        iHeaterRelay = aRelay;
        iLedGreen = aGreen;
        iLedYellow = aYellow;
        iLedRed = aRed;
        iLedBlue = aBlue;
        iTimer = new Timer(true);
        //activateOutput();
        //initializing state
        iControllerState = ControllerStateFactory.createControllerState(this, State.OFF);
    }
    
    public void setControllerState(ControllerState aControllerState){
        iControllerState = aControllerState;
    }
    
    public ControllerState getControllerState(){
        return iControllerState;
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
        iControllerState.turnON();
    }
    
    public void turnOff(){
        iControllerState.turnOFF();
    }
    
    public void turnOnConditionally(){
        iControllerState.turnONConditionally();
    }
    
    public void turnOffConditionally(){
        iControllerState.turnOFFConditionally();
    }
    
    public void setToManual(){
        iControllerState.setToManual();
    }
    
    public void switchMode() {
        //Controlled manually by pushing Mode button
        System.out.print("Switching mode manually: ");
        /*switch (iState){
            case ON:
                iState = State.MANUAL;
                break;
            case MANUAL:
                iState = State.OFF;
                break;
            case OFF:
                iState = State.ON;
        }
        System.out.println(" Mode"+iState);
        */
        //TODO delete the above!!!!!!!!!!!!
        iControllerState.switchState();
        
        //activateOutput();
    }

    private State setMode(State aMode) {
        //Used via SMS
        if (aMode != State.ON && aMode != State.MANUAL && aMode != State.OFF) {
            System.out.println("Controller: setMode error: "+aMode);
            return aMode;
        }
        //iState = aMode;
        
        //TODO delete the line above!!!!!!!!!!!1
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
        
        //activateOutput();
        return this.getControllerState().getState();
    }

    /*public State getState() {
        return iState;
    }*/

    /*private void activateOutput() {
        try {
            switch (iState) {
                case ON:
                    //TODO finish this. write it in each class
                    System.out.println("Switching Thermostat to On");
                    iHeaterStatusLed.turnOn();
                    iHeaterRelay.turnOn();
                    iLedGreen.turnOn();
                    iLedYellow.turnOff();
                    iLedRed.turnOff();
                    break;
                case MANUAL:
                    System.out.println("Switching Thermostat to Manual");
                    iHeaterStatusLed.turnOff();
                    iHeaterRelay.turnOff();
                    iLedGreen.turnOff();
                    iLedYellow.turnOn();
                    iLedRed.turnOff();
                    break;
                case OFF:
                    System.out.println("Switching Thermostat to Off");
                    iHeaterStatusLed.turnOff();
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
    }*/
    
    //TODO fortsätt härifrån med refactoring

    public void activateManualThermostat() {
        /*if (State.MANUAL.equals(iState)) {
            System.out.println("Manual Thermostate: On");
            iHeaterStatusLed.turnOn();
            iHeaterRelay.turnOn();
        }*/
        iControllerState.activateManualThermostat();
    }

    public void deActivateManualThermostat() {
        /*if (State.MANUAL.equals(iState)) {
            System.out.println("Manual Thermostat: Off");
            iHeaterStatusLed.turnOff();
            iHeaterRelay.turnOff();
        }*/
        iControllerState.deActivateManualThermostat();
    }
    
    public void executeCommand(CommandType aCmd, String aText) {
        //used via SMS
        if (aCmd == null) 
            return;
        if (aCmd.equals(CommandType.ON)) {
            /*if (!iState.equals(State.ON)){
                System.out.println("SMS received: Turn On Heater");
                this.setMode(State.ON);
            }else {
                System.out.println("SMS received: command not executed, already On");
            }*/
            this.turnOn();
        } else if (aCmd.equals(CommandType.ON_CONDITIONAL)) {
            this.turnOnConditionally();
        } else if (aCmd.equals(CommandType.MANUAL)) {
            /*if (!iState.equals(State.MANUAL)){
                System.out.println("SMS received: Turn to Manual");
                this.setMode(State.MANUAL);
            }else {
                System.out.println("SMS received: command not executed, already on Manual");
            }*/
            this.setToManual();
        } else if (aCmd.equals(CommandType.OFF)) {
            /*if (!iState.equals(State.OFF)){
                System.out.println("SMS received: Off");
                this.setMode(State.OFF);
            }else {
                System.out.println("SMS received: command not executed, already Off");
            }*/
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
                        //Logger.getLogger(Interpreter.class.getName()).log(Level.SEVERE, null, ex);
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
            System.out.println("Controller: Command via SMS not supported! ");
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
        return iLedGreen;
    }
    
    public Led getLedBlue() {
        return iLedBlue;
    }

    public void setLedGreen(Led aLedGreen) {
        this.iLedGreen = aLedGreen;
    }

    public Led getLedYellow() {
        return iLedYellow;
    }

    public void setLedYellow(Led aLedYellow) {
        this.iLedYellow = aLedYellow;
    }

    public Led getLedRed() {
        return iLedRed;
    }

    public void setLedRed(Led aLedRed) {
        this.iLedRed = aLedRed;
    }
    
    public void close(){
        if (iTimer != null){
            iTimer.cancel();
        }
    }
}
