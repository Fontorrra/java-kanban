package manager;

import task.*;
import java.util.ArrayList;
import java.util.List;
public interface TaskManager {
    //creating tasks
    int createNewTask(Task task);

    int createNewEpic(Epic epic);

    int createNewSubtask(Subtask subtask);

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
    void removeTask(int ID);

    void removeEpic(int ID);

    void removeSubtask(int ID);

    //get task by ID
    Task getTask(int ID);

    Epic getEpic(int ID);

    Subtask getSubtask(int ID);
    //

    ArrayList<Subtask> getAllSubtasksOfEpic(int epicID);

    List<Task> getHistory();
}
