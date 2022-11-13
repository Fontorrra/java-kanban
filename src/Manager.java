import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Manager {
    protected int ID = 1;
    protected HashMap<Integer, Task> tasks;
    protected HashMap<Integer, SubTask> subTasks;
    protected HashMap<Integer, Epic> epics;

    Manager() {
        tasks = new HashMap<>();
        subTasks = new HashMap<>();
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

    void createNewSubTask(SubTask subTask) {
        if (subTask == null) {
            return;
        }
        subTask.setID(ID);
        if (epics.containsKey(subTask.getEpicID())) {
            subTasks.put(ID, subTask);
            epics.get(subTask.getEpicID()).addNewSubTask(subTask);
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
    ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
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
            epic.subtasks = new ArrayList<>();
        }
        subTasks = new HashMap<>();
    }

    void removeAllSubtasksOfEpic(int epicID){
        if (epics.containsKey(epicID)) {
            ArrayList<SubTask> subtasksToDelete = epics.get(epicID).subtasks;
            for (SubTask subTask : subtasksToDelete) {
                if (subTasks.containsValue(subTask)) {
                    subTasks.remove(subTask.getID());
                }
            }
            epics.get(epicID).subtasks = new ArrayList<>();
        }
    }
}
