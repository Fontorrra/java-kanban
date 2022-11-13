import java.util.Objects;

public class SubTask extends Task {
    int epicID;

    public SubTask(int ID, String title, String description, int epicID) {
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
        SubTask subTask = (SubTask) o;
        return this.epicID == subTask.epicID;
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
