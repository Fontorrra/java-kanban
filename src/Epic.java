import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;

    public Epic(int ID, String title, String description) {
        super(ID, title, description);
        this.subtasks = new ArrayList<>();
    }

    public void addNewSubTask(Subtask subtask) {
        if (status == Status.DONE) {
            status = Status.NEW;
        }
        subtasks.add(subtask);
    }

    public void updateSubtaskInEpic(Subtask subtask) {
        int i = 0;
        for (Subtask subtaskInArray : subtasks) {
            if (subtaskInArray.getID() == subtask.getID()) {
                subtasks.remove(i);
                subtasks.add(i, subtask);
                updateStatus();
                return;
            }
            i++;
        }
    }

    public void removeSubtaskInEpic(Subtask subtask) {
        subtasks.remove(subtask);
        updateStatus();
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void removeAllSubtasks(){
        this.subtasks = new ArrayList<>();
        status = Status.NEW;
    }

    public void updateStatus() {
        int numberOfNew = 0;
        for (Subtask subtask : subtasks) {
            if (subtask.getStatus().equals(Status.IN_PROGRESS)) {
                this.status = Status.IN_PROGRESS;
                return;
            }
            else if (subtask.status.equals(Status.NEW)) numberOfNew++;
        }
        if (numberOfNew == subtasks.size()) this.status = Status.NEW;
        else this.status = Status.DONE;
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
        for (Subtask subtask : subtasks) {
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
