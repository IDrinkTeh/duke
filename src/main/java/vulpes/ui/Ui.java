package vulpes.ui;

import java.util.Scanner;

public class Ui {
    private final Scanner scanner; // private

    public Ui() { // make scanner
        this.scanner = new Scanner(System.in);
    }

    public void showWelcome() {
        showLine();
        System.out.println("Canis Lupus? Vulpes vulpes!");
        System.out.println("All right, let's start planning. Who knows shorthand?");
        showLine();
    }

    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showError(String message) {
        System.out.println("Uh-oh, we got it wrong. " + message);
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}