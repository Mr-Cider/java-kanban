package manager;

import allTasks.Task;

import java.util.LinkedList;

public interface IHistoryManager {

    void add(Task task);

    LinkedList<Task> getHistory();

}
