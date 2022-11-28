package manager;

import task.Task;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    LinkedList <Task> tasksHistory;

    InMemoryHistoryManager() {
        tasksHistory = new LinkedList<>();
    }

    @Override
    public void add(Task task) {
        if (tasksHistory.size() < 10) {
            tasksHistory.addLast(task);
        }
        else {
            tasksHistory.removeFirst();
            tasksHistory.addLast(task);
        }
    }

    @Override
    public List<Task> getHistory() {
         return tasksHistory;
    }

}
