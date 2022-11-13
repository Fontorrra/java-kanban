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

    void CreateNewSubTask(SubTask subTask) {
        if (subTask == null) {
            return;
        }
        subTask.setID(ID);
        if (epics.containsKey(subTask.getEpicID())) {
            subTasks.put(ID, subTask);
            epics.get(subTask.getID()).addNewSubTask(subTask);
            ID++;
        }
        else System.out.println("Такого эпика нет");
    }

    void printAllTasks() {
        for (Task task : tasks.values()) {
            System.out.println(task);
        }
    }
    void printAllEpics() {
        for (Epic epic : epics.values()) {
            System.out.println(epic);
        }
    }
    void printAllSubTasks() {
        for (SubTask subtask : subTasks.values()) {
            System.out.println(subtask);
        }
    }
}
