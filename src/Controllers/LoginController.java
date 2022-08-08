/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import java.net.URL;
import java.time.ZoneId;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.*;
import java.util.Locale;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import Model.Main;
import Model.User;
import Utilities.Connection.LoggerUtil;
import Utilities.Connection.DBConnection;
import java.io.FileWriter;
import java.io.PrintWriter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * FXML Controller class for user logins
 *
 * @author mried
 */
public class LoginController implements Initializable {
    @FXML
    private TextField User_Name;
    @FXML
    private PasswordField Password;
    @FXML
    private Label lblZoneName;
    @FXML
    private Label errorMessage;



    Locale locale = Locale.getDefault();
    User user;

    private final static Logger LOGGER = Logger.getLogger(LoggerUtil.class.getName());

    // Reference to the main application.
    ResourceBundle rb = ResourceBundle.getBundle("Utilities.language/Lang", Locale.getDefault());

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ZoneId zoneId = Main.getSystemZone();
        lblZoneName.setText(zoneId.toString());
    }

    /**
     * This method is for the event function that calls when click login button
     *
     * @param event
     */
    @FXML
    private void handleLogin(ActionEvent event) {

        try {
            FileWriter fw = new FileWriter("src/resources/login_activity.txt",true);
            PrintWriter outFile = new PrintWriter(fw);
            outFile.println("string");
            outFile.close();
            if (DBConnection.openConnection()) {
                Connection conn = DBConnection.getConn();
                String sql = "Select * from Users where User_Name=? and Password=?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, User_Name.getText());
                pst.setString(2, Password.getText());
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    errorMessage.setText(rb.getString("Success"));
                    String userId = rs.getString("User_ID");
                    // save to global user info
                    Main.user = new User(userId, User_Name.getText(), Password.getText());
                    Parent root = FXMLLoader.load(getClass().getResource("/FXML/Dashboard.fxml"));

                    Scene scene = new Scene(root);
                    Main.mainStage.hide();
                    Main.mainStage.setScene(scene);
                    Main.mainStage.show();

                    // call 15 mins status
                    Main.checkExistingAppointments(15);

                    LOGGER.info("Login Success, User ID: " + userId);
                } else {
                    errorMessage.setText(rb.getString("incorrect"));
                    User_Name.setText("");
                    Password.setText("");
                    LOGGER.info("Login Failed");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method shows confirmation alert when user requests to close program
     * Uses computers default locale setting for either English or French on login screen only
     * includes lambda function
     */
    @FXML
    private void handleExit(ActionEvent event) {

        // lambda function for confirmation of request and program exit
        ResourceBundle rb = ResourceBundle.getBundle("Utilities.language/Lang", locale);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(rb.getString("exit"));
        alert.setHeaderText(rb.getString("header"));
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent((ButtonType response) -> {
                            Platform.exit();
                            System.exit(0);
                        }
                );
    }

}
