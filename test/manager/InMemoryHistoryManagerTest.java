package manager;


import allTasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static serviceTest.TestMethods.*;

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
        Task task2 = new Task("Обновленный таск", "Описание обновленного таска", 1, "IN_PROGRESS");
        taskManager.updateTask(task2);
        taskManager.getTask(1);
        LinkedList<Task> history = historyManager.getHistory();
        assertEqualsTask(taskForCheck, history.get(0), "Таски не совпадают");
        assertEqualsTask(task2, history.get(1), "Таски не совпадают");
    }

    @Test
    void shouldTaskIsNull() {
     taskManager.getTask(0);
     LinkedList<Task> history = historyManager.getHistory();
        assertEquals(0, history.size(), "Пустой таск добавлен");
    }

}
