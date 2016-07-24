/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermostatapplication;

import thermostatapplication.entity.TemperatureMeasure;
import thermostatapplication.helper.Helper;
import thermostatapplication.properties.GardenProperties;
import java.util.Date;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 *
 * @author Ste
 */
public class SensorDataLoggerCallback implements MqttCallback{
    
    TemperatureStore iTemperatureStore;
            
    public SensorDataLoggerCallback(){
        iTemperatureStore = TemperatureStore.getInstance();
    }

    @Override
    public void connectionLost(Throwable thrwbl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void messageArrived(String aTopic, MqttMessage aMessage) throws Exception {
        
        System.out.println("MqttMessage received [ " +aTopic +" " +aMessage.toString()+" ]");
        
        String[] messageSplitted = aMessage.toString().trim().split(" ");
        //TEMP Anna Temp_pumps 23.01
        //TEMP Babbo Salotto 17.30
        if (messageSplitted.length != 4) return;
        Date dateRead = Helper.resetSecMillsDate(new Date());
        //TODO witch temperature to store?
        //iTemperatureStore.setLastTemperatureRead(new TemperatureMeasure(messageSplitted[2], messageSplitted[1], dateRead, new Float(messageSplitted[3])));
        if ("TEMP".equals(messageSplitted[0]) || GardenProperties.PERSIST_TEMPERATURES){
            storeTemperature(new TemperatureMeasure(messageSplitted[2], messageSplitted[1], dateRead, new Float(messageSplitted[3])));
        }
        
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /////////////////////////
    
    private void storeTemperature(TemperatureMeasure aTemperatureMeasure) {
        // 24 byte each. 1 MB -> 41666 measures.
        if (iTemperatureStore.size() < 40000){
            iTemperatureStore.storeTemperature(aTemperatureMeasure);
        }else {
            System.out.println("StoreSize ("+iTemperatureStore.size()+") exceeded max size!! Not storing anymore");
        }
    }
    
}
