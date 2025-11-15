package vulpes.command;

import vulpes.exception.VulpesException;
import vulpes.storage.Storage;
import vulpes.task.Deadline;
import vulpes.task.Event;
import vulpes.task.Task;
import vulpes.task.Todo;
import vulpes.tasklist.TaskList;
import vulpes.ui.Ui;

public abstract class Command { // to be extended
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws VulpesException;

    public boolean isExit() { // default no
        return false;
    }

    public static enum TaskType { // limit to 3 types
        TODO,
        DEADLINE,
        EVENT
    }
}