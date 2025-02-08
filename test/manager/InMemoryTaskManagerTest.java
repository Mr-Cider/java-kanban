package manager;

import alltasks.Epic;
import alltasks.Subtask;
import alltasks.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager(new InMemoryHistoryManager());
    }

    @Test
    void shouldGetSortedTasks() {
        Task task = new Task("1 задача", "Описание 1 задачи", 1, "NEW",
                25, "26.01.2025 13:02");
        Epic epic = new Epic("Эпик", "Описание", 2);
        Subtask subtask = new Subtask("Сабтаск", "Описание", 3, "NEW", 2,
                50, "25.01.2025 10:30");
        Subtask subtask2 = new Subtask("Сабтаск", "Описание", 4, "NEW", 2,
                25, "01.01.2025 00:00");
        System.out.println(taskManager.getPrioritizedTasks());
        taskManager.addNewTask(task);
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask);
        taskManager.addNewSubtask(subtask2);
        assertEquals(3, taskManager.getPrioritizedTasks().size(), "Количество задач не совпадает.");
    }
}