package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import manager.adapter.DurationAdapter;
import manager.adapter.LocalDateTimeAdapter;
import manager.file.FileBackedTasksManager;
import manager.history.HistoryManager;
import manager.history.InMemoryHistoryManager;
import manager.http.HttpTaskManager;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class Managers {

    public static TaskManager getDefault(int port) {
        return new HttpTaskManager(port);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefaultFile(File file) {
        return new FileBackedTasksManager(file);
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class , new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .setPrettyPrinting();
        return gsonBuilder.create();
    }
}
