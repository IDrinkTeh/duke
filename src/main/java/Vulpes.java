import java.util.Scanner;

public class Vulpes {
    public static void hello() {
        System.out.println("Canis Lupus? Vulpes vulpes!\n");
        System.out.println("What'd the doctor say?\n");
    }

    public static void echo() {
        Scanner scanner = new Scanner(System.in);
        String echo = scanner.nextLine();
        while (!echo.equals("bye")) {
            System.out.println(echo + "\n");
            echo = scanner.nextLine();
        }
        bye();
    }

    public static void bye() {
        System.out.println("*whistles, clicks tongue* (Bye!)");
    }

    public static void main(String[] args) {
        hello();
        echo();
    }
}