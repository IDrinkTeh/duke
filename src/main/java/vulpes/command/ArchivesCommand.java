package vulpes.command;

import vulpes.storage.Storage;
import vulpes.tasklist.TaskList;
import vulpes.ui.Ui;

/**
 * Extension of abstract base class used to show the archives and the tasks therein
 */
public class ArchivesCommand extends Command { // to list
    /**
     * Overrides execution in the abstract base class
     * Produces flavour for user
     * Flavour changes depending on number of tasks in the archives
     * Iterates through archives and prints every task
     *
     * @param archivedTasks Instance of the Tasklist class for archives
     * @param ui Instance of the UI class
     * @param archives Instance of the Storage class for archives
     */
    @Override
    public void execute(TaskList archivedTasks, Ui ui, Storage archives) {
        ui.showMessage("You know what? I'm going to just put dirt in my ears. Yeah. That's better. I can't hear you now."); // flavour

        if (archivedTasks.isEmpty("archives")) { // check for empty archives
            ui.showMessage("The archives are empty. There are no targets at the moment.");
        } else {
            for (int i = 0; i < archivedTasks.size("archives"); i++) { // accounted for index
                ui.showMessage((i + 1) + "." + archivedTasks.get("archives", i).toString()); // print every line
            }
        }
    }
}