package task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;


public class Epic extends Task {
    private ArrayList<Integer> subtaskIds;
    private Optional<LocalDateTime> endTime;

    public Epic(String name, String details, Status status) {
        super(name, details, status);
        this.subtaskIds = new ArrayList<>();
        this.endTime = getEndTime();
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setEndTime(Optional<LocalDateTime> endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Epic{" + super.toString() +
                "subtaskIds=" + subtaskIds +
                '}';
    }

    public void addSubtask(int id) {
        subtaskIds.add(id);
    }

    public void removeSubtask(int id) {
        subtaskIds.remove((Integer) id);
    }

    public void clearSubtasks() {
        subtaskIds.clear();
    }

    @Override
    public Optional<LocalDateTime> getEndTime() {
        return endTime;
    }
}
