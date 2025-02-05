package server;

import alltasks.Task;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;

public class LocalDataTimeAdapter extends TypeAdapter<LocalDateTime> {
    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        if (value != null) {
            out.value(value.format(Task.getDataTimeFormatter()));
        } else {
            out.nullValue();
        }
    }

    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        String dataTimeString = in.nextString();
        if (dataTimeString != null && !dataTimeString.isEmpty()) {
            return LocalDateTime.parse(dataTimeString, Task.getDataTimeFormatter());
        }
        return null;
    }
}
