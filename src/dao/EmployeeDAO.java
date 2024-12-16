package dao;

import config.DatabaseConnection;
import config.Colors;

import input.UserInput;

import java.sql.*;
import java.util.Scanner;

public class EmployeeDAO {
    // private Scanner scnr = new Scanner(System.in); Attempting to use only one scanner
    private Connection connection;
    private Statement stmt;

    public EmployeeDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Creates a database named 'employee_schema' if it doesn't already exist.
     * This method attempts to create a new database and handles the outcome of the operation.
     * <p>
     * The method uses a SQL statement to create the database if it doesn't exist.
     * It then checks the result of the operation and provides appropriate console output.
     * If an SQLException occurs during the process, an error message is printed to the console and exits.
     *
     * @throws SQLException if there's an error in executing the SQL statement or in database access.
     */
    public void createDatabase() {
        int result = -1;

        try {
            stmt = connection.createStatement();
            // CREATE DATABASE IF NOT EXISTS returns 0 for success (whether created or already exists)
            result = stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS employee_schema;");

            if (result == -1) {
                System.err.println("Unexpected result, please run the program again...");
            }
            else {
                System.out.println("Database creation attempted! If it did not exist, it has been created!\n");
            }

        } catch (SQLException e) {
            System.err.println("Error creating SQL scheme: " + e.getMessage());
        }
    }

    /**
     * Creates a table named 'employees' in the connected database schema if it doesn't already exist.
     * The table has columns for id (auto-incrementing primary key), first_name, and last_name.
     * <p>
     * This method attempts to create the table and handles the outcome of the operation.
     * It uses a SQL statement to create the table if it doesn't exist, then checks the result
     * of the operation and provides appropriate console output.
     * If an SQLException occurs during the process, an error message is printed to the console and exits.
     *
     * @param dbConnector The DatabaseConnection object is used to establish a connection
     *                    to the database schema FUNCTION.
     *                    This connection is used to create the statement for executing the SQL query.
     */
    public void createTable(DatabaseConnection dbConnector) {
        connection = dbConnector.connectToDatabaseSchema();

        int result = -1;

        try {
            stmt = connection.createStatement();
            // CREATE TABLE IF NOT EXISTS employees returns 0 for success (whether created or already exists)
            result = stmt.executeUpdate("CREATE TABLE IF NOT EXISTS employees (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "first_name VARCHAR(255) NOT NULL," +
                    "last_name VARCHAR(255) NOT NULL" +
                    ");");
            //System.out.println("result = " + result);

            if (result == -1) {
                System.err.println("Unexpected result, please run the program again...");
            }
            else {
                System.out.println("Table creation attempted! If it did not exist, it has been created!");
            }

        } catch (SQLException e) {
            System.err.println("Error creating table: " + e.getMessage());
        }
        
    }

    public void addEmployee(Scanner scnr) {
        String query = "INSERT INTO employees (first_name, last_name) VALUES (?, ?)";
        String [] employeeName = UserInput.getEmployeeNames(scnr);

        if (connection == null) {
            System.err.println("No database connection established.");
            return;
        }

        try {
            // Use PreparedStatement to prevent "SQL Injection"
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, employeeName[0]);
            pstmt.setString(2, employeeName[1]);
            int affected = pstmt.executeUpdate(); // TODO: Change results to rowsAffected

            //System.out.println(Colors.GREEN + "Rows affected: " + affected + Colors.RESET_COLOR);

            if (affected == 0) {
                System.out.println(Colors.GREEN + "╔════════════════════════════════════════╗");
                System.out.println("║ Rows affected: " + affected + "                       ║");
                System.out.println("║ Error adding employee.                 ║");
                System.out.println("╚════════════════════════════════════════╝" + Colors.RESET_COLOR);
            } else {
                System.out.println(Colors.GREEN + "╔════════════════════════════════════════╗");
                System.out.println("║ Rows affected: " + affected + "                       ║");
                System.out.println("║ Employee successfully added.           ║");
                System.out.println("╚════════════════════════════════════════╝\n" + Colors.RESET_COLOR);
            }

            UserInput.waitForUserContinue(scnr);
        } catch (SQLException e) {
            System.err.println("Error adding employee: " + e.getMessage());
        }
    }

    public void viewEmployees(Scanner scnr) {
        // TODO: Do proper outputting and coloring
        String query = "SELECT * FROM employees";
//        String formattedOutput = "+----+------------+-----------+\n" +
//                    "| ID | First Name | Last Name |\n" +
//                    "+----+------------+-----------+";
        String firstName, lastName;
        int employeeID = - 1;
        ResultSet resultSet;
        try {
            resultSet = stmt.executeQuery(query);

            //System.out.println(formattedOutput);

            while (resultSet.next()) {
                employeeID = resultSet.getInt("id");
                firstName = resultSet.getString("first_name");
                lastName = resultSet.getString("last_name");

                System.out.println(employeeID + " " + firstName + " " + lastName);
            }

            UserInput.waitForUserContinue(scnr);
            //System.out.println("+----+------------+-----------+");

        } catch (SQLException e) {
            System.err.println("Error viewing employees: " + e.getMessage());
        }

    }

    public void updateEmployee(Scanner scnr) { // TODO: use chosenField to see which field to update
        int chosenField = UserInput.getUserChosenField(scnr);
        String query = "UPDATE employees SET first_name = ? WHERE id = ?";
        int employeeID = UserInput.getEmployeeID(scnr);
        String updatedFirstName = UserInput.getUpdatedFirstName(scnr);
        //String updatedLastName = UserInput.getUpdatedLastName(scnr);
        int result;

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, updatedFirstName);
            pstmt.setInt(2, employeeID);

            result = pstmt.executeUpdate();
            System.out.println("Rows affected: " + result);
            // TODO: Implement proper formatting for rows affected like other methods

            UserInput.waitForUserContinue(scnr);

        } catch (SQLException e) {
            System.out.println("Error updating employee: " + e.getMessage());
        }
    }

    public void deleteEmployee(Scanner scnr) {
        String query = "DELETE FROM employees WHERE id =?";
        int employeeID = UserInput.getEmployeeID(scnr);
        int result; // Stores # of rows affected

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, employeeID);

            result = pstmt.executeUpdate();
            //System.out.println(Colors.GREEN + "Rows affected: " + result);
            if (result == 0) {
                // Error message
                System.out.println(Colors.RED + "╔════════════════════════════════════════╗");
                System.out.println("║ Rows affected: " + result + "                        ║");
                System.out.println("║ Error: Employee ID does not exist.      ║");
                System.out.println("╚════════════════════════════════════════╝" + Colors.RESET_COLOR);
            } else {
                // Success message
                System.out.println(Colors.GREEN + "╔════════════════════════════════════════╗");
                System.out.println("║ Rows affected: " + result + "                       ║");
                System.out.println("║ Employee successfully deleted.         ║");
                System.out.println("╚════════════════════════════════════════╝" + Colors.RESET_COLOR);
            }

            UserInput.waitForUserContinue(scnr);

        } catch (SQLException e) {
            System.err.println("Error deleting employee: " + e.getMessage());
        }
    }
}

//
//
//    int affected = stmt.executeUpdate("UPDATE `login_schema`.`USERS` " +
//            "SET `password` = 'EEeee' " +
//            "WHERE (`idusers` = '1');"
//    );
//
//    ResultSet resultSet = stmt.executeQuery("SELECT * FROM USERS");
//
//            while(resultSet.next()) {
//        System.out.println(resultSet.getString("username"));
//        System.out.println(resultSet.getString("password"));
//    }
//
//            System.out.println("Rows affected: " + affected);

