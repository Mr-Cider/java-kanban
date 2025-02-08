package server;

import alltasks.Epic;
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

public class EpicsHendler extends BaseHttpHandler implements HttpHandler {
    public EpicsHendler(ITaskManager manager, Gson gson) throws IOException {
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
                    handleGetEpics(exchange);
                    System.out.println("Посмотреть эпики");
                    break;

                case GET_TASK_ID:
                    handleGetEpicById(exchange, id);
                    System.out.println("Посмотреть эпик по ID");
                    break;

                case GET_EPIC_ID_SUBTASKS:
                    handleGetEpicSubtasks(exchange, id);
                    System.out.println("Посмотреть эпик по id");
                    break;

                case POST_TASK:
                    handleAddNewEpic(exchange);
                    System.out.println("Добавить эпик");
                    break;

                case POST_UPDATE_TASK:
                    handleUpdateEpic(exchange);
                    System.out.println("Обновить эпик");
                    break;

                case DELETE_TASK:
                    handleDeleteEpic(exchange, id);
                    System.out.println("Удалить эпик");
                    break;
                default:
                    sendError(exchange, NOT_FOUND, "Эндпоинт не найден");
            }
        } catch (Exception e) {
            sendError(exchange, INTERNAL_SERVER_ERROR, "Ошибка сервера" + e.getMessage());
        }
    }

    private void handleGetEpics(HttpExchange exchange) throws IOException {
        List<Epic> epics = manager.getEpics();
        sendText(exchange, OK, gson.toJson(epics));
    }

    private void handleGetEpicById(HttpExchange exchange, int id) throws IOException {
        Epic epic = manager.getEpic(id);
        sendText(exchange, OK, gson.toJson(epic));
    }

    private void handleGetEpicSubtasks(HttpExchange exchange, int id) throws IOException {
        List<Subtask> subtasks = manager.getEpicSubtasks(id);
        sendText(exchange, OK, gson.toJson(subtasks));
    }

    private void handleAddNewEpic(HttpExchange exchange) throws IOException {
        try {
            String json = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            System.out.println("JSON: " + json);
            Epic epic = gson.fromJson(json, Epic.class);
            System.out.println("Парсим эпик: " + epic);
            int taskId = manager.addNewEpic(epic);
            System.out.println("ID: " + taskId);
            sendText(exchange, CREATED, gson.toJson(epic));
        } catch (NotFoundException | IntersectionException e) {
            handleException(exchange, e);
        } catch (JsonSyntaxException e) {
            sendError(exchange, BAD_REQUEST, "Некорректный формат JSON");
        } catch (Exception e) {
            System.err.println("Ошибка в handleAddNewEpic: " + e.getMessage());
        }
    }

    private void handleUpdateEpic(HttpExchange exchange) throws IOException {
        try {
            String json = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            System.out.println("Received JSON: " + json);
            Epic epic = gson.fromJson(json, Epic.class);
            System.out.println("Parsed epic: " + epic);
            manager.updateEpic(epic);
            sendText(exchange, CREATED, gson.toJson(epic));
        } catch (NotFoundException | IntersectionException e) {
            handleException(exchange, e);
        } catch (JsonSyntaxException e) {
            sendError(exchange, BAD_REQUEST, "Некорректный формат JSON");
        } catch (Exception e) {
            System.err.println("Error in handleUpdateTask: " + e.getMessage());
        }
    }

    private void handleDeleteEpic(HttpExchange exchange, int id) throws IOException {
        try {
            manager.deleteEpic(id);
        } catch (NotFoundException e) {
            handleException(exchange, e);
        }
        sendText(exchange, OK, "Эпик с id " + id + "удален.");
    }
}



