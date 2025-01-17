package manager;

import alltasks.Epic;
import alltasks.Subtask;
import alltasks.Task;
import exception.ManagerSaveException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

//    private final File file = new File(createFile("restore.csv"));

    public FileBackedTaskManager(IHistoryManager historyManager) throws IOException {
        super(historyManager);
    }


    public void save() {
         List<Task> allTasks = new ArrayList<>();
             allTasks.addAll(getTasks());
             allTasks.addAll(getEpics());
             allTasks.addAll(getSubtasks());

//        System.out.println("Сохраняем...");
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    @Override
    public ArrayList<Task> getTasks() {
        return super.getTasks();
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return super.getSubtasks();
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return super.getEpics();
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        return super.getEpicSubtasks(epicId);
    }

    @Override
    public Task getTask(int id) {
        return super.getTask(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        return super.getSubtask(id);
    }

    @Override
    public Epic getEpic(int id) {
        return super.getEpic(id);
    }

    @Override
    public int addNewTask(Task task) {
        int id = super.addNewTask(task);
//        save();
        return id;
    }

    @Override
    public int addNewEpic(Epic epic) {
        int id = super.addNewEpic(epic);
//        save();
        return id;
    }

    @Override
    public int addNewSubtask(Subtask subtask) {
        int id = super.addNewSubtask(subtask);
//        save();
        return id;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
//        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
//        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
//        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
//        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
//        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }
//    private static FileBackedTaskManager loadFromFile(File file) {
    private static void loadFromFile(File file) {
        System.out.println("Сохраняемся из файла");
    }

    public static String createFile(String fileName) throws ManagerSaveException {
        Path path = Path.of(fileName);
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            return fileName;
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }
}
