package main;

import config.DatabaseConnection;
import config.Colors;
import dao.EmployeeDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    static Scanner scnr = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {

        commandLineMenu();

    }

    public static void commandLineMenu() throws SQLException {
        int menuInput = -1;
        DatabaseConnection dbConnector = new DatabaseConnection();
        Connection connection = dbConnector.connectToDatabaseServer();

        EmployeeDAO employeeDAO = new EmployeeDAO(connection);

        boolean userContinue = false;
        String userValidate = null;
        String schemaName = "employee_schema";
        System.out.println("This program will create a SQL scheme named " + schemaName + " if it doesn't already exist");
        System.out.print("\nEnter '" + schemaName + "' to continue: ");

        while (userContinue == false) {
            userValidate = scnr.nextLine();
            if (!userValidate.equalsIgnoreCase(schemaName)) {
                System.out.print("\nInvalid input. Please enter '" + schemaName + "' to continue: ");
            } else {
                userContinue = true;
                System.out.println("Thank you! Continuing...\n");
            }
        }

        employeeDAO.createDatabase();
        employeeDAO.createTable(dbConnector);

        while (menuInput != 5) {
            // Display main menu
            System.out.println(Colors.ORANGE + "        ╔═(1) Add Employee");
            System.out.println(         "        ║");
            System.out.println(         "        ╠══(2) View Employees");
            System.out.println(         "        ║");
            System.out.println(         "        ╠═══(3) Update Employee");
            System.out.println(         "        ║");
            System.out.println(         "        ╚╦═══(4) Delete Employee");
            System.out.println(         "         ║");
            System.out.println(         "         ╚═══(5) Exit" + Colors.RESET_COLOR);

            System.out.print(Colors.GREEN + "\nPlease choose an option (1-5): " + Colors.RESET_COLOR);

            menuInput = getUserMenuInput();

            switch (menuInput) {
                case 1:
                    employeeDAO.addEmployee(scnr);
                    break;
                case 2:
                    employeeDAO.viewEmployees(scnr);
                    break;
                case 3:
                    employeeDAO.updateEmployee(scnr);
                    break;
                case 4:
                    employeeDAO.deleteEmployee(scnr);
//                    System.out.println(Colors.LIGHT_PURPLE + "This is Light Purple" + Colors.RESET_COLOR);
//                    System.out.println(Colors.LIGHT_BLUE + "This is Light Blue" + Colors.RESET_COLOR);
//                    System.out.println(Colors.DARK_GREEN + "This is Dark Green" + Colors.RESET_COLOR);
//                    System.out.println(Colors.ORANGE + "This is Orange" + Colors.RESET_COLOR);
//                    System.out.println(Colors.PINK + "This is Pink" + Colors.RESET_COLOR);
                    break;
                case 5:
                    System.out.println(Colors.CYAN + "Exiting the program... Thank you!" + Colors.RESET_COLOR);
                    break;
                default:
                    System.out.println(Colors.RED + "Invalid choice. Please choose a valid option (1-5)." + Colors.RESET_COLOR);
            }
        }

        scnr.close();
    }

    /**
     * Prompts the user for menu input and validates it.
     * This method continuously prompts the user until a valid integer between 0 and 5 is entered.
     * It handles invalid inputs by displaying appropriate error messages.
     *
     * @return An integer representing the user's menu choice (0-5).
     */
    public static int getUserMenuInput() {
        int userInput = -1;

        while (true) {
            if (scnr.hasNextInt()) {
                userInput = scnr.nextInt();

                if (userInput < 0 || userInput > 5) {
                    System.out.println(Colors.RED + "Invalid choice. Please choose a valid option between 0 and 5." + Colors.RESET_COLOR);
                } else {
                    break; // Valid input, exit loop
                }
            } else {
                scnr.next(); // Consume non-integer input
                System.out.println(Colors.RED + "Invalid input. Please enter an integer." + Colors.RESET_COLOR);
            }
        }

        System.out.println();
        return userInput;
    }

}