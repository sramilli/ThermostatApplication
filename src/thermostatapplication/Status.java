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
public enum Status {
    ON("On"),
    MANUAL("Manual"),
    OFF("Off");
    
    String iStatus;
    
    Status(String aStatus){
        iStatus = aStatus;
    }
    
    public String toString(){
        return iStatus;
    }
    
}
