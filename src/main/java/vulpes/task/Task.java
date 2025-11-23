package vulpes.task;

/**
 * Abstract base class used to indicate something that needs doing
 */
public abstract class Task { // made abstract to prevent instantiation
    protected String description; // task body
    protected boolean isDone; // mark/unmark flag
    // priority scrapped, replaced with Task type

    /**
     * Constructor with no params, abstract class will not be instantiated
     */
    public Task() {}

    /**
     * Placeholder method to override Java default class for formatted printing
     */
    @Override
    public String toString() { // suggested by AI to streamline printing and respect OOP
        return "";
    }

    /**
     * Placeholder method for writing to file
     */
    public String toFileString() {
        return "";
    }

    /**
     * Method to set protected variable
     */
    public void setStatus(boolean status) {
        this.isDone = status; // set from param
    }

    /**
     * Method to return protected variable
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    /**
     * Method to set protected variable (if ever needed)
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Method to return protected variable
     */
    public String getDescription() {
        return this.description;
    }
    // earlier methods to get and set priority removed, not needed; never changed once first created
}