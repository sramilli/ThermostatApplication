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
class GSMDataInterpreter {
    
    public static GSMCommand getCommand(String aString){
        if (aString == null || "".equals(aString)) return GSMCommand.UNKNOW;
        aString = aString.trim();
        //System.out.println("GSMDataInterpreter: Parsed incoming string from GSM module--->"+aString+"<---");
        if (aString.startsWith(GSMCommand.MESSAGE_ARRIVED.toString())) return GSMCommand.MESSAGE_ARRIVED;
        else if (aString.startsWith(GSMCommand.READ_ALL_MESSAGES.toString())) return GSMCommand.READ_ALL_MESSAGES;
        
        return GSMCommand.UNKNOW;
    }
    
}
