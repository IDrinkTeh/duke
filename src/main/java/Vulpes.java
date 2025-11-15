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

    public static class VulpesException extends Exception { // make use of existing exceptions
        public VulpesException(String message) {
            super(message);
        }
    }

    public static void Processor(Task[] stored, String line, int index) throws VulpesException { // takes in initial input, switches functions based on input; had some help from AI here for ideas for handling errors
        if (line.equals("bye")) bye(); // exit
        else if (line.equals("list")) { // show all in array
            System.out.println("Synchronize your clocks. The time is now 9:45 a.m. (beeping) Here, put these bandit hats on.");
            for (int i = 0; i < index; i++) System.out.println((i + 1) + "." + stored[i].toString());
        } else { // if not then all other cases that modify the array
            String[] input = line.split(" "); // chop up the input line
            switch (input[0]) {
                case "mark":
                case "unmark":
                    if (input.length < 2) { // check if there is param after command
                        throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (mark/unmark command needs a task number! Try again like 'mark/unmark + [index]').");
                    }

                    int testIndex;
                    try { // check if param is valid
                        testIndex = Integer.parseInt(input[1]);
                    } catch (NumberFormatException e) {
                        throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (the task number must be a... well, number... not '" + input[1] + "').");
                    }

                    if (testIndex <= 0 || testIndex > index) { // check if index is within range - from 1 to current
                        throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (task " + testIndex + " doesn't exist! There are only " + index + " targets in the list at the moment).");
                    }

                    if (input[0].equals("mark")) { // if all checks pass code executes
                        stored[testIndex - 1].setStatus(true);
                        System.out.println("It's good for morale. Done.");
                    } else {
                        stored[testIndex - 1].setStatus(false);
                        System.out.println("But it's... not done yet.");
                    }
                    System.out.println("  " + stored[testIndex - 1].toString());
                    break;
                case "todo": // take in description only
                    if (line.trim().equals("todo")) { // check if no description
                        throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (todo command requires a description! Try again like 'todo [description]').");
                    }

                    String todoContent = line.replace("todo ", "");

                    if (todoContent.contains(" /by") || todoContent.contains(" /from") || todoContent.contains(" /to")) { // check for mismatched command-delimiter use
                        throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (todo command cannot have time or date keywords like /by, /from, or /to! Try again with 'deadline' or 'event'?).");
                    }

                    line = line.replace("todo ", ""); // if all checks pass code executes
                    stored[index] = new Todo(line);
                    index = added(stored, index);
                    break;
                case "deadline": // take in description and end
                    if (line.trim().equals("deadline")) { // check for missing params
                        throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (deadline command requires a description and deadline! Try again like 'deadline [description] /by [time/date]");
                    }

                    String deadlineContent = line.replace("deadline ", "");


                    if (deadlineContent.contains(" /from") || deadlineContent.contains(" /to")) { // check for mismatched command-delimiter use
                        throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (deadline command requires /by keyword, and cannot use /from or /to keywords! Try again with 'event'?).");
                    }

                    if (!deadlineContent.contains(" /by")) { // check for mismatched command-delimiter use
                        throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (deadline command requires /by keyword! Try again with 'deadline [description] /by [time/date]').");
                    }

                    String[] deadlineParts = deadlineContent.split(" /by ", 2); // if all checks pass code executes; split with " /by " - spaces for clean split
                    String deadlineDescription = deadlineParts[0].trim();
                    String deadlineEnd = deadlineParts.length > 1 ? deadlineParts[1].trim() : "";

                    if (deadlineDescription.isEmpty() || deadlineEnd.isEmpty()) { // check description or end time are empty
                        throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (deadline command requires description and end time! Try again like 'deadline [description] /[end time]').");
                    }

                    stored[index] = new Deadline(deadlineDescription, deadlineEnd); // if all checks pass code executes
                    index = added(stored, index);
                    break;
                case "event":
                    if (line.trim().equals("event")) { // check if there isn't a single param
                        throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (event command requires description, start time and end time! Try again like 'event [description] /from [start] /to [end]').");
                    }

                    String eventContent = line.replace("event ", "");

                    if (eventContent.contains(" /by")) { // check for mismatched command-delimiter use
                        throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (event command requires /from and /to keywords, and cannot use /by keyword! Try again with 'deadline'?).");
                    }

                    if (!eventContent.contains(" /from") || !eventContent.contains(" /to")) { // check for mismatched command-delimiter use
                        throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (event command requires /from and /to keywords! Try again like 'event [description] /from [start] /to [end]').");
                    }

                    String[] eventPartsFrom = eventContent.split(" /from ", 2); // split with "/from" to get description etc
                    if (eventPartsFrom.length < 2) {
                        throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (event command requires /from keyword! Try again like 'event [description] /from [start] /to [end]').");
                    }

                    String eventDescription = eventPartsFrom[0].trim();
                    String rest = eventPartsFrom[1];

                    String[] eventPartsTo = rest.split(" /to ", 2); // split again with  "/to" to get start and end
                    if (eventPartsTo.length < 2) {
                        throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (event command requires /to keyword! Try again like 'event [description] /from [start] /to [end]').");
                    }

                    String eventStart = eventPartsTo[0].trim();
                    String eventEnd = eventPartsTo[1].trim();

                    // 4. Check if any part is empty
                    if (eventDescription.isEmpty() || eventStart.isEmpty() || eventEnd.isEmpty()) {
                        throw new VulpesException("The **event** command requires a description, start time (after /from), and end time (after /to).");
                    }

                    stored[index] = new Event(eventDescription, eventStart, eventEnd);
                    index = added(stored, index);
                    break;
                default:
                    throw new VulpesException("I don't know what you're talking about, but it sounds illegal (it looks like you aren't issuing a valid command! Try again like 'todo','deadline','event','mark','unmark').");
            }
        }
        System.out.println("____________________________________________________________");
        caller(stored, index);
    }


    public static int added(Task[] stored, int index) { // array storage of items
        System.out.println("Another target added to the list:");
        System.out.println("  " + stored[index].toString());
        if (index + 1 == 1) System.out.println("Now there is 1 target in our list!");
        else  System.out.println("Now there are " + (index + 1) + " targets in our list!");
        return ++index;
    }

    public static void store() { // array storage of items; had some help from AI here for ideas for handling errors
        System.out.println("All right, let's start planning. Who knows shorthand?");
        System.out.println("____________________________________________________________");
        Task[] stored = new Task[100]; // only 1 array needed now
        int index = 0;
        try {
            caller(stored, index); // starts the requests to user
        } catch (VulpesException e) {
            System.out.println("Uh-oh, we got it wrong. " + e.getMessage());
            store();
        }
    }

    public static void caller(Task[] stored, int index) throws VulpesException { // next request from user
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