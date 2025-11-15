package vulpes.command;

import vulpes.exception.VulpesException;
import vulpes.storage.Storage;
import vulpes.task.Deadline;
import vulpes.task.Event;
import vulpes.task.Task;
import vulpes.task.Todo;
import vulpes.tasklist.TaskList;
import vulpes.ui.Ui;

public class StatusCommand extends Command {
    private final int taskIndex;
    private final boolean status; // true for mark, false for unmark

    public StatusCommand(int taskIndex, boolean status) { // const
        this.taskIndex = taskIndex;
        this.status = status;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws VulpesException {
        if (taskIndex <= 0 || taskIndex > tasks.size()) { // validation here
            throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (task "
                    + taskIndex + " doesn't exist! There are only "
                    + tasks.size() + " targets in the list at the moment).");
        }

        Task taskToUpdate = tasks.get(taskIndex - 1); // accounted for index
        taskToUpdate.setStatus(status);

        if (status) {
            ui.showMessage("It's good for morale. Done.");
        } else {
            ui.showMessage("But it's... not done yet.");
        }
        ui.showMessage("  " + taskToUpdate.toString());

        storage.save(tasks); // save
    }
}