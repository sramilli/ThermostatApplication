/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermostatapplication;

import java.util.Date;

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

    public static void main(String[] args) {

        ThermostatApplication iApp = new ThermostatApplication();
        iApp.startApp();
    }

    public ThermostatApplication() {
        super();
    }

    public void startApp() {
        //Starts the switchOFF button
        SwitchOFF iSwitchOFF = new SwitchOFF(SHUTDOWN_BUTTON_17_PI4J_B_REV_2);
        System.out.println("Main Application: SwitchOFF pin opened and initialized!");

        //Starts the Thermostat
        Thermostat iThermostat = new Thermostat(MODE_BUTTON_27_PI4J_B_REV_2, MANUAL_THERMOSTAT_22_PI4J_B_REV_2, GREEN_LED_HEATER_STATUS_PI4J_B_REV_2, GREEN_LED_23_PI4J_B_REV_2, YELLOW_LED_25_PI4J_B_REV_2, RED_LED_24_PI4J_B_REV_2, HEATER_RELAY_7_PI4J_B_REV_2);
        //iThermostat.testSendSMS();
        //iThermostat.testLoopingAT();
        //System.out.println("---> Reading all messages: "+iThermostat.testReadAllMessagesRaw());
        //iThermostat.testReadAllMessages();
        //iThermostat.testReadAllMessagesOneByOne();
        iThermostat.startPollingIncomingCommands(false, 60);

        //Holds the application running until it detects the button press
        /*while (!iSwitchOFF.terminateApp()) {
         whaitABit(5000);
         }*/
        // just live a while
        for (int i = 0; i < 10; i++) {
            System.out.println(iThermostat.getStatus());
            whaitABit(5000);
        }
        System.out.println("Main Application: Prepare to turn Off the system!");
        System.out.println("Main Application: Turning off SwitchOff button");
        iSwitchOFF.close();
        System.out.println("Main Application: Turning off Thermostat");
        iThermostat.stop();
        iThermostat = null;
        whaitABit(5000);

    }

    private void whaitABit(int a) {
        try {
            // wait 1 second before continuing
            Thread.sleep(a);
        } catch (InterruptedException ex) {
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
