/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermostatapplication.entity;

import thermostatapplication.helper.Helper;
import java.util.Date;

/**
 *
 * @author Ste
 */
public class TemperatureMeasure {
    String iLocation;
    Date iDate;
    float iTemp;

    public TemperatureMeasure(String aLocation, Date aDate, float aTemp) {
        iLocation = aLocation;
        iDate = aDate;
        iTemp = aTemp;
    }
    
    public String toString(){
        return "Location: "+getLocation()+" Date: "+Helper.getDateAsString(getDate())+" Temp: "+Helper.getTempAsString(getTemp());
    }

    public String getLocation() {
        return iLocation;
    }

    public void setLocation(String aLocation) {
        this.iLocation = aLocation;
    }

    public Date getDate() {
        return iDate;
    }

    public void setDate(Date aDate) {
        this.iDate = aDate;
    }

    public float getTemp() {
        return iTemp;
    }

    public void setTemp(float aTemp) {
        this.iTemp = aTemp;
    }
    
}
