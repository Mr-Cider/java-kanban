package manager;

import allTasks.Task;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements IHistoryManager {
    static final List<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (history.size() > 9) {
            history.removeFirst();
        }
        history.add(new Task(task));
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
