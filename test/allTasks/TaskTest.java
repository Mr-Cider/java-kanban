package allTasks;

import manager.TaskStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static manager.TaskStatus.IN_PROGRESS;
import static manager.TaskStatus.NEW;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
Task task;


@BeforeEach
void beforeEach() {
    task = new Task("1 задача", "Описание 1 задачи", 1, "NEW");
}

@Test
void shouldIsNotEpic() {
    boolean actual = task.isEpic();
    assertEquals(false, actual, "Объект является эпиком");

}

@Test
void shouldIsNotSubtask() {
    boolean actual = task.isSubtask();
    assertEquals(false, actual, "Объект является сабтаском");
}

    @Test
    void shouldGetName() {
        assertEquals("1 задача", task.getName(), "Ожидаемое и актуальное имя не совпадают");
    }

    @Test
    void shouldSetName() {
        task.setName("Первая задача");
        assertEquals("Первая задача", task.getName(), "Ожидаемое и актуальное имя не совпадают");
    }

    @Test
    void shouldGetDescription() {
        assertEquals("Описание 1 задачи", task.getDescription(), "Ожидаемое и актуальное описание не совпадают");
    }

    @Test
    void shouldSetDescription() {
        task.setDescription("Описание первой задачи");
        assertEquals("Описание первой задачи", task.getDescription(), "Ожидаемое и актуальное описание не совпадают");
    }

    @Test
    void shouldGetId() {
        assertEquals(1, task.getId(), "Ожидаемый и актуальный Id не совпадает");
    }

    @Test
    void shouldSetId() {
        task.setId(2);
        assertEquals(2, task.getId(), "Ожидаемый и актуальный Id не совпадает");
    }

    @Test
    void shouldGetStatus() {
        TaskStatus expected = NEW;
        assertEquals(expected, task.getStatus(), "Ожидаемый и актуальный статус не совпадает");
    }

    @Test
    void shouldSetStatus() {
        TaskStatus expected = IN_PROGRESS;
        task.setStatus("IN_PROGRESS");
        assertEquals(expected, task.getStatus(), "Ожидаемый и актуальный статус не совпадает");
    }

    @Test
    void shouldTaskEqualTaskIfEqualId() {
        Task task2 = new Task("Другая задача", "Описание другой задачи", 1, "DONE");
        assertEquals(task, task2, "Таски не равны");
    }








}