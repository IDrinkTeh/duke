package vulpes.command;

import vulpes.exception.VulpesException;
import vulpes.exception.InvalidTaskException;
import vulpes.storage.Storage;
import vulpes.task.Task;
import vulpes.tasklist.TaskList;
import vulpes.ui.Ui;

/**
 * Extension of abstract base class used to delete tasks from the list
 */
public class ArchiveCommand extends Command {
    private final int taskIndex; // index of task to delete
    private final boolean status; // true for archiving, false for unarchiving

    /**
     * Constructor takes in index of list and whether task is to be archived or unarchived
     *
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
     *
     * @param tasks Instance of the Tasklist class
     * @param ui Instance of the UI class
     * @param storage Instance of the Storage class
     * @throws VulpesException if task to delete does not exist
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws VulpesException {
        if (status) { // archival flag
            if (taskIndex <= 0 || taskIndex > tasks.size("")) { // check for index out of bounds
                throw new InvalidTaskException(taskIndex, tasks.size(""));
            }
            Task transferTask = tasks.remove("", taskIndex - 1); // accounted for index
            tasks.add("archives", transferTask); // add to archives

            // feedback to user
            ui.showMessage("Noted. I've removed this task from the list and added it to the archives:");
            ui.showMessage("  " + transferTask.toString());
            ui.showMessage("Now you have " + tasks.size("archives") + " tasks in the archives.");

        } else { // un-archival flag
            if (taskIndex <= 0 || taskIndex > tasks.size("archives")) { // check for index out of bounds
                throw new InvalidTaskException(taskIndex, tasks.size(""));
            }
            Task transferTask = tasks.remove("archives", taskIndex - 1); // accounted for index
            tasks.add("", transferTask); // add to list

            // feedback to user
            ui.showMessage("Noted. I've removed this task from the archives and added it to the list:");
            ui.showMessage("  " + transferTask.toString());
            ui.showMessage("Now you have " + tasks.size("") + " tasks in the list.");
        }

        storage.save("", tasks.getAllTasks("")); // update list save
        storage.save("archives", tasks.getAllTasks("archives")); // update archives save
    }
}