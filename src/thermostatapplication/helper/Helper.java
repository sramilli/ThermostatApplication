/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermostatapplication.helper;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
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
        return 24 * 60 * 60 * 1000;
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
    
    public static void printCal(String aString, Calendar aCal){
        DateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
        System.out.println(aString+" : "+sdf.format(aCal.getTime()));
    }
    
    public static String calToString(Calendar aCal){
        DateFormat sdf = new SimpleDateFormat("dd MMM HH:mm");
        return sdf.format(aCal.getTime());
    }
    
    public static Calendar parseTime(String aTime) throws ParseException{
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        Date date = formatter.parse(aTime);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        
        Calendar beguinningOfDay = getBeginningOfDay();
        cal.set(Calendar.YEAR, beguinningOfDay.get(Calendar.YEAR));
        cal.set(Calendar.MONTH, beguinningOfDay.get(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH, beguinningOfDay.get(Calendar.DAY_OF_MONTH));
        
        return cal;
    }
    
}
