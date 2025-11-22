package vulpes.command;

import vulpes.exception.VulpesException;
import vulpes.storage.Storage;
import vulpes.task.Task;
import vulpes.tasklist.TaskList;
import vulpes.ui.Ui;

/**
 * Extension of abstract base class used to mark tasks in the list as done or undone
 */
public class StatusCommand extends Command {
    /**
     * Indicates index of task to delete
     */
    private final int taskIndex;
    /**
     * Indicates whether task is to be marked or not
     */
    private final boolean status; // true for marking, false for unmarking

    /**
     * Constructor takes in index of list and whether task is to be marked or not
     * @param taskIndex Indicates index of task to delete
     * @param status Indicates whether task is to be marked or not
     */
    public StatusCommand(int taskIndex, boolean status) {
        this.taskIndex = taskIndex;
        this.status = status;
    }

    /**
     * Overrides execution in the abstract base class
     * Checks for whether index selected for marking/unmarking is valid
     * Marks/unmarks task and produces feedback for user
     * Calls storage to save once task deleted from list
     * @param tasks Instance of TaskList class
     * @param ui Instance of UI class
     * @param storage Instance of Storage class
     * @throws VulpesException if task to mark/unmark does not exist
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws VulpesException {
        if (taskIndex <= 0 || taskIndex > tasks.size("")) {
            throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (task "
                    + taskIndex + " doesn't exist! There are only "
                    + tasks.size("") + " targets in the list at the moment).");
        }

        Task taskToUpdate = tasks.get("", taskIndex - 1); // accounted for index
        taskToUpdate.setStatus(status);

        if (status) {
            ui.showMessage("It's good for morale. Done.");
        } else {
            ui.showMessage("But it's... not done yet.");
        }
        ui.showMessage("  " + taskToUpdate.toString());

        storage.save("", tasks.getAllTasks(""));
    }
}