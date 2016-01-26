/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package thermostatapplication.helper;

import thermostatapplication.CommandType;
import thermostatapplication.entity.SMS;

/**
 *
 * @author Ste
 */
public class Interpreter {
    
    private Interpreter() {
    }
    
    public static Interpreter getInstance() {
        return InterpreterHolder.INSTANCE;
    }
    
    private static class InterpreterHolder {
        private static final Interpreter INSTANCE = new Interpreter();
    }
    
    public CommandType interprete(SMS aSMS){
        String tText = new String(aSMS.getText());
        String tTemp;
        //simple word commands
        //to cout words try str.split("\\w+").length
        if (tText == null || "".equals(tText)){
            System.out.println("Interpreter: Command = null");
            return CommandType.NOT_VALID;
        }
        tText = tText.trim();
        if (tText.equalsIgnoreCase(CommandType.ON.toString())) return CommandType.ON;
        else if (tText.equalsIgnoreCase(CommandType.OFF.toString())) return CommandType.OFF;
        else if (tText.equalsIgnoreCase(CommandType.MANUAL.toString())) return CommandType.MANUAL;
        else if (tText.equalsIgnoreCase(CommandType.STATUS.toString())) return CommandType.STATUS;
        else if (tText.equalsIgnoreCase(CommandType.HELP.toString())) return CommandType.HELP;
        else if (tText.length() >= 8 && (tTemp = tText.substring(0, 8)).equalsIgnoreCase(CommandType.REGISTER_NUMBER.toString())) return CommandType.REGISTER_NUMBER;
        else if (tText.length() >= 12 && (tTemp = tText.substring(0, 12)).equalsIgnoreCase(CommandType.PROGRAM_DAILY.toString())) return CommandType.PROGRAM_DAILY;
        else {
            System.out.println("Interpreter: Command not supported: "+tText);
            return CommandType.NOT_VALID;
        }
      

    }
}
