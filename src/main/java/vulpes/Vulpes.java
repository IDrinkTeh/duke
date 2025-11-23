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

/* List of all sources used as below (Im sorry I didnt really keep track of where I used some of them but I could at least collate them from my search history):

https://www.geeksforgeeks.org/java/java-time-localdate-class-in-java/
https://www.w3schools.com/java/java_ref_arraylist.asp
https://www.w3schools.com/java/java_enums.asp
https://www.geeksforgeeks.org/java/java-time-localdatetime-class-in-java/
https://www.w3schools.com/git/git_branch.asp
https://www.w3schools.com/java/ref_string_trim.asp
https://www.w3schools.com/java/java_polymorphism.asp
https://www.w3schools.com/java/java_ref_string.asp
https://www.w3schools.com/java/ref_scanner_hasnext.asp
https://www.w3schools.com/java/java_switch.asp
https://www.w3schools.com/java/java_ref_scanner.asp
https://www.w3schools.com/java/java_encapsulation.asp
https://www.w3schools.com/java/java_interface.asp
https://www.w3schools.com/java/java_inheritance.asp
https://www.w3schools.com/java/java_hashmap.asp
https://stackoverflow.com/questions/19533625/how-to-pass-a-function-an-array-literal
https://stackoverflow.com/questions/10963878/how-do-you-fork-your-own-repository-on-github
https://stackoverflow.com/questions/5003285/how-can-interfaces-replace-the-need-for-multiple-inheritance-when-have-existing
https://stackoverflow.com/questions/12162688/inheriting-from-multiple-classes-in-java-and-possibly-not-using-interface
https://stackoverflow.com/questions/767821/is-else-if-faster-than-switch-case
https://stackoverflow.com/questions/9128737/fastest-way-to-set-all-values-of-an-array
https://stackoverflow.com/questions/1062960/map-implementation-with-duplicate-keys
https://stackoverflow.com/questions/1062960/map-implementation-with-duplicate-keys
https://stackoverflow.com/questions/2754339/java-how-do-i-create-an-array-of-tuples
https://stackoverflow.com/questions/21676150/setup-git-in-intellij-terminal
https://stackoverflow.com/questions/24368788/what-is-the-alternative-for-while0-or-while1-in-java
https://stackoverflow.com/questions/3528420/why-do-we-need-interfaces-in-java
https://stackoverflow.com/questions/3528420/why-do-we-need-interfaces-in-java
https://www.baeldung.com/find-list-element-java
https://www.geeksforgeeks.org/java/localdatetime-parse-method-in-java-with-examples/
https://www.w3schools.com/java/java_try_catch.asp
https://www.geeksforgeeks.org/java/java-time-localtime-class-in-java/

 */