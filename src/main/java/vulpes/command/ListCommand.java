package vulpes.command;

import vulpes.exception.VulpesException;
import vulpes.storage.Storage;
import vulpes.task.Deadline;
import vulpes.task.Event;
import vulpes.task.Task;
import vulpes.task.Todo;
import vulpes.tasklist.TaskList;
import vulpes.ui.Ui;

public class ListCommand extends Command { // to list
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showMessage("Synchronize your clocks. The time is now 9:45 a.m. Here, put these bandit hats on.");

        if (tasks.isEmpty()) {
            ui.showMessage("The list is empty. There are no targets at the moment.");
        } else {
            for (int i = 0; i < tasks.size(); i++) { // accounted for index
                ui.showMessage((i + 1) + "." + tasks.get(i).toString()); // print every line
            }
        }
    }
}