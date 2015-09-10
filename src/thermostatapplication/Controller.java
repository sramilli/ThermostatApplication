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

    private Status iStatus;  // on, off, manual
    private Led iHeaterStatus;
    private Relay iHeaterRelay;
    private Led iLedGreen;
    private Led iLedYellow;
    private Led iLedRed;
    private Timer iTimer;

    public static boolean ON = true;
    public static boolean OFF = false;

    public Controller(Led aHeaterStatus, Led aGreen, Led aYellow, Led aRed, Relay aRelay) {
        iStatus = Status.OFF;
        iHeaterStatus = aHeaterStatus;
        iHeaterRelay = aRelay;
        iLedGreen = aGreen;
        iLedYellow = aYellow;
        iLedRed = aRed;
        iTimer = new Timer(true);
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
    
    public void executeCommand(CommandType aCmd, String aText) {
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
        } else if (aCmd.equals(CommandType.MANUAL)) {
            if (!iStatus.equals(Status.MANUAL)){
                System.out.println("SMS received: Turn to Manual");
                this.setMode(Status.MANUAL);
            }else {
                System.out.println("SMS received: command not executed, already on Manual");
            }
        } else if (aCmd.equals(CommandType.OFF)) {
            if (!iStatus.equals(Status.OFF)){
                System.out.println("SMS received: Off");
                this.setMode(Status.OFF);
            }else {
                System.out.println("SMS received: command not executed, already Off");
            }
        } else if (aCmd.equals(CommandType.PROGRAM_DAILY)){
            //clear old program
            //iTimer.cancel(); // TODO
            
            String[] tSplittedStringt = aText.split(" ");
            if (tSplittedStringt.length == 2){
                String timeInterval = tSplittedStringt[1];
                //ex. 6:00-8:30
                String[] tSplittedTime = timeInterval.split("-");
                if (tSplittedTime.length == 2){
                    try {Calendar tNow = Helper.getThisInstant();
                        DateFormat formatter = new SimpleDateFormat("HH:mm");
                        Date startDate = formatter.parse(tSplittedTime[0]);
                        System.out.println("Start date: "+startDate);
                        startDate.setTime(startDate.getTime()+Helper.getBeginningOfDay().getTimeInMillis());
                        System.out.println("Start date: "+startDate);
                        Date stopDate = formatter.parse(tSplittedTime[1]);
                        System.out.println("Stop date: "+stopDate);
                        stopDate.setTime(stopDate.getTime()+Helper.getBeginningOfDay().getTimeInMillis());
                        System.out.println("Stop date: "+stopDate);
                        System.out.println("Beginning of day: "+new Date(Helper.getBeginningOfDay().getTimeInMillis()));
                        System.out.println("The first day of PC: "+new Date(Helper.getOneDay()));
                        //Calendar start = Calendar.getInstance();
                        //start.setTime(startDate);
                            //System.out.println("Start: "+start.getTime());
                            //int hourStart = start.get(Calendar.HOUR_OF_DAY);
                            //int minuteStart = start.get(Calendar.MINUTE);
                        //Calendar stop = Calendar.getInstance();
                        //stop.setTime(stopDate);
                            //System.out.println("Stop: "+stop.getTime());
                            //int hourStop = stop.get(Calendar.HOUR_OF_DAY);
                            //int minuteStop = stop.get(Calendar.MINUTE);
                        //System.out.println("Ready to schedule for: "+hourStart+":"+minuteStart+" - "+hourStop+":"+minuteStop);
                        System.out.println("Start date: "+startDate);
                        System.out.println("Stop date: "+stopDate);
                        
                        if (tNow.before(startDate)){
                            //schedule ON from today
                        } else {
                            //schedule ON from tomorrow
                            startDate.setTime(startDate.getTime()+Helper.getOneDay());
                        }
                        System.out.println("Scheduling daily Ignition from: "+startDate);
                        iTimer.scheduleAtFixedRate(new ThermostatIgnitionShutdownTimerTask(this, CommandType.ON), startDate, 24 * 60 * 60 * 1000);
                        
                        if (tNow.before(stopDate)){
                            //schedule OFF from today
                        } else {
                            //schedule OFF from tomorrow
                            stopDate.setTime(stopDate.getTime()+Helper.getOneDay());
                        }
                        System.out.println("Scheduling daily shutdown from: "+stopDate);
                        iTimer.scheduleAtFixedRate(new ThermostatIgnitionShutdownTimerTask(this, CommandType.OFF), stopDate, 24 * 60 * 60 * 1000);
                        
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

    public void close(){
        if (iTimer != null){
            iTimer.cancel();
        }
    }
}
