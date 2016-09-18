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
    private static TemperatureStore iInstance = null;
    Collection<TemperatureMeasure> iTemperatures;
    TemperatureMeasure iLastTemperatureMeasure;
    
    //TODO temporary solution
    public static String LastTemperatureReadString = "";
    
    public static synchronized TemperatureStore getInstance(){
        if (iInstance == null){
            iInstance = new TemperatureStore();
        }
        return iInstance;
    }

    private TemperatureStore(){
        iTemperatures = new ArrayList<>();
        iLastTemperatureMeasure = null;
    }
    
    public int size(){
        if (iTemperatures != null){
            return iTemperatures.size();
        }
        return 0;
    }
    
    synchronized void storeTemperature(TemperatureMeasure aTemperatureMeasure) {
        iTemperatures.add(aTemperatureMeasure);
        System.out.println("Temperature added to store. Total temperatures: "+iTemperatures.size());
    }

    synchronized Collection<TemperatureMeasure> getTemperatures() {
        Collection<TemperatureMeasure> tTemps = new ArrayList<>(iTemperatures.size());
        for (TemperatureMeasure t: iTemperatures){
            tTemps.add(new TemperatureMeasure(t.getLocation(), t.getGroup(), t.getDate(), t.getTemp()));
        }
        return tTemps;
    }
    
    synchronized void removeAll(Collection<TemperatureMeasure> aTemps){
        iTemperatures.removeAll(aTemps);
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
        return Helper.getTempAsString(iLastTemperatureMeasure.getTemp()) + " C (" + Helper.calToString(cal)+")";
    }
    
}

