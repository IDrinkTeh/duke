import vulpes.command.Command;
import vulpes.exception.VulpesException;
import vulpes.parser.Parser;
import vulpes.storage.Storage;
import vulpes.tasklist.TaskList;
import vulpes.ui.Ui;

public class Vulpes { // https://letterboxd.com/film/fantastic-mr-fox/
    //TODO: TextUiTesting if time allows

    private Storage storage;
    private Ui ui;
    private TaskList tasks;

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
                ui.showLine();;
            }
        }
    }

    public static void main(String[] args) {
        new Vulpes("data/Vulpes.txt").run();
    }
}

// google
// w3schools
// looked at jiahaos repo but only for L0/1/2 for ideas of how to encapsulate better