package manager;

import task.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager{
    private int ID = 1;
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Subtask> subtasks;
    private HashMap<Integer, Epic> epics;
    private final HistoryManager historyManager;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
    }

    //creating tasks
    @Override
    public int createNewTask(Task task) {
        if (task == null) {
            return -1;
        }
        task.setID(ID);
        tasks.put(ID, task);
        ID++;
        return ID - 1;
    }

    @Override
    public int createNewEpic(Epic epic) {
        if (epic == null) {
            return - 1;
        }
        epic.setID(ID);
        epics.put(ID, epic);
        ID++;
        return ID - 1;
    }

    @Override
    public int createNewSubtask(Subtask subtask) {
        if (subtask == null) {
            return -1;
        }
        subtask.setID(ID);
        Epic epicToUpdate = epics.getOrDefault(subtask.getEpicID(), null);
        if (epicToUpdate == null) return -1;
        subtasks.put(ID, subtask);
        epicToUpdate.addNewSubTask(subtask);
        ID++;
        return ID - 1;
    }

    //getting a list of tasks by type
    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubTasks() {
        return new ArrayList<>(subtasks.values());
    }

    //removing all tasks by type
    @Override
    public void removeAllTasks() {
        tasks = new HashMap<>();
    }

    @Override
    public void removeAllEpics() {
        epics = new HashMap<>();
    }

    @Override
    public void removeAllSubtasks() {
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
    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getID())) {
            tasks.put(task.getID(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) { //нельзя таким образом менять подзадачи эпика, для этого есть дргуие методы
        if (epics.containsKey(epic.getID())) {
            epics.put(epic.getID(), epic);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getID())) {
            Epic epicToUpdate = epics.getOrDefault(subtask.getEpicID(), null);
            if (epicToUpdate == null) return;
            subtasks.put(subtask.getID(), subtask);
            epicToUpdate.updateSubtaskInEpic(subtask);
            epics.put(epicToUpdate.getID(), epicToUpdate);
        }
    }

    //remove task(any type of task) by ID
    @Override
    public void removeTask(int ID) {
        if (tasks.containsKey(ID)) {
            Task task = tasks.get(ID);
            tasks.remove(task.getID());
        }
    }

    @Override
    public void removeEpic(int ID) {
        if (epics.containsKey(ID)) {
            removeAllSubtasksOfEpic(ID);
            epics.remove(ID);
        }
    }

    @Override
    public void removeSubtask(int ID) {
        if (subtasks.containsKey(ID)) {
            Subtask subtask = subtasks.get(ID);
            subtasks.remove(ID);
            Epic epicToUpdate = epics.get(subtask.getEpicID());
            epicToUpdate.removeSubtaskInEpic(subtask);
            epics.put(epicToUpdate.getID(), epicToUpdate);
        }
    }

    //get task by ID
    @Override
    public Task getTask(int ID) {
        Task task = tasks.getOrDefault(ID, null);
        if (task != null )historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpic(int ID) {
        Epic epic = epics.getOrDefault(ID, null);
        if (epic != null ) historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtask(int ID) {
        Subtask subtask = subtasks.getOrDefault(ID, null);
        if (subtask != null ) historyManager.add(subtask);
        return subtask;
    }

    // getters of ID
    public ArrayList<Integer> getAllTasksID() {
        return new ArrayList<>(tasks.keySet());
    }
    public ArrayList<Integer> getAllEpicsID() {
        return new ArrayList<>(epics.keySet());
    }
    public ArrayList<Integer> getAllSubtasksID() {
        return new ArrayList<>(subtasks.keySet());
    }

    //get all subtasks of task.Epic
    @Override
    public ArrayList<Subtask> getAllSubtasksOfEpic(int epicID) {
        Epic epic = epics.getOrDefault(epicID, null);
        if (epic == null) return null;
        return new ArrayList<>(epic.getSubtasks());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
