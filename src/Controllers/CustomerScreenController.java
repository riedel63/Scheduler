/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Model.*;

import java.net.URL;
import java.sql.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import Utilities.Connection.DBConnection;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;


/**
 * FXML Controller class for the Customer Screen
 * 
 * @author mried
 */
public class CustomerScreenController implements Initializable {

    // table
    @FXML
    private TableView<Customer> tblCustomer;

    // table - columns
    @FXML
    private TableColumn<Customer, String> colCustomerID;
    @FXML
    private TableColumn<Customer, String> colCustomerName;
    @FXML
    private TableColumn<Customer, String> colCustomerAddress;

    @FXML
    private TableColumn<Customer, String> colCustomerCountry;
    @FXML
    private TableColumn<Customer, String> colCustomerDivision;
    @FXML
    private TableColumn<Customer, String> colCustomerPostal;
    @FXML
    private TableColumn<Customer, String> colCustomerPhone;

    @FXML
    private AnchorPane customerDetailsAnchor;

    /**
     * Initial calling function
     * includes lambda function to get customer selection
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // lambda function to get customer selection
        tblCustomer.setOnMouseClicked(tv -> {
            Customer selectedCustomer = tblCustomer.getSelectionModel().getSelectedItem();
            if (selectedCustomer != null) {
                setInitAddCustomScreenController(selectedCustomer);
            }
        });

        onLoadTable();
    }

    /**
     * This method gets custormerlist from database
     *
     * @return s customer list from database
     */
    public ObservableList<Customer> getCustomerList() {
        ObservableList<Customer> resList = FXCollections.observableArrayList();

        try {

            Connection connection = DBConnection.getConn();

            String sql = "SELECT cu.*, fld.Division as Division, ct.Country_ID as Country_ID, ct.Country as Country FROM customers as cu" +
                    " LEFT JOIN first_level_divisions as fld ON cu.Division_ID = fld.Division_ID" +
                    " LEFT JOIN countries as ct ON ct.COUNTRY_ID = fld.COUNTRY_ID";

            ResultSet rs = connection.createStatement().executeQuery(sql);


            while (rs.next()) {
                String customerId = rs.getString("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String Postal_Code = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                String divisionId = rs.getString("Division_ID");

                String division = rs.getString("Division");

                Integer countryId = rs.getInt("Country_ID");
                String country = rs.getString("Country");

                resList.add(new Customer(customerId, customerName, address, divisionId, division,
                        Postal_Code, phone, countryId, country));
            }

        } catch (SQLException ex) {
            Logger.getLogger(UsersScreenController.class.getName()).log(Level.SEVERE, null, ex);

        }

        return resList;
    }

    /**
     * This method is the function for loading customer table
     */
    public void onLoadTable() {
        ObservableList<Customer> customerList = getCustomerList();
        tblCustomer.setEditable(false);

        colCustomerID.setCellValueFactory(new PropertyValueFactory<>("Customer_ID")); //Non editable field
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("Customer_Name"));
        colCustomerCountry.setCellValueFactory(new PropertyValueFactory<>("Country"));
        colCustomerDivision.setCellValueFactory(new PropertyValueFactory<>("Division"));
        colCustomerAddress.setCellValueFactory(new PropertyValueFactory<>("Address"));
        colCustomerPostal.setCellValueFactory(new PropertyValueFactory<>("Postal_Code"));
        colCustomerPhone.setCellValueFactory(new PropertyValueFactory<>("Phone"));

        tblCustomer.setItems(customerList);
    }


    /**
     * This method is for the event function that calls when click add button
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void loadNewCustomerPane(ActionEvent event) throws IOException {
        setInitAddCustomScreenController(null);
    }

    /**
     * This method is for the function to set init info of addCustomerScreenController
     *
     * @param customer
     */
    private void setInitAddCustomScreenController(Customer customer) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/AddCustomerScreen.fxml"));

        AnchorPane pane = null;
        try {
            pane = loader.load();

            customerDetailsAnchor.getChildren().setAll(pane);

            // Give the controller access to the main app.
            AddCustomerScreenController controller = loader.getController();
            controller.setInitInfo(this, customer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

