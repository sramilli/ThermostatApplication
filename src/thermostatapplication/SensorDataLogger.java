/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermostatapplication;

import thermostatapplication.properties.GardenProperties;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ste
 * https://gist.github.com/m2mIO-gister/5275324
 */
public class SensorDataLogger {
    static Logger logger = LoggerFactory.getLogger(SensorDataLogger.class);
    MqttClient iMqttClient;
    MqttConnectOptions iConnOpt;
    MemoryPersistence iPersistence;

    public SensorDataLogger(){
        super();
        iConnOpt = new MqttConnectOptions();
        iConnOpt.setCleanSession(true);
        iConnOpt.setKeepAliveInterval(30 * 60);
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
            logger.error("Error connecting to MQTT [{}]", GardenProperties.MQTT_BROKER);
        }
        logger.info("Connected to  [{}]", GardenProperties.MQTT_BROKER);
        
        try{
            iMqttClient.subscribe(GardenProperties.SENSOR_DATA_LOGGER_TOPIC_TEMPS, GardenProperties.MQTT_QOS_2);
            logger.info("Subscribed to  [{}]", GardenProperties.SENSOR_DATA_LOGGER_TOPIC_TEMPS);
        } catch (Exception e){
            e.printStackTrace();
            logger.error("Error subscribing to  [{}]", GardenProperties.SENSOR_DATA_LOGGER_TOPIC_TEMPS);
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
