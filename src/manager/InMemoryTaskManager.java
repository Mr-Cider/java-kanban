package manager;

import alltasks.Epic;
import alltasks.Subtask;
import alltasks.Task;
import exception.IntersectionException;
import exception.NotFoundException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements ITaskManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();

    protected final Map<Integer, Epic> epics = new HashMap<>();

    protected final Map<Integer, Subtask> subtasks = new HashMap<>();

    private final IHistoryManager historyManager;

    protected int newId = 0;

    protected Set<Task> sortedTasks = new TreeSet<>(Task::compareToDate);

    public InMemoryTaskManager(IHistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public ArrayList<Task> getTasks() {
        if (!tasks.isEmpty()) {
            return new ArrayList<>(tasks.values());
        }
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        if (!subtasks.isEmpty()) {
            return new ArrayList<>(subtasks.values());
        }
        return new ArrayList<>(Collections.emptyList());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        if (!epics.isEmpty()) {
            return new ArrayList<>(epics.values());
        }
        return new ArrayList<>(Collections.emptyList());
    }

    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        return Optional.ofNullable(epics.get(epicId))
                .map(Epic::getSubtaskIds)
                .filter(subtaskIds -> !subtaskIds.isEmpty())
                .map(subtaskIds -> subtaskIds.stream()
                        .map(subtasks::get)
                        .collect(Collectors.toCollection(ArrayList::new)))
                .orElseGet(ArrayList::new);
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task == null) throw new NotFoundException("Задача с указанным id не найдена");
        historyManager.add(tasks.get(id));
        return task;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask == null) throw new NotFoundException("Сабтаск с указанным id не найден");
        historyManager.add(subtasks.get(id));
        return subtask;
    }

    @Override
    public Epic getEpic(int id) {
    Epic epic = epics.get(id);
        if (epic == null) throw new NotFoundException("Эпик с указанным id не найден");
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    public List<Task> getAllTasks() {
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(getTasks());
        allTasks.addAll(getEpics());
        allTasks.addAll(getSubtasks());
        return allTasks;
    }

    @Override
    public int addNewTask(Task task) throws IntersectionException {
        if (task.getTypeOfTask() == TypeOfTask.TASK) {
            if (task.getStartTime() != null) {
                if (checkForIntersectingTasks(task)) {
                    throw new IntersectionException("Задачи пересекаются во времени.");
                }
                addOrUpdateSortedTask(task);
            }

            final int id = ++newId;
            task.setId(id);
            tasks.put(id, task);
            return id;
        }
        return -1;
    }

    @Override
    public int addNewEpic(Epic epic) {
        if (epic.getTypeOfTask().equals(TypeOfTask.EPIC)) {
            final int id = ++newId;
            epic.setId(id);
            epics.put(id, epic);
            return id;
        }
        return -1;
    }

    @Override
    public int addNewSubtask(Subtask subtask) throws IntersectionException {
        if (subtask.getTypeOfTask() != TypeOfTask.SUBTASK) {
            return -1;
        }
        Epic epic = getEpicNotHistory(subtask.getEpicId());
        if (epic == null) {
            return -1;
        }
        if (subtask.getStartTime() != null) {
            if (checkForIntersectingTasks(subtask)) {
                throw new IntersectionException("Задачи пересекаются во времени.");
            }
            addOrUpdateSortedTask(subtask);
            setDateAndTimeInEpic(epic.getId());
        }
        final int id = ++newId;
        subtask.setId(id);
        subtasks.put(id, subtask);
        epic.addSubtaskId(id);
        updateEpicStatus(epic);
        return id;
    }

    private void setDateAndTimeInEpic(int id) {
        Epic epic = getEpicNotHistory(id);
        List<Subtask> epicSubtasks = getEpicSubtasks(id);
        Optional<LocalDateTime> dateTime = epicSubtasks.stream()
                .min(Subtask::compareToDate)
                .map(Subtask::getStartTime);
        dateTime.ifPresent(epic::setStartTime);
        Optional<Subtask> maxSubtask = epicSubtasks.stream().max(Subtask::compareToDate);
        maxSubtask.ifPresent(subtask -> {
            Duration maxDuration = subtask.getDuration();
            epic.setDuration(maxDuration);
            LocalDateTime endDataTime = subtask.getStartTime().plus(maxDuration);
            epic.setEndTime(endDataTime);
        });
    }

    @Override
    public void updateTask(Task task) {
        if (task.getTypeOfTask().equals(TypeOfTask.TASK)) {
            if (!(tasks.containsKey(task.getId()))) {
                throw new NotFoundException("Таск с указанным id не найден");
            }
            tasks.put(task.getId(), task);
            if (task.getStartTime() != null) {
                if (checkForIntersectingTasks(task)) addOrUpdateSortedTask(task);
            }
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (!(epics.containsKey(epic.getId()))) {
            throw new NotFoundException("Эпик с указанным id не найден");
        }
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (!(subtasks.containsKey(subtask.getId()))) {
            throw new NotFoundException("Сабтаск с указанным id не найден");
        }
         Optional.of(subtask).ifPresent(s -> {
                    subtasks.put(s.getId(), s);
Epic epic = getEpicNotHistory(subtask.getEpicId());
Optional.ofNullable(s.getStartTime())
                                .filter(startTime -> checkForIntersectingTasks(s))
                            .ifPresent(startTime -> {
                                addOrUpdateSortedTask(s);
                                setDateAndTimeInEpic(epic.getId());
                            });
                    updateEpicStatus(getEpicNotHistory(epic.getId()));
                });
    }

    @Override
    public void deleteTask(int id) {
        if (!tasks.containsKey(id)) {
            throw new NotFoundException("Таск с указанным id не найден");
        }
        tasks.remove(id);
        sortedTasks.remove(tasks.get(id));
        historyManager.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        if (!epics.containsKey(id)) {
            throw new NotFoundException("Эпик с указанным id не найден");
        }
        Optional.ofNullable(getEpicNotHistory(id)).ifPresent(epic -> {
            epic.getSubtaskIds().forEach(subtaskId -> {
                sortedTasks.remove(subtasks.get(subtaskId));
                subtasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            });
            epics.remove(id);
            historyManager.remove(id);
        });
    }

    @Override
    public void deleteSubtask(int id) {
        if (!(subtasks.containsKey(id))) {
            throw new NotFoundException("Таск с указанным id не найден");
        }
        Subtask subtask = getSubtask(id);
        Epic epic = getEpic(subtask.getEpicId());
        epic.deleteSubtaskId(id);
        subtasks.remove(id);
        if (subtask.getStartTime() != null) {
            sortedTasks.remove(subtask);
            setDateAndTimeInEpic(subtask.getEpicId());
        }
        updateEpicStatus(epic);
        historyManager.remove(id);
    }

    @Override
    public void deleteTasks() {
        List<Integer> keys = new ArrayList<>(tasks.keySet());
        for (Integer key : keys) {
            sortedTasks.remove(new Task(tasks.get(key)));
            historyManager.remove(key);
        }
        tasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        epics.keySet().forEach(id -> {
            historyManager.remove(id);
            Epic epic = epics.get(id);
            epic.getSubtaskIds().forEach(subtaskId -> sortedTasks.remove(new Subtask(getSubtask(subtaskId))));
            epic.cleanSubtaskIds();
            setDateAndTimeInEpic(epic.getId());
            epic.setStatus("NEW");
        });
        subtasks.clear();
    }

    @Override
    public void deleteEpics() {
        epics.keySet().forEach(historyManager::remove);
        subtasks.keySet().forEach(historyManager::remove);
        subtasks.clear();
        epics.clear();
    }

    private void updateEpicStatus(Epic epic) {
        Set<TaskStatus> setStatus = new HashSet<>(); //get all status
        if (getEpicSubtasks(epic.getId()) != null) {
            for (Subtask subtask : getEpicSubtasks(epic.getId())) {
                setStatus.add(subtask.getStatus());
            }
            if (setStatus.isEmpty()) {
                epic.setStatus("NEW");
                return;
            }

            if (setStatus.size() == 1 && setStatus.contains(TaskStatus.NEW)) {
                epic.setStatus("NEW");
                return;
            }

            if (setStatus.contains(TaskStatus.DONE) && setStatus.size() == 1) {
                epic.setStatus("DONE");
                return;
            }
            epic.setStatus("IN_PROGRESS");
        }
    }

    public void addOrUpdateSortedTask(Task task) {
                if (task.getStartTime() != null) {
                    sortedTasks.remove(task);
                    sortedTasks.add(task);
                }
    }

    private boolean checkForIntersectingTasks(Task task) {
        LocalDateTime taskStart = task.getStartTime();
        LocalDateTime taskEnd = task.getEndTime();
        return getPrioritizedTasks().stream().anyMatch(newTask -> {
            LocalDateTime newStartTask = newTask.getStartTime();
            LocalDateTime newEndTask = newTask.getEndTime();
            return taskEnd.isAfter(newStartTask) && taskStart.isBefore(newEndTask);
        });
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
       return new TreeSet<>(Task::compareToDate) {{
           addAll(sortedTasks);
       }};
    }

    private Epic getEpicNotHistory(int id) {
        Epic epic = epics.get(id);
        if (epic == null) throw new NotFoundException("Эпик с указанным id не найден");
        return epics.get(id);
    }
}



