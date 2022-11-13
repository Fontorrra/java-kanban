import java.util.ArrayList;
import java.util.SplittableRandom;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        manager.createNewTask(new Task(0, "first task", "description of first task"));
        manager.createNewTask(new Task(0, "second task", "description of second task"));
        manager.createNewEpic(new Epic(0, "first epic", "description of first epic"));
        manager.createNewEpic(new Epic(0, "second epic", "description of second epic"));
        manager.printAllTasks();
        manager.printAllEpics();
    }
}
