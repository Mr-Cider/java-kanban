package manager;

import alltasks.Epic;
import alltasks.Subtask;
import alltasks.Task;

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
        return historyManager.getHistory(); //
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
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }


    @Override
    public Subtask getSubtask(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
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
    public int addNewTask(Task task) {
        if (task.getTypeOfTask().equals(TypeOfTask.TASK)) {
            final int id = ++newId;
            task.setId(id);
            tasks.put(id, task);
            if (task.getStartTime() != null) {
                if (checkForIntersectingTasks(task)) addOrUpdateSortedTask(task);
            }
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
    public int addNewSubtask(Subtask subtask) {
            if (subtask.getTypeOfTask().equals(TypeOfTask.SUBTASK)) {
                final int id = ++newId;
                Epic epic = getEpicNotHistory(subtask.getEpicId());
                if (epic == null) {
                    return -1;
                }
                subtask.setId(id);
                epic.addSubtaskId(subtask.getId());
                subtasks.put(id, subtask);
                updateEpicStatus(epic);
                Optional.ofNullable(subtask.getStartTime())
                        .filter(startTime -> checkForIntersectingTasks(subtask))
                        .ifPresent(startTime -> {
                            addOrUpdateSortedTask(subtask);
                            setDateAndTimeInEpic(epic.getId());
                        });
                return id;
            }
        return -1;
    }

    public void setDateAndTimeInEpic(int id) {
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
                return;
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
            return;
        }
        epics.put(epic.getId(), epic);
    }


    @Override
    public void updateSubtask(Subtask subtask) {
            Epic epic = getEpic(subtask.getEpicId());
            if (!(subtasks.containsKey(subtask.getId()))) {
                return;
            }
            subtasks.put(subtask.getId(), subtask);
            if (subtask.getStartTime() != null) {
                if (checkForIntersectingTasks(subtask)) addOrUpdateSortedTask(subtask);
            }
            updateEpicStatus(epic);


    }


    @Override
    public void deleteTask(int id) {
        if (!tasks.containsKey(id)) {
            return;
        }
        tasks.remove(id);
        sortedTasks.remove(tasks.get(id));
        historyManager.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.remove(id);
        if (epic == null) {
            return;
        }
        epic.getSubtaskIds().stream().peek(subtasksId -> {
            subtasks.remove(subtasksId);
            historyManager.remove(subtasksId);
        }).map(this::getSubtask).forEach(subtask -> sortedTasks.remove(subtask));
    }

    @Override
    public void deleteSubtask(int id) {
        if (!(subtasks.containsKey(id))) {
            return;
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
            Epic epic = getEpic(id);
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

    public boolean checkForIntersectingTasks(Task task) {
        LocalDateTime taskStart = task.getStartTime();
        LocalDateTime taskEnd = task.getEndTime();
        return getPrioritizedTasks().stream().noneMatch(newTask -> {
            LocalDateTime newStartTask = newTask.getStartTime();
            LocalDateTime newEndTask = newTask.getEndTime();
            return taskEnd.isAfter(newStartTask) && taskStart.isBefore(newEndTask);
        });
    }

    public Set<Task> getPrioritizedTasks() {
       return sortedTasks;
    }

    private Epic getEpicNotHistory(int id) {
        return epics.get(id);
    }
}



