package vulpes.command;

import vulpes.storage.Storage;
import vulpes.tasklist.TaskList;
import vulpes.ui.Ui;

/**
 * Extension of abstract base class used to exit the app
 */
public class ExitCommand extends Command {
    /**
     * Overrides default exit flag to indicate exiting
     */
    @Override
    public boolean isExit() { // now true to exit
        return true;
    }

    /**
     * Overrides execution in the abstract base class
     * Produces feedback for user
     * @param tasks Instance of TaskList class
     * @param ui Instance of UI class
     * @param storage Instance of Storage class
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showMessage("*whistles, clicks tongue* (Bye!)");
    }
}