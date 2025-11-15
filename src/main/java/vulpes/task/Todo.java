package vulpes.task;

public class Todo extends Task { // ToDos: tasks without any date/time attached to it
    public Todo(String description) {
        super.description = description;
        super.isDone = false;
        super.priority = "T";
    }

    public Todo(String status, String description) {
        super.description = description;
        super.isDone = status.equals("1");
        super.priority = "T";
    }

    @Override
    public String toFileString() { // type|status|description
        return "T|" + (super.isDone ? "1" : "0") + "|" + getDescription();
    }

    @Override
    public String toString() {return "[T][" + super.getStatusIcon() + "] " + super.getDescription();} // suggested by AI to streamline printing and respect OOP
}