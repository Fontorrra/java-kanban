package manager.http;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;
import task.*;

import javax.swing.text.NumberFormatter;
import javax.swing.text.html.Option;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    HttpServer httpServer;
    TaskManager manager;
    private final Gson gson = Managers.getGson();

    public HttpTaskServer(int port) throws IOException, InterruptedException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/task", this::task);
        httpServer.createContext("/tasks/subtask", this::subtask);
        httpServer.createContext("/tasks/epic", this::epic);
        httpServer.createContext("/tasks", this::tasks);
        httpServer.start();
        manager = Managers.getDefault(port);
    }

    public void stop() {
        httpServer.stop(0);
    }

    private void tasks(HttpExchange exchange) throws IOException {

       Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(),
                exchange.getRequestMethod(),
                exchange.getRequestURI().getQuery());

       switch (endpoint) {
           case GET_HISTORY:
               writeResponse(exchange, gson.toJson(manager.getHistory()), 200);
               break;

           case GET_PRIORITIZED_TASKS:
               writeResponse(exchange, gson.toJson(manager.getPrioritizedTasks()), 200);
               break;
           default:
               writeResponse(exchange, "Некорректный запрос", 404);
       }
    }

    private void task(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(),
                exchange.getRequestMethod(),
                exchange.getRequestURI().getQuery());

        int taskId = 0;

        if (endpoint.equals(Endpoint.DELETE_TASK) ||
            endpoint.equals(Endpoint.GET_TASK)) {
            Optional<Integer> taskIdOpt = getId(exchange);
            if (taskIdOpt.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор задачи", 400);
                return;
            }
            taskId = taskIdOpt.get();
        }

        switch (endpoint) {

            case GET_TASK:
                Task task = manager.getTask(taskId);
                if (task == null) {
                    writeResponse(exchange, "Задача с идентификатором " + taskId + " не найдена", 404);
                    break;
                }
                writeResponse(exchange, gson.toJson(task), 200);
                break;

            case DELETE_TASK:
                boolean isDeleted = manager.removeTask(taskId);
                if (isDeleted) writeResponse(exchange, "Задача успешно удалена", 200);
                else writeResponse(exchange, "Задача с идентификатором " + taskId + " не найдена", 404);
                break;

            case POST_TASK:
                Task newTask = gson.fromJson(new String(exchange.getRequestBody().readAllBytes()), Task.class);
                task = manager.getTask(newTask.getId());

                if (!newTask.getTaskType().equals(TaskType.TASK)) {
                    writeResponse(exchange, "Некорректный запрос", 400);
                    break;
                }

                if (task == null) {
                    manager.createNewTask(newTask);
                    writeResponse(exchange, "Задача успешно создана", 200);
                    break;
                }

                else {
                    manager.updateTask(newTask);
                    writeResponse(exchange, "Задача успешно обновлена", 200);
                }
                break;

            case GET_TASKS:
                List<Task> tasks = manager.getAllTasks();
                writeResponse(exchange, gson.toJson(tasks), 200);
                break;

            case DELETE_ALL_TASKS:
                manager.removeAllTasks();
                writeResponse(exchange, "Задачи успешно удалены", 200);
                break;

            default:
                writeResponse(exchange, "Некорректный запрос", 400);
                break;
        }

    }

    private void subtask(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(),
                exchange.getRequestMethod(),
                exchange.getRequestURI().getQuery());

        int subtaskId = 0;

        if (endpoint.equals(Endpoint.DELETE_SUBTASK) ||
            endpoint.equals(Endpoint.GET_SUBTASK) ||
            endpoint.equals(Endpoint.GET_EPIC_SUBTASKS)) {
            Optional<Integer> subtaskIdOpt = getId(exchange);
            if (subtaskIdOpt.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор подзадачи", 400);
                return;
            }
            subtaskId = subtaskIdOpt.get();
        }

        switch (endpoint) {
            case GET_SUBTASK:
                Subtask subtask = manager.getSubtask(subtaskId);
                if (subtask == null) {
                    writeResponse(exchange, "Подзадача с идентификатором " + subtaskId + " не найдена", 404);
                    break;
                }
                writeResponse(exchange, gson.toJson(subtask), 200);
                break;
            case DELETE_SUBTASK:
                boolean isDeleted = manager.removeSubtask(subtaskId);
                if (isDeleted) writeResponse(exchange, "Подадача успешно удалена", 200);
                else writeResponse(exchange, "Подзадача с идентификатором " + subtaskId + " не найдена", 404);
                break;

            case POST_SUBTASK:
                Subtask newSubtask = gson.fromJson(new String(exchange.getRequestBody().readAllBytes()), Subtask.class);

                if (!newSubtask.getTaskType().equals(TaskType.SUBTASK)) {
                    writeResponse(exchange, "Некорректный запрос", 400);
                    break;
                }
                subtask = manager.getSubtask(newSubtask.getId());
                if (subtask == null) {
                    manager.createNewSubtask(newSubtask);
                    writeResponse(exchange, "Подзадача успешно создана", 200);
                    break;
                }
                else {
                    manager.updateSubtask(newSubtask);
                    writeResponse(exchange, "Подзадача успешно обновлена", 200);
                }
                break;

            case GET_SUBTASKS:
                List<Subtask> subtasks = manager.getAllSubTasks();
                writeResponse(exchange, gson.toJson(subtasks), 200);
                break;

            case DELETE_ALL_SUBTASKS:
                manager.removeAllSubtasks();
                writeResponse(exchange, "Подзадачи успешно удалены", 200);
                break;

            case GET_EPIC_SUBTASKS:
                subtasks = manager.getAllSubtasksOfEpic(subtaskId);
                writeResponse(exchange, gson.toJson(subtasks), 200);
                break;
            default:
                writeResponse(exchange, "Некорректный запрос", 404);
                break;
    }
}

    private void epic(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(),
                exchange.getRequestMethod(),
                exchange.getRequestURI().getQuery());

        int epicId = 0;

        if (endpoint.equals(Endpoint.DELETE_EPIC) ||
                endpoint.equals(Endpoint.GET_EPIC)) {
            Optional<Integer> epicIdOpt = getId(exchange);
            if (epicIdOpt.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор эпика", 400);
                return;
            }
            epicId = epicIdOpt.get();
        }

        switch (endpoint) {
            case GET_EPIC:
                Epic epic = manager.getEpic(epicId);
                if (epic == null) {
                    writeResponse(exchange, "Эпик с идентификатором " + epicId + " не найден", 404);
                    break;
                }
                writeResponse(exchange, gson.toJson(epic), 200);
                break;
            case DELETE_EPIC:
                boolean isDeleted = manager.removeEpic(epicId);
                if (isDeleted) writeResponse(exchange, "Эпик успешно удален", 200);
                else writeResponse(exchange, "Эпик с идентификатором " + epicId + " не найден", 404);
                break;

            case POST_EPIC:
                Epic newEpic = gson.fromJson(new String(exchange.getRequestBody().readAllBytes()), Epic.class);
                if (!newEpic.getTaskType().equals(TaskType.EPIC)) {
                    writeResponse(exchange, "Некорректный запрос", 400);
                    break;
                }
                epic = manager.getEpic(newEpic.getId());
                if (epic == null) {
                    manager.createNewEpic(newEpic);
                    writeResponse(exchange, "Эпик успешно создан", 200);
                    break;
                }
                else {
                    manager.updateEpic(newEpic);
                    writeResponse(exchange, "Эпик успешно обновлен", 200);
                }
                break;

            case GET_EPICS:
                List<Epic> epics = manager.getAllEpics();
                writeResponse(exchange, gson.toJson(epics), 200);
                break;

            case DELETE_ALL_EPICS:
                manager.removeAllEpics();
                writeResponse(exchange, "Эпики успешно удалены", 200);
                break;

            default:
                writeResponse(exchange, "Некорректный запрос", 404);
                break;
        }
    }

    private Optional<Integer> getId(HttpExchange exchange) {
        String query = exchange.getRequestURI().getRawQuery();
        query = query.substring("id=".length());
        try {
            int id = Integer.parseInt(query);
            return Optional.of(id);
        } catch (NumberFormatException ex) {
            return Optional.empty();
        }
    }

    private void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        if (responseString.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
        }
        else {
            byte[] bytes = responseString.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod, String query) {

        String[] pathSplitted = requestPath.split("/");

        int length = pathSplitted.length;
        if (pathSplitted[length - 1].isBlank()) length--;
        if (length == 1) {
            return Endpoint.UNKNOWN;
        }

        else if (length == 2) {
            if (pathSplitted[1].equals("tasks") && requestMethod.equals("GET")) return Endpoint.GET_PRIORITIZED_TASKS;
        }

        else {
            String taskType = pathSplitted[2];
            switch (taskType) {
                case "task":
                    if (length == 3 && query == null) {
                        if (requestMethod.equals("GET")) return Endpoint.GET_TASKS;
                        if (requestMethod.equals("DELETE")) return Endpoint.DELETE_ALL_TASKS;
                        if (requestMethod.equals("POST")) return Endpoint.POST_TASK;
                    }
                    if (length == 3 && query != null) {
                        if (query.startsWith("id=")) {
                            if (requestMethod.equals("GET")) return Endpoint.GET_TASK;
                            if (requestMethod.equals("DELETE")) return Endpoint.DELETE_TASK;
                        }
                    }
                    return Endpoint.UNKNOWN;
                case "subtask":
                    if (length == 3 && query == null) {
                        if (requestMethod.equals("GET")) return Endpoint.GET_SUBTASKS;
                        if (requestMethod.equals("DELETE")) return Endpoint.DELETE_ALL_SUBTASKS;
                        if (requestMethod.equals("POST")) return Endpoint.POST_SUBTASK;
                    }
                    if (length == 3 && query != null) {
                        if (query.startsWith("id=")) {
                            if (requestMethod.equals("GET")) return Endpoint.GET_SUBTASK;
                            if (requestMethod.equals("DELETE")) return Endpoint.DELETE_SUBTASK;
                        }
                    }

                    if (length == 4 && requestMethod.equals("GET") && query != null) {
                        if (pathSplitted[3].equals("epic") &&
                            query.startsWith("id=")) return Endpoint.GET_EPIC_SUBTASKS;
                    }
                    return Endpoint.UNKNOWN;
                case "epic":
                    if (length == 3 && query == null) {
                        if (requestMethod.equals("GET")) return Endpoint.GET_EPICS;
                        if (requestMethod.equals("DELETE")) return Endpoint.DELETE_ALL_EPICS;
                        if (requestMethod.equals("POST")) return Endpoint.POST_EPIC;
                    }
                    if (length == 3 && query != null) {
                        if (query.startsWith("id=")) {
                            if (requestMethod.equals("GET")) return Endpoint.GET_EPIC;
                            if (requestMethod.equals("DELETE")) return Endpoint.DELETE_EPIC;
                        }
                    }
                    return Endpoint.UNKNOWN;
                case "history":
                    if (requestMethod.equals("GET")) return Endpoint.GET_HISTORY;
                    return Endpoint.UNKNOWN;
                default:
                    return Endpoint.UNKNOWN;
            }
        }
        return Endpoint.UNKNOWN;
    }

    enum Endpoint {
        GET_TASKS,
        GET_EPICS,
        GET_SUBTASKS,
        GET_TASK,
        GET_SUBTASK,
        GET_EPIC,
        POST_TASK,
        POST_EPIC,
        POST_SUBTASK,
        DELETE_TASK,
        DELETE_SUBTASK,
        DELETE_EPIC,
        DELETE_ALL_TASKS,
        DELETE_ALL_SUBTASKS,
        DELETE_ALL_EPICS,
        GET_EPIC_SUBTASKS,
        GET_HISTORY,
        GET_PRIORITIZED_TASKS,
        UNKNOWN
    }

}
