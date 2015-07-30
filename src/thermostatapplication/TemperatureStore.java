/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermostatapplication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Timer;

/**
 *
 * @author Ste
 */
public class TemperatureStore {
    
    Collection<TemperatureMeasure> iTemperatures = new ArrayList<>();
    
    Timer timer = null;
    
    public TemperatureStore(){
        timer = new Timer();                                                                            
        timer.scheduleAtFixedRate(new TemperatureStoreTimerTask(this.getCollection()), Helper.getNextWholeMinuteDate(new Date()), 3 * 60 * 1000); //every three minutes
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
        if (timer != null){
            timer.cancel();
        }
    }

    
}
