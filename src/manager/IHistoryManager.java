package manager;

import alltasks.Task;

import java.util.List;

public interface IHistoryManager {

    void add(Task task);

    void remove(int id);

    List<Task> getHistory();

}
