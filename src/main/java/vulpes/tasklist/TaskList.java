package vulpes.tasklist;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

import vulpes.task.Task;

/**
 * Class that creates the list as well as handles amendments to the list
 */
public class TaskList {
    private ArrayList<Task> listTasks;
    private ArrayList<Task> archivedTasks;

    /**
     * Constructor to make new list with params
     */
    public TaskList() {
        this.listTasks = new ArrayList<>(); // without save file
        this.archivedTasks = new ArrayList<>(); // without save file
    }

    /**
     * Constructor that stores list
     * @param listTasks List of tasks to complete
     * @param archivedTasks List of tasks that were shelved
     */
    public TaskList(ArrayList<Task> listTasks, ArrayList<Task> archivedTasks) {
        this.listTasks = listTasks;
        this.archivedTasks = archivedTasks;
    }

    /**
     * Method that adds tasks to the list
     * @param task A task to add to the list
     */
    public void add(Task task) { // adder
        this.listTasks.add(task);
    }

    /**
     * Method that remove tasks from the list
     * @param tasks A list to remove a task from
     * @param taskIndex A task to remove from the list
     */
    public Task remove(ArrayList<Task> tasks, int taskIndex) { // deleter
        return tasks.remove(taskIndex);
    }

    /**
     * Method that returns a task from the list
     * @param tasks A list to return a task from
     * @param taskIndex A task to return from the list
     */
    public Task get(ArrayList<Task> tasks, int taskIndex) { // finder
        return tasks.get(taskIndex);
    }

    /**
     * Method that returns size of the list
     * @param tasks A list to size up
     */
    public int size(ArrayList<Task> tasks) {
        return tasks.size();
    }

    /**
     * Method that checks whether there are tasks in the list
     * @param tasks A list to check whether there are tasks in
     */
    public boolean isEmpty(ArrayList<Task> tasks) {
        return tasks.isEmpty();
    }

    /**
     * Method that returns full list
     * @param path Selects whether list or archives are returned
     */
    public ArrayList<Task> getAllTasks(Path path) { // return self
        if (path.toString().equals("data/Vulpes.txt")) return listTasks;
        return archivedTasks;
    }
}