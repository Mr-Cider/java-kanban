package server;

import alltasks.Task;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.ITaskManager;

import java.io.IOException;
import java.util.Set;

public class PrioritizedHendler extends BaseHttpHandler implements HttpHandler {
    public PrioritizedHendler(ITaskManager manager, Gson gson) throws IOException {
        super(manager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            switch (getEndpoint(path, exchange.getRequestMethod())) {
                case GET_TASKS:
                    handleGetPrioritized(exchange);
                    System.out.println("Посмотреть отсортированные задачи");
                    break;

                default:
                    sendError(exchange, 404, "Эндпоинт не найден");
            }
        } catch (Exception e) {
            sendError(exchange, 500, "Ошибка сервера" + e.getMessage());
        }
    }

    private void handleGetPrioritized(HttpExchange exchange) throws IOException {
        Set<Task> prioritized = manager.getPrioritizedTasks();
        sendText(exchange, 200, gson.toJson(prioritized));
    }
}



