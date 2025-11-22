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
     * Indicates whether task is to be archived or not
     */
    private final boolean status; // true for archiving, false for unarchiving

    /**
     * Constructor takes in index of list and whether task is to be archived or unarchived
     * @param taskIndex Index of the task selected from list to be deleted
     * @param status Indicates whether task is to be archived or not
     */
    public ArchiveCommand(int taskIndex, boolean status) {
        this.taskIndex = taskIndex;
        this.status = status;
    }

    /**
     * Overrides execution in the abstract base class
     * deletes task from list and creates task in archives
     * checks for whether index selected for deletion is valid
     * produces feedback for user
     * calls storage to save once task deleted from list and added to archives
     * @param tasks Instance of TaskList class
     * @param ui Instance of UI class
     * @param storage Instance of Storage class
     * @throws VulpesException if task to delete does not exist
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws VulpesException {
        if (status) { // archival flag
            if (taskIndex <= 0 || taskIndex > tasks.size("")) {
                throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (task "
                        + taskIndex + " doesn't exist! There are only "
                        + tasks.size("") + " targets in the list at the moment).");
            }
            Task transferTask = tasks.remove("", taskIndex - 1); // accounted for index
            tasks.add("archives", transferTask); // add to archives
            ui.showMessage("Noted. I've removed this task from the list and added it to the archives:");
            ui.showMessage("  " + transferTask.toString());
            ui.showMessage("Now you have " + tasks.size("archives") + " tasks in the archives.");
        } else { // un-archival flag
            if (taskIndex <= 0 || taskIndex > tasks.size("archives")) {
                throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (task "
                        + taskIndex + " doesn't exist! There are only "
                        + tasks.size("") + " targets in the list at the moment).");
            }
            Task transferTask = tasks.remove("archives", taskIndex - 1); // accounted for index
            tasks.add("", transferTask); // add to list
            ui.showMessage("Noted. I've removed this task from the archives and added it to the list:");
            ui.showMessage("  " + transferTask.toString());
            ui.showMessage("Now you have " + tasks.size("") + " tasks in the list.");
        }

        storage.save("", tasks.getAllTasks(""));
        storage.save("archives", tasks.getAllTasks("archives"));
    }
}