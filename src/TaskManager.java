import java.util.ArrayList;
import java.util.HashMap;

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
            ArrayList<Epic> epicArrayList = new ArrayList<>(epics.values());
            return epicArrayList;
        }
        System.out.println("Список эпиков пустой.");
        return null;
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
//        if (epics.get(epicId) == null) {
//            System.out.println("Эпика с таким id не существует.");
//            return null;
//        }
        ArrayList<Subtask> subtaskArrayList = new ArrayList<>(); // ПРОВЕРИТ// Ь
        ArrayList<Integer> subtaskIds = epics.get(epicId).getSubtaskIds();
        for (Integer id : subtaskIds) {
            subtaskArrayList.add(getSubtask(id));
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
            subtasks.put(subtask.epicId, subtask);
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
            System.out.println("Задача не обновлена, передан объект другого типа.");
        }

    }

    @Override
    public void updateEpic(Epic epic) {
        if (!(epics.containsKey(epic.id))) {
            System.out.println("Для обновления эпика нужно передать существующий id эпика. Эпик не обновлён.");
            return;
        }
        epics.put(epic.id, epic);
        System.out.println("Эпик обновлён.");
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        System.out.println("Для обновления сабтаска нужно передать существующий id сабтаска. Сабтаск не обновлён.");
        if (!(subtasks.containsKey(subtask.id))) {
            return;
        }
        subtasks.put(subtask.id, subtask);
        System.out.println("Сабтаск обновлён");

    }

    @Override
    public void deleteTask(int id) {
        if (!(tasks.containsKey(id))) {
            System.out.println("Нет задачи с таким id: " + id);
        }
        tasks.remove(id);
        System.out.println("Задача удалена");
    }

    @Override
    public void deleteEpic(int id) {
        if (!(epics.containsKey(id))) {
            System.out.println("Нет эпика с таким id: " + id);
        }
        epics.remove(id);
        System.out.println("Эпик удален");
    }

    @Override
    public void deleteSubtask(int id) {
        if (!(subtasks.containsKey(id))) {
            System.out.println("Нет подзадачи с таким id: " + id);
        }
        subtasks.remove(id);
        System.out.println("Подзадача удалена");
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
        System.out.println("Список задач очищен.");
    }

    @Override
    public void deleteSubtasks() {
        subtasks.clear();
        System.out.println("Список подзадач очищен.");
    }

    @Override
    public void deleteEpics() {
        subtasks.clear();
        epics.clear();
        System.out.println("Список эпиков и связанных с ними подзадач удален.");
    }
}



