package vulpes.exception;

//Todo: group all exceptions into here and expand this extension
/**
 * Handles unique exceptions
 * Extended from standard Java library
 */
public class VulpesException extends Exception { // extending Java exceptions
    public VulpesException(String message) {
        super(message);
    }
}