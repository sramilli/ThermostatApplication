/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermostatapplication;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Ste
 */
public class DateHelper {
    
    public static Date resetSecMills(Date aDate){
        Calendar cal = Calendar.getInstance();
        cal.setTime(aDate);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    
    public static Date resetSecMillsAndAddMin(Date aDate, int aMin){
        Calendar cal = Calendar.getInstance();
        cal.setTime(aDate);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.MINUTE, aMin);
        return cal.getTime();
    }
    
}
