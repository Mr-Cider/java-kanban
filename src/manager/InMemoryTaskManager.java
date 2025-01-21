package manager;

import alltasks.Epic;
import alltasks.Subtask;
import alltasks.Task;
import java.util.Collections;
import java.util.*;

public class InMemoryTaskManager implements ITaskManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();

    protected final Map<Integer, Epic> epics = new HashMap<>();

    protected final Map<Integer, Subtask> subtasks = new HashMap<>();

    private final IHistoryManager historyManager;

    private int newId = 0;


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

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        if (epics.get(epicId) == null) {
            return new ArrayList<>(Collections.emptyList());
        }
        if (epics.get(epicId).getSubtaskIds().isEmpty()) {
            return new ArrayList<>(Collections.emptyList());
        }
        ArrayList<Subtask> subtaskArrayList = new ArrayList<>();
        ArrayList<Integer> subtaskIds = epics.get(epicId).getSubtaskIds();
        for (Integer id : subtaskIds) {
            subtaskArrayList.add(subtasks.get(id));
        }
        return subtaskArrayList;
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

    @Override
    public int addNewTask(Task task) {
        if (!task.isEpic() && !task.isSubtask()) {
            final int id = ++newId;
            task.setId(id);
            tasks.put(id, task);
            return id;
        }
        return -1;
    }

    @Override
    public int addNewEpic(Epic epic) {
        if (epic.isEpic()) {
            final int id = ++newId;
            epic.setId(id);
            epics.put(id, epic);
            return id;
        }
        return -1;
    }

    @Override
    public int addNewSubtask(Subtask subtask) {
        if (subtask.isSubtask()) {
            final int id = ++newId;
            Epic epic = getEpicNotHistory(subtask.getEpicId());
            if (epic == null) {
                System.out.println("Сабтаск не добавлен, эпик null");
                return -1;
            }
            subtask.setId(id);
            epic.addSubtaskId(subtask.getId());
            subtasks.put(id, subtask);
            updateEpicStatus(epic);
            return id;
        }
        System.out.println("Сабтаск не добавлен, объект не сабтаск");
        return -1;
    }

    @Override
    public void updateTask(Task task) {
        if (!task.isEpic() && !task.isSubtask()) {
            if (!(tasks.containsKey(task.getId()))) {
                return;
            }
            tasks.put(task.getId(), task);
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
        updateEpicStatus(epic);
    }


    @Override
    public void deleteTask(int id) {
        if (!tasks.containsKey(id)) {
            return;
        }
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.remove(id);
        if (epic == null) {
            return;
        }
        ArrayList<Integer> subtaskIds = epic.getSubtaskIds();
        for (Integer subtaskId : subtaskIds) {
            subtasks.remove(subtaskId);
            historyManager.remove(id);
        }
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
        updateEpicStatus(epic);
        historyManager.remove(id);
    }

    @Override
    public void deleteTasks() {
        List<Integer> keys = new ArrayList<>(tasks.keySet());
        for (Integer key : keys) {
            historyManager.remove(key);
        }
        tasks.clear();

    }

    @Override
    public void deleteSubtasks() {
        subtasks.clear();
        for (Integer id : epics.keySet()) {
            historyManager.remove(id);
            Epic epic = getEpic(id);
            epic.cleanSubtaskIds();
            epic.setStatus("NEW");
        }
    }

    @Override
    public void deleteEpics() {
        for (Integer key : epics.keySet()) {
            historyManager.remove(key);
        }
        for (Integer key : subtasks.keySet()) {
            historyManager.remove(key);
        }
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

    private Epic getEpicNotHistory(int id) {
        return epics.get(id);
    }
}



