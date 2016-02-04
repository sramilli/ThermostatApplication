/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermostatapplication;

import thermostatapplication.properties.ThermostatProperties;
import thermostatapplication.helper.Helper;
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
    String iLocation;
    TemperatureStore tTemperatureStore;
    
    public static long EVERY_1_MINUTE = 1 * 60 * 1000;
    //TODO just for testing. do every 5 minutes
    public static long EVERY_5_MINUTES = 5 * 60 * 1000;
    
    public TemperatureReader(String aLocation){
        timerRead = new Timer();
        iLocation = aLocation;
        tTemperatureStore = new TemperatureStore();
    }
    
    public void startReadingTemperatures(){
        Date startMeasureDate = Helper.getNextWholeMinuteDatePlusFiveSec(new Date());
        System.out.println("Start reading temperature at: " + Helper.getDateAsString(startMeasureDate));
        
        timerRead.scheduleAtFixedRate(new TemperatureReaderTimerTask(iLocation, tTemperatureStore), startMeasureDate, EVERY_1_MINUTE);
                
        if (ThermostatProperties.PERSIST_TEMPERATURES){
            timerPersister = new Timer();                                                                            
            timerPersister.scheduleAtFixedRate(new TemperaturePersisterTimerTask(tTemperatureStore), Helper.getNextWholeMinuteDate(new Date()), EVERY_5_MINUTES);
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
