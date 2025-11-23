package vulpes.ui;

import java.util.Scanner;

/**
 * Class used to contain methods for taking input and giving output to user
 */
public class Ui {
    private final Scanner scanner; // private

    /**
     * Constructor that creates scanner for taking in input
     */
    public Ui() { // make scanner
        this.scanner = new Scanner(System.in);
    }

    /**
     * Method to greet the user upon app launch
     */
    public void showWelcome() {
        // flavour galore
        showLine();
        System.out.println("Canis Lupus? Vulpes vulpes!");
        System.out.println("All right, let's from planning. Who knows shorthand?");
        showLine();
    }

    /**
     * Method to print a dividing line
     */
    public void showLine() {
        System.out.println("__________________________________________________________________________________________________________________________________________");
    }

    /**
     * Method to take in next line from user input
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Method to append default error indication to error message presented to user
     *
     * @param message The actual exception/error to display to user
     */
    public void showError(String message) {
        System.out.println("Uh-oh, we got it wrong. " + message);
    }

    /**
     * Method to print to user
     *
     * @param message The message to output to user
     */
    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showHelp() {
        showMessage(
            """
                I want to help you steal some cider. We're going to a book party, and keep your mouth shut about any cider, because no one ever said that.
                
                This is a disambiguation page! This app was made to help you keep track of and complete tasks.
                
                
                
                List of available commands:
                
                'todo [description]'
                    eg. 'todo coding project'
                    allows you to ADD a task that has a [description]
                    the description can be anything you choose
                
                'deadline [description] /by [datetime]'
                    allows you to ADD a task that has a [description] and a due [datetime]
                    /by [time] : you can enter time only, the app will take the due date as today
                        eg. 'deadline coding project /by 23:59'
                    /by [date time] : you can enter time then date, separated with a space
                        eg. 'deadline coding project /by 23:59 23.11.2025'
                
                'event [description] /from [datetime] /to [datetime]'
                    allows you to ADD a task that has a [description] and both start and end [datetime]
                    /from [time] /to [time] : you can choose to enter times only, the app will take the start and end dates as today
                        eg. 'event party /from 00:00 /to 23:59'
                    /from [time] /to [time date] : you can enter the 'from' time and then the 'by' time and date, the app will take start date as today
                        eg. 'event party /from 00:00 /to 23:59 23.11.2025'
                    /from [time date] /to [time date] : you can enter 2 times and 2 dates, each separated with a space
                        eg. 'event party /from 00:00 23.11.2025 /to 23:59 31.12.2099'
                
                'mark [number]'
                    eg. 'mark 1'
                    allows you to MARK a task as done
                    [number] : the index number of task in the list to mark
                
                'unmark [number]'
                    eg. 'unmark 1'
                    allows you to UNMARK a task/mark as undone
                    [number] : the index number of task in the list to unmark
                
                'delete [number]'
                    eg. 'delete 1'
                    allows you to DELETE a task from the list
                    [number] : the index number of task in the list to delete
                    *** deleting is final and cannot be undone! (for now) ***
                
                'list'
                    allows you to DISPLAY all active tasks in the app
                
                'archives'
                    allows you to DISPLAY all archived tasks in the app
                    *** archived tasks cannot be interacted with until unarchived ***
                
                'archive [number]'
                    eg. 'archive 1'
                    allows you to ARCHIVE a task/move task to archives
                    [number] : the index number of task in the list to archive
                    *** archived tasks cannot be interacted with until unarchived ***
                
                'unarchive [number]'
                    eg. 'unarchive 1'
                    allows you to UNARCHIVE a task/move task to list
                    [number] : the index number of task in the list to unarchive
                
                'find [description]'
                    eg. 'find party'
                    allows you to FIND all tasks in the app that fit criteria
                    [description] : the criteria for the search
                    *** the more specific you get, the lower the odds of finding what you seek ***
                
                'view [date]'
                    eg. 'view 23.11.2025'
                    allows you to VIEW all tasks in the app that fit DATE
                    [date] : the criteria for the search
                    *** do not input time or else it will fail ***
                
                'help'
                    HELP
                    this is how you got to this page :3"""
        );
        showLine();
    }
}