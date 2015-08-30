/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermostatapplication;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

/**
 *
 * @author Ste
 */
public class ThermostatTermometerReaderTimerTask extends TimerTask {
                               //BMP180
    AdafruitBMP180 tempSensor;
    float temp, read1, read2, read3;
    float averageMinuteTemp;
    int iteration;
    Date dateRead;
    String name;
    TemperatureStore storeHandler;

    public ThermostatTermometerReaderTimerTask(String aName, TemperatureStore aStoreHandler){
        tempSensor = new AdafruitBMP180();
        temp = 0;
        averageMinuteTemp = 0;
        iteration = 0;
        name = aName;
        storeHandler = aStoreHandler;
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
                System.out.println("ThermostatTermometerReader measure 1" +Helper.getDateAsString(new Date())+" " + Helper.getTempAsString(temp) + " C"); 
                iteration++;
                break;
            case 1:
                //second read
                read2 = temp;
                System.out.println("ThermostatTermometerReade measure 2" +Helper.getDateAsString(new Date())+" " + Helper.getTempAsString(temp) + " C"); 
                iteration++;
                break;
            case 2:
                //third read
                read3 = temp;
                System.out.println("ThermostatTermometerReade measure 3" +Helper.getDateAsString(new Date())+" " + Helper.getTempAsString(temp) + " C"); 
                iteration++;
                break;
            case 3:
                if (read1 != 0 || read2 != 0 || read3 != 0){
                    averageMinuteTemp = (read1 + read2 + read3) / 3;  
                } else averageMinuteTemp = 0;
                iteration = 0;
                System.out.println("AverageMinuteTemperature " + Helper.getDateAsString(dateRead) + " " + Helper.getTempAsString(averageMinuteTemp) );
                storeTemperature(name, dateRead, averageMinuteTemp);
        }
    }

    private void storeTemperature(String NAME, Date aDate, float aTemp) {
        if (storeHandler.size() < 3000){
            storeHandler.storeTemperature(new TemperatureMeasure(NAME, aDate, aTemp));
        }else {
            System.out.println("StoreSize ("+storeHandler.size()+") exceeded max size!! Not storing anymore");
        }
    }

}
