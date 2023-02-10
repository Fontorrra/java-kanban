package manager;
import task.*;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private File file;

    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
    }

    private void save() {
        try (Writer writer = new FileWriter(file.getName())) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : tasks.values()) {
                writer.write(toString(task));
                writer.write("\n");
            }
            for (Epic epic : epics.values()) {
                writer.write(toString(epic));
                writer.write("\n");
            }
            for (Subtask subtask : subtasks.values()) {
                writer.write(toString(subtask));
                writer.write("\n");
            }
            writer.write("\n");
            writer.write(historyToString(historyManager));
        }
        catch (IOException exp) {
            throw new ManagerSaveException();
        }
    }

    @Override
    public void createNewTask(Task task) {
        super.createNewTask(task);
        save();
    }

    @Override
    public void createNewEpic(Epic epic) {
        super.createNewEpic(epic);
        save();
    }

    @Override
    public void createNewSubtask(Subtask subtask) {
        super.createNewSubtask(subtask);
        save();
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void removeEpic(int ID) {
        super.removeEpic(ID);
        save();
    }

    @Override
    public void removeSubtask(int ID) {
        super.removeSubtask(ID);
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    void removeAllSubtasksOfEpic(int epicID) {
        super.removeAllSubtasksOfEpic(epicID);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public Task getTask(int ID) {
        Task task = super.getTask(ID);
        save();
        return task;
    }

    @Override
    public Epic getEpic(int ID) {
        Epic epic = super.getEpic(ID);
        save();
        return epic;
    }

    public Task getTaskWithoutType(int ID)   {
        Task task = tasks.getOrDefault(ID, null);
        if (task != null) return getTask(ID);
        task = subtasks.getOrDefault(ID, null);
        if (task != null) return getSubtask(ID);
        task = epics.getOrDefault(ID, null);
        if (task != null) return getEpic(ID);
        return null;
    }

    @Override
    public Subtask getSubtask(int ID) {
        Subtask subtask =  super.getSubtask(ID);
        save();
        return subtask;
    }

    public static String toString(Task task) {
        TaskType taskType = TaskType.TASK;
        String epicID = "None";
        if (task.getClass().equals(Subtask.class)) {
            taskType = TaskType.SUBTASK;
            epicID = Integer.toString(((Subtask) task).getEpicID());
        }
        if (task.getClass().equals(Epic.class)) taskType = TaskType.EPIC;
        return String.format("%d,%s,%s,%s,%s,%s",
                task.getID(),
                taskType.name(),
                task.getTitle(),
                task.getStatus(),
                task.getDescription(),
                epicID);
    }

    public static Task fromString(String value) {
        String[] values = value.split(",");
        Task task = null;
        if (TaskType.valueOf(values[1]).equals(TaskType.TASK)) {
            task = new Task(Integer.parseInt(values[0]), values[2], values[4]);
        }
        if (TaskType.valueOf(values[1]).equals(TaskType.SUBTASK)) {
            task =  new Subtask(Integer.parseInt(values[0]), values[2], values[4], Integer.parseInt(values[5]));
        }
        if (TaskType.valueOf(values[1]).equals(TaskType.EPIC)) {
            task = new Epic(Integer.parseInt(values[0]), values[2], values[4]);
        }
        task.setStatus(Status.valueOf(values[3]));
        return task;
    }

    public static FileBackedTasksManager loadFromFile(File fileFrom, File fileForManager) throws LoadFromFileException {
        int maxID = 0;
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(fileForManager);
        try(BufferedReader br = new BufferedReader(new FileReader(fileFrom))) {
            br.readLine();
            String line = br.readLine();
            while (!line.isBlank()) {
                Task task = fromString(line);
                if (task.getID() > maxID) maxID = task.getID();
                if (task.getClass().equals(Task.class)) {
                    fileBackedTasksManager.tasks.put(task.getID(), task);
                }
                if (task.getClass().equals(Subtask.class)) {
                    fileBackedTasksManager.subtasks.put(task.getID(),(Subtask)task);
                }
                if (task.getClass().equals(Epic.class)) {
                    fileBackedTasksManager.epics.put(task.getID(), (Epic)task);
                }
                line = br.readLine();
            }
            for (Subtask subtask : fileBackedTasksManager.subtasks.values()) {
                fileBackedTasksManager.setSubtaskToEpic(subtask, subtask.getEpicID());
            }
            fileBackedTasksManager.ID = maxID + 1;
            List<Integer> history = historyFromString(br.readLine());
            for (Integer ID : history) {
                fileBackedTasksManager.getTaskWithoutType(ID);
            }
            return fileBackedTasksManager;
        }
        catch (IOException exp) {
            throw new LoadFromFileException();
        }
    }

    public static String historyToString(HistoryManager manager) {
        StringBuilder result = new StringBuilder();
        List<Task> history = manager.getHistory();
        if (history.size() > 0) {
            for (Task task : history) {
                result.append(String.valueOf(task.getID()));
                result.append(',');
            }
            result.deleteCharAt(result.length() - 1);
        }
        return result.toString();
    }

    public static List<Integer> historyFromString(String value) {
        String[] historyID = value.split(",");
        LinkedList<Integer> IDList = new LinkedList<>();
        for (String ID : historyID) {
            IDList.addLast(Integer.parseInt(ID));
        }
        return IDList;
    }
}
