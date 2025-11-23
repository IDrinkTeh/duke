package vulpes.exception;

// below sources influenced the writing of all exceptions
// https://help.ivanti.com/ps/help/en_US/VTM/22.x/jdev/ps-vtm-javadev/debugging_extensions.htm#extension_1256797978_600494
// https://stackoverflow.com/questions/79434230/java-custom-exception-handling-which-class-to-extend
// https://www.baeldung.com/java-new-custom-exception

/**
 * Class handles exceptions unique to Vulpes
 * Extended from standard Java library
 */
public class VulpesException extends Exception { // extending Java exceptions
    public VulpesException(String message) {
        super(message);
    }
}