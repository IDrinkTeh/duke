package vulpes.command;

import vulpes.exception.VulpesException;
import vulpes.storage.Storage;
import vulpes.task.Deadline;
import vulpes.task.Event;
import vulpes.task.Task;
import vulpes.task.Todo;
import vulpes.tasklist.TaskList;
import vulpes.ui.Ui;

public class ExitCommand extends Command { // to exit
    @Override
    public boolean isExit() { // now true to exit
        return true;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) { // save done by storage instead
        ui.showMessage("*whistles, clicks tongue* (Bye!)");
    }
}