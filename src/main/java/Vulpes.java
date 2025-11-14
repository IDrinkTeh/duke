import java.util.Scanner;

public class Vulpes {
    //TODO:TextUiTesting if time allows
    public static void hello() { // https://letterboxd.com/film/fantastic-mr-fox/
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

    public static void echo() { // echo echo echo...
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

    public static class Task { // partial solution template; replaces earlier 2-array storage system
        protected String description;
        protected boolean isDone;

        public Task(String description) {
            this.description = description;
            this.isDone = false;
        }

        public String getStatusIcon() {
            return (isDone ? "X" : " "); // mark done task with X
        }

        public void setStatus(boolean status) {
            isDone = status;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static void Processor(Task[] stored, String line, int index) { // takes in and switches based on user input
        Scanner scanner = new Scanner(System.in);
        if (line.equals("bye")) {
            bye();
            return;
        }
        if (line.equals("list")) {
            for (int i = 0; i < index; i++) System.out.println((i+1) + ". [" + stored[i].getStatusIcon() + "] " + stored[i].getDescription());
        } else if (line.startsWith("mark ") || line.startsWith("unmark ")) { // limit function to "mark/unmark [position]" only
            if (line.startsWith("mark ")) {
                line = line.replace("mark ", ""); // truncates and leaves only target position to mark/unmark
                stored[Integer.parseInt(line) - 1].setStatus(true);
                System.out.println("It's good for morale. Done.");
            } else {
                line = line.replace("unmark ", ""); // truncates and leaves only target position to mark/unmark
                stored[Integer.parseInt(line) - 1].setStatus(false);
                System.out.println("But it's... not done yet.");
            }
            System.out.println("   [" + stored[Integer.parseInt(line) - 1].getStatusIcon() + "] " + stored[Integer.parseInt(line) - 1].getDescription());
        } else {
            stored[index] = new Task(line);
            ++index;
            System.out.println(line + "?");
        }
        System.out.println("____________________________________________________________");
        line = scanner.nextLine();
        System.out.println("____________________________________________________________");
        Processor(stored, line, index);
    }

    public static void list() { // storage of items
        System.out.println("What's that?");
        System.out.println("____________________________________________________________");
        Task[] stored = new Task[100]; // only 1 array needed now
        int idx = 0;
        Scanner scanner = new Scanner(System.in);
        String nextItem = scanner.nextLine();
        Processor(stored, nextItem, idx);
    }

    public static void bye() { // ends session
        System.out.println("*whistles, clicks tongue* (Bye!)");
    }

    public static void main(String[] args) {
        hello();
        list();
    }
}