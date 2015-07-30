/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermostatapplication;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ste
 */
public class AuthorizedUsers {

    public static final String STEFAN1 = "+46700447531";
    public static final String STEFAN2 = "+393496191740";
    public static final String BABBO = "+393471768654";
    public static List<String> tUsers;
    
    static{
        tUsers = new ArrayList<String>();
        tUsers.add(STEFAN1);
        tUsers.add(STEFAN2);
        tUsers.add(BABBO);
    }

    public static List<String> getAllUsers() {
        return tUsers;
    }
    
    public static void addAuthorizedUser(String aUser){
        System.out.println("Authorizing number ["+aUser+"] ");
        tUsers.add(aUser);
    }
    
    public static void removeAuthorizedUser(String aUser){
        if(tUsers.contains(aUser)){
            tUsers.remove(aUser);
        };
    }

}
