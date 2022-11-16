import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private int ID = 1;
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Subtask> subtasks;
    private HashMap<Integer, Epic> epics;

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

    void createNewSubtask(Subtask subtask) {
        if (subtask == null) {
            return;
        }
        subtask.setID(ID);
        Epic epicToUpdate = epics.getOrDefault(subtask.getEpicID(), null);
        if (epicToUpdate == null) return;
        subtasks.put(ID, subtask);
        epicToUpdate.addNewSubTask(subtask);
        ID++;
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
    void removeAllTasks() {
        tasks = new HashMap<>();
    }

    void removeAllEpics() {
        epics = new HashMap<>();
    }

    void removeAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.removeAllSubtasks();
        }
        subtasks = new HashMap<>();
    }

    void removeAllSubtasksOfEpic(int epicID) {
        Epic epicToUpdate = epics.getOrDefault(epicID, null);

        if (epicToUpdate == null) return;
        ArrayList<Subtask> subtasksToDelete = epicToUpdate.getSubtasks();
        for (Subtask subtask : subtasksToDelete) {
            subtasks.remove(subtask.getID());
        }
        epicToUpdate.removeAllSubtasks();
    }

    //update of task
    void updateTask(Task task) {
        if (tasks.containsKey(task.getID())) {
            tasks.put(task.getID(), task);
        }
    }

    void updateEpic(Epic epic) { //нельзя таким образом менять подзадачи эпика, для этого есть дргуие методы
        if (epics.containsKey(epic.getID())) {
            epics.put(epic.getID(), epic);
        }
    }

    void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getID())) {
            Epic epicToUpdate = epics.getOrDefault(subtask.getEpicID(), null);
            if (epicToUpdate == null) return;
            subtasks.put(subtask.getID(), subtask);
            epicToUpdate.updateSubtaskInEpic(subtask);
            epics.put(epicToUpdate.getID(), epicToUpdate);
        }
    }

    //remove task(any type of task) by ID
    void removeTask(int ID) {
        if (tasks.containsKey(ID)) {
            Task task = tasks.get(ID);
            tasks.remove(task.getID());
        }
    }

    void removeEpic(int ID) {
        if (epics.containsKey(ID)) {
            removeAllSubtasksOfEpic(ID);
            epics.remove(ID);
        }
    }

    void removeSubtask(int ID) {
        if (subtasks.containsKey(ID)) {
            Subtask subtask = subtasks.get(ID);
            subtasks.remove(ID);
            Epic epicToUpdate = epics.get(subtask.getEpicID());
            epicToUpdate.removeSubtaskInEpic(subtask);
            epics.put(epicToUpdate.getID(), epicToUpdate);
        }
    }

    //get task by ID
    Task getTask(int ID) {
        return tasks.getOrDefault(ID, null);
    }

    Epic getEpic(int ID) {
        return epics.getOrDefault(ID, null);
    }

    Subtask getSubtask(int ID) {
        return subtasks.getOrDefault(ID, null);
    }

    // getters of ID
    ArrayList<Integer> getAllTasksID() {
        return new ArrayList<>(tasks.keySet());
    }
    ArrayList<Integer> getAllEpicsID() {
        return new ArrayList<>(epics.keySet());
    }
    ArrayList<Integer> getAllSubtasksID() {
        return new ArrayList<>(subtasks.keySet());
    }

    //get all subtasks of task.Epic
    ArrayList<Subtask> getAllSubtasksOfEpic(int epicID) {
        Epic epic = epics.getOrDefault(epicID, null);
        if (epic == null) return null;
        return new ArrayList<>(epic.getSubtasks());
    }

}
