package server;

import alltasks.Epic;
import alltasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import manager.ITaskManager;
import manager.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;


public class HttpTaskServer {
    private static final int PORT = 8080;
    public final Gson gson;
    public final ITaskManager manager;


    public HttpTaskServer() throws IOException {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Epic.class, new EpicSerializer())
                .registerTypeAdapter(Duration.class, new DurationAdapter()) // Регистрируем адаптер
                .registerTypeAdapter(LocalDateTime.class, new LocalDataTimeAdapter())
                .registerTypeAdapter(Task.class, new TaskAdapter())
                .create();
        this.manager = Managers.getDefault();
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer taskServer = new HttpTaskServer();
        taskServer.start(taskServer);
    }

    public void start(HttpTaskServer taskServer) throws IOException {
        ITaskManager manager = Managers.getFileBackedTaskManager();
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHendler(manager, taskServer.gson));
        httpServer.createContext("/epics", new EpicsHendler(manager, taskServer.gson));
        httpServer.createContext("/subtasks", new SubtasksHendler(manager, taskServer.gson));
        httpServer.createContext("/history", new HistoryHendler(manager, taskServer.gson));
        httpServer.createContext("/prioritized", new PrioritizedHendler(manager, taskServer.gson));
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop(HttpTaskServer taskServer) {
        taskServer.stop(taskServer);
    }

    public Gson getGson() {
        return gson;
    }
}
