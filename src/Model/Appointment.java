/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.time.LocalDateTime;


/**
 * @author mriedel
 */

/**
 * This class is for the Appointment fields and 
 * includes the setters and getters
 */
public class Appointment {

    private String Appointment_ID;
    private String Title;
    private String Description;
    private LocalDateTime Start;
    private LocalDateTime End;

    private String strStart;
    private String strEnd;

    private String Location;
    private String Type;

    private String Customer_ID;
    private String Customer_Name;

    private String User_ID;
    private String User_Name;

    private String Contact_ID;
    private String Contact_Name;
    private String Contact_Email;

    /**
     * constructor
     *
     * @param apptID appointment ID
     * @param title appointment Title
     * @param description appointment Description
     * @param location appointment Location
     * @param type appointment Type
     * @param start appointment Start time
     * @param end appointment End Time
     * @param customerID appointment Customer ID
     * @param customerName appointment Customer Name
     * @param userID logged in UserID
     * @param userName logged in username
     * @param contactID Appointment contact ID
     * @param contactName Appointment contact name
     * @param contactEmail Appointment Contact Email
     */
    public Appointment(String apptID, String title, String description,
                       String location, String type,
                       LocalDateTime start, LocalDateTime end,
                       String customerID, String customerName,
                       String userID, String userName,
                       String contactID, String contactName, String contactEmail
    ) {
        this.Appointment_ID = apptID;
        this.Title = title;
        this.Description = description;
        this.Location = location;
        this.Type = type;
        this.Start = start;
        this.End = end;

        this.strStart = start.format(Main.showFormatter);
        this.strEnd = end.format(Main.showFormatter);

        this.Customer_ID = customerID;
        this.Customer_Name = customerName;
        this.User_ID = userID;
        this.User_Name = userName;
        this.Contact_ID = contactID;
        this.Contact_Name = contactName;
        this.Contact_Email = contactEmail;
    }

    /**
     * get string value for show to alert
     * @return s appointment information to alert
     */
    public String getShowVal() {
        String res = "";
        res += "Title: " + Title + "\n";
        res += "Duration: " + strStart + " - " + strEnd + "\n";
        res += "Customer: " + Customer_Name + "\n";
        res += "Contact Name: " + Contact_Name + "\n";
        res += "Contact Email: " + Contact_Email + "\n";
        res += "Location: " + Location + "\n";
        res += "Type: " + Type + "\n";
        res += "Description: " + Description + "\n";

        return res;
    }

    /**
     *
     * @return s the appointment ID
     */
    public String getAppointment_ID() {
        return Appointment_ID;
    }

    /**
     *
     * @param appointment_ID  sets the appointment ID
     */
    public void setAppointment_ID(String appointment_ID) {
        Appointment_ID = appointment_ID;
    }

    /**
     *
     * @return s the location
     */
    public String getLocation() {
        return Location;
    }

    /**
     *
     * @param location sets the location
     */
    public void setLocation(String location) {
        Location = location;
    }

    /**
     *
     * @return the appointment type
     */
    public String getType() {
        return Type;
    }

    /**
     *
     * @param type sets the appointment type
     */
    public void setType(String type) {
        Type = type;
    }

    /**
     *
     * @return s the customers ID
     */
    public String getCustomer_ID() {
        return Customer_ID;
    }

    /**
     *
     * @param customer_ID sets the customers ID
     */
    public void setCustomer_ID(String customer_ID) {
        Customer_ID = customer_ID;
    }

    /**
     *
     * @return s the user ID
     */
    public String getUser_ID() {
        return User_ID;
    }

    /**
     *
     * @param user_ID sets the User ID
     */
    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
    }

    /**
     *
     * @return s the contact ID
     */
    public String getContact_ID() {
        return Contact_ID;
    }

    /**
     *
     * @param contact_ID sets the Contact ID
     */
    public void setContact_ID(String contact_ID) {
        Contact_ID = contact_ID;
    }

    /**
     *
     * @return s the Contact name
     */
    public String getContact_Name() {
        return Contact_Name;
    }

    /**
     *
     * @param contact_Name sets the Contact name
     */
    public void setContact_Name(String contact_Name) {
        Contact_Name = contact_Name;
    }

    /**
     *
     * @return the Title
     */
    public String getTitle() {
        return Title;
    }

    /**
     *
     * @param title sets the Title
     */
    public void setTitle(String title) {
        Title = title;
    }

    /**
     *
     * @return s the appointment description
     */
    public String getDescription() {
        return Description;
    }

    /**
     *
     * @param description sets the appointment description
     */
    public void setDescription(String description) {
        Description = description;
    }

    /**
     *
     * @return s the customer name
     */
    public String getCustomer_Name() {
        return Customer_Name;
    }

    /**
     *
     * @param customer_Name sets the customer name
     */
    public void setCustomer_Name(String customer_Name) {
        Customer_Name = customer_Name;
    }

    /**
     *
     * @return s the username
     */
    public String getUser_Name() {
        return User_Name;
    }

    /**
     *
     * @param user_Name sets the username
     */
    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    /**
     *
     * @return s the contacts email address
     */
    public String getContact_Email() {
        return Contact_Email;
    }

    /**
     *
     * @param contact_Email sets the contacts email address
     */
    public void setContact_Email(String contact_Email) {
        Contact_Email = contact_Email;
    }

    /**
     *
     * @return s the local start time
     */
    public LocalDateTime getStart() {
        return Start;
    }

    /**
     *
     * @param start sets the local start time
     */
    public void setStart(LocalDateTime start) {
        Start = start;
    }

    /**
     *
     * @return s the local end time
     */
    public LocalDateTime getEnd() {
        return End;
    }

    /**
     *
     * @param end sets the local end time
     */
    public void setEnd(LocalDateTime end) {
        End = end;
    }

    /**
     *
     * @return s appointment start time
     */
    public String getStrStart() {
        return strStart;
    }

    /**
     *
     * @param strStart sets appointment start time
     */
    public void setStrStart(String strStart) {
        this.strStart = strStart;
    }

    /**
     *
     * @return s the appointment end time
     */
    public String getStrEnd() {
        return strEnd;
    }

    /**
     *
     * @param strEnd sets appointment end time
     */
    public void setStrEnd(String strEnd) {
        this.strEnd = strEnd;
    }
}
