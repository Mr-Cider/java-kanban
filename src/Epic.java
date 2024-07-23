import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, setEpicStatus());
    }

    public Epic(String name, String description, int id) {
        super(name, description, id, setEpicStatus());
    }

    private static String setEpicStatus() {
        return "NEW";
}

    @Override
    public boolean isEpic() {
        return true;
    }

    @Override
    public boolean isSubtask() {
        return false;
    }

    public void addSubtaskId(int id) {
        subtaskIds.add(id);
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void cleanSubtaskIds() {
        subtaskIds.clear();
    }
}

