package manager;

import com.google.gson.Gson;
import manager.http.HttpTaskServer;
import manager.server.KVServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTests {

    KVServer kvServer;
    HttpTaskServer httpTaskServer;
    HttpRequest request;
    HttpResponse<String> response;
    Task task;
    Epic epic;
    Subtask subtask;
    Gson gson = Managers.getGson();
    HttpClient client;
    int port; //portForHttpServer
    int portForKVServer;

    @BeforeEach
    public void beforeEach() throws IOException, InterruptedException {
        port = 8080;
        portForKVServer = 8078;

        kvServer = new KVServer(portForKVServer);
        kvServer.start();
        httpTaskServer = new HttpTaskServer(portForKVServer, port);
        client = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:" + port + "/tasks/task");
        URI url2 = URI.create("http://localhost:" + port + "/tasks/epic");
        URI url3 = URI.create("http://localhost:" + port + "/tasks/subtask");
        task = new Task(1, "first task", "description of first task",
                Duration.ofHours(1), LocalDateTime.of(2023, 2, 15, 16, 50, 10));
        epic = new Epic(2, "first epic", "description of first epic");
        subtask = new Subtask(3, "first subtask",
                "subtask of first epic", 2);
        epic.addNewSubTask(subtask);
        request = HttpRequest.newBuilder()
                .uri(url1)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() / 100 != 2) fail();
        request = HttpRequest.newBuilder()
                .uri(url2)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() / 100 != 2) fail();
        request = HttpRequest.newBuilder()
                .uri(url3)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask)))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() / 100 != 2) fail();
    }

    @AfterEach
    public void afterEach() {
        kvServer.stop();
        httpTaskServer.stop();
    }

    @Test
    public void getTasksEndpointTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:" + port + "/tasks/task");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode() / 100, 2);
        assertEquals(response.body(), gson.toJson(List.of(task)));
    }

    @Test
    public void getEpicsEndpointTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:" + port + "/tasks/epic");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode() / 100, 2);
        assertEquals(response.body(), gson.toJson(List.of(epic)));
    }

    @Test
    public void getSubtasksEndpointTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:" + port + "/tasks/subtask");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode() / 100, 2);
        assertEquals(response.body(), gson.toJson(List.of(subtask)));
    }

    @Test
    public void getTaskEndpointClassicTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:" + port + "/tasks/task/?id=1");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode() / 100, 2);
        assertEquals(response.body(), gson.toJson(task));
    }

    @Test
    public void getTaskEndpointWithIncorrectIdTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:" + port + "/tasks/task/?id=100");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode() / 100, 4);
        assertEquals(response.body(), "Задача с идентификатором 100 не найдена");
    }

    @Test
    public void getEpicEndpointClassicTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:" + port + "/tasks/epic/?id=2");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode() / 100, 2);
        assertEquals(response.body(), gson.toJson(epic));
    }

    @Test
    public void getEpicEndpointWithIncorrectIdTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:" + port + "/tasks/epic/?id=100");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode() / 100, 4);
        assertEquals(response.body(), "Эпик с идентификатором 100 не найден");
    }

    @Test
    public void getSubtaskEndpointClassicTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:" + port + "/tasks/subtask/?id=3");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode() / 100, 2);
        assertEquals(response.body(), gson.toJson(subtask));
    }

    @Test
    public void getSubtaskEndpointWithIncorrectIdTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:" + port + "/tasks/subtask/?id=100");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode() / 100, 4);
        assertEquals(response.body(), "Подзадача с идентификатором 100 не найдена");
    }

    @Test
    public void postTaskEndpointWhenTaskUpdateTest() throws IOException, InterruptedException {
        Task newTask = new Task(1, "first task update", "description of first task update",
                Duration.ofHours(1), LocalDateTime.of(2023, 2, 15, 16, 50, 10));
        URI url = URI.create("http://localhost:" + port + "/tasks/task");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(newTask)))
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode() / 100, 2);
        assertEquals(response.body(), "Задача успешно обновлена");
        url = URI.create("http://localhost:" + port + "/tasks/task/?id=1");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.body(), gson.toJson(newTask));
    }

    @Test
    public void postTaskEndpointWhenNewTaskTest() throws IOException, InterruptedException {
        Task newTask = new Task(4, "second task", "description of second task");
        URI url = URI.create("http://localhost:" + port + "/tasks/task");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(newTask)))
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode() / 100, 2);
        assertEquals(response.body(), "Задача успешно создана");
        url = URI.create("http://localhost:" + port + "/tasks/task/?id=4");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.body(), gson.toJson(newTask));
    }

    @Test
    public void postSubtaskEndpointWhenSubtaskUpdateTest() throws IOException, InterruptedException {
        Subtask newSubtask = new Subtask(3, "first subtask update",
                "subtask of first epic update", 2);
        URI url = URI.create("http://localhost:" + port + "/tasks/subtask");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(newSubtask)))
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode() / 100, 2);
        assertEquals(response.body(), "Подзадача успешно обновлена");
        url = URI.create("http://localhost:" + port + "/tasks/subtask/?id=3");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.body(), gson.toJson(newSubtask));
    }

    @Test
    public void postSubtaskEndpointWhenNewSubtaskTest() throws IOException, InterruptedException {
        Subtask newSubtask = new Subtask(4, "second subtask",
                "second subtask of first epic", 2);
        URI url = URI.create("http://localhost:" + port + "/tasks/subtask");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(newSubtask)))
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode() / 100, 2);
        assertEquals(response.body(), "Подзадача успешно создана");
        url = URI.create("http://localhost:" + port + "/tasks/subtask/?id=4");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.body(), gson.toJson(newSubtask));
    }

    @Test
    public void postEpicEndpointWhenEpicUpdateTest() throws IOException, InterruptedException {
        Epic newEpic = new Epic(2, "second subtask",
                "second subtask of first epic");
        URI url = URI.create("http://localhost:" + port + "/tasks/epic");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(newEpic)))
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode() / 100, 2);
        assertEquals(response.body(), "Эпик успешно обновлен");
        url = URI.create("http://localhost:" + port + "/tasks/epic/?id=2");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.body(), gson.toJson(newEpic));
    }

    @Test
    public void postEpicEndpointWhenNewEpicTest() throws IOException, InterruptedException {
        Epic newEpic = new Epic(4, "eeppic",
                "desc");
        URI url = URI.create("http://localhost:" + port + "/tasks/epic");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(newEpic)))
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode() / 100, 2);
        assertEquals(response.body(), "Эпик успешно создан");
        url = URI.create("http://localhost:" + port + "/tasks/epic/?id=4");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.body(), gson.toJson(newEpic));
    }

    @Test
    public void deleteTaskEndpointClassicTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:" + port + "/tasks/task/?id=1");
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode() / 100, 2);
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.body(), "Задача с идентификатором 1 не найдена");
    }

    @Test
    public void deleteTaskEndpointWithIncorrectIdTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:" + port + "/tasks/task/?id=100");
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode() / 100, 4);
        assertEquals(response.body(), "Задача с идентификатором 100 не найдена");
    }

    @Test
    public void deleteSubtaskEndpointClassicTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:" + port + "/tasks/subtask/?id=3");
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode() / 100, 2);
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.body(), "Подзадача с идентификатором 3 не найдена");
    }

    @Test
    public void deleteSubtaskEndpointWithIncorrectIdTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:" + port + "/tasks/subtask/?id=100");
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode() / 100, 4);
        assertEquals(response.body(), "Подзадача с идентификатором 100 не найдена");
    }

    @Test
    public void deleteEpicEndpointClassicTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:" + port + "/tasks/epic/?id=2");
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode() / 100, 2);
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.body(), "Эпик с идентификатором 2 не найден");
    }

    @Test
    public void deleteEpicEndpointWithIncorrectIdTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:" + port + "/tasks/epic/?id=100");
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode() / 100, 4);
        assertEquals(response.body(), "Эпик с идентификатором 100 не найден");
    }

    @Test
    public void deleteAllTasksEndpointTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:" + port + "/tasks/task");
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode() / 100, 2);
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.body(), Collections.emptyList().toString());
    }

    @Test
    public void deleteAllSubtasksEndpointTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:" + port + "/tasks/subtask");
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode() / 100, 2);
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.body(), Collections.emptyList().toString());
    }

    @Test
    public void deleteAllEpicsEndpointTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:" + port + "/tasks/epic");
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode() / 100, 2);
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.body(), Collections.emptyList().toString());
    }

    @Test
    public void getSubtasksOfEpicTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:" + port + "/tasks/subtask/epic/?id=2");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode() / 100, 2);
        assertEquals(response.body(), (gson.toJson(List.of(subtask))));
    }

    @Test
    public void getEmptyHistoryTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:" + port + "/tasks/history");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode() / 100, 2);
        assertEquals(response.body(), Collections.emptyList().toString());
    }

    @Test
    public void getHistoryTest() throws IOException, InterruptedException {

        URI url = URI.create("http://localhost:" + port + "/tasks/task/?id=1");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        url = URI.create("http://localhost:" + port + "/tasks/subtask/?id=3");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:" + port + "/tasks/history");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode() / 100, 2);
        assertEquals(response.body(), gson.toJson(List.of(task, subtask)));
    }

    @Test
    public void getPrioritizedTasksTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:" + port + "/tasks");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode() / 100, 2);
        assertEquals(response.body(), gson.toJson(List.of(task, subtask)));
    }
}
