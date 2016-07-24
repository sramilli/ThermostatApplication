/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermostatapplication;

import thermostatapplication.properties.GardenProperties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 *
 * @author Ste
 * https://gist.github.com/m2mIO-gister/5275324
 */
public class SensorDataLogger {
    MqttClient iMqttClient;
    MqttConnectOptions iConnOpt;
    MemoryPersistence iPersistence;

    public SensorDataLogger(){
        super();
        iConnOpt = new MqttConnectOptions();
        iConnOpt.setCleanSession(true);
        iConnOpt.setKeepAliveInterval(5 * 60);
        iPersistence = new MemoryPersistence();
    }

    public void start() {
        try{
            iMqttClient = new MqttClient(GardenProperties.MQTT_BROKER, GardenProperties.SENSOR_LOGGER_CLIENT_ID, iPersistence);
            //set callback before connecting
            iMqttClient.setCallback(new SensorDataLoggerCallback());
            iMqttClient.connect(iConnOpt);
        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        
        System.out.println("SensorDataLogger connected to " +GardenProperties.MQTT_BROKER);
        
        try{
            iMqttClient.subscribe(GardenProperties.SENSOR_DATA_LOGGER_TOPIC, GardenProperties.MQTT_QOS_2);
            System.out.println("SensorDataLogger subscribed to " +GardenProperties.SENSOR_DATA_LOGGER_TOPIC);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void stop(){
        try {
            //client.unsubscribe("#");
            iMqttClient.disconnect();
        } catch (MqttException ex) {
            ex.printStackTrace();
        }
    }
    
}
