import java.util.Scanner;

public class Vulpes {
    public static void hello() {
        System.out.println("____________________________________________________________");
        System.out.println("Canis Lupus? Vulpes vulpes!");
    }

    /*
    public static void switcher(String x) {
        switch (x) {
            case "echo":
                echo();
            case "bye":
                bye();
            case "echo":
                store();
        }
     }
     */

    public static void echo() {
        System.out.println("What'd the doctor say?");
        System.out.println("____________________________________________________________");
        Scanner scanner = new Scanner(System.in);
        String echo = scanner.nextLine();
            while (!echo.equals("bye")) {
                System.out.println(echo);
                echo = scanner.nextLine();
            }
        bye();
    }

    public static void store() {
        System.out.println("What's that?");
        System.out.println("____________________________________________________________");
        String[] stored = new String[100];
        int idx = 0;
        Scanner scanner = new Scanner(System.in);
        String nextItem = scanner.nextLine();
        System.out.println("____________________________________________________________");
        while (!nextItem.equals("bye")) {
            if (nextItem.equals("list")) {
                for (int i = 0; i < idx; i++) System.out.println(stored[i]);
            } else {
                stored[idx] = nextItem;
                ++idx;
                System.out.println(nextItem + "?");
            }
            System.out.println("____________________________________________________________");
            nextItem = scanner.nextLine();
            System.out.println("____________________________________________________________");
        }
        bye();
    }

    public static void bye() {
        System.out.println("*whistles, clicks tongue* (Bye!)");
    }

    public static void main(String[] args) {
        hello();
        store();
    }
}