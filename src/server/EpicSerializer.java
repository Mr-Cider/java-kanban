package server;

import alltasks.Epic;
import com.google.gson.*;

import java.lang.reflect.Type;

public class EpicSerializer implements JsonSerializer<Epic> {
    @Override
    public JsonElement serialize(Epic epic, Type type, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", epic.getId());
        jsonObject.addProperty("name", epic.getName());
        jsonObject.addProperty("description", epic.getDescription());

        return jsonObject;
    }
}
