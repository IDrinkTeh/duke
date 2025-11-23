package vulpes.exception;

/**
 * Class that handles when user inputs invalid date only
 */
public class InvalidDateException extends VulpesException {
    public InvalidDateException() {
        super(
                """
                        I'm sorry. Maybe my invitation got lost in the mail... (invalid date entered!)
                        (Dates should be in 'dd-mmy-yyy' format)
                        (Please enter the correct date or type 'help!' for... help.)"""
        );
    }
}

// specific exception for invalid date only