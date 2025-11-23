package vulpes.command;

import java.time.LocalDate;
import java.util.ArrayList;
import vulpes.storage.Storage;
import vulpes.task.Deadline;
import vulpes.task.Event;
import vulpes.task.Task;
import vulpes.tasklist.TaskList;
import vulpes.ui.Ui;

/**
 * Extension of abstract base class used to show tasks that fall within a certain date
 */
public class ViewCommand extends Command { // similar to find
    private final LocalDate criteriaDate; // date criteria to look for
    private final ArrayList<Task> listFound = new ArrayList<>(); // subarray of list tasks found
    private final ArrayList<Task> archivesFound = new ArrayList<>(); // subarray of archives tasks found
    boolean isValid = false; // flag for whether target found is valid according to criteria

    /**
     * Constructor that only takes in search criteriaString
     *
     * @param params The parameters that will be used as search criteriaString
     *
     */
    public ViewCommand(LocalDate params) {
        this.criteriaDate = params; // take in date in localdatetime format
    }

    /**
     * Overrides execution in the abstract base class
     * Produces flavour for user
     * Flavour changes depending on number of tasks in the list
     * Iterates through list and prints every task that corresponds to criteria
     *
     * @param tasks   Instance of the Tasklist class
     * @param ui      Instance of the UI class
     * @param storage Instance of the Storage class
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        for (Task task : tasks.getAllTasks("")) { // for all tasks in the list
            matcher("", task); // run it through internal helper
        }
        for (Task task : tasks.getAllTasks("archives")) { // for all tasks in the archives
            matcher("archives", task); // run it through internal helper
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

    /**
     * Helper method that does the actual matching
     * more complex logic than find command
     *
     * @param subarray The subarray to which the task might be added
     * @param task The task to be evaluated against criteria
     */
    public void matcher (String subarray,Task task){
        if (task.getClass().equals(Event.class)) { // if task is an event
            LocalDate fromDate = ((Event) task).getFrom().toLocalDate();
            LocalDate toDate = ((Event) task).getTo().toLocalDate();
            if (fromDate.equals(criteriaDate) || toDate.equals(criteriaDate)) { // if task is an event AND from and/or to falls within criteria
                isValid = true; // flag for listing
            }
        } else if (task.getClass().equals(Deadline.class)) { // if task is a deadline
            LocalDate eventDate = ((Deadline) task).getBy().toLocalDate();
            if (eventDate.equals(criteriaDate)) { // if task is a deadline AND by falls within criteria
                isValid = true; // flag for listing
            }
        } // if task is a to-do or doesn't have a date that falls within criteria, flag remains false
        if (isValid) {
            if (subarray.equals("archives")) {
                archivesFound.add(task);
            } else {
                listFound.add(task);
            }
        }
    }
}