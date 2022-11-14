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
        if (epics.containsKey(subtask.getEpicID())) {
            subtasks.put(ID, subtask);
            epics.get(subtask.getEpicID()).addNewSubTask(subtask);
            ID++;
        } else System.out.println("Такого эпика нет");
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
        if (epics.containsKey(epicID)) {
            ArrayList<Subtask> subtasksToDelete = epics.get(epicID).getSubtasks();
            for (Subtask subtask : subtasksToDelete) {
                if (subtasks.containsValue(subtask)) {
                    subtasks.remove(subtask.getID());
                }
            }
            epics.get(epicID).removeAllSubtasks();
        }
    }

    //update task of any type;
    void updateTask(Task task) {
        if (tasks.containsKey(task.getID())) {
            tasks.put(task.getID(), task);
        }
    }

    void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getID())) {
            epics.put(epic.getID(), epic);
        }
    }

    void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getID())) {
            subtasks.put(subtask.getID(), subtask);
            Epic epicToUpdate = epics.get(subtask.getEpicID());
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

    //получение задачи по ID

    Task getTask(int ID) {
        if (tasks.containsKey(ID)) {
            return tasks.get(ID);
        }
        return null;
    }

    Epic getEpic(int ID) {
        if (epics.containsKey(ID)) {
            return epics.get(ID);
        }
        return null;
    }

    Subtask getSubtask(int ID) {
        if (subtasks.containsKey(ID)) {
            return subtasks.get(ID);
        }
        return null;
    }

    // получение всех  ID

    ArrayList<Integer> getAllTasksID() {
        return new ArrayList<Integer>(tasks.keySet());
    }
    ArrayList<Integer> getAllEpicsID() {
        return new ArrayList<Integer>(epics.keySet());
    }
    ArrayList<Integer> getAllSubtasksID() {
        return new ArrayList<Integer>(subtasks.keySet());
    }

}
