import allTasks.Epic;
import allTasks.Subtask;
import allTasks.Task;
import manager.TaskManager;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();
        System.out.println("-".repeat(50));
        System.out.println("Создание задач: ");
        Task task = new Task("Первая задача", "Описание первой задачи", "NEW");
        Task task2 = new Task("Вторая задача", "Описание второй задачи", "NEW");
        taskManager.addNewTask(task);
        taskManager.addNewTask(task2);
        System.out.println(" ");
        System.out.println("Создание эпиков и сабтасков: ");
        Epic epic = new Epic("Первый эпик", "Описание первого эпика");
        taskManager.addNewEpic(epic);
        System.out.println(taskManager.getEpics());
        System.out.println("Сабтаски к эпику:");
        Subtask subtask = new Subtask("Первый сабтаск к эпику № " + epic.getId(), "Описание к нему", "NEW", epic.getId());
        Subtask subtask2 = new Subtask("Второй сабтаск к эпику № " + epic.getId(), "Описание к нему", "IN_PROGRESS", epic.getId());
        taskManager.addNewSubtask(subtask);
        taskManager.addNewSubtask(subtask2);
        System.out.println(taskManager.getEpicSubtasks(epic.getId()));

        Epic epic2 = new Epic("Второй эпик", "Описание второго эпика");
        taskManager.addNewEpic(epic2);
        Subtask subtask3 = new Subtask("Первый сабтсаск эпику № " + epic2.getId(), "Описание сабтаска", "NEW", epic2.getId());
        taskManager.addNewSubtask(subtask3);
        System.out.println(taskManager.getEpicSubtasks(epic2.getId()));
        System.out.println(taskManager.getEpics());

        System.out.println("-".repeat(50));
        System.out.println("Обновление задач: ");
        System.out.println(" ");

        Task updateTask = new Task("Первая задача", "Описание первой задачи дополнено", task.getId(), "IN_PROGRESS");
        taskManager.updateTask(updateTask);
        System.out.println(taskManager.getTasks());
        Subtask updateSubtask = new Subtask("Первый сабтаск к эпику № " + epic.getId(), "Описание к нему", subtask.getId(), "DONE", epic.getId());
        Subtask updateSubtask2 = new Subtask("Второй сабтаск к эпику № " + epic.getId(), "Описание к нему", subtask2.getId(),"DONE", epic.getId());
        taskManager.updateSubtask(updateSubtask);
        taskManager.updateSubtask(updateSubtask2);
        System.out.println(taskManager.getEpic(epic.getId()));
        System.out.println(taskManager.getEpicSubtasks(epic.getId()));

        System.out.println("-".repeat(50));
        System.out.println("Удаление задач: ");
        System.out.println(" ");

        System.out.println(taskManager.getTasks());
        taskManager.deleteTask(1);
        System.out.println(taskManager.getTasks());
        System.out.println(" ");
        System.out.println("Удаление сабтасков и эпиков:");
        System.out.println(" ");
        System.out.println("До:");
        System.out.println(" ");
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        System.out.println(" ");
        System.out.println("После:");
        System.out.println(" ");
        taskManager.deleteEpic(3);
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        Epic epic1 = new Epic("Эпик №2", "Дополнено описание", epic2.getId());
        taskManager.updateEpic(epic1);
        System.out.println(taskManager.getEpics());
        Subtask subtask4 = new Subtask("Второй сабтаск к эпику № " + epic1.getId(), "Описание к нему", "IN_PROGRESS", epic1.getId());
        taskManager.addNewSubtask(subtask4);
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getEpicSubtasks(epic1.getId()));
        taskManager.deleteSubtasks();
        System.out.println(taskManager.getEpicSubtasks(epic1.getId()));
        System.out.println(taskManager.getEpics());
    }





    }

