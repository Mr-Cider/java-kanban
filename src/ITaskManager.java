import java.util.ArrayList;
import java.util.Set;

public interface ITaskManager {
    ArrayList<Task> getTasks();

    ArrayList<Subtask> getSubtasks();

    ArrayList<Epic> getEpics();

    ArrayList<Subtask> getEpicSubtasks(int epicId);

    Task getTask(int id);

    Subtask getSubtask(int id);

    Epic getEpic(int id);

    int addNewTask(Task task);

    int addNewEpic(Epic epic);

    Integer addNewSubtask(Subtask subtask);

//    static void updateEpicStatus(Epic epic) {
//        Set<String> = //get all status
//        if(...) {
//            epic.setStatus("//");
//            return;
//        }
//        if (...) {
//            epic.setStatus("//");
//            updateEpicStatus(epic);
//        }
//    }

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubtask(int id);

    void deleteTasks();

    void deleteSubtasks();

    void deleteEpics();



}
