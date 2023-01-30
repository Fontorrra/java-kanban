package task;

import manager.HistoryManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Epic extends Task {
    private HashMap<Integer, Subtask> subtasks;

    public Epic(int ID, String title, String description) {
        super(ID, title, description);
        this.subtasks = new HashMap<>();
    }

    public void addNewSubTask(Subtask subtask) {
        if (status != Status.NEW) {
            status = Status.IN_PROGRESS;
        }
        subtasks.put(subtask.getID(), subtask);
    }

    public void updateSubtaskInEpic(Subtask subtask) {
        for (Subtask subtaskInEpic : subtasks.values()) {
            if (subtaskInEpic.getID() == subtask.getID()) {
                subtasks.remove(subtaskInEpic.getID());
                subtasks.put(subtask.getID(), subtask);
                updateStatus();
                return;
            }
        }
    }

    public void removeSubtaskInEpic(Subtask subtask) {
        subtasks.remove(subtask.getID(), subtask);
        updateStatus();
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void removeAllSubtasks(HistoryManager historyManager){
        for (Integer id : subtasks.keySet()) {
            historyManager.remove(id);
        }
        this.subtasks = new HashMap<>();
        status = Status.NEW;
    }

    public void updateStatus() {
        int numberOfNew = 0;
        int numberOfDone = 0;
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getStatus().equals(Status.IN_PROGRESS)) {
                this.status = Status.IN_PROGRESS;
                return;
            }
            else if (subtask.status.equals(Status.NEW)) numberOfNew++;
            else numberOfDone++;
        }
        if (numberOfNew == subtasks.size()) this.status = Status.NEW;
        else if (numberOfDone == subtasks.size()) this.status = Status.DONE;
        else this.status = Status.IN_PROGRESS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasks, epic.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks);
    }

    @Override
    public String toString() {
        ArrayList<Integer> subtasksID = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            subtasksID.add(subtask.getID());
        }
        return "Epic{" +
                "ID=" + ID +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", ID of subtasks=" + subtasksID +
                '}';
    }
}
