package com.tranquility.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection.java
 * 
 * Provides a single static method to get a database connection.
 * Every servlet in the project imports and uses this class.
 * 
 * Usage in a servlet:
 *   Connection conn = DBConnection.getConnection();
 */
public class DBConnection {

    // The URL of your MySQL database.
    // Format: jdbc:mysql://HOST:PORT/DATABASE_NAME
    // If running locally, HOST is localhost and PORT is 3306.
    private static final String URL =
        "jdbc:mysql://localhost:3306/tranquility_db" +
        "?useSSL=false" +
        "&allowPublicKeyRetrieval=true" +
        "&serverTimezone=UTC";

    // Your MySQL username — usually "root" for local development
    private static final String USER = "root";

    // Your MySQL password — change this to your actual password
    // Leave as empty string "" if you have no password set
    private static final String PASSWORD = "yourpassword";

    // Static block: loads the MySQL JDBC driver when the class
    // is first used. This only runs once.
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found.");
            System.err.println("Make sure mysql-connector-java is in your classpath.");
            e.printStackTrace();
        }
    }

    /**
     * Opens and returns a new database connection.
     * 
     * Always close the connection after use:
     *   conn.close();
     * 
     * Or use try-with-resources (recommended):
     *   try (Connection conn = DBConnection.getConnection()) {
     *       // use conn here — it closes automatically
     *   }
     * 
     * @return a live Connection to tranquility_db
     * @throws SQLException if the connection fails
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}