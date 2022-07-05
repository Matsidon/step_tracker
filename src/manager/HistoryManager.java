package manager;

import task.Task;

import java.util.List;

public interface HistoryManager {
    //************ Добавление задачи в просмотренные ************
    void add(Task task);

    //************ История просмотров задач ************
    List<Task> getHistory();

    //************ Удаление задачи из просмотренных ************
    void remove(int id);
}
