package manager;

import manager.http.HttpTaskManager;
import manager.http.HttpTaskServer;
import manager.http.KVTaskClient;
import manager.server.KVServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTests {

    KVServer kvServer;

    HttpTaskManager taskManager1;
    HttpTaskManager taskManager2;
    Task task1;
    Epic epic1;
    Epic epic2;
    Subtask subtask1;
    Subtask subtask2;

    String apiToken1;
    String apiToken2;

    @BeforeEach
    public void beforeEach() {
        try {
            kvServer = new KVServer();
            kvServer.start();
            taskManager1 = (HttpTaskManager) Managers.getDefault(8078);
            apiToken1 = taskManager1.getApiToken();
            task1 = new Task(1, "first task", "description of first task",
                    Duration.ofHours(1), LocalDateTime.of(2023, 2, 15, 16, 50, 10));
            epic1 = new Epic(2, "first epic", "description of first epic");
            epic2 = new Epic(3, "second epic", "description of second epic");

            taskManager1.createNewTask(task1); // 1
            taskManager1.createNewEpic(epic1); // 2
            taskManager1.createNewEpic(epic2); // 3
            subtask1 = new Subtask(0, "first subtask",
                    "subtask of first epic", 2);
            subtask2 = new Subtask(0, "second subtask",
                    "another subtask of first epic", 2,
                    Duration.ofHours(2), LocalDateTime.of(2023, 2, 15, 20, 0));
            taskManager1.createNewSubtask(subtask1); // 4
            taskManager1.createNewSubtask(subtask2); // 5
        } catch (IOException | InterruptedException exception) {
            fail();
        }
    }

    @AfterEach
    public void afterEach() {
        kvServer.stop();
    }

    @Test
    public void loadTest() {
        try {
            taskManager2 = new HttpTaskManager(8078, true);
            System.out.println(taskManager1);
            assertEquals(taskManager1.size(), taskManager2.size());
        }
        catch (IOException | InterruptedException exception) {fail();}
    }
}
