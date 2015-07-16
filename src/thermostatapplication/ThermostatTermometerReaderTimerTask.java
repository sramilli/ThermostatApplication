/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermostatapplication;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.TimerTask;

/**
 *
 * @author Ste
 */
public class ThermostatTermometerReaderTimerTask extends TimerTask {
                               //BMP180
    AdafruitBMP180 tempSensor;
    final NumberFormat NF;
    float temp, read1, read2, read3;
    float averageMinuteTemp;
    int iteration;
    Date dateRead;
    public static String NAME;

    public ThermostatTermometerReaderTimerTask(String aName){
        tempSensor = new AdafruitBMP180();
        NF = new DecimalFormat("##00.00");
        temp = 0;
        averageMinuteTemp = 0;
        iteration = 0;
        NAME = aName;
    }

    @Override
    public void run(){
        try {
            temp = tempSensor.readTemperature();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //System.out.println("RAW: "+ temp);
        System.out.println("ThermostatTermometerReade " +(new Date())+": " + NF.format(temp) + " C");  
        
        switch (iteration){
            case 0:
                //first read
                dateRead = DateHelper.resetSecMills(new Date());
                read1 = temp;
                iteration++;
                break;
            case 1:
                //second read
                read2 = temp;
                iteration++;
                break;
            case 2:
                //third read
                read3 = temp;
                if (read1 != 0 || read2 != 0 || read3 != 0){
                    averageMinuteTemp = (read1 + read2 + read3) / 3;  
                } else averageMinuteTemp = 0;
                iteration = 0;
                System.out.println("AverageMinuteTemperature " + dateRead + ": " + NF.format(averageMinuteTemp) );
                notifyTemperatureHandler(ThermostatTermometerReaderTimerTask.NAME, dateRead.toString(), NF.format(averageMinuteTemp));
        }
    }

    private void notifyTemperatureHandler(String NAME, String aDate, String aTemp) {
        //TODO
    }

}
