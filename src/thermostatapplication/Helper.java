/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermostatapplication;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Ste
 */
public class Helper {
    final static NumberFormat NF = new DecimalFormat("##00.00");
    final static SimpleDateFormat DF = new SimpleDateFormat("YYYYMMdd-HH:mm");
    
    public static Date resetSecMillsDate(Date aDate){
        Calendar cal = Calendar.getInstance();
        cal.setTime(aDate);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    
    public static Date getNextWholeMinuteDate(Date aDate){
        Calendar cal = Calendar.getInstance();
        cal.setTime(aDate);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.MINUTE, 1);
        return cal.getTime();
    }
    
    public static Date getNextWholeMinuteDatePlusFiveSec(Date aDate){
        Calendar cal = Calendar.getInstance();
        cal.setTime(aDate);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.MINUTE, 1);
        cal.add(Calendar.SECOND, 5);
        return cal.getTime();
    }
    
    public static Calendar getBeginningOfDay(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        return cal;
    }
    
    public static Calendar getBeginningOfTomorrow(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return cal;
    }
    
    public static Calendar getThisInstant(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal;
    }
    
    public static long getOneDay(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return cal.getTimeInMillis();
    }
    
    public static Calendar getCalendar(Date aDate){
        Calendar cal = Calendar.getInstance();
        cal.setTime(aDate);
        return cal;
    }
    
    public static String getDateAsString(Date aDate){
        //Fri Jul 17 22:41:00 CEST 2015
        return DF.format(aDate);
    }
    
    public static String getTempAsString(float aTemp){
        return NF.format(aTemp);
    }
    
}
