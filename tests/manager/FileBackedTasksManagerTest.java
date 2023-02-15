package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest {
    File file;
    File emptyFile;
    File fileToSave;
    FileBackedTasksManager manager;

    @BeforeEach
    public void beforeEach() {
        file = new File("testInfo.txt");
        emptyFile = new File("emptyFile.txt");
        fileToSave = new File("info.txt");
        manager = new FileBackedTasksManager(fileToSave);
    }

    @Test
    void fileLoadTest() {
        FileBackedTasksManager newManager = FileBackedTasksManager.loadFromFile(emptyFile, fileToSave);
        assertEquals(newManager.size() , 0);
        newManager = FileBackedTasksManager.loadFromFile(file, fileToSave);
        assertEquals(newManager.getHistory().size(), 4);
        assertEquals(newManager.getEpic(3).getSubtasks().size() , 2); //сохраняюстся подзадачи
        assertEquals(newManager.getSubtask(6).getEpicId(), 3);

    }

}