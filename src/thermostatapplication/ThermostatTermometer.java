/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermostatapplication;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Ste
 */
public class ThermostatTermometer {
    
    Timer timer = null;
    String iName;
    TemperatureStore iStoreHandler;
    
    public ThermostatTermometer(String aName, TemperatureStore aStoreHandler){
                timer = new Timer();
                iName = aName;
                iStoreHandler = aStoreHandler;
    }
    
    public void startMeasureTemperature(){
        Date startMeasureDate = Helper.getNextWholeMinuteDatePlusFiveSec(new Date());
        System.out.println("Start reading temperature at: " + Helper.getDateAsString(startMeasureDate));
        //make three measurement per minute (every 15 sec)
        timer.scheduleAtFixedRate(new ThermostatTermometerReaderTimerTask(iName, iStoreHandler), startMeasureDate, 15 * 1000);
    }
    
    public void stop(){
        if (timer != null){
            timer.cancel();
        }
    }

}
