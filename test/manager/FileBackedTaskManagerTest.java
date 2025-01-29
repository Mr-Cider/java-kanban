package manager;

import alltasks.Epic;
import alltasks.Subtask;
import alltasks.Task;
import exception.ManagerSaveException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    FileBackedTaskManager taskManager;
    File file;


    @Override
    FileBackedTaskManager createTaskManager() throws ManagerSaveException {
        try {
            file = File.createTempFile("restoreTest", ".csv");
            taskManager = new FileBackedTaskManager(new InMemoryHistoryManager(), file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return taskManager;
    }

    @AfterEach
    void afterEach() {
        file.deleteOnExit();
    }


    @Test
    void shouldSaveOnFile() throws IOException {
        List<String> arrayList = new ArrayList<>();
        Task task = new Task("1 задача", "Описание 1 задачи", 1, "NEW");
        Epic epic = new Epic("Первый эпик", "Описание первого эпика", 2);
        Subtask subtask = new Subtask("Первый сабтаск", "Описание первого сабтаска", 3, "NEW", 2);
        taskManager.addNewTask(task);
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(taskManager.getFile()));
        bufferedReader.readLine();
        while (bufferedReader.ready()) {
            arrayList.add(bufferedReader.readLine());
        }
        assertEquals(3, arrayList.size(), "Количество не совпадает");
    }

    @Test
    void shouldLoadFromFile() throws IOException {
        List<String> testTasks = new ArrayList<>();
        testTasks.add("id,type,name,status,description,epic");
        testTasks.add("1,TASK,1 задача,NEW,Описание 1 задачи,");
        testTasks.add("2,EPIC,Первый эпик,NEW,Описание первого эпика,");
        testTasks.add("3,SUBTASK,Первый сабтаск,NEW,Описание первого сабтаска,2");
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            for (String task : testTasks) {
                bufferedWriter.write(task);
                bufferedWriter.newLine();
            }

        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
        taskManager = FileBackedTaskManager.loadFromFile(file);
        List<Task> checkTasks = taskManager.getAllTasks();
        assertEquals(3, checkTasks.size(), "Количество задач не совпадает");
        assertEquals(3, taskManager.newId, "ID не совпадает");
    }

    @Test
    void shouldLoadFromIsEmpty() throws IOException {
        taskManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(0, taskManager.getAllTasks().size(), "Не должно быть задач.");
    }

    @Test
    void shouldThrowExceptionWhenLoadingNullFile() {
        assertThrows(NullPointerException.class, () -> {
            FileBackedTaskManager.loadFromFile(null);
        });
    }

    @Test
    void shouldThrowManagerSaveExceptionWhenFileNotWritable() throws IOException {
        File tempFile = File.createTempFile("readonly", ".csv");
        tempFile.setReadOnly();
        FileBackedTaskManager taskManager2 = new FileBackedTaskManager(
                Managers.getDefaultHistory(),
                tempFile
        );
        assertThrows(ManagerSaveException.class, () -> {
            Task task = new Task("Задача", "Описание", 1, "NEW");
            taskManager2.addNewTask(task);
        });
        tempFile.setWritable(true);
        tempFile.deleteOnExit();
    }
}

