package allTasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
    Epic epic;

    @BeforeEach
    void beforeEach() {
        epic = new Epic("Первый эпик", "Описание первого эпика", 1);
    }

    @Test
    void shouldIsEpic() {
        boolean actual = epic.isEpic();
        assertEquals(true, actual, "Объект не является эпиком");
    }

    @Test
    void shouldIsNotSubtask() {
        boolean actual = epic.isSubtask();
        assertEquals(false, actual, "Объект является сабтаском");
    }

    @Test
    void shouldAddSubtaskIdAndGetSubtaskIds() {
        int id = 2;
        epic.addSubtaskId(2);
        ArrayList<Integer> arr = epic.getSubtaskIds();
        int actual = arr.get(0);
        assertEquals(id, actual, "ID не добавлен");

    }

    @Test
    void shouldCleanSubtaskIds() {
        epic.addSubtaskId(2);
        epic.addSubtaskId(3);
        epic.addSubtaskId(4);
        epic.cleanSubtaskIds();
        ArrayList<Integer> arr = epic.getSubtaskIds();
        assertEquals(0, arr.size(), "Данные не удалены");
    }

    @Test
    void shouldDeleteSubtaskId() {
        epic.addSubtaskId(2);
        epic.addSubtaskId(3);
        epic.addSubtaskId(4);
        int id = 4;
        epic.deleteSubtaskId(id);
        ArrayList<Integer> arr = epic.getSubtaskIds();
        assertEquals(2, arr.size(), "Данные по id " + id + " не удалены");
    }

    @Test
    void shouldEpicEqualsEpicIfEqualsId() {
        Epic epic2 = new Epic("Другой эпик", "Описание другого эпика", 1);
        assertEquals(epic, epic2, "Эпики не равны");
    }
}