package vulpes.tasklist;

import java.util.ArrayList;

import vulpes.task.Task;

/**
 * Class that creates the list as well as handles amendments to the list
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /**
     * Constructor to make new list with params
     */
    public TaskList() {
        this.tasks = new ArrayList<>(); // without save file
    }

    /**
     * Constructor that stores list
     * @param tasks A newly created list of Tasks
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Method that adds tasks to the list
     * @param task A task to add to the list
     */
    public void add(Task task) { // adder
        this.tasks.add(task);
    }

    /**
     * Method that remove tasks from the list
     * @param taskIndex A task to remove from the list
     */
    public Task remove(int taskIndex) { // deleter
        return this.tasks.remove(taskIndex);
    }

    /**
     * Method that returns a task from the list
     * @param taskIndex A task to return from the list
     */
    public Task get(int taskIndex) { // finder
        return this.tasks.get(taskIndex);
    }

    /**
     * Method that returns size of the list
     */
    public int size() {
        return this.tasks.size();
    }

    /**
     * Method that checks whether there are tasks in the list
     */
    public boolean isEmpty() {
        return this.tasks.isEmpty();
    }

    /**
     * Method that returns full list
     */
    public ArrayList<Task> getAllTasks() { // return self
        return this.tasks;
    }
}