package vulpes.task;

public class Event extends Task { // Events: tasks that start at a specific date/time and ends at a specific date/time e.g., (a) team project meeting 2/10/2019 2-4pm (b) orientation week 4/10/2019 to 11/10/2019
    public Event(String description, String start, String end) {
        super.description = description;
        super.isDone = false;
        super.priority = "E";
        this.start = start;
        this.end = end;
    }

    public Event(String status, String description, String start, String end) {
        super.description = description;
        super.isDone = status.equals("1");
        super.priority = "E";
        this.start = start;
        this.end = end;
    }

    @Override
    public String toFileString() { // type|status|description|start|end
        return "E|" + (super.isDone ? "1" : "0") + "|" + getDescription() + "|" + this.start + "|" + this.end;
    }

    @Override
    public String toString() {return "[D][" + super.getStatusIcon() + "] " + super.getDescription() + " (from: " + this.getStart() + " to: " + this.getEnd() + ")";} // suggested by AI to streamline printing and respect OOP

    protected String start;
    protected String end;

    public void setStart(String datetime) {
        this.start = datetime;
    }

    public String getStart() {
        return this.start;
    }

    public void setEnd(String datetime) {
        this.end = datetime;
    }

    public String getEnd() {
        return this.end;
    }
}