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
import java.util.logging.Level;
import java.util.logging.Logger;
import thermostatapplication.devices.BMP280;

/**
 *
 * @author Ste
 */
public class TemperatureReaderTimerTask extends TimerTask {

    AdafruitBMP180 tempSensor;
    BMP280 bmp280;
    float temp;
    float temp_2;
    Date dateRead;
    String iLocation;
    String iGroup;
    TemperatureStore iTemperatureStore;
    private static int BUS_1 = 1;

    public TemperatureReaderTimerTask(String aLocation, String aGroup, TemperatureStore aTemperatureStore){
        System.out.println("TemperatureReaderTimerTask INSTANTIATED!!!");
        tempSensor = new AdafruitBMP180();
        if (ThermostatProperties.BMP280_TEMP_SENSOR_PRESENT_AT_76){
            try {
                bmp280 = new BMP280(BMP280.Protocol.I2C, BMP280.ADDR_SDO_2_GND, BUS_1);
                bmp280.setIndoorNavigationMode();
                bmp280.setMode(BMP280.Mode.NORMAL, true);
                bmp280.setTemperatureSampleRate(BMP280.Temperature_Sample_Resolution.TWO, true);
                bmp280.setPressureSampleRate(BMP280.Pressure_Sample_Resolution.SIXTEEN, true);
                bmp280.setIIRFilter(BMP280.IIRFilter.SIXTEEN, true);
                bmp280.setStandbyTime(BMP280.Standby_Time.MS_POINT_5, true);
            } catch (Exception ex) {
                Logger.getLogger(TemperatureReaderTimerTask.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        temp = 0;
        temp_2 = 0;
        iLocation = aLocation;
        iGroup = aGroup;
        iTemperatureStore = aTemperatureStore;
    }

    @Override
    public void run(){
        try {
            temp = tempSensor.readTemperature();
            if (ThermostatProperties.BMP280_TEMP_SENSOR_PRESENT_AT_76){
                double[] results = bmp280.sampleDeviceReads();
                temp_2 = (float) results[BMP280.TEMP_VAL_C];
            }
            
        } catch (Exception ex) {
            temp = 0;
            temp_2 = 0;
            ex.printStackTrace();
        }
        dateRead = Helper.resetSecMillsDate(new Date());
        System.out.println("TemperatureReaderTimerTask measure: " +Helper.getDateAsString(dateRead)+" " + Helper.getTempAsString(temp) + " C");
        if (ThermostatProperties.BMP280_TEMP_SENSOR_PRESENT_AT_76){
            System.out.println("TemperatureReaderTimerTask measure_2: " +Helper.getDateAsString(dateRead)+" " + Helper.getTempAsString(temp_2) + " C");
        }
        iTemperatureStore.setLastTemperatureRead(new TemperatureMeasure(iLocation, iGroup, dateRead, temp));
        //iTemperatureStore.setLastTemperatureRead(new TemperatureMeasure("280", iGroup, dateRead, temp_2));
        if (ThermostatProperties.PERSIST_TEMPERATURES){
            storeTemperature(new TemperatureMeasure(iLocation, iGroup, dateRead, temp));
            if (ThermostatProperties.BMP280_TEMP_SENSOR_PRESENT_AT_76){
                storeTemperature(new TemperatureMeasure("280", iGroup, dateRead, temp_2));
            }
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
