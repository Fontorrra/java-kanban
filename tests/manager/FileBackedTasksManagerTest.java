package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest {
    File file1;
    File emptyFile;
    File fileToSave;
    FileBackedTasksManager manager1;
    FileBackedTasksManager managerFromFile;

    @Override
    @BeforeEach
    public void beforeEach() {
        super.beforeEach();
        file1 = new File("testInfo.txt");
        emptyFile = new File("emptyFile.txt");
        fileToSave = new File("info.txt");
        manager1 = new FileBackedTasksManager(fileToSave);
        managerFromFile = FileBackedTasksManager.loadFromFile(file1, fileToSave);
    }

    @Test
    void fileLoadForEmptyFile() {
        FileBackedTasksManager newManager = FileBackedTasksManager.loadFromFile(emptyFile, fileToSave);
        assertEquals(newManager.size() , 0);
    }

    @Test
    void fileLoadSavingHistoryTest() {
        assertEquals(managerFromFile.getHistory().size(), 4);
    }

    @Test
    void fileLoadSavingSubtasksForEpic() {
        assertEquals(managerFromFile.getEpic(3).getSubtasks().size() , 2);
    }

    @Test
    void fileLoadSavingEpicIdForSubtask() {
        assertEquals(managerFromFile.getSubtask(6).getEpicId(), 3);
    }

    @Test
    void fileLoadSavingNumberOfElements() {
        assertEquals(managerFromFile.size(), 10);
    }
}