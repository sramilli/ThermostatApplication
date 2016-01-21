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

    Timer timerRead = null;
    Timer timerPersister = null;
    String iName;
    TemperatureStore tTemperatureStore;
    
    public static long EVERY_15_SECONDS = 15 * 1000;
    public static long EVERY_3_MINUTES = 3 * 60 * 1000;
    
    public TemperatureReader(String aName){
        timerRead = new Timer();
        iName = aName;
        tTemperatureStore = new TemperatureStore();
    }
    
    public void startReadingTemperatures(){
        Date startMeasureDate = Helper.getNextWholeMinuteDatePlusFiveSec(new Date());
        System.out.println("Start reading temperature at: " + Helper.getDateAsString(startMeasureDate));
        
        //make three measurement per minute (every 15 sec)
        timerRead.scheduleAtFixedRate(new TemperatureReaderTimerTask(iName, tTemperatureStore), startMeasureDate, EVERY_15_SECONDS);
                
        if (ThermostatProperties.PERSIST_TEMPERATURES){
            timerPersister = new Timer();                                                                            
            timerPersister.scheduleAtFixedRate(new TemperaturePersisterTimerTask(tTemperatureStore), Helper.getNextWholeMinuteDate(new Date()), EVERY_3_MINUTES);
        }
    }
    
    public void stop(){
        if (timerRead != null){
            timerRead.cancel();
        }
        if (timerPersister != null){
            timerPersister.cancel();
        }
        if (tTemperatureStore != null){
            tTemperatureStore.cancel();
        }
        
    }

}