package thermostatapplication;

//import de.pi3g.pi.oled.OLEDDisplay;
import thermostatapplication.devices.SwitchOFF;
import thermostatapplication.properties.ThermostatProperties;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;


/**
 *
 * @author Ste
 */
public class ThermostatApplication {

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

        if (ThermostatProperties.START_READING_TEMPERATURES){
            iTemperatureReader = new TemperatureReader(ThermostatProperties.THERMOMETER_LOCATION, ThermostatProperties.THERMOMETER_GROUP);
            iTemperatureReader.startReadingTemperatures();
        }
        
        //Holds the application running until it detects the button press
        while (!iSwitchOFF.shutdownPi()) {
            waitABit(5000);
        }
        waitABit(3000);
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
