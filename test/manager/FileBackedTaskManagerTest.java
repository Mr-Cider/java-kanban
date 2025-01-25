package manager;

import alltasks.Epic;
import alltasks.Subtask;
import alltasks.Task;
import exception.ManagerSaveException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static manager.TaskStatus.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static servicetest.TestMethods.*;

public class FileBackedTaskManagerTest {
    InMemoryHistoryManager history;
    FileBackedTaskManager taskManager;
    File file;
    
    @BeforeEach
    void beforeEach() throws IOException {
        file = File.createTempFile("restoreTest", ".csv");
        history = new InMemoryHistoryManager();
        taskManager = new FileBackedTaskManager(history, file);
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
    void shouldAddNewTaskAndGetTasks() {
        Task task = new Task("1 задача", "Описание 1 задачи", 1, "NEW");
        Task task2 = new Task("2 задача", "Описание 2 задачи", 2, "NEW");
        taskManager.addNewTask(task);
        taskManager.addNewTask(task2);
        ArrayList<Task> tasks = taskManager.getTasks();
        assertEquals(2, tasks.size(), "Задачи не добавлены");
    }

    @Test
    void shouldGetTask() {
        Task task = new Task("1 задача", "Описание 1 задачи", 1, "NEW");
        taskManager.addNewTask(task);
        assertEqualsTask(task, taskManager.getTask(1), "Задачи не совпадают");
    }

    @Test
    void shouldAddNewEpicGetEpics() {
        Epic epic = new Epic("Первый эпик", "Описание первого эпика", 1);
        Epic epic2 = new Epic("Второй эпик", "Описание второго эпика", 2);
        taskManager.addNewEpic(epic);
        taskManager.addNewEpic(epic2);
        ArrayList<Epic> epics = taskManager.getEpics();
        assertEquals(2, epics.size(), "Эпики не добавлены");
    }

    @Test
    void shouldGetEpic() {
        Epic epic = new Epic("Первый эпик", "Описание первого эпика", 1);
        taskManager.addNewEpic(epic);
        assertEqualsEpic(epic, taskManager.getEpic(1), "Эпики не совпадают");
    }

    @Test
    void shouldAddNewSubtaskAndGetEpicSubtasks() {
        Epic epic = new Epic("Первый эпик", "Описание первого эпика", 1);
        Subtask subtask = new Subtask("Первый сабтаск", "Описание первого эпика", 2, "IN_PROGRESS", 1);
        Subtask subtask2 = new Subtask("Второй сабтаск", "Описание второго сабтаска", 3, "IN_PROGRESS", 1);
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask);
        taskManager.addNewSubtask(subtask2);
        ArrayList<Subtask> subtasks = taskManager.getEpicSubtasks(1);
        assertEquals(2, subtasks.size(), "Сабтаски не добавлены");
    }

    @Test
    void shouldGetSubtask() {
        Epic epic = new Epic("Первый эпик", "Описание первого эпика", 1);
        Subtask subtask = new Subtask("Первый сабтаск", "Описание первого эпика", 2, "IN_PROGRESS", 1);
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask);
        assertEqualsSubtask(subtask, taskManager.getSubtask(2), "Сабтаски не совпадают");
    }

    @Test
    void shouldUpdateTask() {
        Task task = new Task("1 задача", "Описание 1 задачи", 1, "NEW");
        taskManager.addNewTask(task);
        Task task2 = new Task("Обновленная задача", "Описание обновленной задачи", 1, "IN_PROGRESS");
        taskManager.updateTask(task2);
        assertEqualsTask(task2, taskManager.getTask(1), "Таски не совпадают");
    }

    @Test
    void shouldUpdateEpic() {
        Epic epic = new Epic("Первый эпик", "Описание первого эпика", 1);
        taskManager.addNewEpic(epic);
        Epic epic2 = new Epic("Обновленный эпик", "Описание обновленного эпика", 1);
        taskManager.updateEpic(epic2);
        assertEqualsEpic(epic2, taskManager.getEpic(1), "Эпики не совпадают");
    }

    @Test
    void shouldUpdateSubtask() {
        Epic epic = new Epic("Первый эпик", "Описание первого эпика", 1);
        taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("Первый сабтаск", "Описание первого эпика", 2, "IN_PROGRESS", 1);
        taskManager.addNewSubtask(subtask);
        Subtask subtask2 = new Subtask("Обновленный сабтаск", "Описание обновленного сабтаска", 2, "IN_PROGRESS", 1);
        taskManager.updateSubtask(subtask2);
        assertEqualsSubtask(subtask2, taskManager.getSubtask(2), "Эпики не совпадают");
    }

    @Test
    void shouldDeleteTask() {
        Task task = new Task("1 задача", "Описание 1 задачи", 1, "NEW");
        Task task2 = new Task("2 задача", "Описание 2 задачи", 2, "IN_PROGRESS");
        taskManager.addNewTask(task);
        taskManager.addNewTask(task2);
        ArrayList<Task> tasks = taskManager.getTasks();
        assertEquals(2, tasks.size(), "Задачи не добавлены");
        taskManager.deleteTask(1);
        tasks = taskManager.getTasks();
        assertEquals(1, tasks.size(), "Задача не удалилась");
    }

    @Test
    void shouldDeleteEpic() {
        Epic epic = new Epic("Первый эпик", "Описание первого эпика", 1);
        Subtask subtask = new Subtask("Первый сабтаск", "Описание первого эпика", 2, "IN_PROGRESS", 1);
        Subtask subtask2 = new Subtask("Второй сабтаск", "Описание второго сабтаска", 3, "IN_PROGRESS", 1);
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask);
        taskManager.addNewSubtask(subtask2);
        taskManager.deleteEpic(1);
        ArrayList<Epic> epics = taskManager.getEpics();
        ArrayList<Subtask> subtasks = taskManager.getEpicSubtasks(1);
        assertEquals(0, subtasks.size(), "Сабтаски не удалены");
        assertEquals(0, epics.size(), "Эпик не удален");
    }

    @Test
    void shouldDeleteSubtask() {
        Epic epic = new Epic("Первый эпик", "Описание первого эпика", 1);
        Subtask subtask = new Subtask("Первый сабтаск", "Описание первого эпика", 2, "IN_PROGRESS", 1);
        Subtask subtask2 = new Subtask("Второй сабтаск", "Описание второго сабтаска", 3, "IN_PROGRESS", 1);
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask);
        taskManager.addNewSubtask(subtask2);
        taskManager.deleteSubtask(2);
        ArrayList<Subtask> subtasks = taskManager.getEpicSubtasks(1);
        assertEquals(1, subtasks.size(), "Сабтаск не удален");
    }

    @Test
    void shouldDeleteTasks() {
        Task task = new Task("1 задача", "Описание 1 задачи", 1, "NEW");
        Task task2 = new Task("2 задача", "Описание 2 задачи", 2, "IN_PROGRESS");
        taskManager.addNewTask(task);
        taskManager.addNewTask(task2);
        ArrayList<Task> tasks = taskManager.getTasks();
        assertEquals(2, tasks.size(), "Задачи не добавлены");
        taskManager.deleteTasks();
        tasks = taskManager.getTasks();
        assertEquals(0, tasks.size(), "Задачи не удалены");
    }

    @Test
    void shouldDeleteSubtasks() {
        Epic epic = new Epic("Первый эпик", "Описание первого эпика", 1);
        Subtask subtask = new Subtask("Первый сабтаск", "Описание первого эпика", 2, "IN_PROGRESS", 1);
        Subtask subtask2 = new Subtask("Второй сабтаск", "Описание второго сабтаска", 3, "IN_PROGRESS", 1);
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask);
        taskManager.addNewSubtask(subtask2);
        ArrayList<Subtask> subtasks = taskManager.getEpicSubtasks(1);
        assertEquals(2, subtasks.size(), "Сабтаски не добавлены");
        taskManager.deleteSubtasks();
        subtasks = taskManager.getEpicSubtasks(1);
        assertEquals(0, subtasks.size(), "Сабтаски не удалены");
    }

    @Test
    void shouldDeleteEpics() {
        Epic epic = new Epic("Первый эпик", "Описание первого эпика", 1);
        Epic epic2 = new Epic("Второй эпик", "Описание второго эпика", 2);
        Subtask subtask = new Subtask("Первый сабтаск", "Описание первого сабтаска", 3, "IN_PROGRESS", 1);
        Subtask subtask2 = new Subtask("Второй сабтаск", "Описание второго сабтаска", 4, "IN_PROGRESS", 1);
        taskManager.addNewEpic(epic);
        taskManager.addNewEpic(epic2);
        taskManager.addNewSubtask(subtask);
        taskManager.addNewSubtask(subtask2);
        taskManager.deleteEpics();
        ArrayList<Epic> epics = taskManager.getEpics();
        ArrayList<Subtask> subtasks = taskManager.getSubtasks();
        assertEquals(0, subtasks.size(), "Сабтаски не удалились");
        assertEquals(0, epics.size(), "Эпики не удалились");
    }

    @Test
    void shouldUpdateEpicStatus() {
        Epic epic = new Epic("Первый эпик", "Описание первого эпика", 1);
        Subtask subtask = new Subtask("Первый сабтаск", "Описание первого сабтаска", 2, "NEW", 1);
        Subtask subtask2 = new Subtask("Второй сабтаск", "Описание второго сабтаска", 3, "NEW", 1);
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask);
        taskManager.addNewSubtask(subtask2);
        assertEquals(NEW, epic.getStatus(), "Статус должен быть NEW");
        subtask.setStatus("IN_PROGRESS");
        taskManager.updateSubtask(subtask);
        assertEquals(IN_PROGRESS, epic.getStatus(), "Статус должен быть IN_PROGRESS");
        subtask2.setStatus("DONE");
        taskManager.updateSubtask(subtask);
        assertEquals(IN_PROGRESS, epic.getStatus(), "Статус должен быть IN_PROGRESS");
        subtask.setStatus("DONE");
        taskManager.updateSubtask(subtask);
        assertEquals(DONE, epic.getStatus(), "Статус должен быть DONE");
    }

    @Test
    void shouldEpicDoNotAddInEpicId() {
        Epic epic = new Epic("Первый эпик", "Описание первого эпика", 1);
        Subtask subtask = new Subtask("Первый сабтаск", "Описание первого сабтаска", 0, "NEW", 1);
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask);
        ArrayList<Subtask> subtasks = taskManager.getSubtasks();
        assertNotEquals(epic.getId(), subtasks.get(0).getId(), "ID сабтаска и эпика совпадают");
    }

    @Test
    void shouldDoNotMakeSubtaskOnHisEpic() {
        Epic epic = new Epic("Эпик", "Описание", 1);
        Subtask subtask = new Subtask("Сабтаск", "Описание", 1, "NEW", 1);
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask);
        ArrayList<Epic> epics = taskManager.getEpics();
        ArrayList<Subtask> subtasks = taskManager.getEpicSubtasks(1);
        assertNotEquals(epics.get(0).getId(), subtasks.get(0).getId(), "ID сабтаска и эпика совпадают");
    }

    @Test
    void shouldTaskManagerAddAllTaskById() {
        Task task = new Task("1 задача", "Описание 1 задачи", 1, "NEW");
        Epic epic = new Epic("Эпик", "Описание", 2);
        Subtask subtask = new Subtask("Сабтаск", "Описание", 3, "NEW", 2);
        taskManager.addNewTask(task);
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask);
        assertNotNull(taskManager.getTask(1), "Задача не найдена");
        assertNotNull(taskManager.getEpic(2), "Эпик не найден");
        assertNotNull(taskManager.getSubtask(3), "Сабтаск не найден");
    }

    @Test
    void shouldCheckTheImmutabilityOfTheTask() {
        Task task = new Task("1 задача", "Описание 1 задачи", 1, "NEW");
        taskManager.addNewTask(task);
        assertEqualsTask(task, taskManager.getTask(1), "Задачи не совпадают");
    }
}

