package vulpes.exception;

/**
 * Class that handles SHTF errors
 */
public class CriticalException extends VulpesException {
    public CriticalException() {
        super("In the end, we all die. Unless you change. (Pack it up boys... this app is cooked)");
    }
}