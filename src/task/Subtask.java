package task;

import java.util.Objects;

public class Subtask extends Task {
    private int epicID;

    public Subtask(int ID, String title, String description, int epicID) {
        super(ID, title, description);
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }

    public void setEpicID(int epicID) {
        this.epicID = epicID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return this.epicID == subtask.epicID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicID);
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "ID=" + ID  +
                ", epicID='" + epicID + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
