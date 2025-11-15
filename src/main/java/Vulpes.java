import java.util.Scanner;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.List;
import java.util.stream.Collectors;

public class Vulpes { // https://letterboxd.com/film/fantastic-mr-fox/
    //TODO: TextUiTesting if time allows

    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    public Vulpes(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (VulpesException e) {
            ui.showError("...");
            tasks = new TaskList();
        }
    }

    public void run() { // assume I cannot change this
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine(); // show the divider line ("_______")
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (VulpesException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();;
            }
        }
        Storage.readFile(Storage.checkFile());
    }

    public class Ui {
        private final Scanner scanner; // private

        public Ui() { // make scanner
            this.scanner = new Scanner(System.in);
        }

        public static void showWelcome() {
            showLine();
            System.out.println("Canis Lupus? Vulpes vulpes!");
            System.out.println("All right, let's start planning. Who knows shorthand?");
            showLine();
        }

        public static void showLine() {
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

    public static class Parser {

        public static Command parse(String fullCommand) throws VulpesException { // parses full command string, only interprets and packages; supersedes the prior processor
            String[] parts = fullCommand.trim().split(" ", 2);
            String command = parts[0].toLowerCase();
            String params = (parts.length > 1) ? parts[1].trim() : ""; // handle spaces

            switch (command) { // only passing back to command, no execution
                case "bye":
                    return new ExitCommand();

                case "list":
                    return new ListCommand();

                case "delete":
                    return parseDelete(params);

                case "mark":
                case "unmark":
                    return parseStatus(command, params);

                case "todo":
                case "deadline":
                case "event":
                    return parseAdd(command, params);

                default:
                    throw new VulpesException("I don't know what you're talking about, but it sounds illegal (it looks like you aren't issuing a valid command!).");
            }
        }

        private static AddCommand parseAdd(String command, String params) throws VulpesException { // handlers moved
            if (params.isEmpty()) {
                throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (" + command + "' requires more details!).");
            }

            switch (command) {
                case "todo":
                    if (params.contains(" /by") || params.contains(" /from") || params.contains(" /to")) {
                        throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (todo cannot use time keywords like /by, /from, or /to!).");
                    }

                    return new AddCommand(Command.TaskType.TODO, params, null, null, null); // create if checks passed

                case "deadline":
                    if (!params.contains(" /by ")) {
                        throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (deadline command requires a '/by' keyword!).");
                    }
                    String[] deadlineParts = params.split(" /by ", 2);
                    String deadlineDescription = deadlineParts[0].trim();
                    String deadlineBy = (deadlineParts.length > 1) ? deadlineParts[1].trim() : "";

                    if (deadlineDescription.isEmpty() || deadlineBy.isEmpty()) {
                        throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (deadline description and time cannot be empty!).");
                    }
                    // Create an AddCommand for a Deadline
                    return new AddCommand(Command.TaskType.DEADLINE, deadlineDescription, deadlineBy, null, null); // create if checks passed

                case "event":
                    if (!params.contains(" /from ") || !params.contains(" /to ")) {
                        throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (event requires '/from' and '/to' keywords!).");
                    }
                    String[] fromParts = params.split(" /from ", 2);
                    String eventDescription = fromParts[0].trim();
                    if (fromParts.length < 2) { // Defensive check
                        throw new VulpesException("Malformed event command. Missing text after /from.");
                    }
                    String[] toParts = fromParts[1].split(" /to ", 2);
                    if (toParts.length < 2) { // Defensive check
                        throw new VulpesException("Malformed event command. Missing text after /to.");
                    }
                    String eventFrom = toParts[0].trim();
                    String eventTo = toParts[1].trim();

                    if (eventDescription.isEmpty() || eventFrom.isEmpty() || eventTo.isEmpty()) {
                        throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (event description, start time, and end time cannot be empty!).");
                    }
                    // Create an AddCommand for an Event
                    return new AddCommand(Command.TaskType.EVENT, eventDescription, null, eventFrom, eventTo); // create if checks passed

                default: // in case
                    throw new VulpesException("Uh-oh, we got it VERY wrong...");
            }
        }

        private static DeleteCommand parseDelete(String params) throws VulpesException { // handlers moved
            if (params.isEmpty()) {
                throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (delete command requires a task number!).");
            }
            try {
                int taskIndex = Integer.parseInt(params);
                return new DeleteCommand(taskIndex);
            } catch (NumberFormatException e) {
                throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (the task number requires... well, a number... not '" + params + "').");
            }
        }

        private static StatusCommand parseStatus(String command, String params) throws VulpesException { // handlers moved
            if (params.isEmpty()) {
                throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (" + command + " command requires a task number!).");
            }
            try {
                int taskIndex = Integer.parseInt(params);
                boolean isMark = command.equals("mark");
                return new StatusCommand(taskIndex, isMark);
            } catch (NumberFormatException e) {
                throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (the task number requires... well, a number... not '" + params + "').");
            }
        }
    }

    public static class TaskList {
        if (line.equals("bye")) bye(tasks); // exit
        else {
            if (line.equals("list")) { // show all in array
                System.out.println("Synchronize your clocks. The time is now 9:45 a.m. (beeping) Here, put these bandit hats on.");
                for (int i = 0; i < tasks.size(); ++i) System.out.println((i + 1) + "." + tasks.get(i).toString());
            } else { // if not then all other cases that modify the array
                String[] input = line.split(" "); // chop up the input line
                String command = input[0];
                String params = line.replaceFirst(command + "\\s*", "").trim(); // extract params while accounting for them not being around, also handle spacing
                switch (input[0]) {
                    case "mark":
                    case "unmark":
                    case "delete": // borrow mark and unmark logic of 1 command + 1 param
                        if (params.isEmpty()) { // check for missing params
                            throw new Command.VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (mark/unmark command requires a task number! Try again like 'mark/unmark + [index]').");
                        }

                        int testIndex;

                        try { // check if param is valid
                            testIndex = Integer.parseInt(params);
                        } catch (NumberFormatException e) {
                            throw new Command.VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (the task number requires... well, number... not '" + params + "').");
                        }

                        if (testIndex <= 0 || testIndex > tasks.size()) { // check if index is within range - from 1 to current
                            throw new Command.VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (task " + testIndex + " doesn't exist! There are only " + tasks.size() + " targets in the list at the moment).");
                        }

                        if (input[0].equals("mark")) { // if all checks pass code executes
                            tasks.get(testIndex - 1).setStatus(true);
                            System.out.println("It's good for morale. Done.");
                        } else if (input[0].equals("unmark")) {
                            tasks.get(testIndex - 1).setStatus(false);
                            System.out.println("But it's... not done yet.");
                        } else { // delete
                            listUpdater(tasks, "", new String[] {params}); // empty priority is deletion flag
                        }

                        if (!input[0].equals("delete")) System.out.println("  " + tasks.get(testIndex - 1).toString()); // if delete, updater handles printing
                        break;

                    case "todo": // take in description only
                        if (params.isEmpty()) { // check for missing params
                            throw new Command.VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (todo command requires description! Try again like 'todo [description]').");
                        }

                        if (line.contains(" /by") || line.contains(" /from") || line.contains(" /to")) { // check for mismatched command-delimiter use
                            throw new Command.VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (todo command cannot use time or date keywords like /by, /from, or /to! Try again with 'deadline' or 'event'?).");
                        }

                        listUpdater(tasks, "T", new String[] {params}); // pass to updater
                        break;

                    case "deadline": // take in description and end
                        if (params.isEmpty()) { // check for missing params
                            throw new Command.VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (deadline command requires description and deadline! Try again like 'deadline [description] /by [time/date]");
                        }

                        if (line.contains(" /from") || line.contains(" /to")) { // check for mismatched command-delimiter use
                            throw new Command.VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (deadline command requires /by keyword, and cannot use /from or /to keywords! Try again with 'event'?).");
                        }

                        if (!line.contains(" /by")) { // check for mismatched command-delimiter use
                            throw new Command.VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (deadline command requires /by keyword! Try again with 'deadline [description] /by [time/date]').");
                        }

                        String[] deadlineParts = params.split(" /by ", 2); // if all checks pass code executes; split with " /by " - spaces for clean split
                        String deadlineDescription = deadlineParts[0];
                        String deadlineEnd = deadlineParts.length > 1 ? deadlineParts[1] : "";

                        if (deadlineDescription.isEmpty() || deadlineEnd.isEmpty()) { // check description or end time are empty
                            throw new Command.VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (deadline command requires description and end time! Try again like 'deadline [description] /[end time]').");
                        }

                        listUpdater(tasks, "D",new String[] {deadlineDescription, deadlineEnd}); // pass to updater
                        break;

                    case "event":
                        if (params.isEmpty()) { // check for missing params
                            throw new Command.VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (event command requires description, start time and end time! Try again like 'event [description] /from [start] /to [end]').");
                        }

                        if (line.contains(" /by")) { // check for mismatched command-delimiter use
                            throw new Command.VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (event command requires /from and /to keywords, and cannot use /by keyword! Try again with 'deadline'?).");
                        }

                        if (!line.contains(" /from") || !line.contains(" /to")) { // check for mismatched command-delimiter use
                            throw new Command.VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (event command requires /from and /to keywords! Try again like 'event [description] /from [start] /to [end]').");
                        }

                        String[] eventPartsFrom = params.split(" /from ", 2); // split with "/from" to get description etc
                        if (eventPartsFrom.length < 2) {
                            throw new Command.VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (event command requires /from keyword! Try again like 'event [description] /from [start] /to [end]').");
                        }

                        String eventDescription = eventPartsFrom[0].trim();
                        String rest = eventPartsFrom[1];

                        String[] eventPartsTo = rest.split(" /to ", 2); // split again with  "/to" to get start and end
                        if (eventPartsTo.length < 2) {
                            throw new Command.VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (event command requires /to keyword! Try again like 'event [description] /from [start] /to [end]').");
                        }

                        String eventStart = eventPartsTo[0];
                        String eventEnd = eventPartsTo[1];

                        if (eventDescription.isEmpty() || eventStart.isEmpty() || eventEnd.isEmpty()) { // check for missing params
                            throw new Command.VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (event command requires description, start time and and end time! Try again like 'event [description] /from [start] /to [end]').");
                        }

                        listUpdater(tasks, "E",new String[] {eventDescription, eventStart, eventEnd}); // pass to updater
                        break;

                    default:
                        throw new Command.VulpesException("I don't know what you're talking about, but it sounds illegal (it looks like you aren't issuing a valid command! Try again like 'todo','deadline','event','mark','unmark').");
                }
            }
            ui.showLine();;
            caller(tasks);
        }
    }

    public abstract class Command { // to be extended
        public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws VulpesException;

        public boolean isExit() { // default no
            return false;
        }

        public static enum TaskType { // limit to 3 types
            TODO,
            DEADLINE,
            EVENT
        }
    }

    public static class ExitCommand extends Command { // to exit
        @Override
        public boolean isExit() { // now true to exit
            return true;
        }

        @Override
        public void execute(TaskList tasks, Ui ui, Storage storage) { // save done by storage instead
            ui.showMessage("Okay, cuss you. Don't cussing point at me.");
        }
    }

    public static class ListCommand extends Command { // to list
        @Override
        public void execute(TaskList tasks, Ui ui, Storage storage) {
            ui.showMessage("Synchronize your clocks. The time is now 9:45 a.m. Here, put these bandit hats on.");

            if (tasks.isEmpty()) {
                ui.showMessage("The list is empty. There are no targets at the moment.");
            } else {
                for (int i = 0; i < tasks.size(); i++) { // accounted for index
                    ui.showMessage((i + 1) + "." + tasks.get(i).toString()); // print every line
                }
            }
        }
    }

    public static class StatusCommand extends Command {
        private final int taskIndex;
        private final boolean status; // true for mark, false for unmark

        public StatusCommand(int taskIndex, boolean status) { // const
            this.taskIndex = taskIndex;
            this.status = status;
        }

        @Override
        public void execute(TaskList tasks, Ui ui, Storage storage) throws VulpesException {
            if (taskIndex <= 0 || taskIndex > tasks.size()) { // validation here
                throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (task "
                        + taskIndex + " doesn't exist! There are only "
                        + tasks.size() + " targets in the list at the moment).");
            }

            Task taskToUpdate = tasks.get(taskIndex - 1); // accounted for index
            taskToUpdate.setStatus(status);

            if (status) {
                ui.showMessage("It's good for morale. Done.");
            } else {
                ui.showMessage("But it's... not done yet.");
            }
            ui.showMessage("  " + taskToUpdate.toString());

            storage.save(tasks); // save
        }
    }

    public class DeleteCommand extends Command {
        private final int taskIndex;

        public DeleteCommand(int taskIndex) {
            this.taskIndex = taskIndex;
        }

        @Override
        public void execute(TaskList tasks, Ui ui, Storage storage) throws VulpesException {
            if (taskIndex <= 0 || taskIndex > tasks.size()) { // validation here
                throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (task "
                        + taskIndex + " doesn't exist! There are only "
                        + tasks.size() + " targets in the list at the moment).");
            }

            Task removedTask = tasks.remove(taskIndex - 1); // accounted for index

            ui.showMessage("Noted. I've removed this task from the list:");
            ui.showMessage("  " + removedTask.toString());
            ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");

            storage.save(tasks); // save
        }
    }

    public static class AddCommand extends Command {
        private final Command.TaskType type;
        private final String description;
        private final String by;   // For Deadline
        private final String from; // For Event
        private final String to;   // For Event

        public AddCommand(Command.TaskType type, String description, String by, String from, String to) { // full params for all 3, most is with events
            this.type = type;
            this.description = description;
            this.by = by;
            this.from = from;
            this.to = to;
        }

        @Override
        public void execute(TaskList tasks, Ui ui, Storage storage) {
            Task newTask;

            switch (type) { // create right object
                case TODO:
                    newTask = new Todo(description);
                    break;
                case DEADLINE:
                    newTask = new Deadline(description, by);
                    break;
                case EVENT:
                    newTask = new Event(description, from, to);
                    break;
                default: // in case
                    ui.showError("Uh-oh, we got it VERY wrong...");
                    return;
            }

            tasks.add(newTask);

            ui.showMessage("Another target added to the list:");
            ui.showMessage("  " + newTask.toString());
            ui.showMessage("Now you have " + tasks.size() + " targets in the list at the moment.");

            // Save changes to the file
            storage.save(tasks);
        }
    }

    public static class Storage {

        private static final Path filePath = Paths.get("data", "Vulpes.txt"); // components for cross-platform compatibility

        public static boolean checkFile() {
            //TODO: corruption handling
            return Files.exists(filePath);
        }

        private static void readFile(boolean exists) {
            var tasks = new ArrayList<Task>(); // array updated to arraylist now
            if (exists) { // if save file exists
                try (Stream<String> lines = Files.lines(filePath)) { // retrieves all lines from file
                    lines.forEach(line -> { // format we will use is 'type|status|description|by/from|to|'
                        String[] taskParts = line.split("\\|");
                        switch (taskParts[0]) {
                            case "T":
                                tasks.add(new Todo(taskParts[1], taskParts[2]));
                                break;
                            case "D":
                                tasks.add(new Deadline(taskParts[1], taskParts[2], taskParts[3]));
                                break;
                            case "E":
                                tasks.add(new Event(taskParts[1], taskParts[2], taskParts[3], taskParts[4]));
                                break;
                        }
                    });
                } catch (IOException e) {
                    System.err.println("Error reading the file: " + e.getMessage());
                }
            }
            tryCall(tasks); // first call
        }

        private static void writeFile(ArrayList<Task> tasks) {
            List<String> linesToWrite = tasks.stream()
                    .map(Task::toFileString) // make each task to file format string
                    .collect(Collectors.toList());

            try {

                Files.createDirectories(filePath.getParent()); // check directory exists before writing

                Files.write(filePath, linesToWrite); // overwrite old Vulpes file
            } catch (IOException e) {
                System.err.println("Error writing to the file: " + e.getMessage());
            }
        }
    }

    public abstract static class Task { // partial solution template; replaces earlier 2-array storage system; made abstract to prevent instantiation; superseded by to-do
        protected String description; // task body
        protected String priority; // task type
        protected boolean isDone; // mark/unmark flag

        //TODO: enums for priority if time allows

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
        public String toString() {
            return "";
        } // suggested by AI to streamline printing and respect OOP

        public String toFileString() {
            return "";
        }

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

        public Todo(String status, String description) {
            super.description = description;
            super.isDone = status.equals("1");
            super.priority = "T";
        }

        @Override
        public String toFileString() { // type|status|description
            return "T|" + (super.isDone ? "1" : "0") + "|" + getDescription();
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

        public Deadline(String status, String description, String end) {
            super.description = description;
            super.isDone = status.equals("1");
            super.priority = "D";
            this.end = end;
        }

        @Override
        public String toFileString() { // type|status|description|by
            return "D|" + (super.isDone ? "1" : "0") + "|" + getDescription() + "|" + this.end;
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

        public Event(String status, String description, String start, String end) {
            super.description = description;
            super.isDone = status.equals("1");
            super.priority = "E";
            this.start = start;
            this.end = end;
        }

        @Override
        public String toFileString() { // type|status|description|start|end
            return "E|" + (super.isDone ? "1" : "0") + "|" + getDescription() + "|" + this.start + "|" + this.end;
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

    public static class VulpesException extends Exception { // extending Java exceptions
        public VulpesException(String message) {
            super(message);
        }
    }

    public static void listUpdater(ArrayList<Task> tasks, String priority, String [] params) { // announces changes, added is a deletion flag
        if (!priority.isEmpty()) { // if list received a new item
            System.out.println("Another target added to the list:");
            switch (priority) { // add item to list according to type
                case "T":
                    tasks.add(new Todo(params[0])); // if its a to-do
                    break;
                case "D":
                    tasks.add(new Deadline(params[0], params[1])); // if its a deadline
                    break;
                case "E":
                    tasks.add(new Event(params[0], params[1], params[2])); // if its an event
                    break;
            }
            System.out.println("  " + tasks.get(tasks.size() - 1).toString()); // print the item in question
        } else { // if item was deleted from the list
            System.out.println("Target deleted from the list:");
            System.out.println("  " + tasks.get(Integer.parseInt(params[0]) - 1).toString()); // print the item in question
            tasks.remove(Integer.parseInt(params[0]) - 1); // execute delete after printing if it was flagged
        }
    }

    public static void tryCall(ArrayList<Task> tasks) { // first call
        try {
            caller(tasks); // starts the requests to user
        } catch (VulpesException e) {
            System.out.println("Uh-oh, we got it wrong. " + e.getMessage());
            ui.showLine();;
            tryCall(tasks);
        }
    }

    public static void caller(ArrayList<Task> tasks) throws VulpesException { // next request from user
        Scanner scanner = new Scanner(System.in);
        String nextItem = scanner.nextLine();
        ui.showLine();;
        Parser(tasks, nextItem);
    }

    public static void bye(ArrayList<Task> tasks) { // ends session
        System.out.println("*whistles, clicks tongue* (Bye!)");
        ui.showLine();;
        Storage.writeFile(tasks);
    }

    public static void main(String[] args) {
        run();
    }
}

// google
// w3schools
// looked at jiahaos repo but only for L0/1/2 for ideas of how to encapsulate better