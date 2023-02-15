package manager;
import task.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    private void save() {
        try (BufferedWriter writer =new BufferedWriter(new FileWriter(file.getName()))) {
            writer.write("id,type,name,status,description,duration,startTime,epic");
            writer.newLine();
            for (Task task : tasks.values()) {
                writer.write(toString(task));
                writer.newLine();
            }
            for (Epic epic : epics.values()) {
                writer.write(toString(epic));
                writer.newLine();
            }
            for (Subtask subtask : subtasks.values()) {
                writer.write(toString(subtask));
                writer.newLine();
            }
            writer.newLine();
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
    public void removeEpic(int id) {
        super.removeEpic(id);
        save();
    }

    @Override
    public void removeSubtask(int id) {
        super.removeSubtask(id);
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
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    public Task getTaskWithoutType(int id) {
        Task task = tasks.get(id);
        if (task != null) return getTask(id);
        task = subtasks.get(id);
        if (task != null) return getSubtask(id);
        task = epics.get(id);
        if (task != null) return getEpic(id);
        return null;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask =  super.getSubtask(id);
        save();
        return subtask;
    }

    public static String toString(Task task) {
        TaskType taskType = task.getTaskType();
        String epicID = "None";
        if (task.getTaskType() == TaskType.SUBTASK) epicID = Integer.toString(((Subtask)task).getEpicId());
        return String.format("%d,%s,%s,%s,%s,%s,%s,%s",
                task.getId(),
                taskType.name(),
                task.getTitle(),
                task.getStatus(),
                task.getDescription(),
                task.getDuration(),
                task.getStartTime(),
                epicID
                );
    }

    public static Task fromString(String value) {
        String[] values = value.split(",");
        Task task = null;
        TaskType taskType = TaskType.valueOf(values[1]);
        if (!values[5].equals("null")) {
            switch (taskType) {
                case TASK:
                    task = new Task(Integer.parseInt(values[0]), values[2], values[4],
                            Duration.parse(values[5]), LocalDateTime.parse(values[6]));
                    break;
                case EPIC:
                    task = new Epic(Integer.parseInt(values[0]), values[2], values[4]);
                    break;
                case SUBTASK:
                    task = new Subtask(Integer.parseInt(values[0]), values[2], values[4], Integer.parseInt(values[7]),
                            Duration.parse(values[5]), LocalDateTime.parse(values[6]));
                    break;
            }
        } else {
            switch (taskType) {
                case TASK:
                    task = new Task(Integer.parseInt(values[0]), values[2], values[4]);
                    break;
                case EPIC:
                    task = new Epic(Integer.parseInt(values[0]), values[2], values[4]);
                    break;
                case SUBTASK:
                    task = new Subtask(Integer.parseInt(values[0]), values[2], values[4], Integer.parseInt(values[7]));
                    break;
            }
        }
        task.setStatus(Status.valueOf(values[3]));
        return task;
    }

    public static FileBackedTasksManager loadFromFile(File fileFrom, File fileForManager) throws LoadFromFileException {
        int maxId = 0;
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(fileForManager);
       try(BufferedReader br = new BufferedReader(new FileReader(fileFrom))) {
            br.readLine();
            String line = br.readLine();
            if (line == null) return new FileBackedTasksManager(fileForManager);
            while (!line.isBlank()) {
                Task task = fromString(line);
                if (task.getId() > maxId) maxId = task.getId();
                TaskType taskType = task.getTaskType();
                switch (taskType) {
                    case TASK:
                        fileBackedTasksManager.tasks.put(task.getId(), task);
                        break;
                    case EPIC:
                        fileBackedTasksManager.epics.put(task.getId(), (Epic)task);
                        break;
                    case SUBTASK:
                        fileBackedTasksManager.subtasks.put(task.getId(),(Subtask)task);
                        break;
                }
                line = br.readLine();
            }
            for (Subtask subtask : fileBackedTasksManager.subtasks.values()) {
                fileBackedTasksManager.setSubtaskToEpic(subtask, subtask.getEpicId());
            }
            fileBackedTasksManager.id = maxId + 1;
            List<Integer> history = historyFromString(br.readLine());
            for (Integer id : history) {
                fileBackedTasksManager.getTaskWithoutType(id);
            }
            return fileBackedTasksManager;
        }
        catch (IOException exp) {
            throw new LoadFromFileException();
        }
    }

    public static String historyToString(HistoryManager manager) {
        StringJoiner result = new StringJoiner(",");
        List<Task> history = manager.getHistory();
        if (history.size() > 0) {
            for (Task task : history) {
                result.add(String.valueOf(task.getId()));
            }
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
