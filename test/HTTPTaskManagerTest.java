import managers.HTTPTaskManager;
import managers.HttpTaskServer;
import managers.KVServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HTTPTaskManagerTest extends FileBackedTasksManagerTest {
    private KVServer kvServer;
    private HttpTaskServer httpTaskServer;

    @BeforeEach
    void startServer() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HttpTaskServer();
    }

    @AfterEach
    void stopServer() {
        kvServer.stopServer();
        httpTaskServer.stopServer();
    }

    @Override
    @Test
    void shouldShowListOfTasks() {
        super.shouldShowListOfTasks();
    }

    @Override
    @Test
    void shouldShowListOfEpics() {
        super.shouldShowListOfEpics();
    }

    @Override
    @Test
    void shouldShowListOfSubtasks() {
        super.shouldShowListOfSubtasks();
    }

    @Override
    @Test
    void shouldShowNullListOfTasks() {
        super.shouldShowNullListOfTasks();
    }

    @Override
    @Test
    void shouldShowNullListOfEpics() {
        super.shouldShowNullListOfEpics();
    }

    @Override
    @Test
    void shouldShowNullListOfSubtasks() {
        super.shouldShowNullListOfSubtasks();
    }

    @Override
    @Test
    void shouldShowTask() {
        super.shouldShowTask();
    }

    @Override
    @Test
    void shouldShowEpic() {
        super.shouldShowEpic();
    }

    @Override
    @Test
    void shouldShowSubtask() {
        super.shouldShowSubtask();
    }

    @Override
    @Test
    void shouldShowNullTask() {
        super.shouldShowNullTask();
    }

    @Override
    @Test
    void shouldShowNullEpic() {
        super.shouldShowNullEpic();
    }

    @Override
    @Test
    void shouldShowNullSubtask() {
        super.shouldShowNullSubtask();
    }

    @Override
    @Test
    void shouldCreateTask() {
        super.shouldCreateTask();
    }

    @Override
    @Test
    void shouldCreateEpic() {
        super.shouldCreateEpic();
    }

    @Override
    @Test
    void shouldCreateSubtask() {
        super.shouldCreateSubtask();
    }

    @Override
    @Test
    void shouldCreateNullTask() {
        super.shouldCreateNullTask();
    }

    @Override
    @Test
    void shouldCreateNullEpic() {
        super.shouldCreateNullEpic();
    }

    @Override
    @Test
    void shouldCreateNullSubtask() {
        super.shouldCreateNullSubtask();
    }

    @Override
    @Test
    void shouldUpdateTask() {
        super.shouldUpdateTask();
    }

    @Override
    @Test
    void shouldUpdateSubtask() {
        super.shouldUpdateSubtask();
    }

    @Override
    @Test
    void shouldUpdateEpic() {
        super.shouldUpdateEpic();
    }

    @Override
    @Test
    void shouldRemoveTaskById() {
        super.shouldRemoveTaskById();
    }

    @Override
    @Test
    void shouldRemoveSubtaskById() {
        super.shouldRemoveSubtaskById();
    }

    @Override
    @Test
    void shouldRemoveEpicById() {
        super.shouldRemoveEpicById();
    }

    @Override
    @Test
    void shouldRemoveTaskByIncorrectId() {
        super.shouldRemoveTaskByIncorrectId();
    }

    @Override
    @Test
    void shouldRemoveSubtaskByIncorrectId() {
        super.shouldRemoveSubtaskByIncorrectId();
    }

    @Override
    @Test
    void shouldRemoveEpicByIncorrectId() {
        super.shouldRemoveEpicByIncorrectId();
    }

    @Override
    @Test
    void shouldGetListOfSubtaskByEpic() {
        super.shouldGetListOfSubtaskByEpic();
    }

    @Override
    @Test
    void shouldGetListOfNullSubtaskByEpic() {
        super.shouldGetListOfNullSubtaskByEpic();
    }

    @Override
    @Test
    void shouldUpdateStatusEpic() {
        super.shouldUpdateStatusEpic();
    }

    @Override
    @Test
    void shouldUpdateStatusNullEpic() {
        super.shouldUpdateStatusNullEpic();
    }

    @Override
    @Test
    void shouldGetHistory() {
        super.shouldGetHistory();
    }

    @Override
    @Test
    void shouldGetNullHistory() {
        super.shouldGetNullHistory();
    }

    @Override
    @Test
    void shouldSetDurationOfEpic() {
        super.shouldSetDurationOfEpic();
    }

    @Override
    @Test
    void shouldSetNullDurationOfEpic() {
        super.shouldSetNullDurationOfEpic();
    }

    @Override
    @Test
    void shouldSetStartTimeOfEpic() {
        super.shouldSetStartTimeOfEpic();
    }

    @Override
    @Test
    void shouldSetNullStartTimeOfEpic() {
        super.shouldSetNullStartTimeOfEpic();
    }

    @Override
    @Test
    void shouldSetNullStartTimeOfNullEpic() {
        super.shouldSetNullStartTimeOfNullEpic();
    }

    @Override
    @Test
    void shouldSetEndTimeOfEpic() {
        super.shouldSetEndTimeOfEpic();
    }

    @Override
    @Test
    void shouldSetNullEndTimeOfEpic() {
        super.shouldSetNullEndTimeOfEpic();
    }

    @Override
    @Test
    void shouldSetNullEndTimeOfNullEpic() {
        super.shouldSetNullEndTimeOfNullEpic();
    }

    @Override
    @Test
    void shouldGetPrioritizedTasks() {
        super.shouldGetPrioritizedTasks();
    }

    @Override
    @Test
    void shouldGetNullPrioritizedTasks() {
        super.shouldGetNullPrioritizedTasks();
    }

    @Override
    @Test
    void shouldFindIntersection() {
        super.shouldFindIntersection();
    }

    @Test
    void shouldLoadNullHTTPTaskManager() {
        HTTPTaskManager manager1 = HTTPTaskManager.load();
        ArrayList<Task> taskArrayList = manager1.getTask();
        ArrayList<Epic> epicArrayList = manager1.getEpic();
        ArrayList<Subtask> subtaskArrayList = manager1.getSubtask();
        List<Task> historyList = manager1.getHistory();

        assertEquals(0, taskArrayList.size());
        assertEquals(0, epicArrayList.size());
        assertEquals(0, subtaskArrayList.size());
        assertEquals(0, historyList.size());
    }

    @Test
    void shouldLoadHTTPTaskManager() {
        Task task1 = HttpTaskServer.getManager().createTask(new Task("Задача 1", "...", Status.NEW,
                LocalDateTime.of(2000, 1, 3, 0, 0, 0), 15));
        Epic epic1 = HttpTaskServer.getManager().createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Subtask subtask1 = HttpTaskServer.getManager().createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.1",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0), 15));
        HttpTaskServer.getManager().getTaskById(1);
        HttpTaskServer.getManager().getSubtaskById(3);
        HttpTaskServer.getManager().getEpicById(2);
        HTTPTaskManager manager1 = HTTPTaskManager.load();
        ArrayList<Task> taskArrayList = manager1.getTask();
        ArrayList<Epic> epicArrayList = manager1.getEpic();
        ArrayList<Subtask> subtaskArrayList = manager1.getSubtask();
        List<Task> historyList = manager1.getHistory();

        assertEquals(1, taskArrayList.size());
        assertEquals(1, epicArrayList.size());
        assertEquals(1, subtaskArrayList.size());
        assertEquals(3, historyList.size());
    }

}
