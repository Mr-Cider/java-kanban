package alltasks;

import manager.TaskStatus;
import manager.TypeOfTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected TaskStatus status;
    protected TypeOfTask typeOfTask;
    protected Duration duration;  //продолжительность задачи
    protected LocalDateTime startTime; //начало выполнения
    protected LocalDateTime endTime;

    protected static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

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
        return task1.startTime.compareTo(task2.startTime);
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



