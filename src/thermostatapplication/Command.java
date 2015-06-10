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
public enum Command {

    //one word
    ON("on"),
    OFF("off"),
    MANUAL("manual"),
    //TODO not yet implemented!!!
    STATUS("status"),
    HELP("help"),
    REGISTER_NUMBER("register"),
    //multiple word
    PROGRAM_HOUR("program"),
    PROGRAM_WEEK("program"),
    PROGRAM_OFF("program"),
    NOT_VALID("notValid");

    String iCommand;

    Command(String aString) {
        iCommand = aString;
    }
    
    public boolean isValid(){
        boolean valid = false;
        if (this.equals(ON) || this.equals(MANUAL) || this.equals(OFF))
            valid = true;
        return valid;
    }
}
