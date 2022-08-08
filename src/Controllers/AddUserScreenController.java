/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Model.Main;
import Model.User;
import Utilities.Connection.DBConnection;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * FXML Controller for Add User class
 *
 * @author mried
 */
public class AddUserScreenController {

    // fields
    @FXML
    private TextField fieldUserID;
    @FXML
    private TextField fieldUserName;
    @FXML
    private PasswordField fieldPassword;

    // buttons
    @FXML
    private Button btnSaveUser;
    @FXML
    private Button btnDeleteUser;

    // custom variables
    private User savedUser;
    private UsersScreenController usersScreenController;


    /**
     * This method resets the add user form for updating
     *
     * @param user
     */
    private void updateFormInfo(User user) {
        fieldUserID.setText(user.getUser_ID());
        fieldUserName.setText(user.getUser_Name());
        fieldPassword.setText(user.getPassword());

        btnSaveUser.setText("Update");
        btnDeleteUser.setDisable(false);
    }

    /**
     * @param uScreenController initiates user screen controller
     * @param user saves or deletes user
     */
    public void setInitInfo(UsersScreenController uScreenController, User user) {
        this.usersScreenController = uScreenController;

        if (user != null) {
            this.savedUser = user;
            updateFormInfo(user);
        } else {
            this.savedUser = null;
            fieldUserID.clear();
            btnSaveUser.setText("Add");
            btnDeleteUser.setDisable(true);
        }
    }

    /**
     * This method saves a new user to the Users tableview and mysql database
     */
    @FXML
    private void handleSaveUser(ActionEvent event) throws SQLException {

        String strBtnText = btnSaveUser.getText();

        if (strBtnText.equals("Add")) {
            PreparedStatement ps = DBConnection.getConn().prepareStatement("INSERT INTO users (User_Name, Password) VALUES (?, ?)");

            ps.setString(1, fieldUserName.getText());
            ps.setString(2, fieldPassword.getText());

            if (ps.executeUpdate() == 1) {
                Main.showAlert("Success!!", "Successfully Added!", "success");
                onRefreshTable();

                handleResetForm();
            }

        } else {
            PreparedStatement ps = DBConnection.getConn().prepareStatement("UPDATE users SET User_Name = ?, Password = ? WHERE User_ID = ?");

            ps.setString(1, fieldUserName.getText());
            ps.setString(2, fieldPassword.getText());

            ps.setString(3, fieldUserID.getText());

            if (ps.executeUpdate() == 1) {
                Main.showAlert("Success!!", "Successfully Updated!", "success");
                onRefreshTable();

                this.savedUser.setUser_Name(fieldUserName.getText());
                this.savedUser.setPassword(fieldPassword.getText());
            }
        }
    }

    /**
     * This method deletes user from database
     *
     * @param user
     */
    private void deleteUser(User user) {
        try {
            PreparedStatement pst = DBConnection.getConn().prepareStatement("DELETE from users WHERE User_ID = ? ");
            pst.setString(1, user.getUser_ID());
            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * This method deletes users from the User Tableview and includes lambda function
     *
     * @param event to delete users from tableview
     */
    @FXML
    public void handleDeleteUser(ActionEvent event) {

        if (this.savedUser != null) {


            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete " + this.savedUser.getUser_Name() + "?");

            // lambda function for deletion of user from tableview
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {
                                deleteUser(this.savedUser);
                                onRefreshTable();
                                btnSaveUser.setText("Add");
                                btnDeleteUser.setDisable(true);

                                handleResetForm();
                            }
                    );


        } else {
            Main.showAlert("No User selected for Deletion", "Please select a User in the Table to delete", "warning");
        }

    }

    /**
     * This method refreshes User table
     */
    private void onRefreshTable() {

        if (this.usersScreenController != null) {
            usersScreenController.onLoadTable();
        }
    }

    /**
     * This method resets user form fields
     */
    @FXML
    private void handleResetForm() {
        if (btnSaveUser.getText().equals("Add")) {
            fieldUserID.clear();
            fieldUserName.clear();
            fieldPassword.clear();
        } else {
            updateFormInfo(this.savedUser);
        }
    }
}
