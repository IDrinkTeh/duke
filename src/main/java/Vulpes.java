import java.util.Scanner;

public class Vulpes {
    //TODO:TextUiTesting if time allows
    public static void hello() { // https://letterboxd.com/film/fantastic-mr-fox/
        System.out.println("____________________________________________________________");
        System.out.println("Canis Lupus? Vulpes vulpes!");
        store(); // construct storage
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

    public abstract static class Task { // partial solution template; replaces earlier 2-array storage system; made abstract to prevent instantiation; superseded by Todo
        protected String description;
        protected boolean isDone;
        protected String priority;
        /*
        public enum Priority {
            T, // ToDos
            D, // Deadlines
            E // Events
        }
        protected Priority priority;
         */

        public Task(String description, String priority) {
            this.description = description;
            this.isDone = false;
            this.priority = priority;
        }

        public Task() {
            this.description = "";
            this.isDone = false;
            this.priority = "";
        }

        @Override
        public String toString() {return "";} // suggested by AI to streamline printing and respect OOP

        public void setStatus(boolean status) {
            this.isDone = status;
        }

        public String getStatusIcon() {
            return (isDone ? "X" : " "); // mark done task with X
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDescription() {
            return this.description;
        }

        public void setPriority(String priority) {
            this.priority = priority;
        }

        public String getPriority() {
            return this.priority;
        }
    }

    public static class Todo extends Task { // ToDos: tasks without any date/time attached to it
        public Todo(String description) {
            super.description = description;
            super.isDone = false;
            super.priority = "T";
        }

        @Override
        public String toString() {return "[T][" + super.getStatusIcon() + "] " + super.getDescription();} // suggested by AI to streamline printing and respect OOP
    }

    public static class Deadline extends Task { // Deadlines: tasks that need to be done before a specific date/time e.g., submit report by 11/10/2019 5pm
        public Deadline(String description, String end) {
            super.description = description;
            super.isDone = false;
            super.priority = "D";
            this.end = end;
        }

        @Override
        public String toString() {return "[D][" + super.getStatusIcon() + "] " + super.getDescription() + " (by: " + this.getEnd() + ")";} // suggested by AI to streamline printing and respect OOP

        protected String end;

        public void setEnd (String datetime){
            this.end = datetime;
        }

        public String getEnd () {
            return this.end;
        }
    }

    public static class Event extends Task { // Events: tasks that start at a specific date/time and ends at a specific date/time e.g., (a) team project meeting 2/10/2019 2-4pm (b) orientation week 4/10/2019 to 11/10/2019
        public Event(String description, String start, String end) {
            super.description = description;
            super.isDone = false;
            super.priority = "E";
            this.start = start;
            this.end = end;
        }

        @Override
        public String toString() {return "[D][" + super.getStatusIcon() + "] " + super.getDescription() + " (from: " + this.getStart() + " to: " + this.getEnd() + ")";} // suggested by AI to streamline printing and respect OOP

        protected String start;
        protected String end;

        public void setStart(String datetime) {
            this.start = datetime;
        }

        public String getStart() {
            return this.start;
        }

        public void setEnd(String datetime) {
            this.end = datetime;
        }

        public String getEnd() {
            return this.end;
        }
    }

    public static void Processor(Task[] stored, String line, int index) { // takes in initial input, switches functions based on input
        if (line.equals("bye")) bye(); // exit
        else {
            if (line.equals("list")) { // show all in array
                System.out.println("Synchronize your clocks. The time is now 9:45 a.m. (beeping) Here, put these bandit hats on.");
                for (int i = 0; i < index; i++) System.out.println((i + 1) + "." + stored[i].toString());
            } else { // if not then all other cases that modify the array
                String[] input = line.split(" "); // chop up the input line
                switch (input[0]) {
                    case "mark": // limit function to "mark [position]" only
                        stored[Integer.parseInt(input[1]) - 1].setStatus(true); // mark as complete
                        System.out.println("It's good for morale. Done.");
                        System.out.println("  " + stored[Integer.parseInt(input[1]) - 1].toString());
                        break;
                    case "unmark": // limit function to "unmark [position]" only
                        stored[Integer.parseInt(input[1]) - 1].setStatus(false); // mark as incomplete
                        System.out.println("But it's... not done yet.");
                        System.out.println("  " + stored[Integer.parseInt(input[1]) - 1].toString());
                        break;
                    case "todo": // take in description only
                        line = line.replace("todo ", "");
                        stored[index] = new Todo(line); // Instantiate the object first
                        index = added(stored, index);
                        break;
                    case "deadline": // take in description and end
                        input = line.replace("deadline ", "").split(" /");
                        stored[index] = new Deadline(input[0], input[1]);
                        index = added(stored, index);
                        break;
                    case "event": // take in description, start and end
                        input = line.replace("event ", "").split(" /");
                        stored[index] = new Event(input[0], input[1], input[2]);
                        index = added(stored, index);
                        break;
                }
            }
            System.out.println("____________________________________________________________");
            caller(stored, index);
        }
    }

    public static int added(Task[] stored, int index) { // array storage of items
        System.out.println("Another target added to the list:");
        System.out.println("  " + stored[index].toString());
        if (index + 1 == 1) System.out.println("Now there is 1 target in our list!");
        else  System.out.println("Now there are " + (index + 1) + " targets in our list!");
        return ++index;
    }

    public static void store() { // array storage of items
        System.out.println("All right, let's start planning. Who knows shorthand?");
        System.out.println("____________________________________________________________");
        Task[] stored = new Task[100]; // only 1 array needed now
        int index = 0;
        caller(stored, index); // starts the requests to user
    }

    public static void caller(Task[] stored, int index) { // next request from user
        Scanner scanner = new Scanner(System.in);
        String nextItem = scanner.nextLine();
        System.out.println("____________________________________________________________");
        Processor(stored, nextItem, index);
    }

    public static void bye() { // ends session
        System.out.println("*whistles, clicks tongue* (Bye!)");
        System.out.println("____________________________________________________________");
    }

    public static void main(String[] args) {
        hello();
    }
}

// google
// w3schools
// looked at jiahaos repo but only for L0/1/2 for ideas of how to encapsulate better