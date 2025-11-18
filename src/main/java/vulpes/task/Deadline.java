package vulpes.task;

import java.time.LocalDateTime;
import java.time.format.*;

public class Deadline extends Task { // Deadlines: tasks that need to be done before a specific date/time e.g., submit report by 11/10/2019 5pm
    public Deadline(String description, LocalDateTime by) {
        super.description = description;
        super.isDone = false;
        super.priority = "D";
        this.by = by;
    }

    public Deadline(String status, String description, LocalDateTime by) {
        super.description = description;
        super.isDone = status.equals("1");
        super.priority = "D";
        this.by = by;
    }

    // https://www.baeldung.com/java-datetimeformatter
    String dateTimeFormat = "dd.MM.yyyy hh:mm a";
    DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern(dateTimeFormat);

    @Override
    public String toFileString() { // type|status|description|by
        return "D|" + (super.isDone ? "1" : "0") + "|" + getDescription() + "|" + newFormatter.format(this.getBy());
    }

    @Override
    public String toString() {return "[D][" + super.getStatusIcon() + "] " + super.getDescription() + " (by: " + newFormatter.format(this.getBy()) + ")";} // print in different format

    protected LocalDateTime by;

    public void setBy(LocalDateTime datetime){
        this.by = datetime;
    }

    public LocalDateTime getBy() {
        return this.by;
    }
}