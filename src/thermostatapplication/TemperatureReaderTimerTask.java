/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermostatapplication;

import thermostatapplication.entity.TemperatureMeasure;
import thermostatapplication.properties.ThermostatProperties;
import thermostatapplication.helper.Helper;
import thermostatapplication.devices.AdafruitBMP180;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

/**
 *
 * @author Ste
 */
public class TemperatureReaderTimerTask extends TimerTask {

    AdafruitBMP180 tempSensor;
    float temp;
    Date dateRead;
    String iLocation;
    String iGroup;
    TemperatureStore iTemperatureStore;

    public TemperatureReaderTimerTask(String aLocation, String aGroup, TemperatureStore aTemperatureStore){
        System.out.println("TemperatureReaderTimerTask INSTANTIATED!!!");
        tempSensor = new AdafruitBMP180();
        temp = 0;
        iLocation = aLocation;
        iGroup = aGroup;
        iTemperatureStore = aTemperatureStore;
    }

    @Override
    public void run(){
        try {
            temp = tempSensor.readTemperature();
        } catch (Exception ex) {
            temp = 0;
            ex.printStackTrace();
        }
        dateRead = Helper.resetSecMillsDate(new Date());
        System.out.println("TemperatureReaderTimerTask measure: " +Helper.getDateAsString(dateRead)+" " + Helper.getTempAsString(temp) + " C");
        iTemperatureStore.setLastTemperatureRead(new TemperatureMeasure(iLocation, iGroup, dateRead, temp));
        if (ThermostatProperties.PERSIST_TEMPERATURES){
            storeTemperature(new TemperatureMeasure(iLocation, iGroup, dateRead, temp));
        }
    }
    
    private void storeTemperature(TemperatureMeasure aTemperatureMeasure) {
        // 24 byte each. 1 MB -> 41666 measures.
        if (iTemperatureStore.size() < 40000){
            iTemperatureStore.storeTemperature(aTemperatureMeasure);
        }else {
            System.out.println("StoreSize ("+iTemperatureStore.size()+") exceeded max size!! Not storing anymore");
        }
    }

}
