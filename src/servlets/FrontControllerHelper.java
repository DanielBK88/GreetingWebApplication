package servlets;

import entities.User;
import persistence.UserPersistenceManager;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This is a singleton class used by the FrontControllerServlet.
 * All the servlet's functionality which is not directly related to processing requests is capsuled here,
 * like for example the logic validating a new username or sending requests to the database.
 */
public class FrontControllerHelper {

    private UserPersistenceManager persistenceManager;
    private static FrontControllerHelper SINGLETON_INSTANCE;

    private FrontControllerHelper(){
        persistenceManager = UserPersistenceManager.getInstance();
    }

    static FrontControllerHelper getInstance(){
        if(SINGLETON_INSTANCE == null){
            SINGLETON_INSTANCE = new FrontControllerHelper();
        }
        return SINGLETON_INSTANCE;
    }

    /**
     * Requests a lookup in the database for a user with the given name and password.
     * Returns the found user or null, if no matching user was found.
     */
    User checkSignInData(String name, String password){
        return persistenceManager.findUser(name, password);
    }

    /**
     * First requests a lookup in the database if a user with the specified name already exists.
     * If yes, returns null.
     * Otherwise creates a new user, sends a request to persist it and returns the new created user.
     */
    User createNewUser(String name, String password, String remoteIP){

        User userWithSameName = persistenceManager.findUser(name, null);
        if(userWithSameName == null){
            User newUser = new User(name, password, remoteIP);
            int id = persistenceManager.persistUser(newUser);
            newUser.setId(id);
            return newUser;
        }
        return null;
    }

    /**
     * Calls updateUser() on the persistenceManager to update the persisted user's fields accordingly.
     */
    void updateUser(User user){
        persistenceManager.updatetUser(user);
    }


    /**
     * Checks if the specified username and password are ok according to the requirements for a valid username and password
     * and if the password equals to the password confirmation String.
     * Returns null, if this is the check succeeds.
     * Otherwise returns a String that will be displayed to the user, telling what exactly is not ok.
     */
    String userNameAndPasswordMatchTheRequirements(String name, String password, String confrirmPassowrd){

        // Check if password matches confirmation
        if(!password.equals(confrirmPassowrd)) {return "Пароль и повтор пароля не совпадают!"; }

        // Remove spaces in the beginning and the end of the username
        StringBuilder builder = new StringBuilder(name);
        while(builder.charAt(0) == ' ') { builder.deleteCharAt(0); }
        while(builder.charAt(builder.length() - 1) == ' ') { builder.deleteCharAt(builder.length() - 1); }
        name = builder.toString();

        // Check username length
        if(name.length() <= 4){ return "Имя пользователя должно быть длиннее 4 символов."; }

        // Check иф the username contains only latin characters, numbers and dots
        for(char character : name.toCharArray()){
            int unicodeID = (int) character;
            if(!((unicodeID == 46) // dot character
                    || ((unicodeID >= 97) && (unicodeID <= 122))  // lower case latin characters
                    || ((unicodeID >= 65) && (unicodeID <= 90)) // upper case latin characters
                    || ((unicodeID >= 48) && (unicodeID <= 57)) // numeric characters
            )) {
                return "Имя пользователя должно состоять из цифр, букв английского алфавита и точек";
            }
        }

        // Check if the password complexity is sufficient
        if(password.length() < 8){ return "Пароль должен быть длиннее 8 символов."; }
        boolean lowerCaseCharFound = false;
        boolean upperCaseCharFound = false;
        boolean numericCharFound = false;
        for(char character : password.toCharArray()){
            int unicodeID = (int) character;
            if(((unicodeID >= 97) && (unicodeID <= 122)) || ((unicodeID >= 1072) && (unicodeID <= 1103)) || (unicodeID == 1105)) {lowerCaseCharFound = true;}
            if(((unicodeID >= 65) && (unicodeID <= 90)) || ((unicodeID >= 1040) && (unicodeID <= 1071)) || (unicodeID == 1125)) {upperCaseCharFound = true;}
            if((unicodeID >= 48) && (unicodeID <= 57)) {numericCharFound = true;}
        }
        if(!(lowerCaseCharFound && upperCaseCharFound && numericCharFound)){
            return "Пароль недостаточно сложен: Должны быть цифры, заглавные и строчные буквы.";
        }

        return null;
    }

    int increaseAndGetViewsCount(){
        return persistenceManager.increaseAndGetViewsCounter().getCount();
    }
}
