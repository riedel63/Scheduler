/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities.Connection;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author MRiedel
 */

/**
 * This class handles initialization of Logger class and gets called in main method
 * FileHandler is set to create a log file in in the base of the project folder
 * If the file already exists, it will append logs to the that file unless
 * it is being used by another process, has reached the set size limit.
 * In which case, it will create a new file to write to.
 * There is a file count limit of 10. After 10th file,
 * Logger will rotate back to first file and overwrite.
 */
public class LoggerUtil {

    private static final Logger LOGGER = Logger.getLogger(LoggerUtil.class.getName());

    private static FileHandler handler = null;

    /**
     * Initializes the file handler for the activity log
     */
    public static void init() {
        try {
            handler = new FileHandler("src/resources/login_activity.txt", true);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
        Logger logger = Logger.getLogger("");
        handler.setFormatter(new SimpleFormatter());
        logger.addHandler(handler);
        logger.setLevel(Level.INFO);
    }
}

 
/**
public class LoggerUtil {

    private static final Logger LOGGER = Logger.getLogger(LoggerUtil.class.getName());

    private static FileHandler handler = null;

    /**
     * Initializes the file handler for the activity log

    public static void init() {
        try {
            handler = new FileHandler("login_activity.txt", true);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
        Logger logger = Logger.getLogger("");
        handler.setFormatter(new SimpleFormatter());
        logger.addHandler(handler);
        logger.setLevel(Level.INFO);
    }
}
*/