package vulpes.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Extension of abstract base class used specify task with a from and to date/time
 */
public class Event extends Task { // Events: tasks that from at a specific date/time and ends at a specific date/time e.g., (a) team project meeting 2/10/2019 2-4pm (b) orientation week 4/10/2019 to 11/10/2019
    /**
     * Constructor with description, from/to dates
     *
     * @param description The description of the event to be attended
     * @param start The from date of the event to be attended
     * @param end The to date of the event to be attended
     */
    public Event(String description, LocalDateTime start, LocalDateTime end) {
        super.description = description;
        super.isDone = false; // not completed by default
        this.from = start;
        this.to = end;
    }

    // erased superfluous constructor

    // https://www.baeldung.com/java-datetimeformatter
    String dateTimeFormat = "dd.MM.yyyy hh:mm a"; // chosen format
    DateTimeFormatter NEW_FORMATTER = DateTimeFormatter.ofPattern(dateTimeFormat); // formatter based on format

    /**
     * Method to override abstract base class placeholder method for writing to file
     */
    @Override
    public String toFileString() { // type|status|description|from|to
        return "E|" + (super.isDone ? "1" : "0") + "|" + getDescription() + "|" + NEW_FORMATTER.format(this.getFrom()) + "|" + NEW_FORMATTER.format(this.getTo());
    }

    /**
     * Method to override Java default class for formatted printing
     */
    @Override
    public String toString() {return "[D][" + super.getStatusIcon() + "] " + super.getDescription() + " (from: " + NEW_FORMATTER.format(this.getFrom()) + " to: " + NEW_FORMATTER.format(this.getTo()) + ")";} // print in different format

    protected LocalDateTime from; // stores from datetime
    protected LocalDateTime to; // stores to datetime

    /**
     * Method to set protected variable
     */
    public void setFrom(LocalDateTime datetime) { // might be used for future feature
        this.from = datetime;
    }

    /**
     * Method to return protected variable
     */
    public LocalDateTime getFrom() {
        return this.from;
    }

    /**
     * Method to set protected variable
     */
    public void setTo(LocalDateTime datetime) { // might be used for future feature
        this.to = datetime;
    }

    /**
     * Method to return protected variable
     */
    public LocalDateTime getTo() {
        return this.to;
    }
}