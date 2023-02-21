package manager.http;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;
import task.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class HttpTaskServer {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private final int port;
    HttpServer httpServer;
    TaskManager manager;
    private final Gson gson = Managers.getGson();

    public HttpTaskServer(int portForKVClient, int portForHttpServer) {
        try {
            port = portForHttpServer;
            httpServer = HttpServer.create(new InetSocketAddress(port), 0);
            httpServer.createContext("/tasks/task", this::task);
            httpServer.createContext("/tasks/subtask", this::subtask);
            httpServer.createContext("/tasks/epic", this::epic);
            httpServer.createContext("/tasks", this::tasks);
            httpServer.start();
            manager = Managers.getDefault(portForKVClient);
        }
        catch (IOException exception) {
            throw new HttpTaskServerException();
        }
    }

    public void stop() {
        httpServer.stop(0);
    }

    private void tasks(HttpExchange exchange) {

       Endpoint endpoint = Endpoint.getEndpoint(exchange);

       switch (endpoint) {
           case GET_HISTORY:
               writeResponse(exchange, gson.toJson(manager.getHistory()), HttpURLConnection.HTTP_OK);
               break;

           case GET_PRIORITIZED_TASKS:
               writeResponse(exchange, gson.toJson(manager.getPrioritizedTasks()), HttpURLConnection.HTTP_OK);
               break;
           default:
               writeResponse(exchange, "Некорректный запрос", HttpURLConnection.HTTP_NOT_FOUND);
       }
    }

    private void task(HttpExchange exchange) {
        Endpoint endpoint = Endpoint.getEndpoint(exchange);

        int taskId = 0;

        if (endpoint.equals(Endpoint.DELETE_TASK) ||
            endpoint.equals(Endpoint.GET_TASK)) {
            Optional<Integer> taskIdOpt = getId(exchange);
            if (taskIdOpt.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор задачи",
                              HttpURLConnection.HTTP_BAD_REQUEST);
                return;
            }
            taskId = taskIdOpt.get();
        }

        switch (endpoint) {
            case GET_TASK:
                Task task = manager.getTask(taskId);
                if (task == null) {
                    writeResponse(exchange, "Задача с идентификатором " + taskId + " не найдена",
                                  HttpURLConnection.HTTP_NOT_FOUND);
                    break;
                }
                writeResponse(exchange, gson.toJson(task), HttpURLConnection.HTTP_OK);
                break;

            case DELETE_TASK:
                boolean isDeleted = manager.removeTask(taskId);
                if (isDeleted) writeResponse(exchange, "Задача успешно удалена", HttpURLConnection.HTTP_OK);
                else writeResponse(exchange, "Задача с идентификатором " + taskId + " не найдена",
                                   HttpURLConnection.HTTP_NOT_FOUND);
                break;

            case POST_TASK:
                Task newTask;
                try {
                    newTask = gson.fromJson(new String(exchange.getRequestBody().readAllBytes()), Task.class);
                } catch (IOException e) {
                    throw new HttpTaskServerException();
                }
                task = manager.getTask(newTask.getId());

                if (!newTask.getTaskType().equals(TaskType.TASK)) {
                    writeResponse(exchange, "Некорректный запрос", HttpURLConnection.HTTP_BAD_REQUEST);
                    break;
                }

                if (task == null) {
                    manager.createNewTask(newTask);
                    writeResponse(exchange, "Задача успешно создана", HttpURLConnection.HTTP_CREATED);
                    break;
                }

                else {
                    manager.updateTask(newTask);
                    writeResponse(exchange, "Задача успешно обновлена", HttpURLConnection.HTTP_CREATED);
                }
                break;

            case GET_TASKS:
                List<Task> tasks = manager.getAllTasks();
                writeResponse(exchange, gson.toJson(tasks), HttpURLConnection.HTTP_OK);
                break;

            case DELETE_ALL_TASKS:
                manager.removeAllTasks();
                writeResponse(exchange, "Задачи успешно удалены", HttpURLConnection.HTTP_OK);
                break;

            default:
                writeResponse(exchange, "Некорректный запрос", HttpURLConnection.HTTP_NOT_FOUND);
                break;
        }

    }

    private void subtask(HttpExchange exchange) {
        Endpoint endpoint = Endpoint.getEndpoint(exchange);

        int subtaskId = 0;

        if (endpoint.equals(Endpoint.DELETE_SUBTASK) ||
            endpoint.equals(Endpoint.GET_SUBTASK) ||
            endpoint.equals(Endpoint.GET_EPIC_SUBTASKS)) {
            Optional<Integer> subtaskIdOpt = getId(exchange);
            if (subtaskIdOpt.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор подзадачи",
                              HttpURLConnection.HTTP_BAD_REQUEST);
                return;
            }
            subtaskId = subtaskIdOpt.get();
        }

        switch (endpoint) {
            case GET_SUBTASK:
                Subtask subtask = manager.getSubtask(subtaskId);
                if (subtask == null) {
                    writeResponse(exchange, "Подзадача с идентификатором " + subtaskId + " не найдена",
                                  HttpURLConnection.HTTP_NOT_FOUND);
                    break;
                }
                writeResponse(exchange, gson.toJson(subtask), HttpURLConnection.HTTP_OK);
                break;
            case DELETE_SUBTASK:
                boolean isDeleted = manager.removeSubtask(subtaskId);
                if (isDeleted) writeResponse(exchange, "Подадача успешно удалена", HttpURLConnection.HTTP_OK);
                else writeResponse(exchange, "Подзадача с идентификатором " + subtaskId + " не найдена",
                                   HttpURLConnection.HTTP_NOT_FOUND);
                break;

            case POST_SUBTASK:
                Subtask newSubtask;
                try {
                    newSubtask = gson.fromJson(new String(exchange.getRequestBody().readAllBytes()),
                                                       Subtask.class);
                } catch (IOException exception) {
                    throw new HttpTaskServerException();
                }
                if (!newSubtask.getTaskType().equals(TaskType.SUBTASK)) {
                    writeResponse(exchange, "Некорректный запрос", HttpURLConnection.HTTP_BAD_REQUEST);
                    break;
                }
                subtask = manager.getSubtask(newSubtask.getId());
                if (subtask == null) {
                    manager.createNewSubtask(newSubtask);
                    writeResponse(exchange, "Подзадача успешно создана", HttpURLConnection.HTTP_CREATED);
                    break;
                }
                else {
                    manager.updateSubtask(newSubtask);
                    writeResponse(exchange, "Подзадача успешно обновлена", HttpURLConnection.HTTP_CREATED);
                }
                break;

            case GET_SUBTASKS:
                List<Subtask> subtasks = manager.getAllSubTasks();
                writeResponse(exchange, gson.toJson(subtasks), HttpURLConnection.HTTP_OK);
                break;

            case DELETE_ALL_SUBTASKS:
                manager.removeAllSubtasks();
                writeResponse(exchange, "Подзадачи успешно удалены", HttpURLConnection.HTTP_OK);
                break;

            case GET_EPIC_SUBTASKS:
                subtasks = manager.getAllSubtasksOfEpic(subtaskId);
                writeResponse(exchange, gson.toJson(subtasks), HttpURLConnection.HTTP_OK);
                break;
            default:
                writeResponse(exchange, "Некорректный запрос", HttpURLConnection.HTTP_NOT_FOUND);
                break;
    }
}

    private void epic(HttpExchange exchange) {
        Endpoint endpoint = Endpoint.getEndpoint(exchange);

        int epicId = 0;

        if (endpoint.equals(Endpoint.DELETE_EPIC) ||
                endpoint.equals(Endpoint.GET_EPIC)) {
            Optional<Integer> epicIdOpt = getId(exchange);
            if (epicIdOpt.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор эпика",
                              HttpURLConnection.HTTP_BAD_REQUEST);
                return;
            }
            epicId = epicIdOpt.get();
        }

        switch (endpoint) {
            case GET_EPIC:
                Epic epic = manager.getEpic(epicId);
                if (epic == null) {
                    writeResponse(exchange, "Эпик с идентификатором " + epicId + " не найден",
                                  HttpURLConnection.HTTP_NOT_FOUND);
                    break;
                }
                writeResponse(exchange, gson.toJson(epic), HttpURLConnection.HTTP_OK);
                break;
            case DELETE_EPIC:
                boolean isDeleted = manager.removeEpic(epicId);
                if (isDeleted) writeResponse(exchange, "Эпик успешно удален", HttpURLConnection.HTTP_OK);
                else writeResponse(exchange, "Эпик с идентификатором " + epicId + " не найден",
                                   HttpURLConnection.HTTP_NOT_FOUND);
                break;

            case POST_EPIC:
                Epic newEpic;
                try {
                    newEpic = gson.fromJson(new String(exchange.getRequestBody().readAllBytes()), Epic.class);
                } catch (IOException exception) {
                    throw new HttpTaskServerException();
                }
                if (!newEpic.getTaskType().equals(TaskType.EPIC)) {
                    writeResponse(exchange, "Некорректный запрос", HttpURLConnection.HTTP_BAD_REQUEST);
                    break;
                }
                epic = manager.getEpic(newEpic.getId());
                if (epic == null) {
                    manager.createNewEpic(newEpic);
                    writeResponse(exchange, "Эпик успешно создан", HttpURLConnection.HTTP_CREATED);
                    break;
                }
                else {
                    manager.updateEpic(newEpic);
                    writeResponse(exchange, "Эпик успешно обновлен", HttpURLConnection.HTTP_CREATED);
                }
                break;

            case GET_EPICS:
                List<Epic> epics = manager.getAllEpics();
                writeResponse(exchange, gson.toJson(epics), HttpURLConnection.HTTP_OK);
                break;

            case DELETE_ALL_EPICS:
                manager.removeAllEpics();
                writeResponse(exchange, "Эпики успешно удалены", HttpURLConnection.HTTP_OK);
                break;

            default:
                writeResponse(exchange, "Некорректный запрос", HttpURLConnection.HTTP_NOT_FOUND);
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

    private void writeResponse(HttpExchange exchange, String responseString, int responseCode) {
        try {
            if (responseString.isBlank()) {
                exchange.sendResponseHeaders(responseCode, 0);
            } else {
                byte[] bytes = responseString.getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(responseCode, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            }
        } catch (IOException exception) {
            throw new HttpTaskServerException();
        }
    }

}
