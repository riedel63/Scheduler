/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;


/**
 * @author mried
 */

/**
 * This class is for the Divisions fields and 
 * includes the setters and getters
 * Note that Division is the same as state or province
 */
public class Divisions {

    private String Division_ID;
    private String Division;

    /**
     * constructor
     * @param Division_ID ID for state or province
     * @param Division state or province
     */
    public Divisions(String Division_ID, String Division) {
        this.Division_ID = Division_ID;
        this.Division = Division;
    }

    /**
     *
     * @return s Division_ID which is same as state or province ID
     */
    public String getDivision_ID() {
        return Division_ID;
    }

    /**
     *
     * @param Division_ID sets Division_ID which is same as state or province ID
     */
    public void setDivision_ID(String Division_ID) {
        this.Division_ID = Division_ID;
    }

    /**
     *
     * @return s Division which is same as state or province
     */
    public String getDivision() {
        return Division;
    }

    /**
     *
     * @param Division sets Division which is same as state or province
     */
    public void setDivision(String Division) {
        this.Division = Division;
    }

    /**
     *
     * @return s Division
     */
    @Override
    public String toString() {
        return Division;
    }
}

