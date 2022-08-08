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
 * This class is for the Contacts fields and 
 * includes the setters and getters
 */
public class Contacts {

    private String Contact_ID;
    private String Contact_Name;
    private String Contact_Email;

    /**
     * constructor
     *
     * @param contactId ID of Consultant
     * @param contactName Name of Consultant
     * @param contactEmail Email for Consultant
     */
    public Contacts(String contactId, String contactName, String contactEmail) {
        this.Contact_ID = contactId;
        this.Contact_Name = contactName;
        this.Contact_Email = contactEmail;
    }

    /**
     *
     * @param contactId ID for Consultant
     * @param contactName Name for Consultant
     */
    public Contacts(String contactId, String contactName) {
        this.Contact_ID = contactId;
        this.Contact_Name = contactName;
    }

    /**
     *
     * @return s contacts name to string
     */
    @Override
    public String toString() {
        return Contact_Name;
    }

    /**
     *
     * @return s Contacts email address
     */
    public String getContact_Email() {
        return Contact_Email;
    }

    /**
     *
     * @param contact_Email sets Contacts email address
     */
    public void setContact_Email(String contact_Email) {
        Contact_Email = contact_Email;
    }

    /**
     *
     * @return s Contacts id
     */
    public String getContact_ID() {
        return Contact_ID;
    }

    /**
     *
     * @param contact_ID sets Contacts id
     */
    public void setContact_ID(String contact_ID) {
        Contact_ID = contact_ID;
    }

    /**
     *
     * @return s Contacts Name
     */
    public String getContact_Name() {
        return Contact_Name;
    }

    /**
     *
     * @param contact_Name sets Contacts Name
     */
    public void setContact_Name(String contact_Name) {
        Contact_Name = contact_Name;
    }
}
