package vulpes.command;

import vulpes.storage.Storage;
import vulpes.tasklist.TaskList;
import vulpes.ui.Ui;

/**
 * Extension of abstract base class used to show the list and the tasks therein
 */
public class HelpCommand extends Command { // to list
    /**
     * Overrides execution in the abstract base class
     * Gives user all available comments and other tips to use the app
     */
    @Override
    public void execute(TaskList listTasks, Ui ui, Storage list) {
        ui.showMessage("I want to help you steal some cider. We're going to a book party, and keep your mouth shut about any cider, because no one ever said that"); // flavour
        ui.showLine();
        ui.showMessage(
                """
                        This is a disambiguation page! This app was made to help you keep track of and complete tasks.
                        
                        List of available commands:
                        
                        'todo [description]'
                            allows you to add a task that has a [description]
                        
                        'deadline [description] /by [datetime]'
                            allows you to add a task that has a [description] and a due [datetime]
                            you can choose to enter a time only ; the app will take the due date as today
                            alternatively, you can enter the time then the date, separated with a space
                        'event [description] /from [datetime] /to [datetime]'
                            allows you to add a task that has a [description] and both start and end [datetime]
                            you can choose to enter a time only ; the app will take the due date as today
                            alternatively, you can enter the time then the date, separated with a space
                        'mark
                        'unmark
                        'delete
                        'list
                        'archives
                        'archive
                        'find
                        'help
                        
                        the description can be anything you choose
                        
                        """
        ); // flavour
        if (listTasks.isEmpty("")) { // check for empty list
            ui.showMessage("The list is empty. There are no targets at the moment.");
        } else {
            for (int i = 0; i < listTasks.size(""); i++) { // accounted for index
                ui.showMessage((i + 1) + "." + listTasks.get("", i).toString()); // print every line
            }
        }
    }
}