/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;


import Model.User;
import Utilities.Connection.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


/**
 * FXML Controller class for Users Screen
 *
 * @author mried
 */

public class UsersScreenController implements Initializable {

    // table
    @FXML
    private TableView<User> tblUser;

    // table - columns
    @FXML
    private TableColumn<User, String> colUserID;
    @FXML
    private TableColumn<User, String> colUserName;
    @FXML
    private TableColumn<User, String> colPassword;

    @FXML
    private AnchorPane userDetailsAnchor;

    /**
     * @param url
     * @param rb
     * includes lambda function to select user from table
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // lambda function to select user from table
        tblUser.setOnMouseClicked(tv -> {
            User selectedUser = tblUser.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                setInitAddUserScreenController(selectedUser);
            }
        });

        onLoadTable();
    }

    /**
     * THis method loads user list from database
     *
     * @return s userlist from database
     */
    public ObservableList<User> getUserList() {
        ObservableList<User> resList = FXCollections.observableArrayList();

        try {
            Connection connection = DBConnection.getConn();

            String sql = "SELECT * FROM users";

            ResultSet rs = connection.createStatement().executeQuery(sql);

            while (rs.next()) {
                String userId = rs.getString("User_ID");
                String userName = rs.getString("User_Name");
                String password = rs.getString("Password");

                User user = new User(userId, userName, password);
                resList.add(user);
            }

        } catch (SQLException ex) {
            Logger.getLogger(UsersScreenController.class.getName()).log(Level.SEVERE, null, ex);

        }

        return resList;
    }

    /**
     * This method loads the user table
     */
    public void onLoadTable() {
        ObservableList<User> userList = getUserList();
        tblUser.setEditable(false);

        colUserID.setCellValueFactory(new PropertyValueFactory<>("User_ID")); //Non editable field
        colUserName.setCellValueFactory(new PropertyValueFactory<>("User_Name"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("Password"));

        tblUser.setItems(userList);
    }


    /**
     * This method loads the add user form into the AddUserPane
     */

   
    @FXML
    private void loadAddUserPane(ActionEvent event) throws IOException {
        setInitAddUserScreenController(null);
    }

    private void setInitAddUserScreenController(User user) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/AddUserScreen.fxml"));

        AnchorPane pane = null;
        try {
            pane = loader.load();

            userDetailsAnchor.getChildren().setAll(pane);

            // Give the controller access to the main app.
            AddUserScreenController controller = loader.getController();
            controller.setInitInfo(this, user);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * This method refreshes the user tableview showing the new added users
     */
    @FXML
    private void refreshUserTable() {
        onLoadTable();
    }
}
    
      
        