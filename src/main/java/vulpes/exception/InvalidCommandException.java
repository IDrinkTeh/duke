package vulpes.exception;

/**
 * Class that handles when user inputs with missing keywords
 */
public class InvalidCommandException extends VulpesException {
    public InvalidCommandException() {
        super(
                "I don't know what you're talking about, but it sounds illegal. (Command entered is not recognised).\n" +
                        "(Please enter another command or type 'help!' for... help.)"
        );
    }
}

// specific exception for invalid command