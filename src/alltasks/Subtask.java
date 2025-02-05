package alltasks;

import com.google.gson.annotations.SerializedName;
import manager.TypeOfTask;

import java.time.Duration;
import java.util.Objects;

public class Subtask extends Task {
    @SerializedName("typeOfTask")
    protected TypeOfTask typeOfTask = TypeOfTask.SUBTASK;
    private int epicId;

    public Subtask(String name, String description, int id, String status, int epicId) {
        super(name, description, id, status);
        this.typeOfTask = TypeOfTask.SUBTASK;
        this.duration = Duration.ZERO;
        this.startTime = null;
        this.epicId = epicId;
    }

    public Subtask(String name, String description, String status, int epicId) {
        super(name, description, status);
        this.duration = Duration.ZERO;
        this.startTime = null;
        this.typeOfTask = TypeOfTask.SUBTASK;
        this.epicId = epicId;
    }

    public Subtask(String name, String description, int id, String status, int epicId, int duration, String startTime) {
        super(name, description, id, status, duration, startTime);
        this.typeOfTask = TypeOfTask.SUBTASK;
        this.epicId = epicId;
    }

    public Subtask(String name, String description, String status, int epicId, int duration, String startTime) {
        super(name, description, status, duration, startTime);
        this.typeOfTask = TypeOfTask.SUBTASK;
        this.epicId = epicId;
    }

    public Subtask(Subtask copy) {
        super(copy);
        this.epicId = copy.epicId;
    }

    @Override
    public TypeOfTask getTypeOfTask() {
        return typeOfTask;
    }
    public int getEpicId() {
        return epicId;
    }

    @Override
    public boolean isSubtask() {
        return true;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        Subtask subtask = (Subtask) object;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(epicId);
    }
}
