/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 * @author mriedel
 */

/**
 * This class is for the Customer fields and 
 * includes the setters and getters
 * Note that Division is the same as state or province
 */
public class Customer {

    private String Customer_ID;  //This is autogenerated by DB
    private String Customer_Name;
    private String Address;
    private String Division_ID;
    private String Division; //Same as state or province
    private String Postal_Code;
    private String Phone;

    private Integer Country_ID;
    private String Country;

    private static final ObservableList<Customer> allCustomersList = FXCollections.observableArrayList();


    /**
     * @param Customer_ID ID of Customer 
     * @param Customer_Name Name of Customer
     * @param Address Address of Customer
     * @param Division_ID Division ID of Customer
     * @param Division City or province of Customer
     * @param Postal_Code Postal or zip code of Customer
     * @param Phone phone number of customer
     * @param Country_ID Country ID for Customer
     * @param Country Country of Customer
     */
    public Customer(String Customer_ID, String Customer_Name, String Address, String Division_ID, String Division,
                    String Postal_Code, String Phone, Integer Country_ID, String Country) {
        this.Customer_ID = Customer_ID;
        this.Customer_Name = Customer_Name;
        this.Address = Address;
        this.Postal_Code = Postal_Code;
        this.Phone = Phone;

        this.Division_ID = Division_ID;
        this.Division = Division;

        this.Country_ID = Country_ID;
        this.Country = Country;
    }

    /**
     * constructor 2
     * @param Customer_ID ID of Customer in program
     * @param Customer_Name Customer name
     */
    public Customer(String Customer_ID, String Customer_Name) {
        this.Customer_ID = Customer_ID;
        this.Customer_Name = Customer_Name;
    }

    /**
     *
     * @return s customer street address
     */
    public String getAddress() {
        return Address;
    }

    /**
     *
     * @param address sets customer street address
     */
    public void setAddress(String address) {
        Address = address;
    }

    /**
     *
     * @return s customers division id same as state or province id
     */
    public String getDivision_ID() {
        return Division_ID;
    }

    /**
     *
     * @param division_ID sets customers division id
     */
    public void setDivision_ID(String division_ID) {
        Division_ID = division_ID;
    }

    /**
     *
     * @return s customers division
     */
    public String getDivision() {
        return Division;
    }

    /**
     *
     * @param division sets customers division
     */
    public void setDivision(String division) {
        Division = division;
    }

    /**
     *
     * @return s customers postal code or zip code
     */
    public String getPostal_Code() {
        return Postal_Code;
    }

    /**
     *
     * @param postal_Code sets customers postal code or zip code
     */
    public void setPostal_Code(String postal_Code) {
        Postal_Code = postal_Code;
    }

    /**
     *
     * @return s customers phone number
     */
    public String getPhone() {
        return Phone;
    }

    /**
     *
     * @param phone sets customers phone number
     */
    public void setPhone(String phone) {
        Phone = phone;
    }

    /**
     *
     * @return s customers country ID
     */
    public Integer getCountry_ID() {
        return Country_ID;
    }

    /**
     *
     * @param country_ID sets customers country ID
     */
    public void setCountry_ID(Integer country_ID) {
        Country_ID = country_ID;
    }

    /**
     *
     * @return s customers Country
     */
    public String getCountry() {
        return Country;
    }

    /**
     *
     * @param country sets customers Country
     */
    public void setCountry(String country) {
        Country = country;
    }

    /**
     *
     * @return s Customers ID
     */
    public String getCustomer_ID() {
        return Customer_ID;
    }

    /**
     *
     * @param customer_ID sets Customers ID this is autogenerated
     */
    public void setCustomer_ID(String customer_ID) {
        Customer_ID = customer_ID;
    }

    /**
     *
     * @return s customers name
     */
    public String getCustomer_Name() {
        return Customer_Name;
    }

    /**
     *
     * @param customer_Name sets customers name
     */
    public void setCustomer_Name(String customer_Name) {
        Customer_Name = customer_Name;
    }

    /**
     *
     * @return s customer name to string
     */
    @Override
    public String toString() {
        return Customer_Name;
    }
}
