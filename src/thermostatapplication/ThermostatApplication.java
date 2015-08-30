/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermostatapplication;

//import de.pi3g.pi.oled.OLEDDisplay;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ste
 */
public class ThermostatApplication {

    //Thermostat iThermostat;
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
    private static final int GREEN_LED_HEATER_STATUS_PI4J_B_REV_2 = 1;
    private static final int HEATER_RELAY_7_PI4J_B_REV_2 = 11;
    private static final int GREEN_LED_23_PI4J_B_REV_2 = 4;
    private static final int YELLOW_LED_25_PI4J_B_REV_2 = 6;
    private static final int RED_LED_24_PI4J_B_REV_2 = 5;
    private static final int MODE_BUTTON_27_PI4J_B_REV_2 = 2;
    private static final int SHUTDOWN_BUTTON_17_PI4J_B_REV_2 = 0;
    private static final int MANUAL_THERMOSTAT_22_PI4J_B_REV_2 = 3;

    private static boolean live = true;
    public static Date iRunningSince = new Date();
    boolean deleteReadMessages = true;
    
    boolean startReadingTemperatures = false;
    TemperatureStore tTemperatureStore = null;
    ThermostatTermometer thermostatTermometer = null;
    
    public static void main(String[] args) {

        ThermostatApplication iApp = new ThermostatApplication();
        iApp.startApp();
    }

    public ThermostatApplication() {
        super();
    }

    public void startApp() {
        try{
            PropertiesHandler prop = PropertiesHandler.getInstance();
            startReadingTemperatures = new Boolean(prop.getProperty("measureTemps"));
            System.out.println("Read Prop measureTemps correctly");
        } catch (Throwable ex){
            System.out.println("ERROR READING PROP FILE!!!");
            ex.printStackTrace();
        }

        
        SwitchOFF iSwitchOFF = new SwitchOFF(SHUTDOWN_BUTTON_17_PI4J_B_REV_2);
        System.out.println("Main Application: SwitchOFF pin opened and initialized!");

        //Starts the Thermostat
        Thermostat iThermostat = new Thermostat(MODE_BUTTON_27_PI4J_B_REV_2, MANUAL_THERMOSTAT_22_PI4J_B_REV_2, GREEN_LED_HEATER_STATUS_PI4J_B_REV_2, GREEN_LED_23_PI4J_B_REV_2, YELLOW_LED_25_PI4J_B_REV_2, RED_LED_24_PI4J_B_REV_2, HEATER_RELAY_7_PI4J_B_REV_2);
        //iThermostat.testSendSMS();
        //iThermostat.testLoopingAT();
        //iThermostat.testReadAllMessages();
        //iThermostat.testReadAllMessagesOneByOne();
        
        /*
        iThermostat.startPollingIncomingCommands(deleteReadMessages, 60);
        */
       
        if (startReadingTemperatures){
            tTemperatureStore = new TemperatureStore();
            thermostatTermometer = new ThermostatTermometer("Thermostat", tTemperatureStore);
            thermostatTermometer.startMeasureTemperature();
        }
        
            /* TODO ONGOING OLED DISPLAY
            
            OLEDDisplay display = null;
            try {
            display = new OLEDDisplay();
            display.drawString("ABCDE!", 0, 0, true);
            display.drawString("ABCDE!", 1, 10, true);
            display.drawString("ABCDE!", 2, 20, true);
            display.drawString("ABCDE!", 15, 30, true);
            display.drawStringCentered("Hello World!", 25, true);
            display.update();
            } catch (IOException ex) {
            ex.printStackTrace();
            }
            ONGOING OLED DISPLAY */

        //Holds the application running until it detects the button press
        while (!iSwitchOFF.shutdownPi()) {
            waitABit(5000);
        }
        waitABit(3000);
        // just live a while and die for test purposes only
        /*for (int i = 0; i < 10; i++) {
         System.out.println(iThermostat.getStatus());
         waitABit(5000);
         }*/
        System.out.println("Main Application: Prepare to turn Off the system!");
        System.out.println("Main Application: Turning off Thermostat");
        iThermostat.stop();
    if (startReadingTemperatures){
        thermostatTermometer.stop();
        tTemperatureStore.stop();
    
        //print all collected temperatures
        /*for (TemperatureMeasure t: tTemperatureStore.getCollection()){
            System.out.println(t);
        }*/
    }
/* TODO ONGOING OLED DISPLAY
        display.shutdown();
ONGOING OLED DISPLAY */ 

        waitABit(10000);
        iThermostat = null;
        thermostatTermometer = null;
        tTemperatureStore = null;
        //waitABit(10000);
        
        if (!iSwitchOFF.justTerminateApp()){
            try {
                System.out.println("Main Application: Turning off SwitchOff button");
                iSwitchOFF.close();
                waitABit(1000);
                iSwitchOFF = null;
                System.out.println("Shutdown the Pi!");
                final Process p = Runtime.getRuntime().exec("sudo shutdown -h now");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("Just exit the java application");
                System.out.println("Main Application: Turning off SwitchOff button");
                iSwitchOFF.close();
                waitABit(1000);
                iSwitchOFF = null;
        }
        System.out.println("END");

    }

    private void waitABit(int a) {
        try {
            Thread.sleep(a);
        } catch (InterruptedException ex) {
            System.out.println("waitABit InterruptedException!");
            ex.printStackTrace();
        }
    }

}


/*try {
 System.out.println("Helllo wwwworld");
 //final Process p = Runtime.getRuntime().exec("sudo shutdown -h now");
 final Process p = Runtime.getRuntime().exec("ls");

 new Thread(new Runnable() {
 public void run() {
 BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
 String line = null;
 try {
 while ((line = input.readLine()) != null) {
 System.out.println(line);
 }
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
 }).start();

 p.waitFor();
 } catch (IOException ex) {
 System.out.println("Oh my god we all gonna die!!");
 } catch (InterruptedException ex) {
 System.out.println("Oh my god we all gonna die2!!");
 }*/
                //System.setProperty("jdk.dio.registry", "/home/pi/dev/config/dio.properties-raspberrypi"); 
//System.setProperty("java.library.path", "/home/pi/dev/build/deviceio/lib/arm/libdio.so"); 
/*Properties p = System.getProperties();
 Enumeration keys = p.keys();
 while (keys.hasMoreElements()) {
 String key = (String)keys.nextElement();
 String value = (String)p.get(key);
 System.out.println(key + ": " + value);
 }*/
        //??? Configuration.setProperty("java.security.policy", "./dio.policy");
