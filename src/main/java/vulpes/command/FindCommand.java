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
    /**
     * Criteria to perform search with
     */
    private final String criteria; // true for archiving, false for unarchiving
    private final ArrayList<Task> listFound = new ArrayList<>(); // subarray of list tasks found
    private final ArrayList<Task> archivesFound = new ArrayList<>(); // subarray of archives tasks found

    /**
     * Constructor that only takes in search criteria
     * @param params The parameters that will be used as search criteria
     * */
    public FindCommand(String params) {
        this.criteria = params.trim();
    }

    /**
     * Overrides execution in the abstract base class
     * Produces flavour for user
     * Flavour changes depending on number of tasks in the list
     * Iterates through list and prints every task that corresponds to criteria
     * @param tasks Instance of TaskList class for list
     * @param ui Instance of UI class
     * @param list Instance of Storage class for list
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage list) {
        for (Task task : tasks.getAllTasks("")) if (task.getDescription().contains(criteria)) listFound.add(task); // search through list for criteria and append to subarray
        for (Task task : tasks.getAllTasks("archives")) if (task.getDescription().contains(criteria)) archivesFound.add(task); // search through archives for criteria and append to subarray

        ui.showMessage("I'm going to find him, and I'm going to bring him back.");
        ui.showLine();
        if (tasks.isEmpty("")) {
            ui.showMessage("The list is empty. There are no targets at the moment.");
        } else if (!tasks.isEmpty("") && listFound.isEmpty()) {
            ui.showMessage("Nothing was found in the list...");
        } else {
            ui.showMessage(listFound.size() + " found in the list:");
            for (int i = 0; i < listFound.size(); i++) { // accounted for index
                ui.showMessage((i + 1) + "." + listFound.get(i).toString()); // print every line found
            }
        }
        ui.showLine();
        if (tasks.isEmpty("archives")) {
            ui.showMessage("The archives are empty. There are no targets at the moment.");
        } else if (!tasks.isEmpty("archives") && archivesFound.isEmpty()) {
            ui.showMessage("Nothing was found in the archives...");
        } else {
            ui.showMessage(archivesFound.size() + " found in the archives:");
            for (int i = 0; i < archivesFound.size(); i++) { // accounted for index
                ui.showMessage((i + 1) + "." + archivesFound.get(i).toString()); // print every line found
            }
        }
    }
}