package manager;

import allTasks.Task;

import java.util.LinkedList;
import java.util.List;

public interface IHistoryManager {

    void add(Task task);

    void remove(int id);

    List<Task> getHistory();

}
