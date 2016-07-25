/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermostatapplication.properties;

/**
 *
 * @author Ste
 */
public class GardenProperties {
    
    public static boolean START_GARDEN_APPLICATION = true;
    
    public static String ACTUATOR_CLIENT_ID = "ActuatorMqttClient";
    public static String SENSOR_LOGGER_CLIENT_ID = "SensorLoggerMqttClient";
    public static String MQTT_BROKER = "tcp://192.168.0.25:1883";
    public static int MQTT_QOS_2 = 2;
    public static String SENSOR_DATA_LOGGER_TOPIC = "/temps";
    
    public static String MORNING_WATERING_TIME = "23:30";
    public static String EVENING_WATERING_TIME = "23:33";
    
    public static String TOPIC_PUMP_1 = "/pump1";
    public static String TOPIC_PUMP_2 = "/pump2";
    public static String TOPIC_PUMP_3 = "/pump3";
    public static String TOPIC_PUMP_4 = "/pump4";
    public static String TOPIC_PUMP_5 = "/pump5";
    public static int CALIBRATION_PUMP_1 = 1;
    public static int CALIBRATION_PUMP_2 = 1;
    public static int CALIBRATION_PUMP_3 = 1;
    public static int CALIBRATION_PUMP_4 = 1;
    public static int CALIBRATION_PUMP_5 = 1;
    public static int DEFAULT_QUANTITY_PUMP_1 = 1;
    public static int DEFAULT_QUANTITY_PUMP_2 = 2;
    public static int DEFAULT_QUANTITY_PUMP_3 = 3;
    public static int DEFAULT_QUANTITY_PUMP_4 = 4;
    public static int DEFAULT_QUANTITY_PUMP_5 = 5;
    public static boolean PUMP_1_PLUGGED = true;
    public static boolean PUMP_2_PLUGGED = true;
    public static boolean PUMP_3_PLUGGED = true;
    public static boolean PUMP_4_PLUGGED = true;
    public static boolean PUMP_5_PLUGGED = true;
    
    public static boolean PERSIST_TEMPERATURES = true;
    
}
