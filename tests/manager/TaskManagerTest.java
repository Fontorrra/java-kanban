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

abstract class TaskManagerTest {

    TaskManager voidManager;
    TaskManager manager;
    File file;
    Task task1;
    Task task2;
    Epic epic1;
    Epic epic2;
    Subtask subtask1;
    Subtask subtask2;
    Subtask subtask3;

    @BeforeEach
    public void beforeEach() {
        file = new File("info.txt");
        voidManager = new InMemoryTaskManager();
        manager = (FileBackedTasksManager)
                Managers.getDefaultFile(file);
        task1 = new Task(0, "first task", "description of first task");
        task2 = new Task(0, "second task", "description of second task");
        epic1 = new Epic(0, "first epic", "description of first epic");
        epic2 = new Epic(0, "second epic", "description of second epic");

        manager.createNewTask(task1); // 1
        manager.createNewTask(task2); // 2
        manager.createNewEpic(epic1); // 3
        manager.createNewEpic(epic2); // 4
        ArrayList<Epic> epics = manager.getAllEpics();
        subtask1 = new Subtask(0, "first subtask",
                "subtask of first epic", epics.get(0).getId());
        subtask2 = new Subtask(0, "second subtask",
                "another subtask of first epic", epics.get(0).getId());
        subtask3 = new Subtask(0, "third subtask", "subtask of second epic", epics.get(1).getId());
        manager.createNewSubtask(subtask1); // 5
        manager.createNewSubtask(subtask2); // 6
        manager.createNewSubtask(subtask3); // 7
    }

    //разбить большие тесты на тесты поменьше

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
    void updateTaskClassic() {
        Task task = new Task(1, "new title", "new description");
        manager.updateTask(task);
    }

    @Test
    void updateTaskInVoidWithNoExcept() {
        Task task = new Task(1, "new title", "new description");
        voidManager.updateTask(task); //noexcept
    }

    @Test
    void updateTaskThatDoesNotExistWithNoExcept() {
        Task task1 = new Task(100, "title", "desc");
        manager.updateTask(task1);
    }

    @Test
    void updateTaskEqualsNullWithNoExcept() {
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
    void updateEpicInVoidWithNoExceptManager() {
        Task task = new Task(1, "new title", "new description");
        voidManager.updateTask(task); //noexcept
    }

    @Test
    void updateEpicThatDoesNotExistWithNoExcept() {
        Task task1 = new Task(100, "title", "desc");
        manager.updateTask(task1);
    }

    @Test
    void updateEpicEqualsNullWithNoExcept() {
        manager.updateTask(null);
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
    void updateSubtaskInVoidWithNoExceptManager() {
        Subtask subtask = new Subtask(5, "new title", "new description", 3);
        voidManager.updateSubtask(subtask); //noexcept
    }

    @Test
    void updateSubtaskTaskThatDoesNotExistWithNoExcept() {
        Subtask subtask = new Subtask(100, "title", "desc", 3);
        manager.updateTask(subtask);
    }

    @Test
    void updateSubtaskEqualsNullWithNoExcept() {
        manager.updateSubtask(null);
    }

    @Test
    void removeTaskTClassic() {
        manager.removeTask(1);
        assertNull(manager.getTask(1));
    }

    @Test
    void removeTaskInVoidManagerWithNoExcept() {
        voidManager.removeTask(1);
    }

    @Test
    void removeTaskWithIncorrectIdWithNoExcept() {
        manager.removeTask(100);
    }

    @Test
    void removeEpicClassic() {
        manager.removeEpic(3);
        assertNull(manager.getEpic(3));
    }

    @Test
    void removeEpicInVoidManagerWithNoExcept() {
        voidManager.removeEpic(3);
    }

    @Test
    void removeEpicWithIncorrectIdWithNoExcept() {
        manager.removeEpic(100);
    }

    @Test
    void removeSubtaskClassic() {
        manager.removeSubtask(5);
        assertNull(manager.getSubtask(5));
    }

    @Test
    void removeSubtaskInVoidManagerWithNoExcept() {
        voidManager.removeSubtask(1);
    }

    @Test
    void removeSubtaskWithIncorrectIdWithNoExcept() {
        manager.removeSubtask(100);
    }

    @Test
    void getTaskClassic() {
        Task task = manager.getTask(1);
        assertEquals(task, task1);
    }

    @Test
    void getTaskInVoidManager() {
        assertNull(voidManager.getTask(1));
    }

    @Test
    void getTaskWithIncorrectId() {
        assertNull(manager.getTask(100));
    }

    @Test
    void getEpicClassic() {
        Epic epic = manager.getEpic(3);
        assertEquals(epic, epic1);
    }

    @Test
    void getEpicInVoidManager() {
        assertNull(voidManager.getEpic(1));
    }

    @Test
    void getEpicWithIncorrectId() {
        assertNull(manager.getEpic(100));
    }


    @Test
    void getSubtaskTest() {
        Subtask subtask = manager.getSubtask(5);
        assertEquals(subtask, subtask1);
    }

    @Test
    void getSubtaskInVoidManager() {
        assertNull(voidManager.getSubtask(1));
    }

    @Test
    void getSubtaskWithIncorrectId() {
        assertNull(manager.getSubtask(100));
    }


    @Test
    void getAllSubtasksOfEpicClassic() {
        List<Subtask> subtasks = manager.getAllSubtasksOfEpic(3);
        List<Subtask> subtaskList = List.of(subtask1, subtask2);
        assertEquals(subtaskList, subtasks);
    }

    @Test
    void getAllSubtasksOfEpicInVoidManager() {
        assertNull(voidManager.getAllSubtasksOfEpic(3));
    }

    @Test
    void getAllSubtasksOfEpicWithIncorrectId() {
        assertNull(manager.getAllSubtasksOfEpic(100));
    }


    @Test
    void getHistoryTest() {
         Task task = manager.getTask(1);
         Epic epic = manager.getEpic(3);
         Subtask subtask = manager.getSubtask(5);
         List<Task> tasks1 = List.of(task, epic, subtask);
         assertEquals(tasks1, manager.getHistory());
         task = manager.getTask(1);
         assertEquals(manager.getHistory(), List.of(epic, subtask, task));
         manager.removeTask(1);
         assertEquals(manager.getHistory(), List.of(epic, subtask));
    }

    @Test
    void getVoidHistory() {
        assertEquals(manager.getHistory(), Collections.emptyList());
    }

    @Test
    void getHistoryWithRepetition() {
        Task task = manager.getTask(1);
        Epic epic = manager.getEpic(3);
        manager.getTask(1);
        assertEquals(manager.getHistory(), List.of(epic, task));
    }

    @Test
    void getHistoryAfterRemovingElementFromManager() {
        Task task = manager.getTask(1);
        manager.removeTask(1);
        assertEquals(manager.getHistory(), Collections.emptyList());
    }

    @Test
    void getHistoryInVOidManager() {
        assertEquals(voidManager.getHistory(), Collections.emptyList());
    }

    @Test
    void creatingNotValidTaskTest() {
        manager.updateTask(new Task(1, "first task", "description of first task",
                Duration.ofHours(1), LocalDateTime.of(2023, 1, 1, 10, 0)));
        manager.createNewTask(new Task(8, "first task", "description of first task",
                Duration.ofHours(1), LocalDateTime.of(2023, 1, 1, 10, 20)));
        assertNull(manager.getTask(8));
    }

    @Test
    void creatingValidTaskTest() {
        manager.updateTask(new Task(1, "first task", "description of first task",
                Duration.ofHours(1), LocalDateTime.of(2023, 1, 1, 10, 0)));
        manager.createNewTask(new Task(8, "task", "description",
                Duration.ofHours(1), LocalDateTime.of(2023, 1, 1, 11, 20)));
        assertNotNull(manager.getTask(8));
    }

    @Test
    void getEndTimeWithNullDurationAndStartTimeTest() {
        assertNull(manager.getTask(1).getEndTime());
    }

    @Test
    void getEndTimeForTaskClassicTest() {
        manager.updateTask(new Task(1, "first task", "description of first task",
                Duration.ofHours(1), LocalDateTime.of(2023, 1, 1, 10, 0)));
        assertEquals(manager.getTask(1).getEndTime(),
                LocalDateTime.of(2023, 1, 1, 11, 0));
    }

    @Test
    void getEndTimeForSubtaskClassicTest() {
        manager.updateSubtask(new Subtask(5, "first subtask",
                "subtask of first epic", 3,
                Duration.ofHours(1), LocalDateTime.of(2023, 1, 1, 10, 0)));
    }

    @Test
    void getTimeParamsForEpicWithNoSubtaskTest() {
        manager.createNewEpic(new Epic(8, " ", " "));
        assertNull(manager.getEpic(8).getEndTime());
        assertNull(manager.getEpic(8).getStartTime());
        assertNull(manager.getEpic(8).getDuration());
    }

    @Test
    void getTimeParamsForEpicWithAllNullEndTimeInSubtasks() {
        assertNull(manager.getEpic(3).getEndTime());
        assertNull(manager.getEpic(3).getStartTime());
        assertNull(manager.getEpic(3).getDuration());
    }

    @Test
    void getTimeParamsForEpicWithOneSubtask() {
        manager.updateSubtask(new Subtask(7, "third subtask", "subtask of second epic", 4,
                Duration.ofHours(1), LocalDateTime.of(2023, 1, 1, 10, 0)));
        assertEquals(manager.getEpic(4).getEndTime(), LocalDateTime.of(2023, 1, 1, 11, 0));
        assertEquals(manager.getEpic(4).getStartTime(), LocalDateTime.of(2023, 1, 1, 10, 0));
        assertEquals(manager.getEpic(4).getDuration(), Duration.ofHours(1));
    }

    @Test
    void getTimeParamsForEpicWithTwoSubtask() {
        manager.updateSubtask(new Subtask(5, "first subtask",
                "subtask of first epic", 3,
                Duration.ofHours(1), LocalDateTime.of(2023, 1, 1, 10, 0)));
        manager.updateSubtask(new Subtask(6, "first subtask",
                "subtask of first epic", 3,
                Duration.ofHours(1), LocalDateTime.of(2023, 1, 1, 12, 0)));
        assertEquals(manager.getEpic(3).getEndTime(), LocalDateTime.of(2023, 1, 1, 13, 0));
        assertEquals(manager.getEpic(3).getStartTime(), LocalDateTime.of(2023, 1, 1, 10, 0));
        assertEquals(manager.getEpic(3).getDuration(), Duration.ofHours(2));
    }

    @Test
    void getTimeParamsForEpicAfterRemovingSubtask() {
        manager.updateSubtask(new Subtask(5, "first subtask",
                "subtask of first epic", 3,
                Duration.ofHours(1), LocalDateTime.of(2023, 1, 1, 10, 0)));
        manager.updateSubtask(new Subtask(6, "first subtask",
                "subtask of first epic", 3,
                Duration.ofHours(1), LocalDateTime.of(2023, 1, 1, 12, 0)));
        manager.removeSubtask(6);
        assertEquals(manager.getEpic(3).getEndTime(), LocalDateTime.of(2023, 1, 1, 11, 0));
        assertEquals(manager.getEpic(3).getStartTime(), LocalDateTime.of(2023, 1, 1, 10, 0));
        assertEquals(manager.getEpic(3).getDuration(), Duration.ofHours(1));
    }

    @Test
    void getTimeParamsForVoidEpicAfterRemovingSubtask() {
        manager.updateSubtask(new Subtask(7, "third subtask", "subtask of second epic", 4,
                Duration.ofHours(1), LocalDateTime.of(2023, 1, 1, 10, 0)));
        manager.removeSubtask(7);
        assertNull(manager.getEpic(4).getEndTime());
        assertNull(manager.getEpic(4).getStartTime());
        assertNull(manager.getEpic(4).getDuration());
    }

    @Test
    void prioritizedTasksClassicTest() {
        manager.removeSubtask(7);
        manager.updateTask(new Task(1, "first task", "description of first task",
                Duration.ofHours(1), LocalDateTime.of(2023, 1, 1, 10, 0)));
        manager.updateSubtask(new Subtask(5, "first subtask",
                "subtask of first epic", 3,
                Duration.ofHours(2), LocalDateTime.of(2023, 1, 1, 6, 0)));
        manager.updateSubtask(new Subtask(6, "second subtask",
                "another subtask of first epic", 3,
                Duration.ofHours(1), LocalDateTime.of(2023, 1, 2, 12, 0)));
        assertEquals(manager.getPrioritizedTasks(), List.of(manager.getSubtask(5), manager.getTask(1),
                manager.getSubtask(6), manager.getTask(2)));
    }

    @Test
    void prioritizedTasksForVoidManager() {
        assertEquals(voidManager.getPrioritizedTasks(), Collections.emptyList());
    }
}