package vulpes.task;

import java.time.LocalDateTime;
import java.time.format.*;

/**
 * Extension of abstract base class used specify task with an end date/time
 */
public class Deadline extends Task { // Deadlines: tasks that need to be done before a specific date/time e.g., submit report by 11/10/2019 5pm
    /**
     * Constructor with description, start date
     * @param description The description of the deadline to be completed
     * @param by The start date of the deadline to be completed
     */
    public Deadline(String description, LocalDateTime by) {
        super.description = description;
        super.isDone = false;
        this.by = by;
    }

    /**
     * Constructor with description, start date
     * @param status The status of the deadline, whether it has been completed or not
     * @param description The description of the deadline to be completed
     * @param by The start date of the deadline to be completed
     */
    public Deadline(String status, String description, LocalDateTime by) {
        super.description = description;
        super.isDone = status.equals("1");
        this.by = by;
    }

    // https://www.baeldung.com/java-datetimeformatter
    String dateTimeFormat = "dd.MM.yyyy hh:mm a";
    DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern(dateTimeFormat);

    /**
     * Method to override abstract base class placeholder method for writing to file
     */
    @Override
    public String toFileString() { // type|status|description|by
        return "D|" + (super.isDone ? "1" : "0") + "|" + getDescription() + "|" + newFormatter.format(this.getBy());
    }

    /**
     * Method to override Java default class for formatted printing
     */
    @Override
    public String toString() {return "[D][" + super.getStatusIcon() + "] " + super.getDescription() + " (by: " + newFormatter.format(this.getBy()) + ")";} // print in different format

    protected LocalDateTime by;

    /**
     * Method to set protected variable
     */
    public void setBy(LocalDateTime datetime){
        this.by = datetime;
    }

    /**
     * Method to return protected variable
     */
    public LocalDateTime getBy() {
        return this.by;
    }
}