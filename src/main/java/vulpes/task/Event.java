package vulpes.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Extension of abstract base class used specify task with a start and end date/time
 */
public class Event extends Task { // Events: tasks that start at a specific date/time and ends at a specific date/time e.g., (a) team project meeting 2/10/2019 2-4pm (b) orientation week 4/10/2019 to 11/10/2019
    /**
     * Constructor with description, start/end dates
     * @param description The description of the event to be attended
     * @param start The start date of the event to be attended
     * @param end The end date of the event to be attended
     */
    public Event(String description, LocalDateTime start, LocalDateTime end) {
        super.description = description;
        super.isDone = false;
        this.start = start;
        this.end = end;
    }

    /**
     * Constructor with status, description, start/end dates
     * @param status The status of the event, whether it has been attended or not
     * @param description The description of the event to be attended
     * @param start The start date of the event to be attended
     * @param end The end date of the event to be attended
     */
    public Event(String status, String description, LocalDateTime start, LocalDateTime end) {
        super.description = description;
        super.isDone = status.equals("1");
        this.start = start;
        this.end = end;
    }

    // https://www.baeldung.com/java-datetimeformatter
    String dateTimeFormat = "dd.MM.yyyy hh:mm a";
    DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern(dateTimeFormat);

    /**
     * Method to override abstract base class placeholder method for writing to file
     */
    @Override
    public String toFileString() { // type|status|description|start|end
        return "E|" + (super.isDone ? "1" : "0") + "|" + getDescription() + "|" + newFormatter.format(this.getStart()) + "|" + newFormatter.format(this.getEnd());
    }

    /**
     * Method to override Java default class for formatted printing
     */
    @Override
    public String toString() {return "[D][" + super.getStatusIcon() + "] " + super.getDescription() + " (from: " + newFormatter.format(this.getStart()) + " to: " + newFormatter.format(this.getEnd()) + ")";} // print in different format

    protected LocalDateTime start;
    protected LocalDateTime end;

    /**
     * Method to set protected variable
     */
    public void setStart(LocalDateTime datetime) {
        this.start = datetime;
    }

    /**
     * Method to return protected variable
     */
    public LocalDateTime getStart() {
        return this.start;
    }

    /**
     * Method to set protected variable
     */
    public void setEnd(LocalDateTime datetime) {
        this.end = datetime;
    }

    /**
     * Method to return protected variable
     */
    public LocalDateTime getEnd() {
        return this.end;
    }
}