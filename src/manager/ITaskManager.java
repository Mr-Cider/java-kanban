package manager;

import alltasks.Epic;
import alltasks.Subtask;
import alltasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public interface ITaskManager {

    List<Task> getHistory();

    ArrayList<Task> getTasks();

    ArrayList<Subtask> getSubtasks();

    ArrayList<Epic> getEpics();

    ArrayList<Subtask> getEpicSubtasks(int epicId);


    Task getTask(int id);

    Subtask getSubtask(int id);

    Epic getEpic(int id);

    int addNewTask(Task task);

    int addNewEpic(Epic epic);

    int addNewSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubtask(int id);

    void deleteTasks();

    void deleteSubtasks();

    void deleteEpics();

    List<Task> getAllTasks();

    Set<Task> getPrioritizedTasks();
}
