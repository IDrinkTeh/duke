package vulpes.command;

import vulpes.exception.VulpesException;
import vulpes.storage.Storage;
import vulpes.tasklist.TaskList;
import vulpes.ui.Ui;

/**
 * Abstract base class used to contain methods for executing user commands
 */
public abstract class Command { // to be extended
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws VulpesException;

    /**
     * Exit flag is false by default until it is time to say goodbye
     */
    public boolean isExit() { // default no
        return false;
    }

    /**
     * Limits the tasks to 3 types
     */
    public enum TaskType {
        TODO,
        DEADLINE,
        EVENT
    }
}