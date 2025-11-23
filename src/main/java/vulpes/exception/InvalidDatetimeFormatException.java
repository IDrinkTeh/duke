package vulpes.exception;

/**
 * Class that handles when user inputs with missing keywords
 */
public class InvalidDatetimeFormatException extends VulpesException {
    public InvalidDatetimeFormatException(String message) {
        super(
                "I'm sorry. Maybe my invitation got lost in the mail... (" + message + ")\n" +
                "(Please enter the correct keywords - '/by' for deadlines, '/from' and 'to' for events.)\n" +
                "(or type 'help!' for... help.)"
        );
    }
}