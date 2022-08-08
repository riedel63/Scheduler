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
 * This class is for the Country fields and 
 * includes the setters and getters
 */
public class Country {

    private Integer countryId;
    private String country;

    /**
     * constructor
     *
     * @param countryId ID of country in program
     * @param country name of country
     */
    public Country(Integer countryId, String country) {
        this.countryId = countryId;
        this.country = country;
    }

    /**
     *
     * @return s country ID
     */
    public Integer getCountryId() {
        return countryId;
    }

    /**
     *
     * @param countryId sets Country ID
     */
    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    /**
     *
     * @return s Country
     */
    public String getCountry() {
        return country;
    }

    /**
     *
     * @param country sets Country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     *
     * @return s country to string
     */
    @Override
    public String toString() {
        return country;
    }
}