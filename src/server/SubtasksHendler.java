package server;

import alltasks.Subtask;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.IntersectionException;
import exception.NotFoundException;
import manager.ITaskManager;

import java.io.IOException;
import java.util.List;

public class SubtasksHendler extends BaseHttpHandler implements HttpHandler {
    public SubtasksHendler(ITaskManager manager, Gson gson) throws IOException {
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
                    handleGetSubtasks(exchange);
                    System.out.println("Посмотреть сабтаски");
                    break;

                case GET_TASK_ID:
                    handleGetSubtaskById(exchange, id);
                    System.out.println("Посмотреть сабтаск по ID");
                    break;

                case POST_TASK:
                    handleAddNewSubtask(exchange);
                    System.out.println("Добавить сабтаск");
                    break;

                case POST_UPDATE_TASK:
                    handleUpdateSubtask(exchange);
                    System.out.println("Обновить сабтаск");
                    break;

                case DELETE_TASK:
                    handleDeleteSubtask(exchange, id);
                    System.out.println("Удалить сабтаск");
                    break;
                default:
                    sendError(exchange, NOT_FOUND, "Эндпоинт не найден");
            }
        } catch (Exception e) {
            sendError(exchange, INTERNAL_SERVER_ERROR, "Ошибка сервера" + e.getMessage());
        }
    }

    private void handleGetSubtasks(HttpExchange exchange) throws IOException {
        List<Subtask> subtasks = manager.getSubtasks();
        sendText(exchange, OK, gson.toJson(subtasks));
    }

    private void handleGetSubtaskById(HttpExchange exchange, int id) throws IOException {
        Subtask subtask = manager.getSubtask(id);
        sendText(exchange, OK, gson.toJson(subtask));
    }

    private void handleAddNewSubtask(HttpExchange exchange) throws IOException {
        try {
            String json = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            System.out.println("JSON: " + json);
            Subtask subtask = gson.fromJson(json, Subtask.class);
            System.out.println("Парсим сабтаск: " + subtask);
            int taskId = manager.addNewSubtask(subtask);
            System.out.println("ID: " + taskId);
            sendText(exchange, CREATED, gson.toJson(subtask));
        } catch (NotFoundException | IntersectionException e) {
            handleException(exchange, e);
        } catch (JsonSyntaxException e) {
            sendError(exchange, BAD_REQUEST, "Некорректный формат JSON");
        } catch (Exception e) {
            System.err.println("Ошибка handleAddNewSubtask: " + e.getMessage());
        }
    }

    private void handleUpdateSubtask(HttpExchange exchange) throws IOException {
        try {
            String json = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            System.out.println("JSON: " + json);
            Subtask subtask = gson.fromJson(json, Subtask.class);
            System.out.println("Парсим сабтаск: " + subtask);
            manager.updateSubtask(subtask);
            sendText(exchange, CREATED, gson.toJson(subtask));
        } catch (NotFoundException | IntersectionException e) {
            handleException(exchange, e);
        } catch (JsonSyntaxException e) {
            sendError(exchange, BAD_REQUEST, "Некорректный формат JSON");
        } catch (Exception e) {
//            System.err.println("Ошибка в handleUpdateSubtask: " + e.getMessage());
        }
    }

    private void handleDeleteSubtask(HttpExchange exchange, int id) throws IOException {
        try {
            manager.deleteSubtask(id);
        } catch (NotFoundException e) {
            handleException(exchange, e);
        }
        sendText(exchange, OK, "Сабтаск с id " + id + "удален.");
    }
    }


