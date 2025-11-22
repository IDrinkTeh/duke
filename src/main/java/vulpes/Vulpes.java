package vulpes;

import vulpes.command.Command;
import vulpes.exception.VulpesException;
import vulpes.parser.Parser;
import vulpes.storage.Storage;
import vulpes.tasklist.TaskList;
import vulpes.ui.Ui;

import java.nio.file.Path;


/**
 * // <a href="https://letterboxd.com/film/fantastic-mr-fox/">...</a>
 * Credits to Google and Google Gemini, W3Schools, StackOverflow and to LeeJiaHao's Repo for some ideas (L0/1/2 only)
 * A028761[8]M for the individual features : B-ViewSchedules / C-Archive / B-FixedDurationTasks
 */

public class Vulpes {
    //TODO: TextUiTesting if time allows

    private Storage list, archived; // OOP principles, 1 for each
    private Ui ui;
    private TaskList listTasks, archivedTasks; // OOP principles, 1 for each

    /**
     * Instantiation of classes specified in 'run' method
     * @param listPath The file path at which the list will be saved/loaded from the user's local directory
     * @param archivedPath The file path at which the archives will be saved/loaded from the user's local directory
     */
    public Vulpes(String listPath, String archivedPath) {
        ui = new Ui();
        list = new Storage(listPath);
        archived = new Storage(archivedPath);
        try {
            listTasks = new TaskList(list.load(Path.of(listPath)).getAllTasks());
            archivedTasks = new TaskList(archived.load(Path.of(archivedPath)).getAllTasks());
        } catch (VulpesException e) {
            ui.showError("...");
            listTasks = new TaskList(); // start with empty list
            archivedTasks = new TaskList(); // start with empty list
        }
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
        new Vulpes("data/Vulpes.txt", "data/Archived.txt").run();
    }
}