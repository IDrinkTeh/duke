package vulpes.command;

import vulpes.exception.VulpesException;
import vulpes.storage.Storage;
import vulpes.task.Deadline;
import vulpes.task.Event;
import vulpes.task.Task;
import vulpes.task.Todo;
import vulpes.tasklist.TaskList;
import vulpes.ui.Ui;

public class DeleteCommand extends Command {
    private final int taskIndex;

    public DeleteCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws VulpesException {
        if (taskIndex <= 0 || taskIndex > tasks.size()) { // validation here
            throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (task "
                    + taskIndex + " doesn't exist! There are only "
                    + tasks.size() + " targets in the list at the moment).");
        }

        Task removedTask = tasks.remove(taskIndex - 1); // accounted for index

        ui.showMessage("Noted. I've removed this task from the list:");
        ui.showMessage("  " + removedTask.toString());
        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");

        storage.save(tasks); // save
    }
}