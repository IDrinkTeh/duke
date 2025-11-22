package vulpes.storage;

import vulpes.exception.VulpesException;
import vulpes.task.Deadline;
import vulpes.task.Event;
import vulpes.task.Task;
import vulpes.task.Todo;
import vulpes.tasklist.TaskList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Class used to store and retrieve the list from user's local directory
 */
public class Storage {
    /**
     * Manages the path
     */
    private final Path filePath;

    /**
     * Constructor that takes in only path
     * @param filePath The file path at which the list will be saved/loaded from the user's local directory
     */
    public Storage(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    /**
     * Method to check if storage file exists on user's local directory and loads from there
     * creates a file if it does not already exist
     * @return List of tasks loaded from the file - could be none.
     * @throws IOException if read or write fails
     */
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

    /**
     * Method to write to storage file on user's local directory
     * overwrites existing file
     * @param tasks List of tasks to write to file
     * @throws IOException if read or write fails
     */
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

    /**
     * Method segments the lines found
     * parses line by line to create new task
     * checks if line is corrupted
     * returns task to loader
     * @param line Line to parse for loading into list
     * @return Singular parsed task for loading into list
     * @throws VulpesException if data is not formatted the way it was expected to be
     */

    private static final DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm a");

    private static Task parseLineToTask(String line) throws VulpesException {
        String[] parts = line.split("\\|"); // type|status|description|by/from|to

        if (parts.length < 3) { // added validation of minimum 3 parts
            throw new VulpesException("Corrupted data in file. Line is too short: " + line);
        }

        Task task;
        String taskType = parts[0];
        boolean isDone = parts[1].equals("1");

        try { // wrap to catch unexpected errors
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
                    LocalDateTime from = LocalDateTime.parse(parts[3], newFormatter); // added to address DateTimeParseException
                    LocalDateTime to = LocalDateTime.parse(parts[4], newFormatter); // added to address DateTimeParseException
                    task = new Event(parts[2], from, to);
                    break;
                default:
                    throw new VulpesException("Unknown task type '" + taskType + "' found in file. Data may be corrupted.");
            }
        } catch (DateTimeParseException e) {
            throw new VulpesException("Could not parse date, please check format. Error in line: " + line);
        }

        if (isDone) {
            task.setStatus(true);
        }
        return task;
    }
}