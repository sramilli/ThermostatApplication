package thermostatapplication;

//import de.pi3g.pi.oled.OLEDDisplay;
import thermostatapplication.devices.SwitchOFF;
import thermostatapplication.properties.ThermostatProperties;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import thermostatapplication.properties.GardenProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ste
 */
public class ThermostatApplication {
    static Logger logger = LoggerFactory.getLogger(ThermostatApplication.class);
    TemperatureReader iTemperatureReader = null;
    Thermostat iThermostat = null;
    Garden iGarden = null;
    
    public ThermostatApplication() {
        super();
    }
        
    public static void main(String[] args) {
        ThermostatApplication iApp = new ThermostatApplication();
        iApp.startApp();
    }

    public void startApp() {
        logger.info("Starting Thermostatapplication at: [{}]", new Date());
        SwitchOFF iSwitchOFF = new SwitchOFF(ThermostatProperties.SHUTDOWN_BUTTON);
        logger.info("Main Application: SwitchOFF pin opened and initialized");
        iThermostat = new Thermostat();

        //TODO
        //if (ThermostatProperties.START_READING_TEMPERATURES)
        if (false){
            iTemperatureReader = new TemperatureReader(ThermostatProperties.THERMOMETER_LOCATION, ThermostatProperties.THERMOMETER_GROUP);
            iTemperatureReader.startReadingTemperatures();
        }
        
        if (GardenProperties.START_GARDEN_APPLICATION){
            iGarden = new Garden();
            //TODO erase, just for test
            //new Thread(iGarden).start();
            iGarden.run();
            logger.info("GARDEN started");
        }

        
        //Holds the application running until it detects the button press
        while (!iSwitchOFF.shutdownPi()) {
            waitABit(5000);
        }
        if (iGarden != null){
            iGarden.stop();
        }
        waitABit(3000);
        logger.info("Main Application: Turning off Thermostat");
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
            logger.info("Just exit the java application");
            logger.info("Main Application: Turning off SwitchOff button");
            iSwitchOFF.close();
            waitABit(1000);
            iSwitchOFF = null;
        } else {
            try {
                logger.info("Main Application: Turning off SwitchOff button");
                iSwitchOFF.close();
                waitABit(1000);
                iSwitchOFF = null;
                logger.info("Shutdown the Pi");
                final Process p = Runtime.getRuntime().exec("sudo shutdown -h now");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        logger.info("END");
    }

    private void waitABit(int a) {
        try {
            Thread.sleep(a);
        } catch (InterruptedException ex) {
            logger.warn("waitABit InterruptedException");
            ex.printStackTrace();
        }
    }

}
