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
public class Users {

    public static List<User> tUsers;

    static{
        tUsers = new ArrayList<User>();
        tUsers.add(new User("+46700447531", "Stefan", "+46700447531", "stefan.ramilli@gmail.com"));
        tUsers.add(new User("+393496191740", "Stefan", "+393496191740", "stefan.ramilli@gmail.com"));
        tUsers.add(new User("+393471768654", "Ezio", "+393471768654", "ezio.ramilli@gmail.com"));
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
