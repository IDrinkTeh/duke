package vulpes.storage;

import vulpes.exception.InvalidLoadFormatException;
import vulpes.exception.VulpesException;
import vulpes.exception.StorageException;
import vulpes.task.Deadline;
import vulpes.task.Event;
import vulpes.task.Task;
import vulpes.task.Todo;
import vulpes.tasklist.TaskList;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class used to store and retrieve any list from user's local directory
 */
public class Storage {
    private final Path listPath; // path of list file on user's local directory
    private final Path archivesPath; // path of archives file on user's local directory
    private static final DateTimeFormatter NEW_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm a"); // for below datetime related exception usages

    /**
     * Constructor that takes in only path
     *
     * @param listPath The file path at which MissingParametersException list will be saved/loaded from the user's local directory
     * @param archivedPath The file path at which archives will be saved/loaded from the user's local directory
     */
    public Storage(String listPath, String archivedPath) {
        // non-null paths is what the constructor will expect
        assert listPath != null && !listPath.isBlank() : "listPath found null or blank.";
        assert archivedPath != null && !archivedPath.isBlank() : "archivedPath found null or blank.";

        this.listPath = Paths.get(listPath);
        this.archivesPath = Paths.get(archivedPath);
    }

    /**
     * Method to check if storage file exists on user's local directory and loads from there
     * creates a file if it does not already exist
     *
     * @param listPath The file path at which a list will be saved/loaded from the user's local directory
     * @param archivedPath The file path at which archives will be saved/loaded from the user's local directory
     * @return A list of tasks loaded from the file - could be none
     * @throws StorageException If there is load error
     * @throws InvalidLoadFormatException If data in loaded file is corrupted
     */
    public TaskList load(Path listPath, Path archivedPath) throws StorageException, InvalidLoadFormatException {
        // whole load method was reworked to compartmentalise earlier messy logic and reduce nesting
        // helper method used for delegation and to remain in line with OOP principles

        // non-null paths are expected to be provided by caller
        assert listPath != null : "listPath parameter null.";
        assert archivedPath != null : "archivedPath parameter null.";

        ArrayList<Task> listTasks = loadList(listPath);
        ArrayList<Task> archivesTasks = loadList(archivedPath);
        return new TaskList(listTasks, archivesTasks);
    }

    /**
     * Private helper method to handle loading
     * loads only from 1 file
     * better exceptions
     *
     * @param path The path to the file to load.
     * @return A list of tasks loaded
     * @throws StorageException If there is load error
     * @throws InvalidLoadFormatException If one of more lines in loaded file is corrupted
     */
    private ArrayList<Task> loadList(Path path) throws StorageException, InvalidLoadFormatException { // AI improved method from the earlier arrowhead code junk
        // valid path must be present for this method call
        assert path != null : "Path was null.";

        ArrayList<Task> tasks = new ArrayList<>(); // new empty list
        try {
            if (!Files.exists(path)) { // if there is no save path
                Files.createDirectories(path.getParent()); // make save path
                return tasks; // returns list empty
            }

            List<String> lines = Files.readAllLines(path); // list that contains all loaded lines
            for (String line : lines) { // read each line
                if (line.trim().isEmpty()) {
                    continue; // skip empty lines
                }
                tasks.add(parseLineToTask(line)); // let InvalidLoadFormatException from propagate upwards
            }
        } catch (IOException e) { // if read fails
            throw new StorageException(path, e);
        }
        return tasks;
    }

    /**
     * Method to write to storage file on user's local directory
     * overwrites existing file
     * @param tasks A list of tasks to write to file
     * @param list A list being written
     * @throws VulpesException if read or write fails
     */
    public void save(String list, ArrayList<Task> tasks) throws VulpesException {
        // method expects non-null list
        assert tasks != null : "Null task list was provided.";
        assert list != null : "Identifier was null.";

        try {
            ArrayList<String> linesToWrite = new ArrayList<>(); // temp list
            for (Task task : tasks) {
                linesToWrite.add(task.toFileString()); // load up lines
            }
            if (list.equals("archives")) Files.write(archivesPath, linesToWrite); // write
            else Files.write(listPath, linesToWrite);
        } catch (IOException e) {
            throw new VulpesException("Uh-oh, we got it wrong. " + e.getMessage());
        }
    }

    /**
     * Method segments the lines found
     * parses line by line to create new task
     * checks if line is corrupted
     * returns task to loader
     * @param line Line to parse for loading into list
     * @return Singular parsed task for loading into list
     * @throws InvalidLoadFormatException if data is not formatted the way it was expected to be
     */
    private static Task parseLineToTask(String line) throws InvalidLoadFormatException { // AI improved method from the earlier arrowhead code junk
        // empty or blank lines should already be removed
        assert line != null && !line.isBlank() : "Line passed was blank or null.";

        String[] parts = line.split("\\|"); // specify delimiters

        if (parts.length < 3) {
            throw new InvalidLoadFormatException("corrupted line is missing parts: " + line); // specific exception to catch corrupted line
        }

        String taskType = parts[0]; // command
        boolean isDone = parts[1].equals("1"); // marked/unmarked
        String description = parts[2];
        Task task; // task itself

        try {
            switch (taskType) { // based on command
            case "T":
                task = new Todo(description); // create To-do
                break;
            case "D": // create Deadline
                if (parts.length < 4) {
                    throw new InvalidLoadFormatException("corrupted Deadline line missing parts: " + line); // specific exception to catch corrupted line
                }
                LocalDateTime by = LocalDateTime.parse(parts[3], NEW_FORMATTER); // safely parse date
                task = new Deadline(description, by); // if checks passed create Deadline
                break;
            case "E": // create Event
                if (parts.length < 5) {
                    throw new InvalidLoadFormatException("corrupted Event line missing parts: " + line); // specific exception to catch corrupted line
                }

                LocalDateTime from = LocalDateTime.parse(parts[3], NEW_FORMATTER); // safely parse date
                LocalDateTime to = LocalDateTime.parse(parts[4], NEW_FORMATTER); // safely parse date
                task = new Event(description, from, to); // if checks passed create Event
                break;
            default:
                throw new InvalidLoadFormatException("unknown task type '" + taskType + "' found in file");
            }
        } catch (DateTimeParseException e) { // catch and wrap in VulpesException
            throw new InvalidLoadFormatException("invalid date format for line: " + line, e);
        }

        if (isDone) {
            task.setStatus(true);
        }
        return task;
    }
}