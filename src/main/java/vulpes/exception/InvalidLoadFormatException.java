package vulpes.exception;

import java.time.format.DateTimeParseException;

public class InvalidLoadFormatException extends VulpesException {
    public InvalidLoadFormatException(String message) {
        super("Where did you come from again? (" + message + ")");
    }

    /**
     * Constructor to wrap original exception cause
     *
     * @param message Feedback for user
     * @param e       The original exception caught
     */
    public InvalidLoadFormatException(String message, DateTimeParseException e) {
        super("Where did you come from again? (" + message + ")\n" +
                "(error was: " + e + ")");
    }
}