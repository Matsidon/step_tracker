import managers.ManagerSaveException;
import managers.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;

    /**
     * Список задач
     **/
    @Test
    void shouldShowListOfTasks() {
        Task task1 = manager.createTask(new Task("Задача 1", "...", Status.NEW,
                LocalDateTime.of(2000, 1, 3, 0, 0, 0), 15));
        Task task2 = manager.createTask(new Task("Задача 2", "...", Status.NEW,
                LocalDateTime.of(2000, 1, 4, 0, 0, 0), 15));
        assertEquals(2, manager.getTask().size());
        assertNotNull(manager.getTask());
    }

    @Test
    void shouldShowListOfEpics() {
        Epic epic1 = manager.createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Epic epic2 = manager.createEpic(new Epic("Эпик 2", "...", Status.NEW));
        assertEquals(2, manager.getEpic().size());
        assertNotNull(manager.getEpic());
    }

    @Test
    void shouldShowListOfSubtasks() {
        Epic epic1 = manager.createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Subtask subtask1 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.1",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0), 15));
        Subtask subtask2 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.2",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 2, 0, 0, 0), 17));
        Subtask subtask3 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.3",
                "...", Status.NEW));
        assertEquals(3, manager.getSubtask().size());
        assertNotNull(manager.getSubtask());
    }

    @Test
    void shouldShowNullListOfTasks() {
        assertTrue(manager.getTask().isEmpty());
    }

    @Test
    void shouldShowNullListOfEpics() {
        assertTrue(manager.getEpic().isEmpty());
    }

    @Test
    void shouldShowNullListOfSubtasks() {
        assertTrue(manager.getSubtask().isEmpty());
    }

    /**
     * Получение по идентификатору
     **/
    @Test
    void shouldShowTask() {
        Task task1 = manager.createTask(new Task("Задача 1", "...", Status.NEW,
                LocalDateTime.of(2000, 1, 3, 0, 0, 0), 15));
        Task taskById = manager.getTaskById(task1.getId());
        assertEquals(taskById.getId(), task1.getId());
        assertEquals(taskById.getName(), task1.getName());
    }

    @Test
    void shouldShowEpic() {
        Epic epic1 = manager.createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Epic epicById = manager.getEpicById(epic1.getId());
        assertEquals(epicById.getId(), epic1.getId());
        assertEquals(epicById.getName(), epic1.getName());
    }

    @Test
    void shouldShowSubtask() {
        Epic epic1 = manager.createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Subtask subtask1 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.1",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0), 15));
        Subtask subtaskById = manager.getSubtaskById(subtask1.getId());
        assertEquals(subtaskById.getId(), subtask1.getId());
        assertEquals(subtaskById.getName(), subtask1.getName());
    }

    @Test
    void shouldShowNullTask() {
        final NullPointerException ex = assertThrows(NullPointerException.class, () -> manager.getTaskById(1));
        assertNotNull(ex);
    }

    @Test
    void shouldShowNullEpic() {
        final NullPointerException ex = assertThrows(NullPointerException.class, () -> manager.getEpicById(1));
        assertNotNull(ex);
    }

    @Test
    void shouldShowNullSubtask() {
        final NullPointerException ex = assertThrows(NullPointerException.class, () -> manager.getSubtaskById(1));
        assertNotNull(ex);
    }

    /**
     * Создание
     **/
    @Test
    void shouldCreateTask() {
        Task task1 = new Task("Задача 1", "...", Status.NEW,
                LocalDateTime.of(2000, 1, 3, 0, 0, 0), 15);
        Task taskForEqual = manager.createTask(task1);
        assertEquals("Задача 1", taskForEqual.getName());
        assertEquals("...", taskForEqual.getDetails());
        assertEquals(1, taskForEqual.getId());
    }

    @Test
    void shouldCreateEpic() {
        Epic epic1 = new Epic("Эпик 1", "...", Status.NEW);
        Epic epicForEqual = manager.createEpic(epic1);
        assertEquals("Эпик 1", epicForEqual.getName());
        assertEquals("...", epicForEqual.getDetails());
        assertEquals(1, epicForEqual.getId());
    }

    @Test
    void shouldCreateSubtask() {
        Epic epic1 = manager.createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Subtask subtask1 = new Subtask(epic1.getId(), "Сабтакс 1.1",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0), 15);
        Subtask subtaskForEqual = manager.createSubtask(subtask1);
        assertEquals("Сабтакс 1.1", subtaskForEqual.getName());
        assertEquals("...", subtaskForEqual.getDetails());
        assertEquals(2, subtaskForEqual.getId());
    }

    @Test
    void shouldCreateNullTask() {
        Task task1 = new Task(null, null, null);
        Task taskForEqual = manager.createTask(task1);
        assertNull(taskForEqual.getName());
        assertNull(taskForEqual.getDetails());
        assertEquals(1, taskForEqual.getId());
    }

    @Test
    void shouldCreateNullEpic() {
        Epic epic1 = new Epic(null, null, null);
        Epic epicForEqual = manager.createEpic(epic1);
        assertNull(epicForEqual.getName());
        assertNull(epicForEqual.getDetails());
        assertEquals(1, epicForEqual.getId());
    }

    @Test
    void shouldCreateNullSubtask() {
        Epic epic1 = manager.createEpic(new Epic(null, null, null));
        Subtask subtask1 = new Subtask(epic1.getId(), null,
                null, null);
        Subtask subtaskForEqual = manager.createSubtask(subtask1);
        assertNull(subtaskForEqual.getName());
        assertNull(subtaskForEqual.getDetails());
        assertEquals(2, subtaskForEqual.getId());
    }

    /**
     * Обновление
     **/
    @Test
    void shouldUpdateTask() {
        Task task1 = manager.createTask(new Task("Задача 1", "...", Status.NEW));
        Task task2 = new Task("Задача 2", "...", Status.NEW);
        task2.setId(1);
        manager.updateTask(task2);
        assertEquals(task2, manager.getTask().get(0));
    }

    @Test
    void shouldUpdateSubtask() {
        Epic epic1 = manager.createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Subtask subtask1 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.1",
                "...", Status.NEW));
        Subtask subtask2 = new Subtask(epic1.getId(), "Сабтакс 1.2",
                "...", Status.NEW);
        subtask2.setId(2);
        manager.updateSubtask(subtask2);
        assertEquals(subtask2, manager.getSubtask().get(0));
    }

    @Test
    void shouldUpdateEpic() {
        Epic epic1 = manager.createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Epic epic2 = new Epic("Эпик 2", "...", Status.IN_PROGRESS);
        epic2.setId(1);
        manager.updateEpic(epic2);
        assertEquals(epic2, manager.getEpic().get(0));
    }

    /**
     * Удаление по идентификатору
     **/
    @Test
    void shouldRemoveTaskById() {
        Task task1 = manager.createTask(new Task("Задача 1", "...", Status.NEW,
                LocalDateTime.of(2000, 1, 3, 0, 0, 0), 15));
        manager.removeTaskById(task1.getId());
        assertEquals(0, manager.getTask().size());
    }

    @Test
    void shouldRemoveSubtaskById() {
        Epic epic1 = manager.createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Subtask subtask1 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.1",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0), 15));
        manager.removeSubtaskById(subtask1.getId());
        assertEquals(0, manager.getSubtask().size());
    }

    @Test
    void shouldRemoveEpicById() {
        Epic epic1 = manager.createEpic(new Epic("Эпик 1", "...", Status.NEW));
        manager.removeEpicById(epic1.getId());
        assertEquals(0, manager.getEpic().size());
    }

    @Test
    void shouldRemoveTaskByIncorrectId() {
        Task task1 = manager.createTask(new Task("Задача 1", "...", Status.NEW,
                LocalDateTime.of(2000, 1, 3, 0, 0, 0), 15));
        List<Task> list = manager.getTask();
        manager.removeTaskById(2);
        assertEquals(1, list.size());
    }

    @Test
    void shouldRemoveSubtaskByIncorrectId() {
        Epic epic1 = manager.createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Subtask subtask1 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.1",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0), 15));
        manager.removeSubtaskById(3);
        assertEquals(1, manager.getSubtask().size());
    }

    @Test
    void shouldRemoveEpicByIncorrectId() {
        Epic epic1 = manager.createEpic(new Epic("Эпик 1", "...", Status.NEW));
        List<Epic> list = manager.getEpic();
        manager.removeEpicById(2);
        assertEquals(1, list.size());
    }

    /**
     * Получение списка всех подзадач эпика
     **/
    @Test
    void shouldGetListOfSubtaskByEpic() {
        Epic epic1 = manager.createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Subtask subtask1 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.1",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0), 15));
        assertEquals(1, manager.getListOfSubtaskByEpic(1).size());
    }

    @Test
    void shouldGetListOfNullSubtaskByEpic() {
        Epic epic1 = manager.createEpic(new Epic("Эпик 1", "...", Status.NEW));
        assertEquals(0, manager.getListOfSubtaskByEpic(1).size());
    }

    /**
     * Обновление статуса эпика
     **/
    @Test
    void shouldUpdateStatusEpic() {
        Epic epic1 = manager.createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Subtask subtask1 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.1",
                "...", Status.DONE,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0), 15));
        manager.updateStatusEpic(epic1);
        assertEquals(Status.DONE, epic1.getStatus());
    }

    @Test
    void shouldUpdateStatusNullEpic() {
        Epic epic1 = manager.createEpic(new Epic("Эпик 1", "...", Status.NEW));
        manager.updateStatusEpic(epic1);
        assertEquals(Status.NEW, epic1.getStatus());
    }

    /**
     * Список просмотренных задач
     **/
    @Test
    void shouldGetHistory() {
        Epic epic1 = manager.createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Subtask subtask1 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.1",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0), 15));
        Subtask subtask2 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.2",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 2, 0, 0, 0), 17));
        Subtask subtask3 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.3",
                "...", Status.NEW));
        manager.getEpicById(1);
        manager.getSubtaskById(2);
        manager.getSubtaskById(3);
        manager.getSubtaskById(4);
        final List<Task> taskList = manager.getHistory();
        assertEquals(4, taskList.size());
    }

    @Test
    void shouldGetNullHistory() {
        Epic epic1 = manager.createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Subtask subtask1 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.1",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0), 15));
        Subtask subtask2 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.2",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 2, 0, 0, 0), 17));
        Subtask subtask3 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.3",
                "...", Status.NEW));
        final List<Task> taskList = manager.getHistory();
        assertEquals(0, taskList.size());
    }

    /**
     * Расчет продолжительности Эпика
     **/
    @Test
    void shouldSetDurationOfEpic() {
        Epic epic1 = manager.createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Subtask subtask1 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.1",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0), 15));
        Subtask subtask2 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.2",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 2, 0, 0, 0), 17));
        Subtask subtask3 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.3",
                "...", Status.NEW));
        manager.findDurationOfEpic(1);
        final long time = subtask1.getDuration() + subtask2.getDuration() + subtask3.getDuration();
        assertNotEquals(0, epic1.getDuration());
        assertEquals(time, epic1.getDuration());
    }

    @Test
    void shouldSetNullDurationOfEpic() {
        Epic epic1 = manager.createEpic(new Epic("Эпик 1", "...", Status.NEW));
        manager.findDurationOfEpic(1);
        assertEquals(0, epic1.getDuration());
    }

    /**
     * Расчет времени старта Эпика
     **/
    @Test
    void shouldSetStartTimeOfEpic() {
        Epic epic1 = manager.createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Subtask subtask1 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.1",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0), 15));
        Subtask subtask2 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.2",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 2, 0, 0, 0), 17));
        Subtask subtask3 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.3",
                "...", Status.NEW));
        manager.setStartTimeOfEpic(1);
        final LocalDateTime startTime = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
        assertTrue(epic1.getStartTime().isPresent());
        assertEquals(startTime, epic1.getStartTime().get());
    }

    @Test
    void shouldSetNullStartTimeOfEpic() {
        Epic epic1 = manager.createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Subtask subtask3 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.3",
                "...", Status.NEW));
        manager.setStartTimeOfEpic(1);
        assertEquals(Optional.empty(), epic1.getStartTime());
    }

    @Test
    void shouldSetNullStartTimeOfNullEpic() {
        Epic epic1 = manager.createEpic(new Epic("Эпик 1", "...", Status.NEW));
        manager.setStartTimeOfEpic(1);
        assertEquals(Optional.empty(), epic1.getStartTime());
    }

    /**
     * Расчет времени окончания Эпика
     **/
    @Test
    void shouldSetEndTimeOfEpic() {
        Epic epic1 = manager.createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Subtask subtask1 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.1",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0), 15));
        Subtask subtask2 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.2",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 2, 0, 0, 0), 17));
        Subtask subtask3 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.3",
                "...", Status.NEW));
        manager.setEndTimeOfEpic(1);
        final LocalDateTime endTime = LocalDateTime.of(2000, 1, 2, 0, 17, 0);
        assertNotNull(epic1.getEndTime());
        assertTrue(epic1.getEndTime().isPresent());
        assertEquals(endTime, epic1.getEndTime().get());
    }

    @Test
    void shouldSetNullEndTimeOfEpic() {
        Epic epic1 = manager.createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Subtask subtask3 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.3",
                "...", Status.NEW));
        manager.setEndTimeOfEpic(1);
        assertEquals(Optional.empty(), epic1.getEndTime());
    }

    @Test
    void shouldSetNullEndTimeOfNullEpic() {
        Epic epic1 = manager.createEpic(new Epic("Эпик 1", "...", Status.NEW));
        manager.setEndTimeOfEpic(1);
        assertEquals(Optional.empty(), epic1.getEndTime());
    }

    /**
     * Сортировка по времени
     **/
    @Test
    void shouldGetPrioritizedTasks() {
        Epic epic1 = manager.createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Subtask subtask1 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.1",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0), 15));
        Subtask subtask2 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.2",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 2, 0, 0, 0), 17));
        Subtask subtask3 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.3",
                "...", Status.NEW));
        final TreeSet<Task> prioritizedTasks = manager.getPrioritizedTasks();
        assertNotNull(prioritizedTasks);
        assertEquals(subtask3, prioritizedTasks.last());
    }

    @Test
    void shouldGetNullPrioritizedTasks() {
        Epic epic1 = manager.createEpic(new Epic("Эпик 1", "...", Status.NEW));
        final TreeSet<Task> prioritizedTasks = manager.getPrioritizedTasks();
        assertTrue(prioritizedTasks.isEmpty());
    }

    /**
     * Проверка задач на пересечение
     **/
    @Test
    void shouldFindIntersection() {
        final RuntimeException ex = assertThrows(ManagerSaveException.class, () -> {
            Epic epic1 = manager.createEpic(new Epic("Эпик 1", "...", Status.NEW));
            Subtask subtask1 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.1",
                    "...", Status.NEW,
                    LocalDateTime.of(2000, 1, 1, 0, 0, 0), 15));
            Subtask subtask2 = manager.createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.2",
                    "...", Status.NEW,
                    LocalDateTime.of(2000, 1, 1, 0, 0, 0), 17));
        });
        final RuntimeException ex1 = assertThrows(ManagerSaveException.class, () -> {
            Task task1 = manager.createTask(new Task("Задача 1", "...", Status.NEW,
                    LocalDateTime.of(2000, 1, 3, 0, 0, 0), 15));
            Task task2 = manager.createTask(new Task("Задача 2", "...", Status.NEW,
                    LocalDateTime.of(2000, 1, 3, 0, 0, 0), 5));
        });
        assertEquals(ManagerSaveException.class, ex.getClass());
        assertEquals("Нельзя сохранить задачу. Задачи пересекаются во времени", ex.getMessage());
        assertEquals(ManagerSaveException.class, ex1.getClass());
        assertEquals("Нельзя сохранить задачу. Задачи пересекаются во времени", ex1.getMessage());
    }
}

