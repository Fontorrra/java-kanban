package manager;

import task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    List <Task> tasksHistory;

    InMemoryHistoryManager() {
        tasksHistory = new ArrayList<>();
    }

    @Override
    public void add(Task task) {
        if (tasksHistory.size() < 10) {
            tasksHistory.add(task);
        }
        else {
            tasksHistory.remove(0);
            tasksHistory.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
         return tasksHistory;
    }

}
