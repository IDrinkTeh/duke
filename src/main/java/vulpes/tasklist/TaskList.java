package vulpes.tasklist;

import java.util.ArrayList;

import vulpes.exception.VulpesException;
import vulpes.storage.Storage;
import vulpes.task.Deadline;
import vulpes.task.Event;
import vulpes.task.Task;
import vulpes.task.Todo;
import vulpes.tasklist.TaskList;
import vulpes.ui.Ui;

public class TaskList {
    private ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>(); // without save file
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks; // T, D, E
    }

    public void add(Task task) { // adder
        this.tasks.add(task);
    }

    public Task remove(int taskIndex) { // deleter
        return this.tasks.remove(taskIndex);
    }

    public Task get(int taskIndex) { // finder
        return this.tasks.get(taskIndex);
    }

    public int size() {
        return this.tasks.size();
    }

    public boolean isEmpty() {
        return this.tasks.isEmpty();
    }

    public ArrayList<Task> getAllTasks() { // return self
        return this.tasks;
    }
}