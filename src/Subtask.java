import java.util.Objects;

public class Subtask extends Task {
    protected int epicId;


    public Subtask(String name, String description, int id, String status, int epicId) {
        super(name, description, id, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, String status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public boolean isEpic() {
        return false;
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
