/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermostatapplication.entity;

import java.util.ArrayList;
import java.util.List;
import thermostatapplication.properties.ThermostatProperties;

/**
 *
 * @author Ste
 */
public class Users {

    public static List<User> tUsers;

    static{
        tUsers = new ArrayList<User>();
        String[] usr1 = ThermostatProperties.USER_1.split(",");
        String[] usr2 = ThermostatProperties.USER_2.split(",");
        String[] usr3 = ThermostatProperties.USER_3.split(",");
        tUsers.add(new User(usr1[1], usr1[0], usr1[1], usr1[2]));
        tUsers.add(new User(usr2[1], usr2[0], usr2[1], usr2[2]));
        tUsers.add(new User(usr3[1], usr3[0], usr3[1], usr3[2]));
    }

    public static List<User> getUsers() {
        return tUsers;
    }
    
    public static void addAuthorizedUser(String aMobileNr){
        //TODO to complete
        System.out.println("Authorizing number ["+aMobileNr+"] ");
        tUsers.add(new User(aMobileNr, "", aMobileNr, ""));
    }
    
    public static boolean isAuthorized(User aUser){
        if (getUser(aUser.getID()) == null) return false;
        return true;
    }
    
    public static User getUser(String aID){
        if (aID == null || "".equals(aID)){
            System.out.println("User not found: invalid parameter!");
            return null;
        }
        for (User u: tUsers){
            if (aID.equals(u.getID())){
                return new User(u.getID(), u.getName(), u.getMobileNr(), u.getEmail());
            }
        }
        return null;
    }
    
}
