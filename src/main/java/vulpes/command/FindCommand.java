package vulpes.command;

import vulpes.storage.Storage;
import vulpes.task.Task;
import vulpes.tasklist.TaskList;
import vulpes.ui.Ui;

import java.util.ArrayList;

/**
 * Extension of abstract base class used to show specific tasks in the list depending on criteria
 */
public class FindCommand extends Command { // to list
    private final String criteria; // true for archiving, false for unarchiving
    private final ArrayList<Task> listFound = new ArrayList<>(); // subarray of list tasks found
    private final ArrayList<Task> archivesFound = new ArrayList<>(); // subarray of archives tasks found

    /**
     * Constructor that only takes in search criteria
     *
     * @param params The parameters that will be used as search criteria
     * */
    public FindCommand(String params) {
        this.criteria = params.trim(); // trim to improve match accuracy ; other better SEO practices omitted due to time constraints
    }

    /**
     * Overrides execution in the abstract base class
     * Produces flavour for user
     * Flavour changes depending on number of tasks in the list
     * Iterates through list and prints every task that corresponds to criteria
     *
     * @param tasks Instance of the Tasklist class
     * @param ui Instance of the UI class
     * @param storage Instance of the Storage class
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        for (Task task : tasks.getAllTasks("")) {
            if (task.getDescription().contains(criteria)) {
                listFound.add(task); // search through list for criteria and append to subarray
            }
        }
        for (Task task : tasks.getAllTasks("archives")) {
            if (task.getDescription().contains(criteria)) {
                archivesFound.add(task); // search through archives for criteria and append to subarray
            }
        }

        ui.showMessage("I'm going to find him, and I'm going to bring him back."); // flavour
        ui.showLine();

        if (tasks.isEmpty("")) { // check if list empty
            ui.showMessage("The list is empty. There are no targets at the moment.");
        } else if (!tasks.isEmpty("") && listFound.isEmpty()) { // if list not empty but no matches found
            ui.showMessage("Nothing was found in the list...");
        } else { // if matches found
            ui.showMessage(listFound.size() + " found in the list:");
            for (int i = 0; i < listFound.size(); i++) { // accounted for index
                ui.showMessage((i + 1) + "." + listFound.get(i).toString()); // print every line found
            }
        }
        ui.showLine();

        if (tasks.isEmpty("archives")) { // check if archives empty
            ui.showMessage("The archives are empty. There are no targets at the moment.");
        } else if (!tasks.isEmpty("archives") && archivesFound.isEmpty()) { // if archives not empty but no matches found
            ui.showMessage("Nothing was found in the archives...");
        } else { // if matches found
            ui.showMessage(archivesFound.size() + " found in the archives:");
            for (int i = 0; i < archivesFound.size(); i++) { // accounted for index
                ui.showMessage((i + 1) + "." + archivesFound.get(i).toString()); // print every line found
            }
        }
    }
}