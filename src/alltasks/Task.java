package alltasks;

import manager.TaskStatus;

import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected TaskStatus status;

    public Task(String name, String description, int id, String status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = TaskStatus.valueOf(status);
    }

    public Task(String name, String description, String status) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.valueOf(status);
    }

    public Task(Task copy) {
        this.name = copy.name;
        this.description = copy.description;
        this.id = copy.id;
        this.status = copy.status;
    }

    public boolean isEpic() {
        return false;
    }

    public boolean isSubtask() {
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = TaskStatus.valueOf(status);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Task task = (Task) object;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return " " + name + ", " +
                description + ", " +
                "ID " + id + ", " +
                status + " ";
    }
}



