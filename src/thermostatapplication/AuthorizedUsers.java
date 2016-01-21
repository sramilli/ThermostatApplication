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

    public static List<User> tUsers;

    static{
        tUsers = new ArrayList<User>();
        tUsers.add(new User("Stefan1", "+46700447531", "stefan.ramilli@gmail.com"));
        tUsers.add(new User("Stefan2", "+393496191740", "stefan.ramilli@gmail.com"));
        tUsers.add(new User("Ezio", "+393471768654", "ezio.ramilli@gmail.com"));
    }

    public static List<User> getAllUsers() {
        return tUsers;
    }
    
    public static void addAuthorizedUser(String aMobileNr){
        //TODO to complete
        System.out.println("Authorizing number ["+aMobileNr+"] ");
        tUsers.add(new User("", aMobileNr, ""));
    }
    
    public static boolean isAuthorized(String aMobileNr){
        if (getUser(aMobileNr) == null) return false;
        return true;
    }
    
    public static User getUser(String aMobileNr){
        if (aMobileNr == null || "".equals(aMobileNr)){
            System.out.println("User not found: invalid parameter!");
            return null;
        }
        for (User u: tUsers){
            if (aMobileNr.equals(u.getMobileNr())){
                return new User(u.getName(), u.getMobileNr(), u.getEmail());
            }
        }
        return null;
    }

}
