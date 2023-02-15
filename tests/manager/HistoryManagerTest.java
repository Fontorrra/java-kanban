package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    HistoryManager historyManager;
    Task task1;
    Epic epic;
    Task task2;

    @BeforeEach
    public void beforeEach() {
        historyManager = new InMemoryHistoryManager();
        task1 = new Task(1, "title", "desc");
        epic = new Epic(2, "123", "456");
        task2 = new Task(3, "qqq", "www");
    }

    @Test
    void add() {
        assertEquals(historyManager.getHistory(), Collections.emptyList());
        historyManager.add(task1);
        historyManager.add(task1);
        List<Task> history = historyManager.getHistory();
        assertEquals(history, List.of(task1));
        historyManager.add(epic);
        historyManager.add(task2);
        history = historyManager.getHistory();
        assertEquals(history, List.of(task1, epic, task2));
        historyManager.add(task1);
        history = historyManager.getHistory();
        assertEquals(history, List.of(epic, task2, task1));
    }

    @Test
    void remove() {
        historyManager.remove(1);
        historyManager.add(task1);
        historyManager.remove(1);
        assertEquals(historyManager.getHistory(), Collections.emptyList());
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(3);
        assertEquals(historyManager.getHistory(), List.of(task1));
        historyManager.add(task2);
        historyManager.remove(1);
        assertEquals(historyManager.getHistory(), List.of(task2));
        historyManager.add(task1);
        historyManager.add(epic);
        historyManager.add(task2);
        historyManager.remove(2);
        assertEquals(historyManager.getHistory(), List.of(task1, task2));
        historyManager.add(epic);
        historyManager.remove(2);
        assertEquals(historyManager.getHistory(), List.of(task1, task2));
    }
}