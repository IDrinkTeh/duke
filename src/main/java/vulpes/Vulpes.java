package vulpes;

import vulpes.command.Command;
import vulpes.exception.VulpesException;
import vulpes.parser.Parser;
import vulpes.storage.Storage;
import vulpes.tasklist.TaskList;
import vulpes.ui.Ui;

import java.nio.file.Path;

/**
 * // <a href="https://letterboxd.com/film/fantastic-mr-fox/">...</MissingParametersException>
 * Credits to Google and Google Gemini, W3Schools, StackOverflow and to LeeJiaHao's Repo for some ideas (L0/1/2 only)
 * A028761[8]M for the individual features : B-ViewSchedules / C-Archive / B-FixedDurationTasks
 * C-Archive and B-ViewSchedules selected
 * TextUiTesting omitted due to time constraints
 */

public class Vulpes {
    private Storage storage; // allocation for Storage instance
    private Ui ui; // allocation for Ui instance
    private TaskList tasks; // allocation for Tasklist instance
    /**
     * Definition of classes specified in 'run' method
     *
     * @param listPath The file path at which the list will be saved/loaded from the user's local directory
     * @param archivesPath The file path at which the archives will be saved/loaded from the user's local directory
     */
    public Vulpes(String listPath, String archivesPath) {
        ui = new Ui(); // instantiate Ui
        storage = new Storage(listPath, archivesPath); // instantiate Storage with paths of list and archives saves on user's local directory (if any)
        try {
            tasks = storage.load(Path.of(listPath), Path.of(archivesPath)); // attempt loading from the paths
        } catch (VulpesException e) {
            tasks = new TaskList(); // from with empty list if no saves found
            // no feedback required to user, app starts as normal
        }

        // none of below core components should be null if constructor is done
        assert ui != null : "UI object must be initialized.";
        assert storage != null : "Storage must be initialized.";
        assert tasks != null : "TaskList must be initialized.";
    }

    /**
     * Specified method, untouched and original
     */
    public void run() { // assume I cannot change this; I personally would rewrite this to better implement archives
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine(); // show the divider line ("_______")
                Command c = Parser.parse(fullCommand);

                // assume parse() will always be !null; null here is catastrophical
                assert c != null : "parse() returned null.";

                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (VulpesException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }

    /**
     * Main function now pulls from two paths - list and archived
     */
    public static void main(String[] args) {
        new Vulpes("data/Vulpes.txt", "data/Archives.txt").run();
    }
}