package manager;

import task.*;

import javax.print.DocFlavor;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected int id = 1;
    protected HashMap<Integer, Task> tasks;
    protected HashMap<Integer, Subtask> subtasks;
    protected HashMap<Integer, Epic> epics;
    protected final HistoryManager historyManager;
    protected TreeSet<Task> tasksAndSubtasks;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
        tasksAndSubtasks = new TreeSet<>(Comparator.comparing(Task::getStartTime,
                Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Task::getId));
    }

    //creating tasks
    @Override
    public void createNewTask(Task task) {
        if (task == null || !isValid(task)) {
            return;
        }
        task.setId(id);
        tasks.put(id, task);
        id++;
        tasksAndSubtasks.add(task);
    }

    @Override
    public void createNewEpic(Epic epic) {
        if (epic == null) {
            return;
        }
        epic.setId(id);
        epics.put(id, epic);
        id++;
    }

    @Override
    public void createNewSubtask(Subtask subtask) {
        if (subtask == null || !isValid(subtask)) {
            return;
        }
        subtask.setId(id);
        Epic epicToUpdate = epics.getOrDefault(subtask.getEpicId(), null);
        if (epicToUpdate == null) return;
        subtasks.put(id, subtask);
        epicToUpdate.addNewSubTask(subtask);
        tasksAndSubtasks.add(subtask);
        id++;
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
        for (Integer id : tasks.keySet()) {
            historyManager.remove(id);
        }
        for (Task task : tasks.values()) {
            tasksAndSubtasks.remove(task);
        }
        tasks = new HashMap<>();
    }

    @Override
    public void removeAllEpics() {
        for (Integer id : epics.keySet()) {
            removeAllSubtasksOfEpic(id);
            historyManager.remove(id);
        }

        epics = new HashMap<>();
    }

    @Override
    public void removeAllSubtasks() {
        for (Integer id : epics.keySet()) {
            removeAllSubtasksOfEpic(id);
        }
        for (Subtask subtask : subtasks.values()) {
            tasksAndSubtasks.remove(subtask);
        }
        subtasks = new HashMap<>();
    }

    void removeAllSubtasksOfEpic(int epicID) {
        Epic epicToUpdate = epics.getOrDefault(epicID, null);
        if (epicToUpdate == null) return;
        ArrayList<Subtask> subtasksToDelete = epicToUpdate.getSubtasks();
        for (Subtask subtask : subtasksToDelete) {
            subtasks.remove(subtask.getId());
            tasksAndSubtasks.remove(subtask);
        }
        epicToUpdate.removeAllSubtasks(historyManager);
    }

    //update of task
    @Override
    public void updateTask(Task task) {
        if (task == null) return;
        if (tasks.containsKey(task.getId())) {
            Task oldTask = tasks.get(task.getId());
            tasksAndSubtasks.remove(oldTask);
            if (isValid(task)) {
                tasks.put(task.getId(), task);
                tasksAndSubtasks.add(task);
            }
            else {
                tasksAndSubtasks.add(oldTask);
            }
        }
    }

    @Override
    public void updateEpic(Epic epic) { //нельзя таким образом менять подзадачи эпика, для этого есть дргуие методы
        if (epic == null) return;
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask == null) return;
        if (subtasks.containsKey(subtask.getId())) {
            Epic epicToUpdate = epics.getOrDefault(subtask.getEpicId(), null);
            if (epicToUpdate == null) return;
            Subtask oldSubtask = subtasks.get(subtask.getId());
            tasksAndSubtasks.remove(oldSubtask);
            if (isValid(subtask)) {
                subtasks.put(subtask.getId(), subtask);
                epicToUpdate.updateSubtaskInEpic(subtask);
                epics.put(epicToUpdate.getId(), epicToUpdate);
                tasksAndSubtasks.add(subtask);
            }
            else {
                tasksAndSubtasks.add(oldSubtask);
            }
        }
    }

    //remove task(any type of task) by id
    @Override
    public void removeTask(int id) {
        if (tasks.containsKey(id)) {
            historyManager.remove(id);
            Task task = tasks.get(id);
            tasks.remove(task.getId());
            tasksAndSubtasks.remove(task);
        }
    }

    @Override
    public void removeEpic(int id) {
        if (epics.containsKey(id)) {
            historyManager.remove(id);
            removeAllSubtasksOfEpic(id);
            epics.remove(id);
        }
    }

    @Override
    public void removeSubtask(int id) {
        if (subtasks.containsKey(id)) {
            historyManager.remove(id);
            Subtask subtask = subtasks.get(id);
            subtasks.remove(id);
            Epic epicToUpdate = epics.get(subtask.getEpicId());
            epicToUpdate.removeSubtaskInEpic(subtask);
            epics.put(epicToUpdate.getId(), epicToUpdate);
            tasksAndSubtasks.remove(subtask);
        }
    }

    //get task by id
    @Override
    public Task getTask(int id) {
        Task task = tasks.getOrDefault(id, null);
        if (task != null) historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.getOrDefault(id, null);
        if (epic != null) historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) historyManager.add(subtask);
        return subtask;
    }

    // getters of id
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
    public ArrayList<Subtask> getAllSubtasksOfEpic(int epicId) {
        Epic epic = epics.getOrDefault(epicId, null);
        if (epic == null) return null;
        return new ArrayList<>(epic.getSubtasks());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return List.copyOf(tasksAndSubtasks);
    }

    public void setSubtaskToEpic(Subtask subtask, int epicId) {
        Epic epic = epics.getOrDefault(epicId, null);
        if (epic != null) epic.addNewSubTask(subtask);
    }

    private boolean isValid(Task validTask) {
        if (validTask.getStartTime() == null || validTask.getDuration() == null) return true;
        for (Task task : tasksAndSubtasks) {
            if (task.getStartTime() == null || task.getDuration() == null) continue;
            if (task.getStartTime().isBefore(validTask.getEndTime()) &&
                    task.getEndTime().isAfter(validTask.getEndTime())) return false;
            if (task.getStartTime().isBefore(validTask.getStartTime()) &&
                    task.getEndTime().isAfter(validTask.getStartTime())) return false;
        }
        return true;
    }

    public List<Task> getAllTaskOfAnyType() {
        List<Task> taskList = getAllTasks();
        taskList.addAll(getAllEpics());
        taskList.addAll(getAllSubTasks());
        return taskList;
    }

    public Epic getEpicWithoutUpdatingHistory(int epicId) {
        return epics.get(epicId);
    }

    public int size() {
        return tasks.size() + epics.size() + subtasks.size();
    }

}
//необходхимодобавить валидацию и захуярить тесты