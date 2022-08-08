/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Model.*;
import Utilities.Connection.DBConnection;

import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


/**
 * FXML Controller class for the reports
 *
 * @author mriedel
 */
public class ReportsController implements Initializable {

    @FXML
    private TabPane tabPane;

    // appointment table
    @FXML
    private TableView<AppointmentReport> tblApptStatistics;
   
    // columns
    @FXML
    private TableColumn<AppointmentReport, String> colApptMonth;
    @FXML
    private TableColumn<AppointmentReport, String> colApptType;
    @FXML
    private TableColumn<AppointmentReport, String> colApptAmount;

    // tables
    // schedule table
    @FXML
    private TableView<Appointment> tblSchedule;
    @FXML
    private TableColumn<Appointment, String> colScheduleID;
    @FXML
    private TableColumn<Appointment, String> colScheduleStart;
    @FXML
    private TableColumn<Appointment, String> colScheduleEnd;
    @FXML
    private TableColumn<Appointment, String> colScheduleLocation;
    @FXML
    private TableColumn<Appointment, String> colScheduleTitle;
    @FXML
    private TableColumn<Appointment, String> colScheduleDescription;
    @FXML
    private TableColumn<Appointment, String> colScheduleType;
    @FXML
    private TableColumn<Appointment, String> colScheduleCustomerID;

    // bar chart
    @FXML
    private BarChart barChartCustomer;
    
    // combobox for contacts
    @FXML
    private ComboBox<Contacts> cbxContact;

    /**
     * @param location
     * @param resources
     * includes lambda function to get appointment types by month
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // lambda function to get appointment types by month
        tabPane.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue.getText().equals("Appointments Types By Month")) {
                        onLoadApptStatisticsTable();
                    } else if (newValue.getText().equals("Schedule")) {
                        onLoadScheduleTable();
                    } else {
                        onLoadCustomerChart();
                    }
                }
        );

        onLoadContacts();

        onLoadApptStatisticsTable();
        onLoadCustomerChart();
    }

    /**
     * This method loads appointments from database
     *
     * @return s appointment lists from database
     */
    public ObservableList<Appointment> getAppointmentList() {
        ObservableList<Appointment> resList = FXCollections.observableArrayList();

        try {
            Connection connection = DBConnection.getConn();

            String sql = "SELECT a.*, cu.Customer_Name as Customer_Name, u.User_Name as User_Name, co.Contact_ID as Contact_ID, co.Contact_Name as Contact_Name, co.Email as Contact_Email" +
                    " FROM appointments as a" +
                    " LEFT JOIN customers as cu ON cu.Customer_ID = a.Customer_ID" +
                    " LEFT JOIN users as u ON u.User_ID = a.User_ID" +
                    " LEFT JOIN contacts as co ON co.Contact_ID = a.Contact_ID" +
                    " WHERE a.User_ID = " + Main.user.getUser_ID();

            Contacts contact = cbxContact.getValue();
            String contactId = contact.getContact_ID();
            if (!contactId.equals("0") && !contactId.equals("")) {
                sql += " AND a.Contact_ID = " + contactId;
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
     * This method loads schedule table
     */
    private void onLoadScheduleTable() {
        ObservableList<Appointment> apptList = getAppointmentList();
        tblSchedule.setEditable(false);

        colScheduleID.setCellValueFactory(new PropertyValueFactory<>("Appointment_ID")); //Non editable field
        colScheduleTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
        colScheduleType.setCellValueFactory(new PropertyValueFactory<>("Type"));
        colScheduleDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));

        colScheduleStart.setCellValueFactory(new PropertyValueFactory<>("strStart"));
        colScheduleEnd.setCellValueFactory(new PropertyValueFactory<>("strEnd"));

        colScheduleCustomerID.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));

        tblSchedule.setItems(apptList);
    }

    @FXML
    private void onChangeCustomerComboBox(ActionEvent event) {
        onLoadScheduleTable();
    }

    /**
     * This method loads the contacts
     */
    private void onLoadContacts() {
        ObservableList<Contacts> resList = FXCollections.observableArrayList();

        try {
            Connection connection = DBConnection.getConn();

            String sql = "SELECT Contact_ID, Contact_Name" +
                    " FROM contacts" +
                    " ORDER BY Contact_Name";

            ResultSet rs = connection.createStatement().executeQuery(sql);

            resList.add(new Contacts("0", "All"));

            while (rs.next()) {
                String contactId = rs.getString("Contact_ID");
                String contactName = rs.getString("Contact_Name");

                Contacts contact = new Contacts(contactId, contactName);
                resList.add(contact);
            }

        } catch (SQLException ex) {
            Logger.getLogger(UsersScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }

        cbxContact.setItems(resList);
        cbxContact.setValue(resList.get(0));
    }

    /**
     * This method loads appointments by month, type, amounts, and sets to table
     */
    private void onLoadApptStatisticsTable() {
        ObservableList<AppointmentReport> resList = FXCollections.observableArrayList();

        try {
            Connection connection = DBConnection.getConn();

            String sql = "SELECT MONTHNAME(`Start`) AS `Month`, Type AS `Type`, COUNT(*) as `Amount`" +
                    " FROM appointments" +
                    " WHERE User_ID = " + Main.user.getUser_ID() +
                    " GROUP BY MONTHNAME(`Start`), Type";

            ResultSet rs = connection.createStatement().executeQuery(sql);

            while (rs.next()) {
                String month = rs.getString("Month");
                String type = rs.getString("Type");
                String amount = rs.getString("Amount");

                AppointmentReport apptReport = new AppointmentReport(month, type, amount);
                resList.add(apptReport);
            }

        } catch (SQLException ex) {
            Logger.getLogger(UsersScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }

        tblApptStatistics.setEditable(false);
        colApptMonth.setCellValueFactory(new PropertyValueFactory<>("month")); //Non editable field
        colApptType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colApptAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        tblApptStatistics.setItems(resList);
    }

    /**
     * This method loads customer info and sets to barchart by location
     */
    private void onLoadCustomerChart() {
        barChartCustomer.getData().clear();

        ObservableList<XYChart.Data<String, Integer>> customerList = FXCollections.observableArrayList();
        XYChart.Series<String, Integer> series = new XYChart.Series<>();

        try {
            PreparedStatement pst = DBConnection.getConn().prepareStatement(
                    "SELECT first_level_divisions.Division, COUNT(Division) as `Count`" +
                            " FROM customers, first_level_divisions" +
                            " WHERE customers.Division_ID = first_level_divisions.Division_ID" +
                            " GROUP BY Division");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String city = rs.getString("Division");
                Integer count = rs.getInt("Count");
                customerList.add(new Data<>(city, count));
            }

        } catch (SQLException sqe) {
            System.out.println("Check your SQL");
            sqe.printStackTrace();
        } catch (Exception e) {
            System.out.println("Something besides the SQL went wrong.");
            e.printStackTrace();
        }
        series.getData().addAll(customerList);
        barChartCustomer.getData().add(series);
    }
}
