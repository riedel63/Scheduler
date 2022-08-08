
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 * @author mriedel
 */

/**
 * This class is for the Customer Address fields and 
 * includes the setters and getters
 * Note that Division is the same as state or province
 */
public class Address {

    private String Address;
    private String Division;  //same as state or province
    private String Postal_Code;
    private String Phone;

    /**
     * constructor
     *
     * @param Address customer street address
     * @param Division customer state or province
     * @param Postal_Code customer zip code or postal code
     * @param Phone customer phone number
     */
    public Address(String Address, String Division, String Postal_Code, String Phone) {
        this.Address = Address;
        this.Division = Division;
        this.Postal_Code = Postal_Code;
        this.Phone = Phone;
    }

    /**
     *
     * @return customers address
     */
    public String getAddress() {
        return Address;
    }

    /**
     *
     * @param Address sets customers address
     */
    public void setAddress(String Address) {
        this.Address = Address;
    }

    /**
     *
     * @return customers province or state
     */
    public String getDivision() {
        return Division;
    }

    /**
     *
     * @param Division sets customers province or state
     */
    public void setDivision(String Division) {
        this.Division = Division;   //same as State or Province
    }

    /**
     *
     * @return customers postal or zip code
     */
    public String getPostal_Code() {
        return Postal_Code;
    }

    /**
     *
     * @param Postal_Code sets customers postal or zip code
     */
    public void setPostal_Code(String Postal_Code) {
        this.Postal_Code = Postal_Code;
    }

    /**
     *
     * @return customers phone number
     */
    public String getPhone() {
        return Phone;
    }

    /**
     *
     * @param Phone sets customers phone number
     */
    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

}