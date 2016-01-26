/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermostatapplication;

import thermostatapplication.entity.TemperatureMeasure;
import thermostatapplication.helper.Helper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

/**
 *
 * @author Ste
 */
public class TemperatureStore {
    Collection<TemperatureMeasure> iTemperatures;
    TemperatureMeasure iLastTemperatureMeasure;
    
    //TODO temporary solution
    public static String LastTemperatureReadString = "";

    public TemperatureStore(){
        iTemperatures = new ArrayList<>();
        iLastTemperatureMeasure = null;
    }
    
    public int size(){
        if (iTemperatures != null){
            return iTemperatures.size();
        }
        return 0;
    }
    
    void storeTemperature(TemperatureMeasure aTemperatureMeasure) {
        iTemperatures.add(aTemperatureMeasure);
        System.out.println("Temperature added to store. Total temperatures: "+iTemperatures.size());
    }

    Collection<TemperatureMeasure> getTemperatures() {
        return iTemperatures;
    }
    
    public void cancel(){
        iTemperatures = null;
    }

    public void setLastTemperatureRead(TemperatureMeasure aTemperatureMeasure) {
        iLastTemperatureMeasure = aTemperatureMeasure;
        //TODO temporary solution
        LastTemperatureReadString = getLastTemperatureReadInString();
    }
    
    public TemperatureMeasure getLastTemperatureRead() {
        return iLastTemperatureMeasure;
    }
    
    public String getLastTemperatureReadInString() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(iLastTemperatureMeasure.getDate());
        return Helper.getTempAsString(iLastTemperatureMeasure.getTemp()) + " C " + Helper.calToString(cal);
    }
    
}

