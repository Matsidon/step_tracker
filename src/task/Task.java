package task;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class Task {
    private String name;
    private String details;
    private int id;
    private Status status;
    private long duration;
    private Optional<LocalDateTime> startTime;

    public Task(String name, String details, Status status) {
        this.name = name;
        this.details = details;
        this.status = status;
        this.duration = 0;
        this.startTime = Optional.empty();
    }

    public Task(String name, String details, Status status, LocalDateTime startTime, int duration) {
        this.name = name;
        this.details = details;
        this.status = status;
        this.startTime = Optional.of(startTime);
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public Optional<LocalDateTime> getStartTime() {
        return startTime;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setStartTime(Optional<LocalDateTime> startTime) {
        this.startTime = startTime;
    }

    public Status getStatus() {

        return status;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getDetails() {
        return details;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Optional<LocalDateTime> getEndTime() {
        if (startTime.isPresent()) {
            return Optional.of(startTime.get().plus(Duration.ofMinutes(duration)));
        }
        return Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && duration == task.duration && Objects.equals(name, task.name) && Objects.equals(details, task.details) && status == task.status && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, details, id, status, duration, startTime);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", details='" + details + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }
}
