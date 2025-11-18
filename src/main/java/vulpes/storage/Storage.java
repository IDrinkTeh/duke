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

public class Storage {
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