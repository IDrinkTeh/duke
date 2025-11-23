package vulpes.exception;

/**
 * Class that handles when user inputs with missing keywords
 */
public class InvalidTaskException extends VulpesException {
    public InvalidTaskException(int taskIndex, int taskSize) {
        super(
                "I'm sorry. Maybe my invitation got lost in the mail... (task " + taskIndex + " doesn't exist!)\n" +
                        "(There are only " + taskSize + " targets in the list at the moment)\n" +
                        "(Please enter a valid index or type 'help!' for... help.)"
        );
    }
}

// specific exception for invalid task