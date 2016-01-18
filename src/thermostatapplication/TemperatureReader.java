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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 *
 * @author Ste
 */
public class TemperatureReader {
    
    Collection<TemperatureMeasure> iTemperatures;
    Timer timerRead = null;
    Timer timer = null;
    String iName;
    
    public static long EVERY_15_SECONDS = 15 * 1000;
    public static long EVERY_3_MINUTES = 3 * 60 * 1000;
    
    public TemperatureReader(String aName){
        iTemperatures = new ArrayList<>();
        timerRead = new Timer();
        iName = aName;
    }
    
    public void startReadingTemperatures(){
        Date startMeasureDate = Helper.getNextWholeMinuteDatePlusFiveSec(new Date());
        System.out.println("Start reading temperature at: " + Helper.getDateAsString(startMeasureDate));
        
        //make three measurement per minute (every 15 sec)
        timerRead.scheduleAtFixedRate(new ThermostatTermometerReaderTimerTask(iName, this), startMeasureDate, EVERY_15_SECONDS);
                
        if (ThermostatProperties.STORE_TEMPERATURES){
            timer = new Timer();                                                                            
            timer.scheduleAtFixedRate(new TemperatureStoreTimerTask(this.getCollection()), Helper.getNextWholeMinuteDate(new Date()), EVERY_3_MINUTES);
        }
    }
    
    public int size(){
        if (iTemperatures != null){
            return iTemperatures.size();
        }
        return 9999;
    }
    
    void storeTemperature(TemperatureMeasure aTemperatureMeasure) {
        iTemperatures.add(aTemperatureMeasure);
        System.out.println("Temperature added to store. Total temperatures: "+iTemperatures.size());
    }

    Collection<TemperatureMeasure> getCollection() {
        return iTemperatures;
    }
    
    public void stop(){
        if (timerRead != null){
            timerRead.cancel();
        }
        if (timer != null){
            timer.cancel();
        }
    }

}
