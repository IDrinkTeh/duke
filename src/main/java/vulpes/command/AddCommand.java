package vulpes.command;

import vulpes.exception.VulpesException;
import vulpes.storage.Storage;
import vulpes.task.Deadline;
import vulpes.task.Event;
import vulpes.task.Task;
import vulpes.task.Todo;
import vulpes.tasklist.TaskList;
import vulpes.ui.Ui;

import java.time.*;

/**
 * Extension of abstract base class used to add tasks to the list
 */
public class AddCommand extends Command {

    private final Command.TaskType type;
    private final String description;
    /**
     * For Deadline
     */
    private final LocalDateTime by;
    /**
     * For Event
     */
    private final LocalDateTime from;
    /**
     * For Event
     */
    private final LocalDateTime to;

    /**
     * Constructor for To-do, no by, from, to
     */
    public AddCommand(TaskType type, String description) {
        this.type = type;
        this.description = description;
        this.by = null;
        this.from = null;
        this.to = null;
    }

    /**
     * Constructor for Deadline, no from and to
     */
    public AddCommand(TaskType type, String description, LocalDateTime by) {
        this.type = type;
        this.description = description;
        this.by = by;
        this.from = null;
        this.to = null;
    }

    /**
     * Constructor for Events, no by
     */
    public AddCommand(TaskType type, String description, LocalDateTime from, LocalDateTime to) {
        this.type = type;
        this.description = description;
        this.by = null;
        this.from = from;
        this.to = to;
    }

    /**
     * Overrides execution in the abstract base class
     * Specifies which task type to create
     * Produces feedback for user
     * Calls storage to save once task added to list
     */
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

        tasks.add("", newTask);

        ui.showMessage("Another target added to the list:");
        ui.showMessage("  " + newTask.toString());
        ui.showMessage("Now you have " + tasks.size("") + " targets in the list at the moment.");

        storage.save("", tasks);
    }
}