package managers;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

public class FileBackedTasksManager extends InMemoryTaskManager {

    public FileBackedTasksManager(String path) {
        try {
            Files.readString(Path.of(path));
        } catch (IOException e) {
            System.out.println("Не удалось прочитать файл");
        }
    }

    public FileBackedTasksManager() {
        super();
    }

    public static void main(String[] args) {
        TaskManager fileBackedTasksManager1 = new FileBackedTasksManager();
        //Создание задач
        Task task1 = fileBackedTasksManager1.createTask(new Task("Задача 1", "...", Status.NEW,
                LocalDateTime.of(2000, 1, 3, 0, 0, 0), 15));
        Task task2 = fileBackedTasksManager1.createTask(new Task("Задача 2", "...", Status.NEW,
                LocalDateTime.of(2000, 1, 4, 0, 0, 0), 15));
        Epic epic1 = fileBackedTasksManager1.createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Subtask subtask1 = fileBackedTasksManager1.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.1",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0), 15));
        Subtask subtask2 = fileBackedTasksManager1.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.2",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 2, 0, 0, 0), 17));
        Subtask subtask3 = fileBackedTasksManager1.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.3",
                "...", Status.NEW));
        Epic epic2 = fileBackedTasksManager1.createEpic(new Epic("Эпик 2", "...", Status.NEW));

        //Просмотр задач
        fileBackedTasksManager1.getSubtaskById(subtask1.getId());
        fileBackedTasksManager1.getEpicById(epic1.getId());
        fileBackedTasksManager1.getTaskById(task1.getId());
        fileBackedTasksManager1.getSubtaskById(subtask2.getId());
        fileBackedTasksManager1.getEpicById(epic2.getId());
        fileBackedTasksManager1.getTaskById(task2.getId());
        fileBackedTasksManager1.getSubtaskById(subtask2.getId());
        fileBackedTasksManager1.getSubtaskById(subtask3.getId());
        //Проверка корректности восстановления данных
        TaskManager fileBackedTasksManager = loadFromFile("src\\tasks.csv");
        System.out.println("Просмотренные задачи:");
        for (Task task : fileBackedTasksManager.getTask()) {
            System.out.println(task.getId() + " " + task.getName());
        }
        System.out.println("Просмотренные эпики:");
        for (Epic epic : fileBackedTasksManager.getEpic()) {
            System.out.println(epic.getId() + " " + epic.getName());
        }
        System.out.println("Просмотренные сабтаски:");
        for (Subtask subtask : fileBackedTasksManager.getSubtask()) {
            System.out.println(subtask.getId() + " " + subtask.getName());
        }
        System.out.println("История просмотра:");
        for (Task task : fileBackedTasksManager.getHistory()) {
            System.out.println(task.getId() + " " + task.getName());
        }
        //Сортировка по времени
        System.out.println("Сортировка по времени");
        TreeSet<Task> sortTask = fileBackedTasksManager.getPrioritizedTasks();
        for (Task task : sortTask) {
            System.out.println(task.getId() + " " + task.getName() + " " + task.getStartTime());
        }
        // Пересечения
        System.out.println("Пересечения");
        fileBackedTasksManager.findIntersection();
        // Проверка времени старта эпиков
        for (Epic epic : fileBackedTasksManager.getEpic()) {
            System.out.println(epic.getId() + " " + epic.getName() + " " + epic.getStartTime() + " " + epic.getDuration());
        }
    }

    /**
     * Сохранение текущего состояния менеджера в файл
     **/
    protected void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("src\\tasks.csv"))) {
            for (Epic epic : epics.values()) {
                bufferedWriter.write(toString(epic) + "\n");
            }
            for (Subtask subtask : subtasks.values()) {
                bufferedWriter.write(toString(subtask) + "\n");
            }
            for (Task task : tasks.values()) {
                bufferedWriter.write(toString(task) + "\n");
            }
            bufferedWriter.write("");
            bufferedWriter.newLine();
            bufferedWriter.write(historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения в файл");
        }
    }

    /**
     * Восстановление данных из файла при запуске
     **/
    protected static FileBackedTasksManager loadFromFile(String file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while (bufferedReader.ready()) {
                String str = bufferedReader.readLine();
                if (!str.isEmpty() || !str.isBlank()) {
                    Task task = fromString(str);
                    if (task instanceof Epic) {
                        fileBackedTasksManager.epics.put(task.getId(), (Epic) task);
                    } else if (task instanceof Subtask) {
                        fileBackedTasksManager.subtasks.put(task.getId(), (Subtask) task);
                    } else {
                        fileBackedTasksManager.tasks.put(task.getId(), task);
                    }
                } else {
                    String strHistory = bufferedReader.readLine();
                    fileBackedTasksManager.historyFromString(strHistory);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка восстановления из файла");
        }
        return fileBackedTasksManager;
    }

    /**
     * Сохранение задачи в строку
     **/
    private static String toString(Task task) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        String taskLine;
        String id = Integer.toString(task.getId());
        String typeEpic = String.valueOf(TypeOfTasks.EPIC);
        String typeSubtask = String.valueOf(TypeOfTasks.SUBTASK);
        String typeTask = String.valueOf(TypeOfTasks.TASK);
        String name = task.getName();
        String status = String.valueOf(task.getStatus());
        String details = task.getDetails();
        String duration = Long.toString(task.getDuration());

        if (task.getStartTime().isPresent()) {
            String startTime = task.getStartTime().get().format(formatter);
            if (task instanceof Epic) {
                taskLine = String.join(", ", id, typeEpic, name, status, details, startTime, duration);
            } else if (task instanceof Subtask) {
                String epicId = Integer.toString(((Subtask) task).getEpicId());
                taskLine = String.join(", ", id, typeSubtask, name, status, details, epicId, startTime, duration);
            } else {
                taskLine = String.join(", ", id, typeTask, name, status, details, startTime, duration);
            }
        } else {
            String startTime = null;
            if (task instanceof Epic) {
                taskLine = String.join(", ", id, typeEpic, name, status, details, startTime, duration);
            } else if (task instanceof Subtask) {
                String epicId = Integer.toString(((Subtask) task).getEpicId());
                taskLine = String.join(", ", id, typeSubtask, name, status, details, epicId, startTime, duration);
            } else {
                taskLine = String.join(", ", id, typeTask, name, status, details, startTime, duration);
            }
        }
        return taskLine;
    }

    /**
     * Создание задачи из строки
     **/
    private static Task fromString(String value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        String[] taskArray = value.split(", ");
        TypeOfTasks type = TypeOfTasks.valueOf(taskArray[1]);
        String name = taskArray[2];
        String details = taskArray[4];
        Status status = Status.valueOf(taskArray[3]);
        int id = Integer.parseInt(taskArray[0]);
        Task task;

        switch (type) {
            case EPIC:
                int durationEpic = Integer.parseInt(taskArray[6]);
                if (!taskArray[5].equals("null")) {
                    LocalDateTime startTimeEpic = LocalDateTime.parse(taskArray[5], formatter);
                    task = new Epic(name, details, status);
                    task.setId(id);
                    task.setStartTime(Optional.of(startTimeEpic));
                    task.setDuration(durationEpic);
                } else {
                    task = new Epic(name, details, status);
                    task.setId(id);
                    task.setStartTime(Optional.empty());
                    task.setDuration(durationEpic);
                }
                break;
            case SUBTASK:
                int epicId = Integer.parseInt(taskArray[5]);
                int durationSubtask = Integer.parseInt(taskArray[7]);
                if (!taskArray[6].equals("null")) {
                    LocalDateTime startTimeSubtask = LocalDateTime.parse(taskArray[6], formatter);
                    task = new Subtask(epicId, name, details, status, startTimeSubtask, durationSubtask);
                    task.setId(id);
                } else {
                    task = new Subtask(epicId, name, details, status);
                    task.setId(id);
                }
                break;
            case TASK:
                int durationTask = Integer.parseInt(taskArray[6]);
                if (!taskArray[5].equals("null")) {
                    LocalDateTime startTimeTask = LocalDateTime.parse(taskArray[5], formatter);
                    task = new Task(name, details, status, startTimeTask, durationTask);
                    task.setId(id);
                } else {
                    task = new Task(name, details, status);
                    task.setId(id);
                }
                break;
            default:
                task = new Task(null, null, null);
        }
        return task;
    }

    /**
     * Сохранение менеджера истории в CSV
     **/
    private static String historyToString(HistoryManager manager) {
        List<Task> listOfTasks = manager.getHistory();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < listOfTasks.size(); i++) {
            stringBuilder.append(listOfTasks.get(i).getId() + ", ");
        }
        return stringBuilder.toString();
    }

    /**
     * Восстановление менеджера истории из CSV
     **/
    private List<Task> historyFromString(String value) {
        String[] tasksId = value.split(", ");
        for (int i = 0; i < tasksId.length; i++) {
            if (tasks.containsKey(Integer.parseInt(tasksId[i]))) {
                historyManager.add(tasks.get(Integer.parseInt(tasksId[i])));
            } else if (epics.containsKey(Integer.parseInt(tasksId[i]))) {
                historyManager.add(epics.get(Integer.parseInt(tasksId[i])));
            } else {
                historyManager.add(subtasks.get(Integer.parseInt(tasksId[i])));
            }
        }
        return historyManager.getHistory();
    }

    /**
     * Получение по идентификатору
     **/
    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;

    }

    /**
     * Создание
     **/
    @Override
    public Task createTask(Task task) {
        Task task1 = super.createTask(task);
        save();
        return task1;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic epic1 = super.createEpic(epic);
        save();
        return epic1;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Subtask subtask1 = super.createSubtask(subtask);
        save();
        return subtask1;
    }

    /**
     * Обновление
     **/
    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    /**
     * Удаление по идентификатору
     **/
    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }
    //************ Удаление спика задач ************

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    /**
     * Получение списка всех подзадач эпика
     **/
    @Override
    public ArrayList<Subtask> getListOfSubtaskByEpic(int id) {
        ArrayList<Subtask> subtasks1 = super.getListOfSubtaskByEpic(id);
        save();
        return subtasks1;
    }

    /**
     * Обновление статуса эпика
     **/
    @Override
    public void updateStatusEpic(Epic epic) {
        super.updateStatusEpic(epic);
        save();
    }

    /**
     * Список просмотренных задач
     **/
    @Override
    public List<Task> getHistory() {
        List<Task> tasks1 = super.getHistory();
        save();
        return tasks1;
    }

    /**
     * Расчет продолжительности Эпика
     **/
    @Override
    public void findDurationOfEpic(int epicId) {
        super.findDurationOfEpic(epicId);
        save();
    }

    /**
     * Расчет времени старта Эпика
     **/
    @Override
    public void setStartTimeOfEpic(int epicId) {
        super.setStartTimeOfEpic(epicId);
        save();
    }

    /**
     * Расчет времени окончания Эпика
     **/
    @Override
    public void setEndTimeOfEpic(int epicId) {
        super.setEndTimeOfEpic(epicId);
        save();
    }

    /**
     * Сортировка по времени
     **/
    @Override
    public TreeSet getPrioritizedTasks() {
        TreeSet<Task> listOfTasks1 = super.getPrioritizedTasks();
        save();
        return listOfTasks1;
    }

    /**
     * Проверка задач на пересечение
     **/
    @Override
    public void findIntersection() {
        super.findIntersection();
        save();
    }
}

