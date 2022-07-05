import manager.Managers;
import manager.TaskManager;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        Task task1 = taskManager.createTask(new Task("Задача 1", "...", Status.NEW));
        Task task2 = taskManager.createTask(new Task("Задача 2", "...", Status.NEW));
        Epic epic1 = taskManager.createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Subtask subtask1 = taskManager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.1", "...", Status.NEW));
        Subtask subtask2 = taskManager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.2", "...", Status.NEW));
        Subtask subtask3 = taskManager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.3", "...", Status.NEW));
        Epic epic2 = taskManager.createEpic(new Epic("Эпик 2", "...", Status.NEW));

        System.out.println("Просмотр задач");
        listOfViewedTasks(taskManager, task1, task2, epic1, subtask1, subtask2, epic2, subtask3);
        System.out.println("Список просмотренных задач");
        printTasks(taskManager);
        System.out.println("Удаление задач: Task1 и Epic1");
        removeTask(taskManager, task1, epic1);
        System.out.println("Список просмотренных задач");
        printTasks(taskManager);
    }

    static void printTasks(TaskManager taskManager) {
        for (Task elem : taskManager.getHistory()) {
            System.out.println(elem);
        }
    }

    //Удаление задач
    static void removeTask(TaskManager taskManager, Task task1, Epic epic1) {
        taskManager.removeTaskById(task1.getId());
        taskManager.removeEpicById(epic1.getId());
    }

    //Список просмотренных задач
    static void listOfViewedTasks(TaskManager taskManager, Task task1, Task task2, Epic epic1, Subtask subtask1, Subtask subtask2, Epic epic2, Subtask subtask3) {
        taskManager.getSubtaskById(subtask1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getTaskById(task1.getId());
        taskManager.getSubtaskById(subtask2.getId());
        taskManager.getEpicById(epic2.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getSubtaskById(subtask2.getId());
        taskManager.getSubtaskById(subtask3.getId());

        taskManager.getSubtaskById(subtask1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getTaskById(task1.getId());
        taskManager.getSubtaskById(subtask2.getId());
        taskManager.getEpicById(epic2.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getSubtaskById(subtask2.getId());
        taskManager.getSubtaskById(subtask3.getId());
    }
}
