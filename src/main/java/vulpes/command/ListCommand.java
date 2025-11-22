package vulpes.command;

import vulpes.storage.Storage;
import vulpes.tasklist.TaskList;
import vulpes.ui.Ui;

/**
 * Extension of abstract base class used to show the list and the tasks therein
 */
public class ListCommand extends Command { // to list
    /**
     * Overrides execution in the abstract base class
     * Produces flavour for user
     * Flavour changes depending on number of tasks in the list
     * Iterates through list and prints every task
     * @param listTasks Instance of TaskList class for list
     * @param ui Instance of UI class
     * @param list Instance of Storage class for list
     */
    @Override
    public void execute(TaskList listTasks, Ui ui, Storage list) {
        ui.showMessage("Synchronize your clocks. The time is now 9:45 a.m. Here, put these bandit hats on.");

        if (listTasks.isEmpty()) {
            ui.showMessage("The list is empty. There are no targets at the moment.");
        } else {
            for (int i = 0; i < listTasks.size(); i++) { // accounted for index
                ui.showMessage((i + 1) + "." + listTasks.get(i).toString()); // print every line
            }
        }
    }
}