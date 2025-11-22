package vulpes.tasklist;

import java.util.ArrayList;

import vulpes.task.Task;

/**
 * Class that creates the list as well as handles amendments to the list
 */
public class TaskList {
    private ArrayList<Task> listTasks;
    private ArrayList<Task> archivedTasks;

    /**
     * Constructor to make new list without params or contents
     */
    public TaskList() {
        this.listTasks = new ArrayList<>(); // without save file
        this.archivedTasks = new ArrayList<>(); // without save file
    }

    /**
     * Constructor that holds list and archives
     * @param tasks Pair ot list and archives
     */
    public TaskList(ArrayList<ArrayList<Task>> tasks) {
        this.listTasks = tasks.get(0);
        this.archivedTasks = tasks.get(1);
    }

    public TaskList(ArrayList<Task> listTasks, ArrayList<Task> archivedTasks) {
        this.listTasks = listTasks;
        this.archivedTasks = archivedTasks;
    }

    /**
     * Method that adds tasks to the list
     * @param list A list to add a task to
     * @param task A task to add to a list
     */
    public void add(String list, Task task) { // adder
        if (list.equals("arc")) this.archivedTasks.add(task);
        else this.listTasks.add(task);
    }

    /**
     * Method that remove tasks from the list
     * @param list A list to remove a task from
     * @param taskIndex A task to remove from the list
     */
    public Task remove(String list, int taskIndex) { // deleter
        if (list.equals("arc")) return this.archivedTasks.remove(taskIndex);
        return this.listTasks.remove(taskIndex);
    }

    /**
     * Method that returns a task from the list
     * @param list A list to return a task from
     * @param taskIndex A task to return from the list
     */
    public Task get(String list, int taskIndex) { // finder
        if (list.equals("arc")) return this.archivedTasks.get(taskIndex);
        return this.listTasks.get(taskIndex);
    }

    /**
     * Method that returns size of the list
     * @param list A list to size up
     */
    public int size(String list) {
        if (list.equals("arc")) return this.archivedTasks.size();
        return this.listTasks.size();
    }

    /**
     * Method that checks whether there are tasks in the list
     * @param list A list to check whether there are tasks in
     */
    public boolean isEmpty(String list) {
        if (list.equals("arc")) return this.archivedTasks.isEmpty();
        return this.listTasks.isEmpty();
    }

    /**
     * Method that returns pair of list and archives
     */
    public ArrayList<Task> getAllTasks(String list) { // return self
        if (list.equals("arc")) return this.archivedTasks;
        return this.listTasks;
    }
}