/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class for the Dashboard
 *
 * @author mried
 */
public class DashboardController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            onLoadAppointmentScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method loads appointment screen
     *
     * @throws IOException
     */
    private void onLoadAppointmentScreen() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/FXML/AppointmentScreen.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    /**
     * This method is for the event function for loading appointment screen
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void loadAppointmentScreen(ActionEvent event) throws IOException {
        onLoadAppointmentScreen();
    }

    /**
     * This method is for the event function for loading customer screen
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void loadCustomersScreen(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/FXML/CustomerScreen.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    /**
     * This method is for the event function for loading user screen
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void loadUsersScreen(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/FXML/UsersScreen.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    /**
     * This method is for the event function for loading report screen
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void loadReportsScreen(ActionEvent event) throws IOException {
        TabPane pane = FXMLLoader.load(getClass().getResource("/FXML/Reports.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    /**
     * This method is for the event function for exiting app and includes lambda function
     *
     * @param event
     */
    @FXML
    private void exitProgram(ActionEvent event) {

        // lambda function for alerting and exiting the app
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("Do you wish to exit the program?");
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent((ButtonType response) -> {
                            Platform.exit();
                            System.exit(0);
                        }
                );
    }
}
