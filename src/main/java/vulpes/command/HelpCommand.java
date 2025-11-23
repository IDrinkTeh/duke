package vulpes.command;

import vulpes.storage.Storage;
import vulpes.tasklist.TaskList;
import vulpes.ui.Ui;

/**
 * Extension of abstract base class used to show the list and the tasks therein
 */
public class HelpCommand extends Command { // to list
    /**
     * Overrides execution in the abstract base class
     * Gives user all available comments and other tips to use the app
     */
    @Override
    public void execute(TaskList listTasks, Ui ui, Storage list) {
        ui.showHelp();
    }
}