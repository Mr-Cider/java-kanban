package alltasks;

import manager.TypeOfTask;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, "NEW");
        this.typeOfTask = TypeOfTask.EPIC;
    }

    public Epic(String name, String description, int id) {
        super(name, description, id, "NEW");
        this.typeOfTask = TypeOfTask.EPIC;
    }

    public Epic(Epic copy) {
        super(copy);
        this.typeOfTask = TypeOfTask.EPIC;
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

    public void deleteSubtaskId(int id) {
        if (subtaskIds.contains(id)) {
            subtaskIds.remove(Integer.valueOf(id));
        }
    }
}

