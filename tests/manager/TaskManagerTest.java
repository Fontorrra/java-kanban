package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {

    TaskManager voidManager;
    TaskManager manager;
    File file;


    @BeforeEach
    public void beforeEach() {
        file = new File("info.txt");
        voidManager = new InMemoryTaskManager();
        manager = (FileBackedTasksManager)
                Managers.getDefaultFile(file);
        manager.createNewTask(new Task(0, "first task", "description of first task")); // 1
        manager.createNewTask(new Task(0, "second task", "description of second task")); // 2
        manager.createNewEpic(new Epic(0, "first epic", "description of first epic")); // 3
        manager.createNewEpic(new Epic(0, "second epic", "description of second epic")); // 4
        ArrayList<Epic> epics = manager.getAllEpics();
        manager.createNewSubtask(new Subtask(0, "first subtask",
                "subtask of first epic", epics.get(0).getId())); // 5
        manager.createNewSubtask(new Subtask(0, "second subtask",
                "another subtask of first epic", epics.get(0).getId())); // 6
        manager.createNewSubtask(new Subtask(0, "third subtask",
                "subtask of second epic", epics.get(1).getId())); // 7
    }

    @Test
    void removeAllTasksTest() {
        manager.removeAllTasks();
        assertTrue(manager.getAllTasks().isEmpty());
        voidManager.removeAllTasks();
        assertTrue(voidManager.getAllTasks().isEmpty());
    }

    @Test
    void removeAllEpicsTest() {
        manager.removeAllEpics();
        assertTrue(manager.getAllEpics().isEmpty());
        voidManager.removeAllEpics();
        assertTrue(voidManager.getAllEpics().isEmpty());
    }

    @Test
    void removeAllSubtasksTest() {
        manager.removeAllSubtasks();
        assertTrue(manager.getAllSubTasks().isEmpty());
        voidManager.removeAllSubtasks();
        assertTrue(voidManager.getAllSubTasks().isEmpty());
    }

    @Test
    void updateTaskTest() {
        Task task = new Task(1, "new title", "new description");
        manager.updateTask(task);
        assertEquals(manager.getTask(1), task);
        voidManager.updateTask(task); //noexcept
        Task task1 = new Task(100, "title", "desc");
        manager.updateTask(task1); //noexcept
        manager.updateTask(null);
    }

    @Test
    void updateEpicTest() {
        Epic epic = new Epic(3, "new title", "new description");
        manager.updateEpic(epic);
        assertEquals(manager.getEpic(3), epic);
        voidManager.updateEpic(epic); //noexcept
        assertNull(voidManager.getEpic(epic.getId()));
        Epic epic1 = new Epic(100, "title", "desc");
        manager.updateEpic(epic1); //noexcept
        assertNull(manager.getEpic(epic1.getId()));
        manager.updateEpic(null);
    }

    @Test
    void updateSubtaskTest() {
        Subtask subtask = new Subtask(5, "new title", "new description", 3);
        manager.updateSubtask(subtask);
        assertEquals(manager.getSubtask(5), subtask);
        voidManager.updateSubtask(subtask); //noexcept
        assertNull(voidManager.getSubtask(subtask.getId()));
        Subtask subtask1 = new Subtask(100, "title", "desc", 3);
        manager.updateSubtask(subtask1); //noexcept
        assertNull(manager.getSubtask(subtask1.getId()));
        manager.updateSubtask(null);
    }

    @Test
    void removeTaskTest() {
        manager.removeTask(1);
        assertNull(manager.getTask(1));
        voidManager.removeTask(1); //noexcept
        manager.removeTask(100); //noexcept
    }

    @Test
    void removeEpicTest() {
        manager.removeEpic(3);
        assertNull(manager.getEpic(3));
        voidManager.removeEpic(1); //noexcept
        manager.removeEpic(100); //noexcept
    }

    @Test
    void removeSubtaskTest() {
        manager.removeSubtask(5);
        assertNull(manager.getSubtask(5));
        voidManager.removeSubtask(1);
        manager.removeSubtask(100);
    }

    @Test
    void getTaskTest() {
        Task task = manager.getTask(1);
        assertEquals(task, new Task(1, "first task", "description of first task"));
        assertNull(voidManager.getTask(1));
        assertNull(manager.getTask(100));
    }

    @Test
    void getEpicTest() {
        Epic epic = manager.getEpic(3);
        Epic epic1 = new Epic(3, "first epic", "description of first epic");
        epic1.addNewSubTask(new Subtask(5, "first subtask",
                "subtask of first epic", 3));
        epic1.addNewSubTask(new Subtask(6, "second subtask",
                "another subtask of first epic", 3));
        assertEquals(epic, epic1);
        assertNull(voidManager.getEpic(1));
        assertNull(manager.getEpic(100));
    }

    @Test
    void getSubtaskTest() {
        Subtask subtask = manager.getSubtask(5);
        assertEquals(subtask, new Subtask(5, "first subtask",
                "subtask of first epic", 3));
        assertNull(voidManager.getSubtask(1));
        assertNull(manager.getSubtask(100));
    }

    @Test
    void getAllSubtasksOfEpicTest() {
        List<Subtask> subtasks = manager.getAllSubtasksOfEpic(3);
        List<Subtask> subtaskList = List.of(
                new Subtask(5, "first subtask", "subtask of first epic", 3),
                new Subtask(6, "second subtask", "another subtask of first epic", 3));
        assertEquals(subtaskList, subtasks);
    }

    @Test
    void getHistoryTest() {
         assertEquals(manager.getHistory(), Collections.emptyList());
         Task task = manager.getTask(1);
         Epic epic = manager.getEpic(3);
         Subtask subtask = manager.getSubtask(5);
         List<Task> tasks1 = List.of(task, epic, subtask);
         assertEquals(tasks1, manager.getHistory());
         task = manager.getTask(1);
         assertEquals(manager.getHistory(), List.of(epic, subtask, task));
         manager.removeTask(1);
        assertEquals(manager.getHistory(), List.of(epic, subtask));
         assertEquals(voidManager.getHistory(), Collections.emptyList());
    }

    @Test
    void validationTests() {
        manager.updateTask(new Task(1, "first task", "description of first task",
                Duration.ofHours(1), LocalDateTime.of(2023, 1, 1, 10, 0)));
        //not valid
        manager.createNewTask(new Task(8, "first task", "description of first task",
                Duration.ofHours(1), LocalDateTime.of(2023, 1, 1, 10, 20)));
        assertNull(manager.getTask(8));
        //valid
        manager.createNewTask(new Task(8, "first task", "description of first task",
                Duration.ofHours(1), LocalDateTime.of(2023, 1, 1, 11, 20)));
        assertNotNull(manager.getTask(8));
    }

    @Test
    void getEndTimeTests() {
        assertNull(manager.getTask(1).getEndTime());
        manager.updateTask(new Task(1, "first task", "description of first task",
                Duration.ofHours(1), LocalDateTime.of(2023, 1, 1, 10, 0)));
        assertEquals(manager.getTask(1).getEndTime(),
                LocalDateTime.of(2023, 1, 1, 11, 0));
        assertNull(manager.getEpic(3).getEndTime());
        manager.updateSubtask(new Subtask(5, "first subtask",
                "subtask of first epic", 3,
                Duration.ofHours(1), LocalDateTime.of(2023, 1, 1, 10, 0)));
        assertEquals(manager.getEpic(3).getEndTime(), LocalDateTime.of(2023, 1, 1, 11, 0));
        assertEquals(manager.getEpic(3).getStartTime(), LocalDateTime.of(2023, 1, 1, 10, 0));
        assertEquals(manager.getEpic(3).getDuration(), Duration.ofHours(1));
        manager.updateSubtask(new Subtask(6, "first subtask",
                "subtask of first epic", 3,
                Duration.ofHours(1), LocalDateTime.of(2023, 1, 1, 12, 0)));
        assertEquals(manager.getEpic(3).getEndTime(), LocalDateTime.of(2023, 1, 1, 13, 0));
        assertEquals(manager.getEpic(3).getStartTime(), LocalDateTime.of(2023, 1, 1, 10, 0));
        assertEquals(manager.getEpic(3).getDuration(), Duration.ofHours(2));
    }

    @Test
    void prioritizedTasksTest() {
        manager.removeSubtask(7);
        assertEquals(manager.getPrioritizedTasks().size(), 4);
        manager.updateTask(new Task(1, "first task", "description of first task",
                Duration.ofHours(1), LocalDateTime.of(2023, 1, 1, 10, 0)));
        assertEquals(manager.getPrioritizedTasks().size(), 4);
        manager.updateSubtask(new Subtask(5, "first subtask",
                "subtask of first epic", 3,
                Duration.ofHours(2), LocalDateTime.of(2023, 1, 1, 6, 0)));
        assertEquals(manager.getPrioritizedTasks().size(), 4);
        manager.updateSubtask(new Subtask(6, "second subtask",
                "another subtask of first epic", 3,
                Duration.ofHours(1), LocalDateTime.of(2023, 1, 2, 12, 0)));
        //assertEquals(manager.getPrioritizedTasks().size(), 4);
        assertEquals(manager.getPrioritizedTasks(), List.of(manager.getSubtask(5), manager.getTask(1),
                manager.getSubtask(6), manager.getTask(2)));
        assertEquals(voidManager.getPrioritizedTasks(), Collections.emptyList());
    }
}