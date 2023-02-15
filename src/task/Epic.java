package task;

import manager.HistoryManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Epic extends Task {
    private HashMap<Integer, Subtask> subtasks;
    private LocalDateTime endTime;

    public Epic(int id, String title, String description) {
        super(id, title, description, null, null);
        this.taskType = TaskType.EPIC;
        this.subtasks = new HashMap<>();
    }

    private void updateTimeParamsForAddingOrUpdating(Subtask subtask) {
        if (subtask.getEndTime() == null) return;
        if (duration != null) duration = duration.plus(subtask.duration);
        else duration = subtask.duration;
        if (startTime != null) {
            if(startTime.isAfter(subtask.startTime)) startTime = subtask.startTime;
        }
        else {
            startTime = subtask.startTime;
        }
        if (endTime != null) {
            if (endTime.isBefore(subtask.getEndTime())) endTime = subtask.getEndTime();
        }
        else endTime = subtask.getEndTime();
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void addNewSubTask(Subtask subtask) {
        if (status != Status.NEW) {
            status = Status.IN_PROGRESS;
        }
        subtasks.put(subtask.getId(), subtask);
        if (subtask.startTime != null) updateTimeParamsForAddingOrUpdating(subtask);
    }

    public void updateSubtaskInEpic(Subtask subtask) {
        for (Subtask subtaskInEpic : subtasks.values()) {
            if (subtaskInEpic.getId() == subtask.getId()) {
                subtasks.remove(subtaskInEpic.getId());
                subtasks.put(subtask.getId(), subtask);
                updateStatus();
                updateTimeParamsForAddingOrUpdating(subtask);
                return;
            }
        }
    }

    public void removeSubtaskInEpic(Subtask subtask) {
        subtasks.remove(subtask.getId(), subtask);
        updateStatus();
        if (subtask.getEndTime() == null || endTime == null) return;
        duration = duration.minus(subtask.duration);
        if (subtask.startTime.equals(startTime)) {
            LocalDateTime minStartTime = endTime;
            for (Subtask subtask1 : subtasks.values()) {
                if (subtask1.startTime.isBefore(minStartTime)) {
                    minStartTime = subtask1.startTime;
                }
            }
            startTime = minStartTime;
        }

        if (subtask.getEndTime().equals(endTime)) {
            LocalDateTime maxEndTime = startTime;
            for (Subtask subtask1 : subtasks.values()) {
                if (subtask1.getEndTime().isAfter(maxEndTime)) {
                    maxEndTime = subtask1.startTime;
                }
            }
            endTime = maxEndTime;
        }
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
        duration = null;
        startTime = null;
        endTime = null;
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
            subtasksID.add(subtask.getId());
        }
        return "Epic{" +
                "ID=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", ID of subtasks=" + subtasksID +
                '}';
    }
}
