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
public class CommandParser {

    public static CommandType parse(SMS aSMS) {
        Interpreter it = Interpreter.getInstance();
        CommandType tCmd = it.interprete(aSMS);
        return tCmd;
    }

}
