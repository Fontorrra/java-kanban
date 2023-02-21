package manager.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import manager.Managers;
import manager.file.FileBackedTasksManager;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {

    private final KVTaskClient client ;
    private final Gson gson;

    public HttpTaskManager(int port) {
        this(port, false);
    }

    public HttpTaskManager(int port, boolean load) {
        gson = Managers.getGson();
        client = new KVTaskClient(port);
        if (load) load();
    }

    public String getApiToken() {
        return client.getApiToken();
    }

    protected void addTasks(List<? extends Task> tasks) {
        for (Task task : tasks) {
            int taskId = task.getId();
            if (taskId >= this.id) id = taskId + 1;
            TaskType taskType = task.getTaskType();
            switch (taskType) {
                case TASK:
                    this.tasks.put(taskId, task);
                    tasksAndSubtasks.add(task);
                    break;
                case SUBTASK:
                    this.subtasks.put(taskId, (Subtask) task);
                    tasksAndSubtasks.add(task);
                    break;
                case EPIC:
                    this.epics.put(taskId, (Epic) task);
            }
        }
    }

    protected void load() {
        ArrayList<Task> tasks;
        ArrayList<Epic> epics;
        ArrayList<Subtask> subtasks;
        tasks = gson.fromJson(client.load("tasks"), new TypeToken<ArrayList<Task>>() {
        }.getType());
        epics = gson.fromJson(client.load("epics"), new TypeToken<ArrayList<Epic>>() {
        }.getType());
        subtasks = gson.fromJson(client.load("subtasks"), new TypeToken<ArrayList<Subtask>>() {
        }.getType());

        List<Integer> history = gson.fromJson(client.load("history"), new TypeToken<ArrayList<Integer>>() {
        }.getType());

        for (int id : history) {
            historyManager.add(getTaskWithoutType(id));
        }
        addTasks(tasks);
        addTasks(epics);
        addTasks(subtasks);
    }

    @Override
    protected void save() {
        String jsonTasks = gson.toJson(getAllTasks());
        client.put("tasks", jsonTasks);
        String jsonSubtasks = gson.toJson(getAllSubTasks());
        client.put("subtasks", jsonSubtasks);
        String jsonEpics = gson.toJson(getAllEpics());
        client.put("epics", jsonEpics);
        String jsonHistory = gson.toJson(historyManager.getHistory()
                .stream()
                .map(Task::getId)
                .collect(Collectors.toList()));
        client.put("history", jsonHistory);
    }
}
