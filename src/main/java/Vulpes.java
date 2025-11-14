import java.util.Scanner;
import java.util.Arrays;

public class Vulpes { // L0

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

    public static void echo() { // L1
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

    public static void store() { // L2,3
        System.out.println("What's that?");
        System.out.println("____________________________________________________________");
        String[] stored = new String[100];
        String[] done = new String[100]; // no time for multimap
        Arrays.fill(done, " "); // all unmarked by default
        int idx = 0;
        Scanner scanner = new Scanner(System.in);
        String nextItem = scanner.nextLine();
        System.out.println("____________________________________________________________");
        while (!nextItem.equals("bye")) {
            if (nextItem.equals("list")) {
                for (int i = 0; i < idx; i++) System.out.println((i+1) + ". [" + done[i] + "] " + stored[i]);
            } else if (nextItem.startsWith("mark ")) { // limit function to "mark [number]" only
                nextItem = nextItem.replace("mark ","");
                done[Integer.parseInt(nextItem)-1] = "x";
                System.out.println("It's good for morale. Done.");
                System.out.println("   [" + done[Integer.parseInt(nextItem)-1] + "] " + stored[Integer.parseInt(nextItem)-1]);
            } else if (nextItem.startsWith("unmark ")) { // limit function to "unmark [number]" only
                nextItem = nextItem.replace("unmark ","");
                done[Integer.parseInt(nextItem)-1] = " ";
                System.out.println("But it's... not done yet.");
                System.out.println("   [" + done[Integer.parseInt(nextItem)-1] + "] " + stored[Integer.parseInt(nextItem)-1]);
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