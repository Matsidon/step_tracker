package managers;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

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

    /**
     * Список задач
     **/
    @Override
    public ArrayList<Task> getTask() {
        if (tasks != null) {
            return new ArrayList<>(tasks.values());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public ArrayList<Subtask> getSubtask() {
        if (subtasks != null) {
            return new ArrayList<>(subtasks.values());
        } else {
            return new ArrayList<>();
        }
    }


    @Override
    public ArrayList<Epic> getEpic() {
        if (epics != null) {
            return new ArrayList<>(epics.values());
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Получение по идентификатору
     **/
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

    /**
     * Создание
     **/
    @Override
    public Task createTask(Task task) {
        int idByTask = generateId();
        task.setId(idByTask);
        tasks.put(idByTask, task);
        try {
            findIntersection();
        } catch (RuntimeException e) {
            tasks.remove(idByTask, task);
            throw new ManagerSaveException("Нельзя сохранить задачу. Задачи пересекаются во времени");
        }
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        int idByEpic = generateId();
        epic.setId(idByEpic);
        epics.put(idByEpic, epic);
        try {
            findIntersection();
        } catch (RuntimeException e) {
            epics.remove(idByEpic, epic);
            throw new ManagerSaveException("Нельзя сохранить задачу. Задачи пересекаются во времени");
        }
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicId())) {
            int idBySubtask = generateId();
            subtask.setId(idBySubtask);
            subtasks.put(idBySubtask, subtask);
            try {
                findIntersection();
            } catch (RuntimeException e) {
                subtasks.remove(idBySubtask, subtask);
                throw new ManagerSaveException("Нельзя сохранить задачу. Задачи пересекаются во времени");
            }
            epics.get(subtask.getEpicId()).addSubtask(idBySubtask);
            updateStatusEpic(epics.get(subtask.getEpicId()));
            setStartTimeOfEpic(subtask.getEpicId());
            findDurationOfEpic(subtask.getEpicId());
            setEndTimeOfEpic(subtask.getEpicId());
        } else {
            throw new ManagerSaveException("Нельзя сохранить задачу. Нет эпика с таким id");
        }
        return subtask;
    }

    /**
     * Обновление
     **/
    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
        try {
            findIntersection();
        } catch (RuntimeException e) {
            tasks.remove(task.getId(), task);
            throw new ManagerSaveException("Нельзя сохранить задачу. Задачи пересекаются во времени");
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        try {
            findIntersection();
        } catch (RuntimeException e) {
            subtasks.remove(subtask.getId(), subtask);
            throw new ManagerSaveException("Нельзя сохранить задачу. Задачи пересекаются во времени");
        }
        int epicId = subtask.getEpicId();
        updateStatusEpic(epics.get(epicId));
        setStartTimeOfEpic(epicId);
        findDurationOfEpic(epicId);
        setEndTimeOfEpic(epicId);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        try {
            findIntersection();
        } catch (RuntimeException e) {
            epics.remove(epic.getId(), epic);
            throw new ManagerSaveException("Нельзя сохранить задачу. Задачи пересекаются во времени");
        }
    }

    /**
     * Удаление по идентификатору
     **/
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

    /**
     * Удаление спика задач
     **/
    @Override
    public void removeAllTasks() {
        for (Integer id : tasks.keySet()) {
            historyManager.remove(id);
        }
        tasks.clear();
    }

    @Override
    public void removeAllSubtasks() {
        for (Integer id : subtasks.keySet()) {
            historyManager.remove(id);
            int epicId = subtasks.get(id).getEpicId();
            epics.get(epicId).removeSubtask(id);
            subtasks.remove(id);
            updateStatusEpic(epics.get(epicId));
            setStartTimeOfEpic(epicId);
            findDurationOfEpic(epicId);
            setEndTimeOfEpic(epicId);
        }
    }

    @Override
    public void removeAllEpics() {
        for (Integer id : epics.keySet()) {
            for (int elem : epics.get(id).getSubtaskIds()) {
                historyManager.remove(elem);
            }
            historyManager.remove(id);
            epics.get(id).clearSubtasks();
        }
        epics.clear();
    }

    /**
     * Получение списка всех подзадач эпика
     **/
    @Override
    public ArrayList<Subtask> getListOfSubtaskByEpic(int id) {
        ArrayList<Subtask> listOfSubtasksByEpic = new ArrayList<>();
        for (Integer elem : epics.get(id).getSubtaskIds()) {
            listOfSubtasksByEpic.add(subtasks.get(elem));
        }
        return listOfSubtasksByEpic;
    }

    /**
     * Обновление статуса эпика
     **/
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

    /**
     * Список просмотренных задач
     **/
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    /**
     * Расчет продолжительности Эпика
     **/
    @Override
    public void findDurationOfEpic(int epicId) {
        long duration = 0;
        for (Integer subtaskId : epics.get(epicId).getSubtaskIds()) {
            duration += subtasks.get(subtaskId).getDuration();
        }
        epics.get(epicId).setDuration(duration);
    }

    /**
     * Расчет времени старта Эпика
     **/
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
        if (startTime == LocalDateTime.MAX) {
            epics.get(epicId).setEndTime(Optional.empty());
        } else {
            epics.get(epicId).setStartTime(Optional.of(startTime));
        }
    }

    /**
     * Расчет времени окончания Эпика
     **/
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
        if (endTime == LocalDateTime.MIN) {
            epics.get(epicId).setEndTime(Optional.empty());
        } else {
            epics.get(epicId).setEndTime(Optional.of(endTime));
        }
    }

    /**
     * Сортировка по времени
     **/
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

    /**
     * Проверка задач на пересечение
     **/
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
