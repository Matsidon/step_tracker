package task;

import java.time.LocalDateTime;

public class Subtask extends Task {
    private int epicId;

    public Subtask(int epicId, String name, String details, Status status) {
        super(name, details, status);
        this.epicId = epicId;
    }

    public Subtask(int epicId, String name, String details, Status status, LocalDateTime startTime, int duration) {
        super(name, details, status, startTime, duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" + super.toString() +
                "epicId=" + epicId +
                '}';
    }
}
