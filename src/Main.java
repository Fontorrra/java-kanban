import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        manager.createNewTask(new Task(0, "first task", "description of first task"));
        manager.createNewTask(new Task(0, "second task", "description of second task"));
        manager.createNewEpic(new Epic(0, "first epic", "description of first epic"));
        manager.createNewEpic(new Epic(0, "second epic", "description of second epic"));
        ArrayList<Epic> epics = manager.getAllEpics();
        manager.createNewSubTask(new Subtask(0, "first sub task",
                        "sub task of first epic", epics.get(0).getID()));
        manager.createNewSubTask(new Subtask(0, "second sub task",
                        "another sub task of first epic", epics.get(0).getID()));
        manager.createNewSubTask(new Subtask(0, "third sub task",
                        "sub task of second epic", epics.get(1).getID()));
        ArrayList<Task> tasks = manager.getAllTasks();
        ArrayList<Subtask> subtasks = manager.getAllSubTasks();
        //manager.printAllTasks();
        //manager.printAllEpics();
    }
}
