package vulpes.command;

import vulpes.exception.InvalidTaskException;
import vulpes.exception.VulpesException;
import vulpes.storage.Storage;
import vulpes.task.Task;
import vulpes.tasklist.TaskList;
import vulpes.ui.Ui;

/**
 * Extension of abstract base class used to delete tasks from the list
 */
public class DeleteCommand extends Command {
    private final int taskIndex; // index of task to delete

    /**
     * Constructor that only takes in index of task in list
     *
     * @param taskIndex Index of the task selected from list to be deleted
     */
    public DeleteCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    /**
     * Overrides execution in the abstract base class
     * Checks for whether index selected for deletion is valid
     * Deletes task and produces feedback for user
     * Calls storage to save once task deleted from list
     *
     * @param tasks Instance of the Tasklist class
     * @param ui Instance of the UI class
     * @param storage Instance of the Storage class
     * @throws VulpesException if task to delete does not exist
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws VulpesException {
        if (taskIndex <= 0 || taskIndex > tasks.size("")) { // check for index out of bounds
            throw new InvalidTaskException("list", taskIndex, tasks.size(""));
        }

        Task removedTask = tasks.remove("", taskIndex - 1); // accounted for index

        // feedback to user
        ui.showMessage("Noted. I've removed this task from the list:");
        ui.showMessage("  " + removedTask.toString());
        ui.showMessage("Now you have " + tasks.size("") + " tasks in the list.");

        storage.save("", tasks.getAllTasks("")); // update list save
    }
}