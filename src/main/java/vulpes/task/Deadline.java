package vulpes.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Extension of abstract base class used specify task with an to date/time
 */
public class Deadline extends Task { // Deadlines: tasks that need to be done before a specific date/time e.g., submit report by 11/10/2019 5pm
    /**
     * Constructor with description, from date
     *
     * @param description The description of the deadline to be completed
     * @param by The from date of the deadline to be completed
     */
    public Deadline(String description, LocalDateTime by) {
        super.description = description;
        super.isDone = false; // not completed by default
        this.by = by;
    }

    /**
     * Constructor with description, from date
     *
     * @param status The status of the deadline, whether it has been completed or not
     * @param description The description of the deadline to be completed
     * @param by The from date of the deadline to be completed
     */
    public Deadline(String status, String description, LocalDateTime by) {
        super.description = description;
        if (status.equals("1")) {
            super.isDone = true; // mark complete if true
        }
        this.by = by;
    }

    // https://www.baeldung.com/java-datetimeformatter
    String dateTimeFormat = "dd.MM.yyyy hh:mm a"; // chosen format
    DateTimeFormatter NEW_FORMATTER = DateTimeFormatter.ofPattern(dateTimeFormat); // formatter based on format

    /**
     * Method to override abstract base class placeholder method for writing to file
     */
    @Override
    public String toFileString() { // type|status|description|by
        return "D|" + (super.isDone ? "1" : "0") + "|" + getDescription() + "|" + NEW_FORMATTER.format(this.getBy());
    }

    /**
     * Method to override Java default class for formatted printing
     */
    @Override
    public String toString() {return "[D][" + super.getStatusIcon() + "] " + super.getDescription() + " (by: " + NEW_FORMATTER.format(this.getBy()) + ")";} // print in different format

    protected LocalDateTime by; // stores by datetime

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