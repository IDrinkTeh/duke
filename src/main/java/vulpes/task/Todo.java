package vulpes.task;

/**
 * Extension of abstract base class used specify task with description only
 */
public class Todo extends Task { // ToDos: tasks without any date/time attached to it
    /**
     * Constructor with description, from date
     *
     * @param description The description of the To-do to be completed
     */
    public Todo(String description) {
        super.description = description;
        super.isDone = false; // marked or unmarked
    }

    // erased superfluous constructor

    /**
     * Method to override abstract base class placeholder method for writing to file
     */
    @Override
    public String toFileString() { // type|status|description
        return "T|" + (super.isDone ? "1" : "0") + "|" + getDescription();
    }

    /**
     * Method to override Java default class for formatted printing
     */
    @Override
    public String toString() { // suggested by AI to streamline printing and respect OOP
        return "[T][" + super.getStatusIcon() + "] " + super.getDescription();
    }
}