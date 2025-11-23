package vulpes.tasklist;
import vulpes.task.Task;

import java.util.ArrayList;

/**
 * Class that creates the list as well as handles amendments to the list
 */
public class TaskList {
    private ArrayList<Task> listTasks; // allocate list of tasks
    private ArrayList<Task> archivedTasks; // allocate archives of tasks

    /**
     * Constructor to make new list without params or contents
     */
    public TaskList() {
        this.listTasks = new ArrayList<>(); // without save file
        this.archivedTasks = new ArrayList<>(); // without save file
    }

    /**
     * Constructor that takes in tasklists of list and archives
     *
     * @param tasks Array of list and archives
     */
    public TaskList(ArrayList<ArrayList<Task>> tasks) {
        this.listTasks = tasks.get(0); // load from list file
        this.archivedTasks = tasks.get(1); // load from archives file
    }

    /**
     * Constructor that takes in tasklists of list and archives
     *
     * @param listTasks The list of tasks
     * @param archivedTasks The archives of tasks
     */
    public TaskList(ArrayList<Task> listTasks, ArrayList<Task> archivedTasks) {
        this.listTasks = listTasks; // load from list file
        this.archivedTasks = archivedTasks; // load from archives file
    }

    /**
     * Method that adds tasks to the list
     *
     * @param list A list to add a task to
     * @param task A task to add to a list
     */
    public void add(String list, Task task) { // adder
        if (list.equals("archives")) { // check for which to add to
            this.archivedTasks.add(task);
        } else {
            this.listTasks.add(task);
        }
    }

    /**
     * Method that remove tasks from the list
     *
     * @param list A list to remove a task from
     * @param taskIndex A task to remove from the list
     */
    public Task remove(String list, int taskIndex) { // deleter
        if (list.equals("archives")) { // check for which to remove from
            return this.archivedTasks.remove(taskIndex);
        }
        return this.listTasks.remove(taskIndex);
    }

    /**
     * Method that returns a task from the list
     *
     * @param list A list to return a task from
     * @param taskIndex A task to return from the list
     */
    public Task get(String list, int taskIndex) { // finder
        if (list.equals("archives")) { // check for which to fetch from
            return this.archivedTasks.get(taskIndex);
        }
        return this.listTasks.get(taskIndex);
    }

    /**
     * Method that returns size of the list
     *
     * @param list A list to size up
     */
    public int size(String list) {
        if (list.equals("archives")) { // check for which to get size from
            return this.archivedTasks.size();
        }
        return this.listTasks.size();
    }

    /**
     * Method that checks whether there are tasks in the list
     *
     * @param list A list to check whether there are tasks in
     */
    public boolean isEmpty(String list) { // check for which to check for emptiness
        if (list.equals("archives")) {
            return this.archivedTasks.isEmpty();
        }
        return this.listTasks.isEmpty();
    }

    /**
     * Method that returns pair of list and archives
     */
    public ArrayList<Task> getAllTasks(String list) { // return self
        if (list.equals("archives")) { // check for which to return full tasklist from
            return this.archivedTasks;
        }
        return this.listTasks;
    }
}