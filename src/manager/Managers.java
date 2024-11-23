package manager;

public class Managers {

    public static ITaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static IHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}

