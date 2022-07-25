import managers.FileBackedTasksManager;
import managers.HistoryManager;
import managers.InMemoryHistoryManager;
import managers.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class InMemoryHistoryManagerTest {

    @Test
    void shouldShowNullHistoryTasks() {
        TaskManager manager = new FileBackedTasksManager();
        HistoryManager manager1 = new InMemoryHistoryManager();
        Task task1 = manager.createTask(new Task("Задача 1", "...", Status.NEW,
                LocalDateTime.of(2000, 1, 3, 0, 0, 0), 15));
        Task task2 = manager.createTask(new Task("Задача 2", "...", Status.NEW,
                LocalDateTime.of(2000, 1, 4, 0, 0, 0), 15));
        Epic epic1 = manager.createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Subtask subtask1 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.1",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0), 15));
        Subtask subtask2 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.2",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 2, 0, 0, 0), 17));
        Subtask subtask3 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.3",
                "...", Status.NEW));
        Epic epic2 = manager.createEpic(new Epic("Эпик 2", "...", Status.NEW));

        assertEquals(0, manager1.getHistory().size());

    }

    @Test
    void shouldShowCorrectHistoryTasksWithoutDouble() {
        TaskManager manager = new FileBackedTasksManager();
        HistoryManager manager1 = new InMemoryHistoryManager();
        Task task1 = manager.createTask(new Task("Задача 1", "...", Status.NEW,
                LocalDateTime.of(2000, 1, 3, 0, 0, 0), 15));
        Task task2 = manager.createTask(new Task("Задача 2", "...", Status.NEW,
                LocalDateTime.of(2000, 1, 4, 0, 0, 0), 15));
        Epic epic1 = manager.createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Subtask subtask1 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.1",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0), 15));
        Subtask subtask2 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.2",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 2, 0, 0, 0), 17));
        Subtask subtask3 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.3",
                "...", Status.NEW));
        Epic epic2 = manager.createEpic(new Epic("Эпик 2", "...", Status.NEW));
        //Просмотр задач с дублированием
        manager1.add(task1);
        manager1.add(task2);
        manager1.add(epic1);
        manager1.add(subtask1);
        manager1.add(subtask2);
        manager1.add(subtask3);
        manager1.add(epic2);
        manager1.add(task1);

        assertEquals(7, manager1.getHistory().size());
    }

    @Test
    void removeInHistoryInStart() {
        TaskManager manager = new FileBackedTasksManager();
        HistoryManager manager1 = new InMemoryHistoryManager();
        Task task1 = manager.createTask(new Task("Задача 1", "...", Status.NEW,
                LocalDateTime.of(2000, 1, 3, 0, 0, 0), 15));
        Task task2 = manager.createTask(new Task("Задача 2", "...", Status.NEW,
                LocalDateTime.of(2000, 1, 4, 0, 0, 0), 15));
        Epic epic1 = manager.createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Subtask subtask1 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.1",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0), 15));
        Subtask subtask2 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.2",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 2, 0, 0, 0), 17));
        Subtask subtask3 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.3",
                "...", Status.NEW));
        Epic epic2 = manager.createEpic(new Epic("Эпик 2", "...", Status.NEW));
        //Просмотр задач
        manager1.add(task1);
        manager1.add(task2);
        manager1.add(epic1);
        manager1.add(subtask1);
        manager1.add(subtask2);
        manager1.add(subtask3);
        manager1.add(epic2);
        //Удаление задач из просмотренных в начале
        manager1.remove(task1.getId());

        assertEquals(6, manager1.getHistory().size());
        assertFalse(manager1.getHistory().contains(task1));
    }

    @Test
    void removeInHistoryInMedium() {
        TaskManager manager = new FileBackedTasksManager();
        HistoryManager manager1 = new InMemoryHistoryManager();
        Task task1 = manager.createTask(new Task("Задача 1", "...", Status.NEW,
                LocalDateTime.of(2000, 1, 3, 0, 0, 0), 15));
        Task task2 = manager.createTask(new Task("Задача 2", "...", Status.NEW,
                LocalDateTime.of(2000, 1, 4, 0, 0, 0), 15));
        Epic epic1 = manager.createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Subtask subtask1 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.1",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0), 15));
        Subtask subtask2 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.2",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 2, 0, 0, 0), 17));
        Subtask subtask3 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.3",
                "...", Status.NEW));
        Epic epic2 = manager.createEpic(new Epic("Эпик 2", "...", Status.NEW));
        //Просмотр задач
        manager1.add(task1);
        manager1.add(task2);
        manager1.add(epic1);
        manager1.add(subtask1);
        manager1.add(subtask2);
        manager1.add(subtask3);
        manager1.add(epic2);
        //Удаление задач из просмотренных в середине
        manager1.remove(subtask1.getId());

        assertEquals(6, manager1.getHistory().size());
        assertFalse(manager1.getHistory().contains(subtask1));
    }

    @Test
    void removeInHistoryInTheEnd() {
        TaskManager manager = new FileBackedTasksManager();
        HistoryManager manager1 = new InMemoryHistoryManager();
        Task task1 = manager.createTask(new Task("Задача 1", "...", Status.NEW,
                LocalDateTime.of(2000, 1, 3, 0, 0, 0), 15));
        Task task2 = manager.createTask(new Task("Задача 2", "...", Status.NEW,
                LocalDateTime.of(2000, 1, 4, 0, 0, 0), 15));
        Epic epic1 = manager.createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Subtask subtask1 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.1",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0), 15));
        Subtask subtask2 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.2",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 2, 0, 0, 0), 17));
        Subtask subtask3 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.3",
                "...", Status.NEW));
        Epic epic2 = manager.createEpic(new Epic("Эпик 2", "...", Status.NEW));
        //Просмотр задач
        manager1.add(task1);
        manager1.add(task2);
        manager1.add(epic1);
        manager1.add(subtask1);
        manager1.add(subtask2);
        manager1.add(subtask3);
        manager1.add(epic2);
        //Удаление задач из просмотренных в конце
        manager1.remove(epic2.getId());

        assertEquals(6, manager1.getHistory().size());
        assertFalse(manager1.getHistory().contains(epic2));

    }
}
