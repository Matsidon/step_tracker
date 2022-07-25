import managers.HttpTaskServer;
import managers.KVServer;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
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

    /**
     * Статус эпика без подзадач
     **/
    @Test
    public void shouldShowTheStatusNewOfEpicWithoutSubtasks() {
        TaskManager taskManager = Managers.getDefault();
        tasks.Epic epic1 = taskManager.createEpic(new tasks.Epic("Эпик 1", "...", tasks.Status.NEW));
        assertEquals(tasks.Status.NEW, epic1.getStatus());
    }

    /**
     * Статус эпика с подзадачами со статусом NEW
     **/
    @Test
    public void shouldShowTheStatusNewOfEpicWithNewSubtasks() {
        TaskManager taskManager = Managers.getDefault();
        tasks.Epic epic1 = taskManager.createEpic(new tasks.Epic("Эпик 1", "...", tasks.Status.NEW));
        tasks.Subtask subtask1 = taskManager.createSubtask(new tasks.Subtask(epic1.getId(), "Сабтакс 1.1", "...", tasks.Status.NEW));
        tasks.Subtask subtask2 = taskManager.createSubtask(new tasks.Subtask(epic1.getId(), "Сабтакс 1.2", "...", tasks.Status.NEW));
        tasks.Subtask subtask3 = taskManager.createSubtask(new tasks.Subtask(epic1.getId(), "Сабтакс 1.3", "...", tasks.Status.NEW));
        assertEquals(tasks.Status.NEW, epic1.getStatus());
    }

    /**
     * Статус эпика с подзадачами со статусом DONE
     **/
    @Test
    public void shouldShowTheStatusDoneOfEpicWithDoneSubtasks() {
        TaskManager taskManager = Managers.getDefault();
        tasks.Epic epic1 = taskManager.createEpic(new tasks.Epic("Эпик 1", "...", tasks.Status.NEW));
        tasks.Subtask subtask1 = taskManager.createSubtask(new tasks.Subtask(epic1.getId(), "Сабтакс 1.1", "...", tasks.Status.DONE));
        tasks.Subtask subtask2 = taskManager.createSubtask(new tasks.Subtask(epic1.getId(), "Сабтакс 1.2", "...", tasks.Status.DONE));
        tasks.Subtask subtask3 = taskManager.createSubtask(new tasks.Subtask(epic1.getId(), "Сабтакс 1.3", "...", tasks.Status.DONE));
        assertEquals(tasks.Status.DONE, epic1.getStatus());
    }

    /**
     * Статус эпика с подзадачами со статусом NEW или DONE
     **/
    @Test
    public void shouldShowTheStatusInProgressOfEpicWithNewOrDoneSubtasks() {
        TaskManager taskManager = Managers.getDefault();
        tasks.Epic epic1 = taskManager.createEpic(new tasks.Epic("Эпик 1", "...", tasks.Status.NEW));
        tasks.Subtask subtask1 = taskManager.createSubtask(new tasks.Subtask(epic1.getId(), "Сабтакс 1.1", "...", tasks.Status.DONE));
        tasks.Subtask subtask2 = taskManager.createSubtask(new tasks.Subtask(epic1.getId(), "Сабтакс 1.2", "...", tasks.Status.NEW));
        tasks.Subtask subtask3 = taskManager.createSubtask(new tasks.Subtask(epic1.getId(), "Сабтакс 1.3", "...", tasks.Status.DONE));
        assertEquals(tasks.Status.IN_PROGRESS, epic1.getStatus());
    }

    /**
     * Статус эпика с подзадачами со статусом IN_PROGRESS
     **/
    @Test
    public void shouldShowTheStatusInProgressOfEpicWithInProgressSubtasks() {
        TaskManager taskManager = Managers.getDefault();
        tasks.Epic epic1 = taskManager.createEpic(new tasks.Epic("Эпик 1", "...", tasks.Status.NEW));
        tasks.Subtask subtask1 = taskManager.createSubtask(new tasks.Subtask(epic1.getId(), "Сабтакс 1.1", "...", tasks.Status.IN_PROGRESS));
        tasks.Subtask subtask2 = taskManager.createSubtask(new tasks.Subtask(epic1.getId(), "Сабтакс 1.2", "...", tasks.Status.IN_PROGRESS));
        tasks.Subtask subtask3 = taskManager.createSubtask(new tasks.Subtask(epic1.getId(), "Сабтакс 1.3", "...", tasks.Status.IN_PROGRESS));
        assertEquals(tasks.Status.IN_PROGRESS, epic1.getStatus());
    }
}
