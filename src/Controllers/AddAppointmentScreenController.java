/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Model.*;
import Utilities.Connection.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;


/**
 * FXML Controller class for adding appointments
 *
 * @author mried
 */
public class AddAppointmentScreenController implements Initializable {

    // customer table
    @FXML
    private TableView<Customer> tblCustomerSelect;

    // customer table - columns
    @FXML
    private TableColumn<Customer, String> colCustomerName;

    // fields
    @FXML
    private TextField fieldApptID;
    @FXML
    private TextField fieldTitle;
    @FXML
    private TextField fieldLocation;
    @FXML
    private TextField fieldDescription;
    @FXML
    private TextField fieldType;

    // start date picker and time
    @FXML
    private DatePicker fieldStartDate;
    @FXML
    private ComboBox<String> cbxStartHour;
    @FXML
    private ComboBox<String> cbxStartMinute;

    // end date picker and time
    @FXML
    private DatePicker fieldEndDate;
    @FXML
    private ComboBox<String> cbxEndHour;
    @FXML
    private ComboBox<String> cbxEndMinute;

    // search field for customer
    @FXML
    private TextField fieldSearchCustomer;

    // buttons
    @FXML
    private Button btnSaveAppt;
    @FXML
    private Button btnDeleteAppt;

    // combobox
    @FXML
    private ComboBox<Contacts> cbxContact;

    // custom variables
    private Appointment savedAppt;
    private AppointmentScreenController apptScreenController;


    /**
     * Initializes the controller class.
     *
     * @param url url
     * @param rb resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        onLoadInitData();
    }

    /**
     * This method loads the customer list
     */
    private void onLoadCustomerTable() {
       
        try {
            String search = fieldSearchCustomer.getText();

            String sql = "SELECT * FROM customers WHERE Customer_Name LIKE '%" + search + "%' ORDER BY Customer_Name";

            PreparedStatement statement = DBConnection.getConn().prepareStatement(
                    sql);

            ResultSet rs = statement.executeQuery();

            ObservableList<Customer> listCustomers = FXCollections.observableArrayList();

            while (rs.next()) {
                String customerID = rs.getString("Customer_ID");
                String customerName = rs.getString("Customer_Name");

                listCustomers.add(new Customer(customerID, customerName));
            }

            tblCustomerSelect.setEditable(false);

            colCustomerName.setCellValueFactory(new PropertyValueFactory<>("Customer_Name"));

            tblCustomerSelect.setItems(listCustomers);

        } catch (SQLException sqe) {
            System.out.println("Check your SQL");
            sqe.printStackTrace();
        } catch (Exception e) {
            System.out.println("Something besides the SQL went wrong.");
        }
    }

    /**
     * This method loads the hours list, minutes list, and contact list
     * for creating appointments
     */
    private void onLoadInitData() {

        ObservableList<String> listHour = FXCollections.observableArrayList();
        for (int i = 0; i < 24; i++) {
            String val = i + "";
            if (i < 10) {
                val = "0" + i;
            }

            listHour.add(val);
        }

        ObservableList<String> listMinute = FXCollections.observableArrayList();
        for (int i = 0; i < 60; i++) {
            String val = i + "";
            if (i < 10) {
                val = "0" + i;
            }

            listMinute.add(val);
        }

        cbxStartHour.setItems(listHour);
        cbxEndHour.setItems(listHour);

        cbxStartHour.setValue(listHour.get(0));
        cbxEndHour.setValue(listHour.get(0));

        cbxStartMinute.setItems(listMinute);
        cbxEndMinute.setItems(listMinute);

        cbxStartMinute.setValue(listMinute.get(0));
        cbxEndMinute.setValue(listHour.get(0));

        // load contact list
        try {
            String sql = "SELECT * FROM contacts ORDER BY Contact_Name";

            PreparedStatement statement = DBConnection.getConn().prepareStatement(
                    sql);

            ResultSet rs = statement.executeQuery();

            ObservableList<Contacts> listContacts = FXCollections.observableArrayList();

            ObservableList<Appointment> appointList = FXCollections.observableArrayList();

            while (rs.next()) {
                String contactId = rs.getString("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String contactEmail = rs.getString("Email");

                listContacts.add(new Contacts(contactId, contactName, contactEmail));
            }

            cbxContact.setItems(listContacts);

        } catch (SQLException sqe) {
            System.out.println("Check your SQL");
            sqe.printStackTrace();
        } catch (Exception e) {
            System.out.println("Something besides the SQL went wrong.");
        }

        onLoadCustomerTable();
    }

    /**
     * This method saves the appointments and inserts into the database
     * @throws SQLException
     */
    @FXML
    private void handleSave() throws SQLException {
        if (!checkValidateForm()) {
            return;
        }

        String addBtnText = btnSaveAppt.getText();

        if (addBtnText.equals("Add")) {

            PreparedStatement ps = DBConnection.getConn().prepareStatement("INSERT INTO appointments (Title, " +
                    "Description, Location, `Type`, `Start`, `End`, Customer_ID, User_ID, Contact_ID)" +
                    " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, fieldTitle.getText());
            ps.setString(2, fieldDescription.getText());
            ps.setString(3, fieldLocation.getText());
            ps.setString(4, fieldType.getText());

            String startDate = fieldStartDate.getValue().format(Main.getFormat("yyyy-MM-dd"));
            String startTime = cbxStartHour.getValue() + ":" + cbxStartMinute.getValue();
            LocalDateTime startDateTime = LocalDateTime.parse(startDate + " " + startTime, Main.sqlFormatter);
            String strStartDateTime = Main.getZoneDateTimeFormat(startDateTime, Main.sqlFormatter);

            ps.setString(5, strStartDateTime);

            String endDate = fieldEndDate.getValue().format(Main.getFormat("yyyy-MM-dd"));
            String endTime = cbxEndHour.getValue() + ":" + cbxEndMinute.getValue();
            LocalDateTime endDateTime = LocalDateTime.parse(endDate + " " + endTime, Main.sqlFormatter);
            String strEndDateTime = Main.getZoneDateTimeFormat(endDateTime, Main.sqlFormatter);
            ps.setString(6, strEndDateTime);

            Customer customer = tblCustomerSelect.getSelectionModel().getSelectedItem();
            ps.setString(7, customer.getCustomer_ID());
            ps.setString(8, Main.user.getUser_ID());
            ps.setString(9, cbxContact.getValue().getContact_ID());

            if (ps.executeUpdate() == 1) {

                Main.showAlert("Success!!", "Successfully Added!", "success");
                onRefreshTable();

                handleResetForm();
            }
        } else {
            PreparedStatement ps = DBConnection.getConn().prepareStatement(
                    "UPDATE appointments SET Title = ?," +
                            " Description = ?, Location = ?, `Type` = ?, `Start` = ?, `End` = ?," +
                            " Customer_ID = ?, User_ID = ?, Contact_ID = ?" +
                            " WHERE Appointment_ID = ?");
            ps.setString(1, fieldTitle.getText());
            ps.setString(2, fieldDescription.getText());
            ps.setString(3, fieldLocation.getText());
            ps.setString(4, fieldType.getText());

            String startDate = fieldStartDate.getValue().format(Main.getFormat("yyyy-MM-dd"));
            String startTime = cbxStartHour.getValue() + ":" + cbxStartMinute.getValue();
            LocalDateTime startDateTime = LocalDateTime.parse(startDate + " " + startTime, Main.sqlFormatter);
            String strStartDateTime = Main.getZoneDateTimeFormat(startDateTime, Main.sqlFormatter);

            ps.setString(5, strStartDateTime);

            String endDate = fieldEndDate.getValue().format(Main.getFormat("yyyy-MM-dd"));
            String endTime = cbxEndHour.getValue() + ":" + cbxEndMinute.getValue();
            LocalDateTime endDateTime = LocalDateTime.parse(endDate + " " + endTime, Main.sqlFormatter);
            String strEndDateTime = Main.getZoneDateTimeFormat(endDateTime, Main.sqlFormatter);

            ps.setString(6, strEndDateTime);

            Customer customer = tblCustomerSelect.getSelectionModel().getSelectedItem();
            ps.setString(7, customer.getCustomer_ID());
            ps.setString(8, Main.user.getUser_ID());
            ps.setString(9, cbxContact.getValue().getContact_ID());
            ps.setString(10, this.savedAppt.getAppointment_ID());

            if (ps.executeUpdate() == 1) {

                Main.showAlert("Success!!", "Successfully Updated!", "success");
                onRefreshTable();

                this.savedAppt.setTitle(fieldTitle.getText());
                this.savedAppt.setDescription(fieldDescription.getText());

                this.savedAppt.setLocation(fieldLocation.getText());
                this.savedAppt.setType(fieldType.getText());

                this.savedAppt.setCustomer_ID(customer.getCustomer_ID());
                this.savedAppt.setUser_ID(Main.user.getUser_ID());
                this.savedAppt.setContact_ID(cbxContact.getValue().getContact_ID());
            }
        }
    }

    /**
     * cancel button event function
     */
    @FXML
    void handleCancel() {
        if (this.apptScreenController != null) {
            this.apptScreenController.onCancel();
        }
    }

    /**
     * function to search customers, when enter or click search button
     */
    @FXML
    private void handleSearchCustomer() {
        onLoadCustomerTable();
    }

    /**
     * This method deletes a selected appointment
     */
    @FXML
    private void handleDeleteAppt() {

        if (this.savedAppt != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete Appointment " + this.savedAppt.getAppointment_ID() + " "
                    + this.savedAppt.getType() + "?");
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {
                                deleteAppt(this.savedAppt);
                                onRefreshTable();
                                this.savedAppt = null;
                                fieldApptID.clear();
                                btnSaveAppt.setText("Add");
                                btnDeleteAppt.setDisable(true);
                                handleResetForm();
                            }
                    );
        } else {
            Main.showAlert("No Appointment selected for Deletion", "Please select an appointment in the Table to delete", "warning");
        }
    }

    /**
     * This method update appointment information
     *
     * @param appt
     */
    private void updateFormInfo(Appointment appt) {
        fieldApptID.setText(appt.getAppointment_ID());
        fieldTitle.setText(appt.getTitle());
        fieldLocation.setText(appt.getLocation());
        fieldDescription.setText(appt.getDescription());
        Contacts contact = new Contacts(appt.getContact_ID(), appt.getContact_Name(), "");
        cbxContact.setValue(contact);
        fieldType.setText(appt.getType());

        LocalDateTime startDateTime = appt.getStart();

        fieldStartDate.setValue(appt.getStart().toLocalDate());
        cbxStartHour.setValue(startDateTime.format(Main.getFormat("HH")));
        cbxStartMinute.setValue(startDateTime.format(Main.getFormat("mm")));

        LocalDateTime endDateTime = appt.getEnd();

        fieldEndDate.setValue(appt.getEnd().toLocalDate());
        cbxEndHour.setValue(endDateTime.format(Main.getFormat("HH")));
        cbxEndMinute.setValue(endDateTime.format(Main.getFormat("mm")));

        for (int i = 0; i < tblCustomerSelect.getItems().size(); i++) {
            Customer customer1 = tblCustomerSelect.getItems().get(i);
            if (customer1.getCustomer_ID().equals(appt.getCustomer_ID())) {
                tblCustomerSelect.getSelectionModel().select(i);
                break;
            }
        }

        btnSaveAppt.setText("Update");
        btnDeleteAppt.setDisable(false);
    }

    /**
     * @param apptController appointment controller
     * @param appt appointments
     */
    public void setInitInfo(AppointmentScreenController apptController, Appointment appt) {

        this.apptScreenController = apptController;

        if (appt != null) {
            this.savedAppt = appt;
            updateFormInfo(appt);
        } else {
            this.savedAppt = null;

            fieldApptID.clear();
            btnSaveAppt.setText("Add");
            btnDeleteAppt.setDisable(true);
        }
    }

    /**
     * This method refreshes the appointment table
     */
    private void onRefreshTable() {

        if (this.apptScreenController != null) {
            apptScreenController.onLoadTable();
        }
    }

    /**
     * This method clears the appointment form fields
     */
    @FXML
    private void handleResetForm() {

        if (btnSaveAppt.getText().equals("Add")) {
            fieldApptID.clear();
            fieldLocation.clear();
            fieldType.clear();
            fieldDescription.clear();
            fieldTitle.clear();
        } else {
            updateFormInfo(this.savedAppt);
        }
    }

    /**
     * This method Deletes selected customer from appointments table
     *
     * @param appt
     */
    private void deleteAppt(Appointment appt) {

        try {
            PreparedStatement pst = DBConnection.getConn().prepareStatement("DELETE from appointments WHERE Appointment_ID = ?");
            pst.setString(1, appt.getAppointment_ID());
            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Validates Customer details to ensure no missing or invalid data
     *
     * @return true if no errors
     */
    private boolean checkValidateForm() {
        String title = fieldTitle.getText();
        Contacts contact = cbxContact.getValue();

        Customer customer = tblCustomerSelect.getSelectionModel().getSelectedItem();

        String errorMessage = "";
        //first checks to see if inputs are null
        if (title == null || title.length() == 0) {
            errorMessage += "Please enter the Appointment's title.\n";
        }
        if (contact == null) {
            errorMessage += "Please select contact.\n";
        }
        if (customer == null) {
            errorMessage += "Please Select customer.\n";
        }

        // check time
        LocalDate startDate = fieldStartDate.getValue();

        if (startDate == null) {
            errorMessage += "Please select Start Date.\n";
        }

        String startTime = cbxStartHour.getValue() + ":" + cbxStartMinute.getValue();

        LocalDate endDate = fieldEndDate.getValue();
        String endTime = cbxEndHour.getValue() + ":" + cbxEndMinute.getValue();

        if (endDate == null) {
            errorMessage += "Please select End Date.\n";
        }

        if (startDate != null && endDate != null) {
            String strStartDateTime = startDate.format(Main.getFormat("yyyy-MM-dd")) + " " + startTime;
            String strEndDateTime = endDate.format(Main.getFormat("yyyy-MM-dd")) + " " + endTime;

            if (strEndDateTime.compareTo(strStartDateTime) < 0) {
                errorMessage += "End time should be later than start time.\n";
            } else {
                if (endTime.compareTo("22:00") > 0 || startTime.compareTo("08:00") < 0) {
                    errorMessage += "Please only select times between 08:00 - 22:00";
                }
            }

            if (errorMessage.equals("")) {

                // check from database
                LocalDateTime startLocalDateTime = LocalDateTime.parse(strStartDateTime, Main.sqlFormatter);
                String startZoneDateTime = Main.getZoneDateTimeFormat(startLocalDateTime, Main.sqlFormatter);

                LocalDateTime endLocalDateTime = LocalDateTime.parse(strEndDateTime, Main.sqlFormatter);
                String endZoneDateTime = Main.getZoneDateTimeFormat(endLocalDateTime, Main.sqlFormatter);

                PreparedStatement ps = null;
                try {
                    String sql = "SELECT * FROM appointments WHERE ((Start <= ? AND End > ?)" +
                            " OR (Start < ? AND End >= ?)) AND User_ID = ?";

                    if (this.savedAppt != null) {
                        // means update
                        sql += " AND Appointment_ID != " + this.savedAppt.getAppointment_ID();
                    }

                    ps = DBConnection.getConn().prepareStatement(sql);

                    ps.setString(1, startZoneDateTime);
                    ps.setString(2, startZoneDateTime);
                    ps.setString(3, endZoneDateTime);
                    ps.setString(4, endZoneDateTime);

                    ps.setString(5, Main.user.getUser_ID());


                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        errorMessage += "Appointment overlaps with another";
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }

        if (errorMessage.equals("")) {
            return true;
        }

        // Show the error message.
        Main.showAlert("Please correct Appointment fields", errorMessage, "error");
        return false;
    }

}
