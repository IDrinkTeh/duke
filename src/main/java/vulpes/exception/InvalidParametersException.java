package vulpes.exception;

/**
 * Class that handles when user inputs command without params
 */
public class InvalidParametersException extends VulpesException {
    public InvalidParametersException(String command) {
        super("I'm sorry. Maybe my invitation got lost in the mail... ('" + command + "' requires valid details!)\n" +
                "(Please enter the correct parameters or type 'help!' for... help.)"
        );
    }
}

// specific exception for command that has invalid params