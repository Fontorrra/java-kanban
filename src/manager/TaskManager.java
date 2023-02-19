package manager;

import task.*;
import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    //creating tasks
    Task createNewTask(Task task);

    Epic createNewEpic(Epic epic);

    Subtask createNewSubtask(Subtask subtask);

    //getting a list of tasks by type
    ArrayList<Task> getAllTasks();

    ArrayList<Epic> getAllEpics();

    ArrayList<Subtask> getAllSubTasks();

    //removing all tasks by type
    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubtasks();

    //update of task
    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    //remove task(any type of task) by ID
    boolean removeTask(int id);

    boolean removeEpic(int id);

    boolean removeSubtask(int id);

    //get task by ID
    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubtask(int id);
    //

    ArrayList<Subtask> getAllSubtasksOfEpic(int epicId);

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();
}
