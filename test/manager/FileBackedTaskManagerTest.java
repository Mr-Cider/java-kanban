package manager;

import alltasks.Epic;
import alltasks.Subtask;
import alltasks.Task;
import exception.ManagerSaveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class FileBackedTaskManagerTest {
    InMemoryHistoryManager history;
    InMemoryTaskManager manager;
    FileBackedTaskManager backManager;

    @BeforeEach
    void beforeEach() throws IOException {
        history = new InMemoryHistoryManager();
        manager = new InMemoryTaskManager(history);
        backManager = new FileBackedTaskManager(history);
    }

    @Test
    void shouldSave() throws IOException {


        Epic epic = new Epic("Первый эпик", "Описание первого эпика", 3);
        Subtask subtask = new Subtask("Первый сабтаск", "Описание первого эпика", 4, "IN_PROGRESS", 3);
        Task task = new Task("1 задача", "Описание 1 задачи", 1, "NEW");
        Task task2 = new Task("2 задача", "Описание 2 задачи", 2, "NEW");
        Subtask subtask2 = new Subtask("Второй сабтаск", "Описание второго сабтаска", 5, "IN_PROGRESS", 3);
        backManager.addNewTask(task);
        backManager.addNewTask(task2);
        backManager.addNewEpic(epic);
        backManager.addNewSubtask(subtask);
        backManager.addNewSubtask(subtask2);
        backManager.save();
    }
        @Test
        void shouldCreateFile() throws ManagerSaveException {
            try {
                FileBackedTaskManager.createFile("restore.csv");
            } catch (IOException e) {
                throw new ManagerSaveException();
            }

        }
        }

