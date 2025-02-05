package manager;

import java.io.IOException;

public class Managers {

    public static ITaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static ITaskManager getFileBackedTaskManager () throws IOException {
        return new FileBackedTaskManager(getDefaultHistory());
    }

    public static IHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}

