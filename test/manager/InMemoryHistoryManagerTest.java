package manager;


import alltasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static servicetest.TestMethods.*;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager historyManager;
    InMemoryTaskManager taskManager;

    @BeforeEach
    void beforeEach() {
        historyManager = new InMemoryHistoryManager();
        taskManager = new InMemoryTaskManager(historyManager);
    }

    @Test
    void shouldAddAndGetHistory() {
        Task task = new Task("1 таск", "Описание 1 таска", 1, "NEW");
        Task taskForCheck = new Task("1 таск", "Описание 1 таска", 1, "NEW");
        taskManager.addNewTask(task);
        taskManager.getTask(1);
        assertEquals(taskForCheck, taskManager.getHistory().get(0));
        Task task2 = new Task("Обновленный таск", "Описание обновленного таска", 1, "IN_PROGRESS");
        Task taskForCheck2 = new Task("Обновленный таск", "Описание обновленного таска", 1, "IN_PROGRESS");
        taskManager.updateTask(task2);
        taskManager.getTask(1);
        assertEqualsTask(taskForCheck2, taskManager.getHistory().get(0), "Таски не совпадают");
    }

    @Test
    void shouldTaskIsNull() {
        taskManager.getTask(0);
        List<Task> history = historyManager.getHistory();
        assertEquals(0, history.size(), "Пустой таск добавлен");
    }

    @Test
    void shouldNewAddAndRewriting() {
        Task task = new Task("1 таск", "Описание 1 таска", 1, "NEW");
        Task task2 = new Task("3 таск", "Описание 3 таска", 3, "NEW");
        Task task3 = new Task("5 таск", "Описание 5 таска", 5, "NEW");
        Task task4 = new Task("1 таск", "Описание 1пывпмыукамыку таска", 1, "NEW");
        historyManager.add(task);
        historyManager.add(task2);
        historyManager.add(task3);
        assertEquals(3, historyManager.getHistory().size(), "Размер не совпадает.");
        historyManager.add(task4);
        assertEquals(3, historyManager.getHistory().size(), "Размер не совпадает.");
    }

    @Test
    void shouldRemoveInHistory() {
        Task task = new Task("1 таск", "Описание 1 таска", 1, "NEW");
        Task task2 = new Task("3 таск", "Описание 3 таска", 3, "NEW");
        Task task3 = new Task("5 таск", "Описание 5 таска", 5, "NEW");
        historyManager.add(task);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(3);
        assertEquals(2, historyManager.getHistory().size(), "Размер не совпадает.");
    }

    @Test
    void shouldRemove3TasksInHistory() {
        Task task = new Task("1 таск", "Описание 1 таска", 1, "NEW");
        Task task2 = new Task("3 таск", "Описание 3 таска", 3, "NEW");
        Task task3 = new Task("5 таск", "Описание 5 таска", 5, "NEW");
        Task task4 = new Task("8 таск", "Описание 8 таска", 8, "NEW");
        historyManager.add(task);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);
        historyManager.remove(1);
        historyManager.remove(3);
        assertEquals(2, historyManager.getHistory().size(), "Размер не совпадает");
    }

    @Test
    void shouldAdd20Tasks() {
        int tasksSize = 20;
        for (int i = 0; i < tasksSize; i++) {
            String taskNum = String.valueOf(i + 1);
            Task task = new Task(taskNum, taskNum, i + 1, "NEW");
            historyManager.add(task);
        }
        assertEquals(20, historyManager.getHistory().size(), "Размер истории не совпадает");
    }
}