package vulpes.exception;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Class that handles file load or write errors
 */
public class StorageException extends VulpesException {
    /**
     * Constructor to wrap standard IO exception
     *
     * @param path Path for feedback to the user
     * @param e The original exception caught
     */
    public StorageException(Path path, IOException e) {
        super("Where did you come from again? (error reading from storage file: " + path + ", error was: " + e + ")"); // important to be able to trace original error for heavy class such as storage
    }
}