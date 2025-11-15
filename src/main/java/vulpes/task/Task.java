package vulpes.task;

public abstract class Task { // made abstract to prevent instantiation
    protected String description; // task body
    protected String priority; // task type
    protected boolean isDone; // mark/unmark flag

    //TODO: enums for priority if time allows?

    public Task(String description, String priority) {
        this.description = description;
        this.isDone = false;
        this.priority = priority;
    }

    public Task() {
        this.description = "";
        this.isDone = false;
        this.priority = "";
    }

    @Override
    public String toString() {
        return "";
    } // suggested by AI to streamline printing and respect OOP

    public String toFileString() {
        return "";
    }

    public void setStatus(boolean status) {
        this.isDone = status;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getPriority() {
        return this.priority;
    }
}