package manager;

import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HistoryManager historyManager = Managers.getDefaultHistory();
    private int id = 0;


    private int generateId() {
        return ++id;
    }

    //************ Список задач ************
    @Override
    public ArrayList<Task> getTask() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Subtask> getSubtask() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Epic> getEpic() {
        return new ArrayList<>(epics.values());
    }

    //************ Получение по идентификатору ************
    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    //************ Создание ************
    @Override
    public Task createTask(Task task) {
        int idByTask = generateId();
        task.setId(idByTask);
        tasks.put(idByTask, task);
        findIntersection();
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        int idByEpic = generateId();
        epic.setId(idByEpic);
        epics.put(idByEpic, epic);
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicId())) {
            int idBySubtask = generateId();
            subtask.setId(idBySubtask);
            subtasks.put(idBySubtask, subtask);
            epics.get(subtask.getEpicId()).addSubtask(idBySubtask);
            updateStatusEpic(epics.get(subtask.getEpicId()));
        }
        findIntersection();
        setStartTimeOfEpic(subtask.getEpicId());
        findDurationOfEpic(subtask.getEpicId());
        setEndTimeOfEpic(subtask.getEpicId());
        return subtask;
    }

    //************ Обновление ************
    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
        findIntersection();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        int epicId = subtask.getEpicId();
        updateStatusEpic(epics.get(epicId));
        findIntersection();
        setStartTimeOfEpic(epicId);
        findDurationOfEpic(epicId);
        setEndTimeOfEpic(epicId);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    //************ Удаление по идентификатору ************
    @Override
    public void removeTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        }
        historyManager.remove(id);
    }

    @Override
    public void removeSubtaskById(int id) {
        int epicId;
        if (subtasks.containsKey(id)) {
            epicId = subtasks.get(id).getEpicId();
            epics.get(epicId).removeSubtask(id);
            subtasks.remove(id);
            updateStatusEpic(epics.get(epicId));
            setStartTimeOfEpic(epicId);
            findDurationOfEpic(epicId);
            setEndTimeOfEpic(epicId);
        }
        historyManager.remove(id);
    }

    @Override
    public void removeEpicById(int id) {
        if (epics.containsKey(id)) {
            for (int elem : epics.get(id).getSubtaskIds()) {
                historyManager.remove(elem);
            }
            epics.get(id).clearSubtasks();
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    //************ Получение списка всех подзадач эпика ************
    @Override
    public ArrayList<Subtask> getListOfSubtaskByEpic(int id) {
        ArrayList<Subtask> listOfSubtasksByEpic = new ArrayList<>();
        for (Integer elem : epics.get(id).getSubtaskIds()) {
            listOfSubtasksByEpic.add(subtasks.get(elem));
        }
        return listOfSubtasksByEpic;
    }

    //************ Обновление статуса эпика ************
    @Override
    public void updateStatusEpic(Epic epic) {
        ArrayList<Integer> listOfSubtaskIdsByEpic = epic.getSubtaskIds();
        int countNew = 0;
        int countDone = 0;
        for (Integer elem : listOfSubtaskIdsByEpic) {
            if (Status.NEW.equals(subtasks.get(elem).getStatus())) {
                countNew++;
            }
        }
        for (Integer elem : listOfSubtaskIdsByEpic) {
            if (Status.DONE.equals(subtasks.get(elem).getStatus())) {
                countDone++;
            }
        }
        if (epic.getSubtaskIds() == null || listOfSubtaskIdsByEpic.size() == countNew) {
            epic.setStatus(Status.NEW);
        } else if (listOfSubtaskIdsByEpic.size() == countDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    //************ Список просмотренных задач ************
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    //************ Расчет продолжительности Эпика ************
    @Override
    public void findDurationOfEpic(int epicId) {
        long duration = 0;
        for (Integer subtaskId : epics.get(epicId).getSubtaskIds()) {
            duration += subtasks.get(subtaskId).getDuration();
        }
        epics.get(epicId).setDuration(duration);
    }

    //************ Расчет времени старта Эпика ************
    @Override
    public void setStartTimeOfEpic(int epicId) {
        if (epics.get(epicId) == null) {
            throw new RuntimeException("Не найден эпик с id = " + epicId);
        }
        LocalDateTime startTime = LocalDateTime.MAX;
        for (Integer subtaskId : epics.get(epicId).getSubtaskIds()) {
            Optional<LocalDateTime> optionalStartTime = subtasks.get(subtaskId).getStartTime();
            if (optionalStartTime.isPresent() && startTime.isAfter(optionalStartTime.get())) {
                startTime = optionalStartTime.get();
            }
        }
        epics.get(epicId).setStartTime(Optional.of(startTime));
    }

    //************ Расчет времени окончания Эпика ************
    @Override
    public void setEndTimeOfEpic(int epicId) {
        if (epics.get(epicId) == null) {
            throw new RuntimeException("Не найден эпик с id = " + epicId);
        }
        LocalDateTime endTime = LocalDateTime.MIN;
        for (Integer subtaskId : epics.get(epicId).getSubtaskIds()) {
            Optional<LocalDateTime> optionalEndTime = subtasks.get(subtaskId).getEndTime();
            if (optionalEndTime.isPresent() && endTime.isBefore(optionalEndTime.get())) {
                endTime = optionalEndTime.get();
            }
        }
        epics.get(epicId).setEndTime(Optional.of(endTime));
    }

    //************ Сортировка по времени ************
    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        Comparator<Task> comparator = new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                if (t1.getStartTime().isPresent() && t2.getStartTime().isPresent()) {
                    if (t1.getStartTime().get().isBefore(t2.getStartTime().get())) {
                        return -1;
                    } else if (t1.getStartTime().get().isAfter(t2.getStartTime().get())) {
                        return 1;
                    } else {
                        if (t1.getId() < t2.getId()) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                } else if (t1.getStartTime().isPresent() && t2.getStartTime().isEmpty()) {
                    return -1;
                } else if (t1.getStartTime().isEmpty() && t2.getStartTime().isPresent()) {
                    return 1;
                } else {
                    if (t1.getId() < t2.getId()) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            }
        };

        TreeSet<Task> listOfTasks = new TreeSet<>(comparator);
        listOfTasks.addAll(tasks.values());
        listOfTasks.addAll(subtasks.values());
        return listOfTasks;
    }

    //************ Проверка задач на пересечение ************
    @Override
    public void findIntersection() {
        List<Task> tasksList = new ArrayList<>();

        for (Object task : getPrioritizedTasks()) {
            tasksList.add((Task) task);
        }
        for (int i = 0; i < tasksList.size() - 1; i++) {
            if (tasksList.get(i).getStartTime().isPresent() && tasksList.get(i + 1).getStartTime().isPresent()) {
                if (tasksList.get(i).getStartTime().get().isBefore(tasksList.get(i + 1).getStartTime().get())) {
                    if (tasksList.get(i).getEndTime().get().isAfter(tasksList.get(i + 1).getStartTime().get())) {
                        throw new RuntimeException("Задачи пересекаются во времени");
                    }
                } else {
                    throw new RuntimeException("Задачи пересекаются во времени");
                }
            }
        }
    }
}
