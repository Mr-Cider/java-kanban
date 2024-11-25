package manager;

import allTasks.Task;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements IHistoryManager {
    private final List<Task> history = new LinkedList<>();
    private static final int MAX_HISTORY_SIZE = 9;

    @Override
    public void add(Task task) {
        if (!(task == null)) {
            if (history.size() > MAX_HISTORY_SIZE) {
                history.removeFirst();
            }
            history.add(new Task(task));
        }
    }

    @Override
    public LinkedList<Task> getHistory() {
        return new LinkedList<>(history);
    }
}
