package vulpes.task;

/**
 * Extension of abstract base class used specify task with description only
 */
public class Todo extends Task { // ToDos: tasks without any date/time attached to it
    /**
     * Constructor with description, start date
     * @param description The description of the To-do to be completed
     */
    public Todo(String description) {
        super.description = description;
        super.isDone = false;
    }

    /**
     * Constructor with description, start date
     * @param status The status of the To-do, whether it has been completed or not
     * @param description The description of the To-do to be completed
     */
    public Todo(String status, String description) {
        super.description = description;
        super.isDone = status.equals("1");
    }

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
    public String toString() {return "[T][" + super.getStatusIcon() + "] " + super.getDescription();} // suggested by AI to streamline printing and respect OOP
}