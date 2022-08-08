/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;


/**
 * @author mried
 */


/**
 * This class is for the User fields and 
 * includes the setters and getters
 */

public class User {

    /**
     * System Users ID
     */
    public String User_ID;

    /**
     * System Users Name
     */
    public String User_Name;

    /**
     * System Users Password
     */
    public String Password;


    /**
     * constructor
     *
     * @param User_ID ID of program user
     * @param User_Name Username of program user
     * @param Password Password of program user
     */
    public User(String User_ID, String User_Name, String Password) {
        this.User_ID = User_ID;
        this.User_Name = User_Name;
        this.Password = Password;
    }

    /**
     *
     * @return s User_ID
     */
    public String getUser_ID() {
        return User_ID;
    }

    /**
     *
     * @param User_ID sets User_ID
     */
    public void setUser_ID(String User_ID) {
        this.User_ID = User_ID;
    }

    /**
     *
     * @return s User name
     */
    public String getUser_Name() {
        return User_Name;
    }

    /**
     *
     * @param User_Name sets User name
     */
    public void setUser_Name(String User_Name) {
        this.User_Name = User_Name;
    }

    /**
     *
     * @return s User Password
     */
    public String getPassword() {
        return Password;
    }

    /**
     *
     * @param Password sets User password
     */
    public void setPassword(String Password) {
        this.Password = Password;
    }

    /**
     *
     * @param User_ID sets user ID
     * @param User_Name sets Username
     * @param Password sets Userpasswrd
     */
    public void setUser(String User_ID, String User_Name, String Password) {
    }
}

