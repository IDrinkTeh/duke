package vulpes.ui;

import vulpes.command.ListCommand;

import java.util.Scanner;

/**
 * Class used to contain methods for taking input and giving output to user
 */
public class Ui {
    private final Scanner scanner; // private

    /**
     * Constructor that creates scanner for taking in input
     */
    public Ui() { // make scanner
        this.scanner = new Scanner(System.in);
    }

    /**
     * Method to greet the user upon app launch
     */
    public void showWelcome() {
        showLine();
        System.out.println("Canis Lupus? Vulpes vulpes!");
        System.out.println("All right, let's start planning. Who knows shorthand?");
        showLine();
    }

    /**
     * Method to print a dividing line
     */
    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    /**
     * Method to take in next line from user input
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Method to append default error indication to error message presented to user
     * @param message The actual exception/error to display to user
     */
    public void showError(String message) {
        System.out.println("Uh-oh, we got it wrong. " + message);
    }

    /**
     * Method to print to user
     * @param message The message to output to user
     */
    public void showMessage(String message) {
        System.out.println(message);
    }
}