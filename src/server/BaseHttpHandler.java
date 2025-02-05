package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exception.IntersectionException;
import exception.NotFoundException;
import manager.ITaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class BaseHttpHandler {
    protected static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    protected final ITaskManager manager;
    protected final Gson gson;

    public BaseHttpHandler(ITaskManager manager, Gson gson) throws IOException {
        this.manager = manager;
        this.gson = gson;
    }

    protected void sendText(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(statusCode, response.getBytes(DEFAULT_CHARSET).length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes(DEFAULT_CHARSET));
        }
    }

    protected void sendError(HttpExchange exchange, int statusCode, String message) throws IOException {
        String response = gson.toJson(Map.of("error", message));
        exchange.getResponseHeaders().set("Content-Type", "application.json; charset=utf-8");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes(DEFAULT_CHARSET));
        }
    }

    protected void handleException(HttpExchange exchange, Exception e) throws IOException {
        if (e instanceof NotFoundException) {
            sendError(exchange, 404, e.getMessage());
        }
        if (e instanceof IntersectionException) {
            sendError(exchange, 406, e.getMessage());
        }
    }

    protected Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");
        switch (requestMethod) {
            case "GET" -> {
                if (pathParts.length == 3) {
                    return Endpoint.GET_TASK_ID;
                }
                if (pathParts.length == 2) {
                    return Endpoint.GET_TASKS;
                }
                if (pathParts.length == 4) {
                    return Endpoint.GET_EPIC_ID_SUBTASKS;
                }
            }
            case "POST" -> {
                if (pathParts.length == 2) {
                    return Endpoint.POST_TASK;
                }
                if (pathParts.length == 3) {
                    return Endpoint.POST_UPDATE_TASK;
                }
            }
            case "DELETE" -> {
                return Endpoint.DELETE_TASK;
            }
        }
        return Endpoint.UNKNOWN;
    }

    public String searchId(String path) {
            String id = path.replaceAll(".*\\{([^}]+)\\}.*", "$1");
            if (id.matches("\\d+")) {
                return id;
            }
            return null;
        }
    }




