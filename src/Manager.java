import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Manager {
    protected int ID = 1;
    protected HashMap<Integer, Task> tasks;
    protected HashMap<Integer, Subtask> subtasks;
    protected HashMap<Integer, Epic> epics;

    Manager() {
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
    }

    //creating tasks
    void createNewTask(Task task) {
        if (task == null) {
            return;
        }
        task.setID(ID);
        tasks.put(ID, task);
        ID++;
    }

    void createNewEpic(Epic epic) {
        if (epic == null) {
            return;
        }
        epic.setID(ID);
        epics.put(ID, epic);
        ID++;
    }

    void createNewSubTask(Subtask subtask) {
        if (subtask == null) {
            return;
        }
        subtask.setID(ID);
        if (epics.containsKey(subtask.getEpicID())) {
            subtasks.put(ID, subtask);
            epics.get(subtask.getEpicID()).addNewSubTask(subtask);
            ID++;
        }
        else System.out.println("Такого эпика нет");
    }

    //getting a list of tasks by type
    ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }
    ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }
    ArrayList<Subtask> getAllSubTasks() {
        return new ArrayList<>(subtasks.values());
    }

    //removing all tasks by type

    void removeAllTasks(){
        tasks = new HashMap<>();
    }
    void removeAllEpics(){
        epics = new HashMap<>();
    }

    void removeAllSubtasks(){
        for (Epic epic : epics.values()) {
            epic.removeSubtasks();
        }
        subtasks = new HashMap<>();
    }

    void removeAllSubtasksOfEpic(int epicID){
        if (epics.containsKey(epicID)) {
            ArrayList<Subtask> subtasksToDelete = epics.get(epicID).getSubtasks();
            for (Subtask subtask : subtasksToDelete) {
                    if (subtasks.containsValue(subtask)) {
                    subtasks.remove(subtask.getID());
                }
            }
            epics.get(epicID).removeSubtasks();
        }
    }

    void updateTask(Task task) {

    }
}
