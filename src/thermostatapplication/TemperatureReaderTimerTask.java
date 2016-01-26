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
                               //BMP180
    AdafruitBMP180 tempSensor;
    float temp, read1, read2, read3;
    float averageMinuteTemp;
    int iteration;
    Date dateRead;
    String name;
    TemperatureStore iTemperatureStore;

    public TemperatureReaderTimerTask(String aName, TemperatureStore aTemperatureStore){
        System.out.println("TemperatureReaderTimerTask INSTANTIATED!!!");
        tempSensor = new AdafruitBMP180();
        temp = 0;
        averageMinuteTemp = 0;
        iteration = 0;
        name = aName;
        iTemperatureStore = aTemperatureStore;
    }

    @Override
    public void run(){
        try {
            temp = tempSensor.readTemperature();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
         
        switch (iteration){
            case 0:
                //first read
                dateRead = Helper.resetSecMillsDate(new Date());
                read1 = temp;
                System.out.println("ThermostatTermometerReader measure 1: " +Helper.getDateAsString(new Date())+" " + Helper.getTempAsString(temp) + " C"); 
                iteration++;
                break;
            case 1:
                //second read
                read2 = temp;
                System.out.println("ThermostatTermometerReade measure 2: " +Helper.getDateAsString(new Date())+" " + Helper.getTempAsString(temp) + " C"); 
                iteration++;
                break;
            case 2:
                //third read
                read3 = temp;
                System.out.println("ThermostatTermometerReade measure 3: " +Helper.getDateAsString(new Date())+" " + Helper.getTempAsString(temp) + " C"); 
                iteration++;
                break;
            case 3:
                if (read1 != 0 || read2 != 0 || read3 != 0){
                    averageMinuteTemp = (read1 + read2 + read3) / 3;  
                } else averageMinuteTemp = 0;
                iteration = 0;
                
                System.out.println("AverageMinuteTemperature " + Helper.getDateAsString(dateRead) + " " + Helper.getTempAsString(averageMinuteTemp) );
                iTemperatureStore.setLastTemperatureRead(new TemperatureMeasure(name, dateRead, averageMinuteTemp));
                if (ThermostatProperties.PERSIST_TEMPERATURES){
                    storeTemperature(new TemperatureMeasure(name, dateRead, averageMinuteTemp));
                }
        }
    }
    
    /*public void updateLastTemperatureRead(TemperatureMeasure aTemperatureMeasure){
        //TODO UGLY!!!  dont do it like this!
        //Calendar cal = Calendar.getInstance();
        //cal.setTime(dateRead);
        iTemperatureStore.setLastTemperatureRead(aTemperatureMeasure);
        //ThermostatApplication.lastTemperatureRead = Helper.getTempAsString(averageMinuteTemp) + " C " + Helper.calToString(cal);
    }*/

    private void storeTemperature(TemperatureMeasure aTemperatureMeasure) {
        if (iTemperatureStore.size() < 3000){
            iTemperatureStore.storeTemperature(aTemperatureMeasure);
        }else {
            System.out.println("StoreSize ("+iTemperatureStore.size()+") exceeded max size!! Not storing anymore");
        }
    }

}
