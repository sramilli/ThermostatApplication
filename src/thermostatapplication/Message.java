/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermostatapplication;

/**
 *
 * @author Ste
 */
public class Message {
    
    String iMobNr;
    String iEmailAddr;
    String iBody;

    public Message(String aMobNr, String aEmailAddr, String aBody){
        iMobNr = aMobNr;
        iEmailAddr = aEmailAddr;
        iBody = aBody;
    }

    public String getMobNr() {
        return iMobNr;
    }

    public void setMobNr(String aMobNr) {
        this.iMobNr = aMobNr;
    }

    public String getEmailAddr() {
        return iEmailAddr;
    }

    public void setEmailAddr(String aEmailAddr) {
        this.iEmailAddr = aEmailAddr;
    }

    public String getBody() {
        return iBody;
    }

    public void setBody(String aBody) {
        this.iBody = aBody;
    }
    
    public boolean hasValidEmailAddr(){
        if (iEmailAddr != null && !"".equals(iEmailAddr)) return true;
        return false;
    }

    boolean hasValidMobNr() {
        if (iMobNr != null && !"".equals(iMobNr)) return true;
        return false;
    }
    
}
