package vulpes.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task { // Events: tasks that start at a specific date/time and ends at a specific date/time e.g., (a) team project meeting 2/10/2019 2-4pm (b) orientation week 4/10/2019 to 11/10/2019
    public Event(String description, LocalDateTime start, LocalDateTime end) {
        super.description = description;
        super.isDone = false;
        super.priority = "E";
        this.start = start;
        this.end = end;
    }

    public Event(String status, String description, LocalDateTime start, LocalDateTime end) {
        super.description = description;
        super.isDone = status.equals("1");
        super.priority = "E";
        this.start = start;
        this.end = end;
    }


    // https://www.baeldung.com/java-datetimeformatter
    String dateTimeFormat = "dd.MM.yyyy hh:mm a";
    DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern(dateTimeFormat);

    @Override
    public String toFileString() { // type|status|description|start|end
        return "E|" + (super.isDone ? "1" : "0") + "|" + getDescription() + "|" + newFormatter.format(this.getStart()) + "|" + newFormatter.format(this.getEnd());
    }

    @Override
    public String toString() {return "[D][" + super.getStatusIcon() + "] " + super.getDescription() + " (from: " + newFormatter.format(this.getStart()) + " to: " + newFormatter.format(this.getEnd()) + ")";} // print in different format

    protected LocalDateTime start;
    protected LocalDateTime end;

    public void setStart(LocalDateTime datetime) {
        this.start = datetime;
    }

    public LocalDateTime getStart() {
        return this.start;
    }

    public void setEnd(LocalDateTime datetime) {
        this.end = datetime;
    }

    public LocalDateTime getEnd() {
        return this.end;
    }
}