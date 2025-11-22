package vulpes.storage;

import vulpes.exception.VulpesException;
import vulpes.task.Deadline;
import vulpes.task.Event;
import vulpes.task.Task;
import vulpes.task.Todo;
import vulpes.tasklist.TaskList;

import java.util.HashMap;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

/**
 * Class used to store and retrieve any list from user's local directory
 */
public class Storage {
    /**
     * Manages the path
     */
    private final Path list;
    private final Path archived;

    /**
     * Constructor that takes in only path
     * @param listPath The file path at which a list will be saved/loaded from the user's local directory
     * @param archivedPath The file path at which archives will be saved/loaded from the user's local directory
     */
    public Storage(String listPath, String archivedPath) {
        this.list = Paths.get(listPath);
        this.archived = Paths.get(archivedPath);
    }

    /**
     * Method to check if storage file exists on user's local directory and loads from there
     * creates a file if it does not already exist
     * @param listPath The file path at which a list will be saved/loaded from the user's local directory
     * @param archivedPath The file path at which archives will be saved/loaded from the user's local directory
     * @return A list of tasks loaded from the file - could be none.
     * @throws IOException if read or write fails
     */
    public TaskList load(Path listPath, Path archivedPath) throws VulpesException { // TaskList usage
        HashMap<Path, ArrayList<Task>> collection = new HashMap<>(); // https://www.w3schools.com/java/java_hashmap.asp
        collection.put(listPath ,new ArrayList<Task>());
        collection.put(archivedPath ,new ArrayList<Task>());
        collection.forEach(
                (path, tasks) -> { https://www.w3schools.com/java/ref_hashmap_foreach.asp
                    try {
                        if (Files.exists(path)) { // if there is save
                            List<String> lines = Files.readAllLines(path); // load all
                            for (String line : lines) {
                                if (line.trim().isEmpty()) { // attempt to handle problematic lines
                                    continue;
                                }
                                tasks.add(parseLineToTask(line)); // parse line
                            }
                        } else {
                            Files.createDirectories(path.getParent()); // check directory before save
                        }
                    } catch (IOException e) { // these are bullshit and have to be fixed
                        try {
                            throw new VulpesException("Uh-oh, we got it wrong. " + e.getMessage());
                        } catch (VulpesException ex) {
                            throw new RuntimeException(ex);
                        }
                    } catch (VulpesException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        return new TaskList(collection.get(listPath), collection.get(archivedPath)); // pass back tasks
    }

    /**
     * Method to write to storage file on user's local directory
     * overwrites existing file
     * @param tasks A list of tasks to write to file
     * @param path The path to write to
     * @throws IOException if read or write fails
     */
    public void save(TaskList tasks, Path path) throws VulpesException {
        try {
            ArrayList<String> linesToWrite = new ArrayList<>(); // temp list
            for (Task task : tasks.getAllTasks(path)) {
                linesToWrite.add(task.toFileString()); // load up lines
            }
            Files.write(path, linesToWrite); // write
        } catch (IOException e) {
            throw new VulpesException("Uh-oh, we got it wrong. " + e.getMessage());
        }
    }

    /**
     * Formatter to ensure proper writing and reading
     */
    private static final DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm a");

    /**
     * Method segments the lines found
     * parses line by line to create new task
     * checks if line is corrupted
     * returns task to loader
     * @param line Line to parse for loading into list
     * @return Singular parsed task for loading into list
     * @throws VulpesException if data is not formatted the way it was expected to be
     */
    private static Task parseLineToTask(String line) throws VulpesException {
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
                task = new Deadline(parts[2], LocalDateTime.parse(parts[3]));
                break;
            case "E":
                if (parts.length < 5) throw new VulpesException("Corrupted event data in file: " + line);
                task = new Event(parts[2], LocalDateTime.parse(parts[3]), LocalDateTime.parse(parts[4]));
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