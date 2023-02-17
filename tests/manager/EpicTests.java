package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicTests {

    TaskManager manager;
    Epic epic;
    Subtask subtask1;
    Subtask subtask2;

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryTaskManager();
        epic = new Epic(1, "epic title", "epic description");
        manager.createNewEpic(epic);
        subtask1 = new Subtask(2, "subtask1 title", "subtask1 description", 1);
        subtask2 = new Subtask(3, "subtask2 title", "subtask2 description", 1);
    }

    @Test
    void shouldBeNewWhenNoSubtasks() {
        assertEquals(manager.getAllSubtasksOfEpic(1), Collections.emptyList());
        assertEquals(manager.getEpic(1).getStatus(), Status.NEW);
    }

    @Test
    void shouldBeNewWhenNoSubtasksAfterDeleting() {
        manager.createNewSubtask(subtask1);
        subtask1.setStatus(Status.DONE);
        manager.updateSubtask(subtask1);
        manager.removeSubtask(subtask1.getId());
        assertEquals(manager.getEpic(1).getStatus(), Status.NEW);
    }

    @Test
    void shouldBeNewWhenAllSubtaskAreNew() {
        manager.createNewSubtask(subtask1);
        manager.createNewSubtask(subtask2);
        assertEquals(manager.getEpic(1).getStatus(), Status.NEW);
    }

    @Test
    void shouldBeDoneWhenAllSubtasksAreDone() {
        manager.createNewSubtask(subtask1);
        manager.createNewSubtask(subtask2);
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask2);
        assertEquals(manager.getEpic(1).getStatus(), Status.DONE);
    }

    @Test
    void shouldBeInProgressWhenDoneAndNewSubtasks() {
        manager.createNewSubtask(subtask1);
        manager.createNewSubtask(subtask2);
        subtask1.setStatus(Status.DONE);
        manager.updateSubtask(subtask1);
        assertEquals(manager.getEpic(1).getStatus(), Status.IN_PROGRESS);
    }

    @Test
    void shouldBeInProgressWhenInProgressSubtasks() {
        manager.createNewSubtask(subtask1);
        manager.createNewSubtask(subtask2);
        subtask1.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask2);
        assertEquals(manager.getEpic(1).getStatus(), Status.IN_PROGRESS);
    }

    @Test
    void shouldBeInProgressWhenNotAllNewOrDoneSubtasks() {
        Subtask subtask3 = new Subtask(4, "  ", "  ", 1);
        manager.createNewSubtask(subtask1);
        manager.createNewSubtask(subtask2);
        manager.createNewSubtask(subtask3);
        subtask3.setStatus(Status.DONE);
        subtask2.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subtask3);
        manager.updateSubtask(subtask2);
        assertEquals(manager.getEpic(1).getStatus(), Status.IN_PROGRESS);
    }
}
