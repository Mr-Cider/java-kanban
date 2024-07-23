import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TaskManager implements ITaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();

    private final HashMap<Integer, Epic> epics = new HashMap<>();

    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private int generatorId = 0;


    @Override
    public ArrayList<Task> getTasks() {
        if (!tasks.isEmpty()) {
            ArrayList<Task> taskArrayList = new ArrayList<>(tasks.values());
            return taskArrayList;
        }
        System.out.println("Список тасков пустой.");
        return null;
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        if (!subtasks.isEmpty()) {
            ArrayList<Subtask> subtaskArrayList = new ArrayList<>(subtasks.values());
            return subtaskArrayList;
        }
        System.out.println("Список сабтасков пустой.");
        return null;
    }

    @Override
    public ArrayList<Epic> getEpics() {
        if (!epics.isEmpty()) {
            ArrayList<Epic>  epicArrayList = new ArrayList<>(epics.values());
            return epicArrayList;
        }
        System.out.println("Список эпиков пустой.");
        return null;
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        if (epics.get(epicId) == null) {
            System.out.println("Эпика с таким id не существует.");
            return null;
        }
        if (epics.get(epicId).getSubtaskIds().isEmpty()) {
            System.out.println("В эпике нет сабтасков.");
            return null;
        }
        ArrayList<Subtask> subtaskArrayList = new ArrayList<>(); // ПРОВЕРИТ// Ь
        ArrayList<Integer> subtaskIds = epics.get(epicId).getSubtaskIds();
        for (Integer id : subtaskIds) {
            subtaskArrayList.add(subtasks.get(id));
        }
        return subtaskArrayList;
}


    @Override
    public Task getTask(int id) {
        return tasks.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        return epics.get(id);
    }

    @Override
    public int addNewTask(Task task) {
        if (!task.isEpic() && !task.isSubtask()) {
            final int id = ++generatorId;
            task.setId(id);
            tasks.put(id, task);
            return id;
        }
        System.out.println("Таск не добавлен, так как передан объект другого типа.");
        return -1;
    }

    @Override
    public int addNewEpic(Epic epic) {
        if (epic.isEpic()) {
            final int id = ++generatorId;
            epic.setId(id);
            epics.put(id, epic);
            updateEpicStatus(epic);
            return id;
        }
        System.out.println("Передан объект другого типа. Эпик не добавлен");
        return -1;
    }

    @Override
    public Integer addNewSubtask(Subtask subtask) {
        if (subtask.isSubtask()) {
            final int id = ++generatorId;
            Epic epic = getEpic(subtask.getEpicId());
            if (epic == null) {
                System.out.println("Данный эпик ещё не создан");
                return -1;
            }
            subtask.setId(id);
            epic.addSubtaskId(subtask.getId());
            subtasks.put(id, subtask);
            updateEpicStatus(epic);
            return id;
        }
        System.out.println("Передан объект другого типа. Сабтаск не добавлен.");
        return -1;
    }

    @Override
    public void updateTask(Task task) {
        if (!task.isEpic() && !task.isSubtask()) {
            if (!(tasks.containsKey(task.id))) {
                System.out.println("Для обновления задачи нужно передать существующий id. Задача не обновлена.");
                return;
            }
            tasks.put(task.id, task);
            System.out.println("Задача обновлена.");
        } else {
            System.out.println("Таск не обновлен, передан объект другого типа.");
        }

    }

    @Override
    public void updateEpic(Epic epic) {
        if (!(epics.containsKey(epic.id))) {
            System.out.println("Для обновления эпика нужно передать существующий id эпика. Эпик не обновлён.");
            return;
        }
        epics.put(epic.id, epic);
        updateEpicStatus(epic);
        System.out.println("Эпик обновлён.");
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Epic epic = getEpic(subtask.getEpicId());
        if (!(subtasks.containsKey(subtask.getId()))) {
            System.out.println("Для обновления сабтаска нужно передать существующий id сабтаска. Сабтаск не обновлён.");
            return;
        }
        subtasks.put(subtask.id, subtask);
        updateEpicStatus(epic);
        System.out.println("Сабтаск обновлён");
    }



    @Override
    public void deleteTask(int id) {
        if (!(tasks.containsKey(id))) {
            System.out.println("Нет таска с таким id: " + id);
        }
        tasks.remove(id);
        System.out.println("Таск c ID " + id + " удален.");
    }

    @Override
    public void deleteEpic(int id) {
        if (!(epics.containsKey(id))) {
            System.out.println("Нет эпика с таким id: " + id);
        }
        ArrayList<Integer> subtaskIds = epics.get(id).getSubtaskIds();
        for (Integer subtaskId : subtaskIds) {
            subtasks.remove(subtaskId);
        }
        epics.remove(id);
        System.out.println("Эпик c id + " + id + " и связанные с ним сабтаски удалены.");
    }

    @Override
    public void deleteSubtask(int id) {
        if (!(subtasks.containsKey(id))) {
            System.out.println("Нет сабтаска с таким id: " + id);
        }
        Subtask subtask = getSubtask(id);
        Epic epic = getEpic(subtask.getEpicId());
        epic.subtaskIds.remove(id);
        subtasks.remove(id);
        updateEpicStatus(epic);
        System.out.println("Сабтаск с id " + id + " удален.");
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
        System.out.println("Список тасков очищен.");
    }

    @Override
    public void deleteSubtasks() {
        subtasks.clear();
        for (Integer id : epics.keySet()) {
            Epic epic = getEpic(id);
            updateEpicStatus(epic);
            if (!(epic.getSubtaskIds().isEmpty())) {
                epic.cleanSubtaskIds();
            }
        }
        System.out.println("Список сабтасков очищен.");
    }

    @Override
    public void deleteEpics() {
        subtasks.clear();
        epics.clear();
        System.out.println("Список эпиков и связанных с ними сабтасков удален.");
    }

    public void updateEpicStatus(Epic epic) {
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
        System.out.println("В эпике нет сабтасков");
    }
}



