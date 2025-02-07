package server;

import alltasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.IntersectionException;
import exception.NotFoundException;
import manager.ITaskManager;

import java.io.IOException;
import java.util.List;

public class TasksHendler extends BaseHttpHandler implements HttpHandler {

    public TasksHendler(ITaskManager manager, Gson gson) throws IOException {
        super(manager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            int id = 0;
            try {
                String idS = searchId(path);
                if (idS != null) id = Integer.parseInt(idS);
            } catch (NotFoundException e) {
                sendText(exchange, BAD_REQUEST, "Некорректный id");
                return;
            }

            switch (getEndpoint(path, exchange.getRequestMethod())) {
                case GET_TASKS:
                    handleGetTasks(exchange);
                    System.out.println("Посмотреть задачи");
                    break;

                case GET_TASK_ID:
                    handleGetTaskById(exchange, id);
                    System.out.println("Посмотреть задачу по ID");
                    break;

                case POST_TASK:
                    handleAddNewTask(exchange);
                    System.out.println("Добавить задачу");
                    break;

                case POST_UPDATE_TASK:
                    handleUpdateTask(exchange);
                    System.out.println("Обновить задачу");
                    break;

                case DELETE_TASK:
                    handleDeleteTask(exchange, id);
                    System.out.println("Удалить задачу");
                    break;
                default:
                    sendError(exchange, NOT_FOUND, "Эндпоинт не найден");
            }
        } catch (Exception e) {
            sendError(exchange, INTERNAL_SERVER_ERROR, "Ошибка сервера" + e.getMessage());
        }
    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        List<Task> tasks = manager.getTasks();
        sendText(exchange, OK, gson.toJson(tasks));
    }

    private void handleGetTaskById(HttpExchange exchange, int id) throws IOException {
        Task task = manager.getTask(id);
        sendText(exchange, OK, gson.toJson(task));
    }

    private void handleAddNewTask(HttpExchange exchange) throws IOException {
        try {
            String json = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            System.out.println("JSON: " + json);
            Task task = gson.fromJson(json, Task.class);
            System.out.println("Парсим task: " + task);
            int taskId = manager.addNewTask(task);
            System.out.println("ID: " + taskId);
            sendText(exchange, CREATED, gson.toJson(task));
        } catch (NotFoundException | IntersectionException e) {
            handleException(exchange, e);
        } catch (JsonSyntaxException e) {
            sendError(exchange, BAD_REQUEST, "Некорректный формат JSON");
        } catch (Exception e) {
            System.err.println("Ошибка в handleAddNewTask: " + e.getMessage());
        }
    }

    private void handleUpdateTask(HttpExchange exchange) throws IOException {
        try {
            String json = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            System.out.println("JSON: " + json);
            Task task = gson.fromJson(json, Task.class);
            System.out.println("Парсим task: " + task);
            manager.updateTask(task);
            sendText(exchange, CREATED, gson.toJson(task));
        } catch (NotFoundException | IntersectionException e) {
            handleException(exchange, e);
        } catch (JsonSyntaxException e) {
            sendError(exchange, BAD_REQUEST, "Некорректный формат JSON");
        } catch (Exception e) {
            System.err.println("Ошибка в handleUpdateTask: " + e.getMessage());
        }
    }

    private void handleDeleteTask(HttpExchange exchange, int id) throws IOException {
        try {
            manager.deleteTask(id);
        } catch (NotFoundException e) {
            handleException(exchange, e);
        }
        sendText(exchange, OK, "Задача с id " + id + "удалена.");
    }
}