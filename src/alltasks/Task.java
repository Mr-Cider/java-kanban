package alltasks;

import com.google.gson.annotations.SerializedName;
import manager.TaskStatus;
import manager.TypeOfTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    @SerializedName("name")
    protected String name;

    @SerializedName("description")
    protected String description;

    @SerializedName("id")
    protected int id;

    @SerializedName("status")
    protected TaskStatus status;

    @SerializedName("typeOfTask")
    protected TypeOfTask typeOfTask;

    @SerializedName("duration")
    protected Duration duration;

    @SerializedName("startTime")
    protected LocalDateTime startTime; //начало выполнения

    @SerializedName("endTime")
    protected LocalDateTime endTime;

    protected static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public Task() {
        this.typeOfTask = TypeOfTask.TASK;
        this.duration = Duration.ZERO;
    }

    public Task(String name, String description, int id, String status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = TaskStatus.valueOf(status);
        this.duration = Duration.ZERO;
        this.startTime = null;
        this.endTime = null;
        this.typeOfTask = TypeOfTask.TASK;
    }

    public Task(String name, String description, String status) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.valueOf(status);
        this.duration = Duration.ZERO;
        this.startTime = null;
        this.endTime = null;
        this.typeOfTask = TypeOfTask.TASK;
    }

    public Task(String name, String description, int id, String status, int duration, String startTime) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = TaskStatus.valueOf(status);
        this.typeOfTask = TypeOfTask.TASK;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = LocalDateTime.parse(startTime, DATE_TIME_FORMATTER);
        this.endTime = getEndTime();
    }


    public Task(String name, String description, String status, int duration, String startTime) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.valueOf(status);
        this.typeOfTask = TypeOfTask.TASK;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = LocalDateTime.parse(startTime, DATE_TIME_FORMATTER);
        this.endTime = getEndTime();
    }

    public Task(Task copy) {
        this.name = copy.name;
        this.description = copy.description;
        this.id = copy.id;
        this.status = copy.status;
        this.duration = copy.duration;
        this.startTime = copy.startTime;
        this.typeOfTask = TypeOfTask.EPIC;
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

    public TypeOfTask getTypeOfTask() {
        return typeOfTask;
    }

    public DateTimeFormatter getDateTimeFormatter() {
        return DATE_TIME_FORMATTER;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }

    public static int compareToDate(Task task1, Task task2) {
        if (task1.getStartTime() == null && task2.getStartTime() == null) return 0;
        if (task1.getStartTime() == null) return 1; // Задачи без времени в конец
        if (task2.getStartTime() == null) return -1;
        return task1.getStartTime().compareTo(task2.getStartTime());
    }

    public static DateTimeFormatter getDataTimeFormatter() {
        return DATE_TIME_FORMATTER;
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
        return  name + ", " +
                description + ", " +
                "ID " + id + ", " +
                status + ", " +
                duration.toMinutes() + ", " +
                startTime + "]\n";
    }
}



