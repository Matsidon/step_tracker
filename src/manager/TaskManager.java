package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.*;

public interface TaskManager {
    //************ Список задач ************

    ArrayList<Task> getTask();

    ArrayList<Subtask> getSubtask();

    ArrayList<Epic> getEpic();

    //************ Получение по идентификатору ************

    Task getTaskById(int id);

    Subtask getSubtaskById(int id);

    Epic getEpicById(int id);

    //************ Создание ************

    Task createTask(Task task);

    Epic createEpic(Epic epic);

    Subtask createSubtask(Subtask subtask);

    //************ Обновление ************

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    //************ Удаление по идентификатору ************

    void removeTaskById(int id);

    void removeSubtaskById(int id);

    void removeEpicById(int id);

    //************ Получение списка всех подзадач эпика ************

    ArrayList<Subtask> getListOfSubtaskByEpic(int id);

    //************ Обновление статуса эпика ************

    void updateStatusEpic(Epic epic);

    //************ Список просмотренных задач ************

    List<Task> getHistory();

    //************ Расчет продолжительности Эпика ************

    void findDurationOfEpic(int epicId);

    //************ Расчет времени старта Эпика ************

    void setStartTimeOfEpic(int epicId);

    //************ Расчет времени окончания Эпика ************

    void setEndTimeOfEpic(int epicId);

    //************ Сортировка по времени ************

    TreeSet getPrioritizedTasks();

    //************ Проверка задач на пересечение ************

    void findIntersection();
}