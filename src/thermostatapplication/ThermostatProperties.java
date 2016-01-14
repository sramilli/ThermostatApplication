/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermostatapplication;

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
    public static String THERMOSTAT_LOCATION = "";
    public static boolean SOFT_SHUTDOWN_ENABLED = false;
    public static int GSM_BAUD_RATE = 9600;
    
    static {
        try{
            PropertiesHandler prop = PropertiesHandler.getInstance();
            //TODO change this to true (on mine and Dad) and reenable the reading
            //START_READING_TEMPERATURES = new Boolean(prop.getProperty("START_READING_TEMPERATURES"));
            THERMOSTAT_LOCATION = prop.getProperty("THERMOSTAT_LOCATION");
            GSM_BAUD_RATE = new Integer(prop.getProperty("gsmBaudRate"));
            GREEN_HEATER_STATUS_LED = new Integer(prop.getProperty("GREEN_HEATER_STATUS_LED"));
            HEATER_RELAY = new Integer(prop.getProperty("HEATER_RELAY"));
            GREEN_STATE_LED = new Integer(prop.getProperty("GREEN_STATE_LED"));
            YELLOW_STATE_LED = new Integer(prop.getProperty("YELLOW_STATE_LED"));
            RED_STATE_LED = new Integer(prop.getProperty("RED_STATE_LED"));
            //TODO 
            //BLUE_PROGRAM_LED
            //STORE_TEMPERATURES = false
            MODE_BUTTON = new Integer(prop.getProperty("MODE_BUTTON"));
            SHUTDOWN_BUTTON = new Integer(prop.getProperty("SHUTDOWN_BUTTON"));
            MANUAL_THERMOSTAT_INPUT = new Integer(prop.getProperty("MANUAL_THERMOSTAT_INPUT"));

            System.out.println("Read Prop measureTemps correctly");
        } catch (Throwable ex){
            System.out.println("ERROR READING PROP FILE!!!");
            ex.printStackTrace();
        }
    }
    
}
