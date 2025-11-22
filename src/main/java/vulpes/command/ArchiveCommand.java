package vulpes.command;

import vulpes.exception.VulpesException;
import vulpes.storage.Storage;
import vulpes.task.Task;
import vulpes.tasklist.TaskList;
import vulpes.ui.Ui;

/**
 * Extension of abstract base class used to delete tasks from the list
 */
public class ArchiveCommand extends Command {
    /**
     * Indicates index of task to delete
     */
    private final int taskIndex;

    /**
     * Standard contractor that only takes in index of task in list
     * @param taskIndex Index of the task selected from list to be deleted
     */
    public ArchiveCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    /**
     * Overrides execution in the abstract base class
     * Checks for whether index selected for deletion is valid
     * Deletes task and produces feedback for user
     * Calls storage to save once task deleted from list
     * @param tasks Instance of TaskList class
     * @param ui Instance of UI class
     * @param storage Instance of Storage class
     * @throws VulpesException if task to delete does not exist
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws VulpesException {
        if (taskIndex <= 0 || taskIndex > tasks.size("")) {
            throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (task "
                    + taskIndex + " doesn't exist! There are only "
                    + tasks.size("") + " targets in the list at the moment).");
        }

        Task removedTask = tasks.remove("", taskIndex - 1); // accounted for index

        ui.showMessage("Noted. I've removed this task from the list:");
        ui.showMessage("  " + removedTask.toString());
        ui.showMessage("Now you have " + tasks.size("") + " tasks in the list.");

        storage.save("", tasks);
    }
}