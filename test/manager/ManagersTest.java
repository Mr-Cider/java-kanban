package manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManagersTest {

    @Test
    void shouldGetDefaultIsNotNull() {
        assertNotNull(Managers.getDefault());
    }

    @Test
    void shouldGetDefaultHistoryIsNotNull() {
        assertNotNull(Managers.getDefaultHistory());
    }
}