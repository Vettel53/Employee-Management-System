package config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Properties;

public class DatabaseConnection {
    private String dbUrl = null;
    private String dbUsername = null;
    private String dbPassword = null;
    private String dbSchemaUrl = null;
    private Connection connection = null;
    Properties properties = new Properties();
    
    
    /**
     * This function connects to the MySQL DATABASE using the properties loaded from the 'db.properties' file.
     * <p>
     * Connecting to the DATABASE is not to be confused to connecting to the DATABASE SCHEMA
     * Connecting to the DATABASE is the actual connection to the database server (ex: mySQL)
     * Connecting to the DATABASE SCHEMA is the actual connection to the database that organizes and stores the
     * tables (a schema).
     * <p>
     *
     * @return A Connection object representing the connection to the database.
     *         If any error occurs during the connection process, the function will print the error message and return null.
     */
    public Connection connectToDatabaseServer() {
    
        try {
            // Load db.properties file
            FileInputStream input = new FileInputStream("src/config/db.properties");
            properties.load(input);
    
            // Access individual properties in db.properties file
            dbUrl = properties.getProperty("db.url");
            dbUsername = properties.getProperty("db.username");
            dbPassword = properties.getProperty("db.password");
            dbSchemaUrl = properties.getProperty("db.schemaUrl");
    
            // Connect to MySQL database
            //connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        } catch (FileNotFoundException e) {
            System.err.println("File not found!");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unknown error...");
            e.printStackTrace();
        }
    
        try {
            connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return connection;
    }
    /**
     * Establishes a connection to the specific database SCHEMA using the properties loaded from the 'db.properties' file.
         * See ABOVE method for more information on difference between connecting to SCHEMA and DATABASE
     * This method connects to a particular SCHEMA within the database, which organizes and stores the tables.
     *
     * @return A Connection object representing the connection to the database SCHEMA.
     *         If any error occurs during the connection process, the function will print the stack trace and return null.
     * @throws SQLException If a database access error occurs or the url is null
     */
    public Connection connectToDatabaseSchema() {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(dbSchemaUrl, dbUsername, dbPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }
}
