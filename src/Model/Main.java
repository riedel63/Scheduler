/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import javafx.stage.Stage;
import Utilities.Connection.DBConnection;
import Utilities.Connection.LoggerUtil;

import javafx.scene.Parent;

import java.util.ResourceBundle;


/**
 * @author M Riedel
 */

/**
 * This the Main Class of the application
 */
public class Main extends Application {

    /**
     * calls main stage and locale
     */
    public static Stage mainStage;
    Locale locale = Locale.getDefault();

    /**
     * declares user
     */
    public static User user;

    /**
     * formats how date and time is shown
     */
    public static DateTimeFormatter showFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

    /**
     * Formats SQl format of date time
     */
    public static DateTimeFormatter sqlFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     *
     * @return s system time zone
     */
    public static ZoneId getSystemZone() {
        return ZoneId.systemDefault();
    }

    /**
     *
     * @param headerText Alert header
     * @param content Alert content
     * @param type Alert Type
     */
    public static void showAlert(String headerText, String content, String type) {
        Locale.setDefault(Locale.ENGLISH);

        Alert alert = null;
        String title = "Notification!!";
        switch (type) {
            case "warning":
                alert = new Alert(Alert.AlertType.WARNING);
                title = "Warning!!";
                break;
            case "success":
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                title = "Notification!!";
                break;
            case "error":
                alert = new Alert(Alert.AlertType.ERROR);
                title = "Error!!";
                break;
        }

//        Alert alert = new Alert(Alert.AlertType.WARNING);
        if (alert != null) {
            alert.setTitle(title);
            alert.setHeaderText(headerText);
            alert.setContentText(content);
            alert.showAndWait();
        }
    }

    /**
     * get dateTimeFormart by param
     *
     * @param format pattern of date time
     * @return date and time
     */
    public static DateTimeFormatter getFormat(String format) {
        return DateTimeFormatter.ofPattern(format);
    }

    /**
     * get Zondatetime string format from local date time and given format param
     *
     * @param localDateTime Machine local date and time
     * @param format format of date time
     * @return s time in UTC
     */
    public static String getZoneDateTimeFormat(LocalDateTime localDateTime, DateTimeFormatter format) {
        ZoneId zoneId = getSystemZone();
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        ZonedDateTime zdtUtc = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));

        return zdtUtc.format(format);
    }

    /**
     * checks and gets appointments after logged in... based on min value
     *
     * @param minVal returns value of minutes
     */
    public static void checkExistingAppointments(int minVal) {

        String curTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Timestamp(System.currentTimeMillis()));
        LocalDateTime tempLocalDateTime = LocalDateTime.parse(curTime, Main.sqlFormatter);
        String strCurTime = Main.getZoneDateTimeFormat(tempLocalDateTime, Main.sqlFormatter);

        String sql = "SELECT a.*, cu.Customer_Name as Customer_Name, u.User_Name as User_Name, co.Contact_ID as Contact_ID, co.Contact_Name as Contact_Name, co.Email as Contact_Email" +
                " FROM appointments as a" +
                " LEFT JOIN customers as cu ON cu.Customer_ID = a.Customer_ID" +
                " LEFT JOIN users as u ON u.User_ID = a.User_ID" +
                " LEFT JOIN contacts as co ON co.Contact_ID = a.Contact_ID" +
                " WHERE a.User_ID = ? AND a.`Start` > ? AND a.`Start` <= ? + INTERVAL ? MINUTE";
        PreparedStatement ps = null;
        try {
            ps = DBConnection.getConn().prepareStatement(sql);

            ps.setString(1, user.getUser_ID());
            ps.setString(2, strCurTime);
            ps.setString(3, strCurTime);

            ps.setInt(4, minVal);

            ResultSet rs = ps.executeQuery();
            ObservableList<Appointment> resList = FXCollections.observableArrayList();

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

            if (resList.size() > 0) {
                StringBuilder showContent = new StringBuilder();
                for (Appointment item : resList) {
                    showContent.append(item.getShowVal()).append("\n");
                }

                Main.showAlert("There are some appointments in " + minVal + " minutes", showContent.toString(), "success");
            } else {
                Main.showAlert("Notification", "There is no appointment in " + 15 + " minutes", "success");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param primaryStage primary screen for program login
     * @throws Exception 
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        ResourceBundle rb = ResourceBundle.getBundle("Utilities.language/Lang", locale);


        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/Login.fxml"), rb);
        Scene scene = new Scene(root);

        mainStage = primaryStage;
        mainStage.setScene(scene);
        mainStage.setTitle("SCHEDULER");
        mainStage.show();
    }

    /**
     * runs args then calls closeConn() to close database connections
     *
     * @param args
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        //Locale.setDefault(Locale.FRANCE);
        System.out.println(Locale.getDefault());
        DBConnection.openConnection();
        LoggerUtil.init();
        launch(args);
        DBConnection.closeConnection();
    }
}
