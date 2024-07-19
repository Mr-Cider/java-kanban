public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();
          Task task = new Task("Раз", "Описание", "NEW");
          taskManager.addNewTask(task);
        Epic epic = new Epic("Первый эпик", "Грандиозная задача", "NEW");
        taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("Первый подсос", "я первый я", "NEW", 2);
                taskManager.addNewSubtask(subtask);
        Subtask subtask2 = new Subtask("Второй подсос", "яq второй я", "IN_PROGRESS", 2);
        taskManager.addNewSubtask(subtask2);
        System.out.println(taskManager.getEpicSubtasks(2).toString());

//        taskManager.getTasks();
//        Task task = new Task("Раз", "Описание", "NEW");
//        taskManager.addNewTask(task);
//        System.out.println(taskManager.getTasks());
//        Task task1 = new Task("Раздва", "Описание 2.0", "IN_PROGRESS");
//        taskManager.updateTask(task1);
//        System.out.println(taskManager.getTasks());
//        Epic epic = new Epic("Первый эпик", "Грандиозная задача", "NEW");
//        taskManager.addNewEpic(epic);
//        System.out.println(taskManager.getEpics());
//        Subtask subtask = new Subtask("Первый подсос", "я первый я", "NEW", 2);
//        taskManager.addNewSubtask(subtask);
//        System.out.println(taskManager.getEpicSubtasks(2));
    }
}
