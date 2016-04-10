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
public class ThermostatProperties {

    //private static int GREEN_LED_HEATER_STATUS = 18;
    //private static int HEATER_RELAY = 7;
    //private static int GREEN_LED = 23;
    //private static int YELLOW_LED = 25;
    //private static int RED_LED = 24;
    //private static int MODE_BUTTON = 27;
    //private static int MODE_BUTTON_PORT = 0;
    //private static int SHUTDOWN_BUTTON = 17;
    //private static int SHUTDOWN_BUTTON_PORT = 0;
    //private static int MANUAL_THERMOSTAT = 22;
    //private static int MANUAL_THERMOSTAT_PORT = 0;
    public static int GREEN_HEATER_STATUS_LED = 1;
    public static int HEATER_RELAY = 11;            //7_PI4J_B_REV_2
    public static int GREEN_STATE_LED = 4;          //23_PI4J_B_REV_2
    public static int YELLOW_STATE_LED = 6;         //25_PI4J_B_REV_2
    public static int RED_STATE_LED = 5;            //24_PI4J_B_REV_2
    public static int BLUE_PROGRAM_LED = 7;
    public static int MODE_BUTTON = 2;              //27_PI4J_B_REV_2
    public static int SHUTDOWN_BUTTON = 0;          //17_PI4J_B_REV_2
    public static int MANUAL_THERMOSTAT_INPUT = 3;  //22_PI4J_B_REV_2

    public static boolean START_READING_TEMPERATURES = true;
    public static boolean STORE_TEMPERATURES = false;
    public static boolean PERSIST_TEMPERATURES = false;
    public static String THERMOMETER_LOCATION = "";
    public static String THERMOMETER_GROUP = "Unknown";
    public static boolean SOFT_SHUTDOWN_ENABLED = false;
    public static boolean PREFER_EMAIL_REPLIES_IF_AVAILABLE = true;
    public static String A = null;
    public static String B = null;
    public static String C = null;
    public static String ML_URL = null;
    public static int GSM_BAUD_RATE = 9600;
    public static String USER_1 = null;
    public static String USER_2 = null;
    public static String USER_3 = null;
    
    static {
        try{
            PropertiesHandler prop = PropertiesHandler.getInstance();
            START_READING_TEMPERATURES = new Boolean(prop.getProperty("START_READING_TEMPERATURES"));
            SOFT_SHUTDOWN_ENABLED = new Boolean(prop.getProperty("SOFT_SHUTDOWN_ENABLED"));
            THERMOMETER_LOCATION = prop.getProperty("THERMOMETER_LOCATION");
            THERMOMETER_GROUP = prop.getProperty("THERMOMETER_GROUP");
            GSM_BAUD_RATE = new Integer(prop.getProperty("gsmBaudRate"));
            GREEN_HEATER_STATUS_LED = new Integer(prop.getProperty("GREEN_HEATER_STATUS_LED"));
            HEATER_RELAY = new Integer(prop.getProperty("HEATER_RELAY"));
            GREEN_STATE_LED = new Integer(prop.getProperty("GREEN_STATE_LED"));
            YELLOW_STATE_LED = new Integer(prop.getProperty("YELLOW_STATE_LED"));
            RED_STATE_LED = new Integer(prop.getProperty("RED_STATE_LED"));
            MODE_BUTTON = new Integer(prop.getProperty("MODE_BUTTON"));
            SHUTDOWN_BUTTON = new Integer(prop.getProperty("SHUTDOWN_BUTTON"));
            MANUAL_THERMOSTAT_INPUT = new Integer(prop.getProperty("MANUAL_THERMOSTAT_INPUT"));
            BLUE_PROGRAM_LED = new Integer(prop.getProperty("BLUE_PROGRAM_LED"));
            PREFER_EMAIL_REPLIES_IF_AVAILABLE = new Boolean(prop.getProperty("PREFER_EMAIL_REPLIES_IF_AVAILABLE"));
            STORE_TEMPERATURES = new Boolean(prop.getProperty("STORE_TEMPERATURES"));
            PERSIST_TEMPERATURES = new Boolean(prop.getProperty("PERSIST_TEMPERATURES"));
            A = prop.getProperty("A");  //not backwords compatible safe
            B = prop.getProperty("B");  //not backwords compatible safe
            C = prop.getProperty("C");  //not backwords compatible safe
            ML_URL = prop.getProperty("ML_URL");  //not backwords compatible safe
            USER_1 = prop.getProperty("USER_1");
            USER_2 = prop.getProperty("USER_2");
            USER_3 = prop.getProperty("USER_3");
            System.out.println("Read Prop measureTemps correctly");
        } catch (Throwable ex){
            System.out.println("ERROR READING PROP FILE!!!");
            ex.printStackTrace();
        }
    }
    
}
