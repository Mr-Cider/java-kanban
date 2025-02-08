package server;

import alltasks.Epic;
import alltasks.Subtask;
import alltasks.Task;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import manager.TypeOfTask;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class TaskAdapter extends TypeAdapter<Task> {
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter()) // Регистрируем адаптер
            .registerTypeAdapter(LocalDateTime.class, new LocalDataTimeAdapter())
            .create();

    public void write(JsonWriter out, Task value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        switch (value.getTypeOfTask()) {
            case EPIC -> gson.toJson(value, Epic.class, out);
            case SUBTASK -> gson.toJson(value, Subtask.class, out);
            default -> gson.toJson(value, Task.class, out);
        }
    }

    @Override
    public Task read(JsonReader in) throws IOException {
        JsonObject jsonObject = gson.fromJson(in, JsonObject.class);

        if (!jsonObject.has("typeOfTask")) {
            throw new JsonParseException("Missing required field: typeOfTask");
        }

        String type = jsonObject.get("typeOfTask").getAsString();
        return switch (TypeOfTask.valueOf(type)) {
            case EPIC -> gson.fromJson(jsonObject, Epic.class);
            case SUBTASK -> gson.fromJson(jsonObject, Subtask.class);
            default -> gson.fromJson(jsonObject, Task.class);
        };
    }
}

