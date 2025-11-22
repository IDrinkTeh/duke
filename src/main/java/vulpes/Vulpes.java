import vulpes.command.Command;
import vulpes.exception.VulpesException;
import vulpes.parser.Parser;
import vulpes.storage.Storage;
import vulpes.tasklist.TaskList;
import vulpes.ui.Ui;


/**
 * // <a href="https://letterboxd.com/film/fantastic-mr-fox/">...</a>
 * Credits to Google and Google Gemini, W3Schools, StackOverflow and to LeeJiaHao's Repo for some ideas (L0/1/2 only)
 */

public class Vulpes {
    //TODO: TextUiTesting if time allows

    /**
     * Allocating instances of classes specified in 'run' method
     */
    private Storage storage;
    private Ui ui;
    private TaskList tasks;

    /**
     * Instantiation of classes specified in 'run' method
     * @param filePath The file path at which the list will be saved/loaded from the user's local directory
     */
    public Vulpes(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load().getAllTasks());
        } catch (VulpesException e) {
            ui.showError("...");
            tasks = new TaskList(); // start with empty list
        }
    }

    /**
     * Specified method, untouched and original
     */
    public void run() { // assume I cannot change this
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
     * Self-explanatory
     */
    public static void main(String[] args) {
        new Vulpes("data/Vulpes.txt").run();
    }
}