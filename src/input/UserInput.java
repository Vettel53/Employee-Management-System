package input;

import java.awt.*;
import java.util.Scanner;
import config.Colors;

public class UserInput {
    private static final String firstField = "id";
    private static final String secondField = "first_name";
    private static final String thirdField = "last_name";

    public static String[] getEmployeeNames(Scanner scnr) {
        String firstName, lastName;

        System.out.print(Colors.GREEN + "Please enter your employee's first name: " + Colors.RESET_COLOR);
        firstName = scnr.next();
        System.out.print(Colors.GREEN + "\nPlease enter your employee's last name: " + Colors.RESET_COLOR);
        lastName = scnr.next();
        System.out.println();

        return new String[] {firstName, lastName};
    }

    public static String getUpdatedFirstName(Scanner scnr) {
        String firstName;

        System.out.print(Colors.GREEN + "Please enter your employee's NEW first name: " + Colors.RESET_COLOR);
        firstName = scnr.next();
        System.out.println();

        return firstName;
    }

    public static String getUpdatedLastName(Scanner scnr) {
        String lastName;

        System.out.print(Colors.GREEN + "\nPlease enter your employee's NEW last name: " + Colors.RESET_COLOR);
        lastName = scnr.next();
        System.out.println();

        return lastName;
    }

    public static int getEmployeeID(Scanner scnr) { // TODO: Implement error handling for invalid employee ID
        int employeeID;

        System.out.print(Colors.GREEN + "\nPlease enter the employee ID: " + Colors.RESET_COLOR);

        while (!scnr.hasNextInt()) {
            scnr.next(); // Consume non-integer input
            System.out.println(Colors.RED + "Invalid input. Please enter an integer." + Colors.RESET_COLOR);
        }

        employeeID = scnr.nextInt();
        System.out.println();

        return employeeID;
    }

    public static void waitForUserContinue(Scanner scnr) {
        System.out.print(Colors.GREEN + "\nPress Enter to continue..." + Colors.RESET_COLOR);
        try {
            System.in.read();
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
        System.out.println();

        //System.out.println(Color.BLUE + "Continuing..." + Colors.RESET_COLOR);
    }

    public static int getUserChosenField(Scanner scnr) { // TODO: Should be finished, just look over it
        int userChoice = -1;

        System.out.print(Colors.ORANGE + "\nChoose a field to update (1 - 3)\n" + Colors.RESET_COLOR);
        System.out.println(Colors.ORANGE + "\t(1) " + firstField + Colors.RESET_COLOR);
        System.out.println(Colors.ORANGE + "\t(2) " + secondField + Colors.RESET_COLOR);
        System.out.println(Colors.ORANGE + "\t(3) " + thirdField + Colors.RESET_COLOR);
        System.out.print(Colors.RED + "\nPlease choose an option (1-3): " + Colors.RESET_COLOR);

        while (true) {

            while (!scnr.hasNextInt()) {
                scnr.next(); // Consume non-integer input
                System.out.print(Colors.RED + "Invalid input. Please enter an integer: " + Colors.RESET_COLOR);
            }
            userChoice = scnr.nextInt();
            if (userChoice >= 1 && userChoice <= 3) {
                break;
            } else {
                System.out.print(Colors.RED + "Out of range! Enter a valid field: " + Colors.RESET_COLOR);
            }

        }

        return userChoice;

    }
}
