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

    private Storage storage;
    private Ui ui;
    private TaskList tasks;

    public Vulpes(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load().getAllTasks());
        } catch (VulpesException e) {
            ui.showError("...");
            tasks = new TaskList(); // start with empty list
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
    }

    public static class Ui {
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

                    return new AddCommand(Command.TaskType.DEADLINE, deadlineDescription, deadlineBy, null, null); // create if checks passed

                case "event":
                    if (!params.contains(" /from ") || !params.contains(" /to ")) {
                        throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (event requires '/from' and '/to' keywords!).");
                    }
                    String[] fromParts = params.split(" /from ", 2);
                    String eventDescription = fromParts[0].trim();
                    if (fromParts.length < 2) {
                        throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (event requires dates after the keywords!).");
                    }
                    String[] toParts = fromParts[1].split(" /to ", 2);
                    if (toParts.length < 2) {
                        throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (event requires dates after the keywords!).");
                    }
                    String eventFrom = toParts[0].trim();
                    String eventTo = toParts[1].trim();

                    if (eventDescription.isEmpty() || eventFrom.isEmpty() || eventTo.isEmpty()) {
                        throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (event description, start time, and end time cannot be empty!).");
                    }

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
        private ArrayList<Task> tasks;

        public TaskList() {
            this.tasks = new ArrayList<>(); // without save file
        }

        public TaskList(ArrayList<Task> tasks) {
            this.tasks = tasks; // T, D, E
        }

        public void add(Task task) { // adder
            this.tasks.add(task);
        }

        public Task remove(int taskIndex) { // deleter
            return this.tasks.remove(taskIndex);
        }

        public Task get(int taskIndex) { // finder
            return this.tasks.get(taskIndex);
        }

        public int size() {
            return this.tasks.size();
        }

        public boolean isEmpty() {
            return this.tasks.isEmpty();
        }

        public ArrayList<Task> getAllTasks() { // return self
            return this.tasks;
        }
    }

    public abstract static class Command { // to be extended
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
            ui.showMessage("*whistles, clicks tongue* (Bye!)");
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

    public static class DeleteCommand extends Command {
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
        public void execute(TaskList tasks, Ui ui, Storage storage) throws VulpesException {
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

            storage.save(tasks); // save
        }
    }

    public static class Storage {
        private final Path filePath; // manage path

        public Storage(String filePath) {
            this.filePath = Paths.get(filePath);
        }

        public TaskList load() throws VulpesException {
            ArrayList<Task> loadedTasks = new ArrayList<>();
            try {
                if (Files.exists(filePath)) { // if there is save
                    List<String> lines = Files.readAllLines(filePath); // load all
                    for (String line : lines) {
                        if (line.trim().isEmpty()) { // attempt to handle problematic lines
                            continue;
                        }
                        loadedTasks.add(parseLineToTask(line)); // parse line
                    }
                } else {
                    Files.createDirectories(filePath.getParent()); // check directory before save
                }
            } catch (IOException e) {
                throw new VulpesException("Uh-oh, we got it wrong. " + e.getMessage());
            }
            return new TaskList(loadedTasks); // pass back tasks
        }

        public void save(TaskList tasks) throws VulpesException {
            try {
                ArrayList<String> linesToWrite = new ArrayList<>(); // temp list
                for (Task task : tasks.getAllTasks()) {
                    linesToWrite.add(task.toFileString()); // load up lines
                }
                Files.write(this.filePath, linesToWrite); // write
            } catch (IOException e) {
                throw new VulpesException("Uh-oh, we got it wrong. " + e.getMessage());
            }
        }

        private static Task parseLineToTask(String line) throws VulpesException { // parses 1 line
            String[] parts = line.split("\\|"); // type|status|description|by/from|to

            if (parts.length < 3) { // added validation of minimum 3 parts
                throw new VulpesException("Corrupted data in file. Line is too short: " + line);
            }

            Task task;
            String taskType = parts[0];
            boolean isDone = parts[1].equals("1");

            switch (taskType) {
                case "T":
                    task = new Todo(parts[2]);
                    break;
                case "D":
                    if (parts.length < 4) throw new VulpesException("Corrupted deadline data in file: " + line);
                    task = new Deadline(parts[2], parts[3]);
                    break;
                case "E":
                    if (parts.length < 5) throw new VulpesException("Corrupted event data in file: " + line);
                    task = new Event(parts[2], parts[3], parts[4]);
                    break;
                default:
                    throw new VulpesException("Unknown task type '" + taskType + "' found in file. Data may be corrupted.");
            }

            if (isDone) {
                task.setStatus(true);
            }
            return task;
        }
    }

    public abstract static class Task { // made abstract to prevent instantiation
        protected String description; // task body
        protected String priority; // task type
        protected boolean isDone; // mark/unmark flag

        //TODO: enums for priority if time allows?

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

    public static void main(String[] args) {
        new Vulpes("data/Vulpes.txt").run();
    }
}

// google
// w3schools
// looked at jiahaos repo but only for L0/1/2 for ideas of how to encapsulate better