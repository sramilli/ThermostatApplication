/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermostatapplication;

//import de.pi3g.pi.oled.OLEDDisplay;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;


/**
 *
 * @author Ste
 */
public class ThermostatApplication {

    public static Calendar iRunningSince = Calendar.getInstance();
    public static String lastTemperatureRead = "";
    TemperatureReader iTemperatureReader = null;
    
    public ThermostatApplication() {
        super();
    }
        
    public static void main(String[] args) {
        ThermostatApplication iApp = new ThermostatApplication();
        iApp.startApp();
    }

    public void startApp() {
        
        System.out.println("Starting Thermostatapplication at: "+new Date());
        SwitchOFF iSwitchOFF = new SwitchOFF(ThermostatProperties.SHUTDOWN_BUTTON);
        System.out.println("Main Application: SwitchOFF pin opened and initialized!");
        Thermostat iThermostat = new Thermostat();
        //iThermostat.testSendSMS();
        //iThermostat.testLoopingAT();
        //iThermostat.testReadAllMessages();
        //iThermostat.testReadAllMessagesOneByOne();
       
        if (ThermostatProperties.START_READING_TEMPERATURES){
            iTemperatureReader = new TemperatureReader("Thermostat"+ThermostatProperties.THERMOSTAT_LOCATION);
            iTemperatureReader.startReadingTemperatures();
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

        System.out.println("Main Application: Prepare to turn Off the system!");
        System.out.println("Main Application: Turning off Thermostat");
        iThermostat.stop();
        if (iTemperatureReader != null){
            iTemperatureReader.stop();
        }
        /* TODO ONGOING OLED DISPLAY
        display.shutdown();
        ONGOING OLED DISPLAY */ 

        waitABit(10000);
        iThermostat = null;
        iTemperatureReader = null;
        
        if (iSwitchOFF.justTerminateApp() && ThermostatProperties.SOFT_SHUTDOWN_ENABLED){
            System.out.println("Just exit the java application");
            System.out.println("Main Application: Turning off SwitchOff button");
            iSwitchOFF.close();
            waitABit(1000);
            iSwitchOFF = null;
        } else {
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
