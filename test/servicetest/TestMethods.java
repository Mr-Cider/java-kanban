package servicetest;

import alltasks.Epic;
import alltasks.Subtask;
import alltasks.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMethods {
    public static void assertEqualsTask(Task expected, Task actual, String message) {
        assertEquals(expected.getId(), actual.getId(), message + " id");
        assertEquals(expected.getDescription(), actual.getDescription(), message + " description");
        assertEquals(expected.getName(), actual.getName(), message + " name");
        assertEquals(expected.getStatus(), actual.getStatus(), message + "status");

    }

    public static void assertEqualsEpic(Epic expected, Epic actual, String message) {
        assertEquals(expected.getId(), actual.getId(), message + " id");
        assertEquals(expected.getDescription(), actual.getDescription(), message + " description");
        assertEquals(expected.getName(), actual.getName(), message + " name");
        assertEquals(expected.getStatus(), actual.getStatus(), message + "status");
    }

    public static void assertEqualsSubtask(Subtask expected, Subtask actual, String message) {
        assertEquals(expected.getId(), actual.getId(), message + " id");
        assertEquals(expected.getDescription(), actual.getDescription(), message + " description");
        assertEquals(expected.getName(), actual.getName(), message + " name");
        assertEquals(expected.getStatus(), actual.getStatus(), message + "status");
        assertEquals(expected.getEpicId(), actual.getEpicId(), message + "epicId");
    }
}

