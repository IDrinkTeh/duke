package vulpes.command;

import vulpes.storage.Storage;
import vulpes.tasklist.TaskList;
import vulpes.ui.Ui;

/**
 * Extension of abstract base class used to show the list and the tasks therein
 */
public class ArchiveCommand extends Command { // to list
    /**
     * Overrides execution in the abstract base class
     * Produces flavour for user
     * Flavour changes depending on number of tasks in the list
     * Iterates through list and prints every task
     * @param tasks Instance of TaskList class
     * @param ui Instance of UI class
     * @param storage Instance of Storage class
     */
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