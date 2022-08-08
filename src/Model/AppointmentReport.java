/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 * @author mriedel
 */

/**
 * Class created to have object to place in list for Appointment Types by Month Report
 */


public class AppointmentReport {

    private String month;
    private String amount;
    private String type;

    /**
     * constructor
     *
     * @param month field for appointment report
     * @param type type of appointment for report
     * @param amount number of appointments for type of appointments report
     */
    public AppointmentReport(String month, String type, String amount) {
        this.month = month;
        this.type = type;
        this.amount = amount;
    }

    /**
     *
     * @return s month for report
     */
    public String getMonth() {
        return month;
    }

    /**
     *
     * @param month sets month for reports
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     *
     * @return s appointment type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type sets appointment type for reports
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return s number of appointments by type for report
     */
    public String getAmount() {
        return amount;
    }

    /**
     *
     * @param amount sets number of appointments per type
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }


}
