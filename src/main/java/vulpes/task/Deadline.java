package vulpes.task;

public class Deadline extends Task { // Deadlines: tasks that need to be done before a specific date/time e.g., submit report by 11/10/2019 5pm
    public Deadline(String description, String end) {
        super.description = description;
        super.isDone = false;
        super.priority = "D";
        this.end = end;
    }

    public Deadline(String status, String description, String end) {
        super.description = description;
        super.isDone = status.equals("1");
        super.priority = "D";
        this.end = end;
    }

    @Override
    public String toFileString() { // type|status|description|by
        return "D|" + (super.isDone ? "1" : "0") + "|" + getDescription() + "|" + this.end;
    }

    @Override
    public String toString() {return "[D][" + super.getStatusIcon() + "] " + super.getDescription() + " (by: " + this.getEnd() + ")";} // suggested by AI to streamline printing and respect OOP

    protected String end;

    public void setEnd (String datetime){
        this.end = datetime;
    }

    public String getEnd () {
        return this.end;
    }
}