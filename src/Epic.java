import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Epic extends Task {
    ArrayList<SubTask> subtasks;

    public Epic(int ID, String title, String description) {
        super(ID, title, description);
        this.subtasks = new ArrayList<>();
    }

    public void addNewSubTask(SubTask subTask) {
        if (status == Status.DONE) {
            status = Status.NEW;
        }
        subtasks.add(subTask);
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
        String result = super.toString();
        result = result.replace("Task", "Epic");
        result += "subtasks size=" + subtasks.size();
        return result;
    }
}
