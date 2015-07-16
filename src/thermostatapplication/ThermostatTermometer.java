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
    
    Timer timer;
    public static final String NAME = "ThermostatTermometer";
    
    public ThermostatTermometer(){
                timer = new Timer();
    }
    
    public void startMeasureTemperature(){
        Date startMeasureDate = DateHelper.resetSecMillsAndAddMin(new Date(), 1);
        System.out.println("Start reading remperature at: " + startMeasureDate);
        //make three measurement per minute (every 20 sec)
        timer.scheduleAtFixedRate(new ThermostatTermometerReaderTimerTask(NAME), startMeasureDate, 20 * 1000);
    }
    
    public void stop(){
        if (timer != null){
            timer.cancel();
        }
    }

}
