/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities.Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * @author mried
 */

/**
 * This class is for the Database connection 
 * using mysql-connector-java-8.0.27
 */
public abstract class DBConnection {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    private static String password = "Passw0rd!"; // Password
    private static Connection conn = null;  // Connection Interface

    /**
     * opens connection
     * @return connection or error message
     */
    public static boolean openConnection() {
        boolean res = false;
        try {
            Class.forName(driver); // Locate Driver
            conn = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
            System.out.println("Connection successful!");
            res = true;
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }

        return res;
    }

    /**
     * return conn variable
     * @return conn variable
     */
    //Returns Connection
    public static Connection getConn() {
        return conn;
    }

    /**
     * closes connect
     */
    public static void closeConnection() {
        try {
            conn.close();
            System.out.println("Connection closed!");
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
    }
}
