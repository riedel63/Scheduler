/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Model.Appointment;
import Model.Contacts;
import Model.Main;
import Utilities.Connection.DBConnection;

import java.net.URL;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class for Appointment Screen
 *
 * @author mried
 */
public class AppointmentScreenController implements Initializable {

    // table
    @FXML
    private TableView<Appointment> tblAppt;

    // table - columns
    @FXML
    private TableColumn<Appointment, String> colApptID;
    @FXML
    private TableColumn<Appointment, String> colApptStart;
    @FXML
    private TableColumn<Appointment, String> colApptEnd;
    @FXML
    private TableColumn<Appointment, String> colApptLocation;
    @FXML
    private TableColumn<Appointment, String> colApptTitle;
    @FXML
    private TableColumn<Appointment, String> colApptDescription;
    @FXML
    private TableColumn<Appointment, String> colApptType;
    @FXML
    private TableColumn<Appointment, String> colApptCustomerID;
    @FXML
    private TableColumn<Appointment, String> colApptContactName;
    @FXML
    private TableColumn<Appointment, String> colUserID;


    @FXML
    private AnchorPane apptDetailsAnchor;


    // radio buttons
    @FXML
    private RadioButton radioWeek;
    @FXML
    private RadioButton radioMonth;

    // combo box

    /**
     * Contacts combo box
     */
    @FXML
    public ComboBox<Contacts> contactComboBox;

  
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     * lambda function to get appointment selection
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // lambda function to get appointment selection
        tblAppt.setOnMouseClicked(tv -> {
            Appointment selectedAppt = tblAppt.getSelectionModel().getSelectedItem();
            if (selectedAppt != null) {
                setInitAddApptScreenController(selectedAppt);
            }
        });

        onLoadTable();
    }

    /**
     * This method gets appointment list from database
     * radio buttons allow view by week or month
     * @param bConsiderRadio  radio buttons to filter by week or month
     * @return s appointment list filtered by week or month
     */
    public ObservableList<Appointment> getAppointmentList(boolean bConsiderRadio) {
        ObservableList<Appointment> resList = FXCollections.observableArrayList();

        try {



            Connection connection = DBConnection.getConn();

            String sql = "SELECT a.*, cu.Customer_Name as Customer_Name, u.User_Name as User_Name, co.Contact_ID as Contact_ID, co.Contact_Name as Contact_Name, co.Email as Contact_Email" +
                    " FROM appointments as a" +
                    " LEFT JOIN customers as cu ON cu.Customer_ID = a.Customer_ID" +
                    " LEFT JOIN users as u ON u.User_ID = a.User_ID" +
                    " LEFT JOIN contacts as co ON co.Contact_ID = a.Contact_ID" +
                    " WHERE a.User_ID = " + Main.user.getUser_ID();

            if (bConsiderRadio) {
                String curTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Timestamp(System.currentTimeMillis()));
                LocalDateTime tempLocalDateTime =  LocalDateTime.parse(curTime, Main.sqlFormatter);
                String strCurTime = Main.getZoneDateTimeFormat(tempLocalDateTime, Main.sqlFormatter);
                if (radioWeek.isSelected()) {
                    sql += " AND `Start` <= '" + strCurTime + "' + INTERVAL 7 day";
                }

                if (radioMonth.isSelected()) {
                    sql += " AND `Start` <= '" + strCurTime + "' + INTERVAL 1 month";
                }
            }

            ResultSet rs = connection.createStatement().executeQuery(sql);

            while (rs.next()) {
                String apptID = rs.getString("Appointment_ID");
                String apptTitle = rs.getString("Title");
                String apptDescription = rs.getString("Description");
                String apptLocation = rs.getString("Location");
                String apptType = rs.getString("Type");

                Timestamp tsStart = rs.getTimestamp("Start");
                LocalDateTime apptStart = tsStart.toLocalDateTime();

                Timestamp tsEnd = rs.getTimestamp("End");
                LocalDateTime apptEnd = tsEnd.toLocalDateTime();

                String customerID = rs.getString("Customer_ID");
                String customerName = rs.getString("Customer_Name");

                String userID = rs.getString("User_ID");
                String userName = rs.getString("User_Name");

                String contactID = rs.getString("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String contactEmail = rs.getString("Contact_Email");

                Appointment appt = new Appointment(apptID, apptTitle, apptDescription,
                        apptLocation, apptType, apptStart, apptEnd, customerID,
                        customerName, userID, userName, contactID, contactName, contactEmail
                );

                resList.add(appt);
            }

        } catch (SQLException ex) {
            Logger.getLogger(UsersScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resList;
    }

    /**
     * This method loads the appointments table
     */
    public void onLoadTable() {
        ObservableList<Appointment> apptList = getAppointmentList(true);
        tblAppt.setEditable(false);

        colApptID.setCellValueFactory(new PropertyValueFactory<>("Appointment_ID")); //Non editable field
        colApptTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
        colApptDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        colApptLocation.setCellValueFactory(new PropertyValueFactory<>("Location"));
        colApptType.setCellValueFactory(new PropertyValueFactory<>("Type"));

        colApptStart.setCellValueFactory(new PropertyValueFactory<>("strStart"));
        colApptEnd.setCellValueFactory(new PropertyValueFactory<>("strEnd"));
        colApptCustomerID.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
        colApptContactName.setCellValueFactory(new PropertyValueFactory<>("Contact_Name"));

        colUserID.setCellValueFactory(new PropertyValueFactory<>("User_ID"));;

        tblAppt.setItems(apptList);
    }

    /**
     * Filters to show appointments from current date to a month out
     *
     * @param event
     */
    @FXML
    void handleApptMonth(ActionEvent event) {
        radioWeek.setSelected(false);

        onLoadTable();
    }

    /**
     * Filters to show appointments from current date to a week out
     *
     * @param event
     */
    @FXML
    void handleApptWeek(ActionEvent event) {
        radioMonth.setSelected(false);
        onLoadTable();
    }

    /**
     * This method loads appointment screen and gives controller access to main app
     */
    private void setInitAddApptScreenController(Appointment appt) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/AddAppointmentScreen.fxml"));

        AnchorPane pane = null;
        try {
            pane = loader.load();

            apptDetailsAnchor.setVisible(true);
            apptDetailsAnchor.getChildren().setAll(pane);

            // Gives the controller access to the main app.
            AddAppointmentScreenController controller = loader.getController();
            controller.setInitInfo(this, appt);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is for the cancel function
     */
    public void onCancel() {
        apptDetailsAnchor.setVisible(false);
    }

    /**
     * This method is for event function that calls when click add appointment button
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void handleAddAppt(ActionEvent event) throws IOException {
        setInitAddApptScreenController(null);
    }

    /**
     * This method is for event function that calls when click refresh button
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void handleRefresh(ActionEvent event) throws IOException {
        onLoadTable();
    }
}
