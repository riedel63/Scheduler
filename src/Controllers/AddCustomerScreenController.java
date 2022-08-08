/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Model.*;
import Utilities.Connection.DBConnection;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;


/**
 * FXML Controller class for adding customers
 * 
 * @author mried
 */
public class AddCustomerScreenController implements Initializable {

    // fields
    @FXML
    private TextField fieldCustomerName;
    @FXML
    private TextField fieldAddress;
    @FXML
    private TextField fieldPostalCode;
    @FXML
    private TextField fieldPhone;
    @FXML
    private TextField fieldCustomerID;

    // buttons
    @FXML
    private Button btnSaveCustomer;
    @FXML
    private Button btnDeleteCustomer;

    // combobox
    @FXML
    private ComboBox<Divisions> cbxDivision;
    @FXML
    private ComboBox<Country> cbxCountry;

    // custom variables
    private Customer savedUser;
    private CustomerScreenController customerScreenController;


    /**
     * Initializes the controller class for loading countries.
     *
     * @param url for loading countries
     * @param rb for loading countries
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadCountries();
    }

    /**
     * This method loads divisions when select country from combobox
     *
     * @param country
     */
    private void loadDivisions(Country country) {
        try {
            Integer countryId = country.getCountryId();
            String sql = "SELECT * FROM first_level_divisions WHERE COUNTRY_ID = ? ORDER BY Division";

            PreparedStatement statement = DBConnection.getConn().prepareStatement(
                    sql);

            statement.setInt(1, countryId);

            ResultSet rs = statement.executeQuery();

            ObservableList<Divisions> listDivisions = FXCollections.observableArrayList();

            while (rs.next()) {
                String Division_ID = rs.getString("Division_ID");
                String Division = rs.getString("Division");
                listDivisions.add(new Divisions(Division_ID, Division));
            }

            cbxDivision.setItems(listDivisions);

        } catch (SQLException sqe) {
            System.out.println("Check your SQL");
            sqe.printStackTrace();
        } catch (Exception e) {
            System.out.println("Something besides the SQL went wrong.");
        }
    }

    /**
     * This method loads countries into combobox
     */
    private void loadCountries() {
        try {

            String sql = "SELECT * FROM countries WHERE Country IS NOT NULL AND Country != '' ORDER BY Country";

            PreparedStatement statement = DBConnection.getConn().prepareStatement(
                    sql);
            ResultSet rs = statement.executeQuery();

            ObservableList<Country> listCountry = FXCollections.observableArrayList();

            while (rs.next()) {
                Integer countryId = rs.getInt("Country_ID");
                String country = rs.getString("Country");
                listCountry.add(new Country(countryId, country));
            }

            cbxCountry.setItems(listCountry);
        } catch (SQLException sqe) {
            System.out.println("Check your SQL");
            sqe.printStackTrace();
        } catch (Exception e) {
            System.out.println("Something besides the SQL went wrong.");
        }
    }

    /**
     * This method 
     * @param customer
     */
    private void updateFormInfo(Customer customer) {
        fieldCustomerID.setText(customer.getCustomer_ID());
        fieldCustomerName.setText(customer.getCustomer_Name());
        fieldAddress.setText(customer.getAddress());
        Country country = new Country(customer.getCountry_ID(), customer.getCountry());
        cbxCountry.setValue(country);
        loadDivisions(country);

        cbxDivision.setValue(new Divisions(customer.getDivision_ID(), customer.getDivision()));

        fieldPostalCode.setText(customer.getPostal_Code());
        fieldPhone.setText(customer.getPhone());

        btnSaveCustomer.setText("Update");
        btnDeleteCustomer.setDisable(false);
    }

    /**
     * @param cusController Initiates controls customer screen
     * @param customer Saving or deletion of customer
     */
    public void setInitInfo(CustomerScreenController cusController, Customer customer) {

        this.customerScreenController = cusController;

        if (customer != null) {
            this.savedUser = customer;
            updateFormInfo(customer);
        } else {
            this.savedUser = null;

            fieldCustomerID.clear();
            btnSaveCustomer.setText("Add");
            btnDeleteCustomer.setDisable(true);
        }
    }


    /**
     * event function that calls when change country combobox
     * to load the divisions combobox
     * @param event
     */
    @FXML
    private void onChangeCountryCombobox(ActionEvent event) {
        Country selectedCountry = cbxCountry.getValue();
        loadDivisions(selectedCountry);
    }

    /**
     * This method refreshes the customer table
     */
    private void onRefreshTable() {

        if (this.customerScreenController != null) {
            customerScreenController.onLoadTable();
        }
    }

    /**
     * This method adds or updates customer infomation
     *
     * @param event
     * @throws SQLException
     */
    @FXML
    private void handleSaveCustomer(ActionEvent event) throws SQLException {

        if (!checkValidateForm()) {
            return;
        }

        String addBtnText = btnSaveCustomer.getText();

        if (addBtnText.equals("Add")) {

            PreparedStatement ps = DBConnection.getConn().prepareStatement("INSERT INTO customers (Customer_Name, Address, Division_ID, Postal_Code, Phone) VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, fieldCustomerName.getText());
            ps.setString(2, fieldAddress.getText());
            ps.setString(3, cbxDivision.getValue().getDivision_ID());
            ps.setString(4, fieldPostalCode.getText());
            ps.setString(5, fieldPhone.getText());

            if (ps.executeUpdate() == 1) {

                Main.showAlert("Success!!", "Successfully Added!", "success");
                onRefreshTable();

                handleResetForm();
            }
        } else {
            PreparedStatement ps = DBConnection.getConn().prepareStatement("UPDATE customers SET Customer_Name = ?, Address = ?, Division_ID = ?, Postal_Code = ?, Phone = ? WHERE Customer_ID = ?");
            ps.setString(1, fieldCustomerName.getText());
            ps.setString(2, fieldAddress.getText());
            ps.setString(3, cbxDivision.getValue().getDivision_ID());
            ps.setString(4, fieldPostalCode.getText());
            ps.setString(5, fieldPhone.getText());
            ps.setString(6, fieldCustomerID.getText());

            if (ps.executeUpdate() == 1) {

                Main.showAlert("Success!!", "Successfully Updated!", "success");
                onRefreshTable();

                this.savedUser.setCustomer_Name(fieldCustomerName.getText());
                this.savedUser.setAddress(fieldAddress.getText());

                this.savedUser.setCountry_ID(cbxCountry.getValue().getCountryId());
                this.savedUser.setCountry(cbxCountry.getValue().getCountry());

                this.savedUser.setDivision_ID(cbxDivision.getValue().getDivision_ID());
                this.savedUser.setDivision(cbxDivision.getValue().getDivision());

                this.savedUser.setPostal_Code(fieldPostalCode.getText());
                this.savedUser.setPhone(fieldPhone.getText());
            }
        }
    }

    /**
     * This method resets form fields
     */
    @FXML
    private void handleResetForm() {

        if (btnSaveCustomer.getText().equals("Add")) {
            fieldCustomerID.clear();
            fieldCustomerName.clear();
            fieldAddress.clear();
            fieldPostalCode.clear();
            fieldPhone.clear();
        } else {
            updateFormInfo(this.savedUser);
        }
    }

    /**
     * This method deletes selected customer from customers table
     * and deletes any appointments.
     * @param customer
     */
    private void deleteCustomer(Customer customer) {

        try {
            String sql = "DELETE FROM appointments WHERE Customer_ID = ?";
            PreparedStatement pst = DBConnection.getConn().prepareStatement(sql);
            pst.setString(1, customer.getCustomer_ID());

            pst.executeUpdate();

            sql = "DELETE FROM customers WHERE Customer_ID = ?";
            pst = DBConnection.getConn().prepareStatement(sql);
            pst.setString(1, customer.getCustomer_ID());

            if (pst.executeUpdate() != 1) {
                Main.showAlert("Notification!", "There are some issue in database", "warning");
                return;
            }

            Main.showAlert("Notification!", "Successfully Delete customer", "success");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method deletes customer info and includes lambda function
     */
    @FXML
    private void handleDeleteCustomer() {

        if (this.savedUser != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete " + this.savedUser.getCustomer_Name() + "?");

            // lambda function for alert and customer deletion
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {
                                deleteCustomer(this.savedUser);
                                onRefreshTable();
                                this.savedUser = null;
                                fieldCustomerID.clear();
                                btnSaveCustomer.setText("Add");
                                btnDeleteCustomer.setDisable(true);
                                handleResetForm();
                            }
                    );
        } else {
            Main.showAlert("No Customer selected for Deletion", "Please select a Customer in the Table to delete", "warning");
        }
    }

    /**
     * This method validates Customer details to ensure no missing or invalid data
     *
     * @return true if no errors
     */
    private boolean checkValidateForm() {
        String name = fieldCustomerName.getText();
        String address = fieldAddress.getText();
        String zip = fieldPostalCode.getText();
        String phone = fieldPhone.getText();
        String division = cbxDivision.getValue().getDivision();

        String country = cbxCountry.getValue().getCountry();

        String errorMessage = "";
        //first checks to see if inputs are null
        if (name == null || name.length() == 0) {
            errorMessage += "Please enter the Customer's name.\n";
        }
        if (address == null || address.length() == 0) {
            errorMessage += "Please enter an address.\n";
        }
        if (country == null || country.length() == 0) {
            errorMessage += "Please Select a Country.\n";
        }
        if (division == null || division.length() == 0) {
            errorMessage += "Please Select a City.\n";
        }

        if (zip == null || zip.length() == 0) {
            errorMessage += "Please enter the Postal Code.\n";
        } else if (zip.length() > 10 || zip.length() < 5) {
            errorMessage += "Please enter a valid Postal Code.\n";
        }
        if (phone == null || phone.length() == 0) {
            errorMessage += "Please enter a Phone Number (including Area Code).";
        } else if (phone.length() < 10 || phone.length() > 15) {
            errorMessage += "Please enter a valid phone number (including Area Code).\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Main.showAlert("Please correct Customer fields", errorMessage, "error");
            return false;
        }
    }

}
