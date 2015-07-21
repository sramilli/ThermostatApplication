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
class TemperatureMeasure {
    String iName;
    Date iDate;
    float iTemp;

    public TemperatureMeasure(String aName, Date aDate, float aTemp) {
        iName = aName;
        iDate = aDate;
        iTemp = aTemp;
    }
    
    public String toString(){
        return "Name: "+getName()+" Date: "+Helper.getDateAsString(getDate())+" Temp: "+Helper.getTempAsString(getTemp());
    }

    public String getName() {
        return iName;
    }

    public void setName(String aName) {
        this.iName = aName;
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
