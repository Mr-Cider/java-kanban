package alltasks;

import manager.TypeOfTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subtaskIds = new ArrayList<>();

    public Epic() {
        this.typeOfTask = TypeOfTask.EPIC;
        this.duration = Duration.ZERO;
    }

    public Epic(String name, String description) {
        super(name, description, "NEW");
        this.duration = Duration.ZERO;
        this.startTime = null;
        this.endTime = getEndTime();
        this.typeOfTask = TypeOfTask.EPIC;
    }

    public Epic(String name, String description, int id) {
        super(name, description, id, "NEW");
        this.duration = Duration.ZERO;
        this.startTime = null;
        this.endTime = getEndTime();
        this.typeOfTask = TypeOfTask.EPIC;
    }

    public Epic(Epic copy) {
        super(copy);
        this.typeOfTask = TypeOfTask.EPIC;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        if (!subtaskIds.isEmpty()) {
            return endTime;
    } else {
        return null;
    }
}

    @Override
    public boolean isEpic() {
        return true;
    }

    public void addSubtaskId(int id) {
        subtaskIds.add(id);
    }

    public ArrayList<Integer> getSubtaskIds() {
        return new ArrayList<>(subtaskIds);
    }

    public void cleanSubtaskIds() {
        subtaskIds.clear();
    }

    public void deleteSubtaskId(int id) {
        if (subtaskIds.contains(id)) {
            subtaskIds.remove(Integer.valueOf(id));
        }
    }

    @Override
    public String toString() {
        return  name + ", " +
                description + ", " +
                "ID " + id + ", " +
                status + ", " +
                duration.toMinutes() + ", " +
                startTime + ", " +
                endTime + "]\n";
    }
}

