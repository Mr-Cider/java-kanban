package alltasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubtaskTest {
    Subtask subtask;

    @BeforeEach
    void beforeEach() {
        subtask = new Subtask("Первый сабтаск", "Описание к нему", 2, "NEW", 1);
    }

    @Test
    void shouldGetEpicId() {
        assertEquals(1, subtask.getEpicId(), "ID эпика не совпадает");
    }

    @Test
    void shouldIsEpic() {
        assertEquals(false, subtask.isEpic(), "Объект является эпиком");
    }

    @Test
    void shouldIsSubtask() {
        assertEquals(true, subtask.isSubtask(), "Объект не является сабтаском");
    }

    @Test
    void shouldSubtaskEqualsSubtaskIfEqualsId() {
        Subtask subtask2 = new Subtask("Другой сабтаск", "Описание к нему", 2, "DONE", 1);
        assertEquals(subtask, subtask2, "Сабтаски не равны");
    }
}
