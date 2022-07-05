import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {

    //************ Статус эпика без подзадач ************
    @Test
    public void shouldShowTheStatusNewOfEpicWithoutSubtasks() {
        TaskManager taskManager = Managers.getDefault();
        task.Epic epic1 = taskManager.createEpic(new task.Epic("Эпик 1", "...", task.Status.NEW));
        assertEquals(task.Status.NEW, epic1.getStatus());
    }

    //************ Статус эпика с подзадачами со статусом NEW ************
    @Test
    public void shouldShowTheStatusNewOfEpicWithNewSubtasks() {
        TaskManager taskManager = Managers.getDefault();
        task.Epic epic1 = taskManager.createEpic(new task.Epic("Эпик 1", "...", task.Status.NEW));
        task.Subtask subtask1 = taskManager.createSubtask(new task.Subtask(epic1.getId(), "Сабтакс 1.1", "...", task.Status.NEW));
        task.Subtask subtask2 = taskManager.createSubtask(new task.Subtask(epic1.getId(), "Сабтакс 1.2", "...", task.Status.NEW));
        task.Subtask subtask3 = taskManager.createSubtask(new task.Subtask(epic1.getId(), "Сабтакс 1.3", "...", task.Status.NEW));
        assertEquals(task.Status.NEW, epic1.getStatus());
    }
    //************ Статус эпика с подзадачами со статусом DONE ************
    @Test
    public void shouldShowTheStatusDoneOfEpicWithDoneSubtasks() {
        TaskManager taskManager = Managers.getDefault();
        task.Epic epic1 = taskManager.createEpic(new task.Epic("Эпик 1", "...", task.Status.NEW));
        task.Subtask subtask1 = taskManager.createSubtask(new task.Subtask(epic1.getId(), "Сабтакс 1.1", "...", task.Status.DONE));
        task.Subtask subtask2 = taskManager.createSubtask(new task.Subtask(epic1.getId(), "Сабтакс 1.2", "...", task.Status.DONE));
        task.Subtask subtask3 = taskManager.createSubtask(new task.Subtask(epic1.getId(), "Сабтакс 1.3", "...", task.Status.DONE));
        assertEquals(task.Status.DONE, epic1.getStatus());
    }
    //************ Статус эпика с подзадачами со статусом NEW или DONE ************
    @Test
    public void shouldShowTheStatusInProgressOfEpicWithNewOrDoneSubtasks() {
        TaskManager taskManager = Managers.getDefault();
        task.Epic epic1 = taskManager.createEpic(new task.Epic("Эпик 1", "...", task.Status.NEW));
        task.Subtask subtask1 = taskManager.createSubtask(new task.Subtask(epic1.getId(), "Сабтакс 1.1", "...", task.Status.DONE));
        task.Subtask subtask2 = taskManager.createSubtask(new task.Subtask(epic1.getId(), "Сабтакс 1.2", "...", task.Status.NEW));
        task.Subtask subtask3 = taskManager.createSubtask(new task.Subtask(epic1.getId(), "Сабтакс 1.3", "...", task.Status.DONE));
        assertEquals(task.Status.IN_PROGRESS, epic1.getStatus());
    }
    //************ Статус эпика с подзадачами со статусом IN_PROGRESS ************
    @Test
    public void shouldShowTheStatusInProgressOfEpicWithInProgressSubtasks() {
        TaskManager taskManager = Managers.getDefault();
        task.Epic epic1 = taskManager.createEpic(new task.Epic("Эпик 1", "...", task.Status.NEW));
        task.Subtask subtask1 = taskManager.createSubtask(new task.Subtask(epic1.getId(), "Сабтакс 1.1", "...", task.Status.IN_PROGRESS));
        task.Subtask subtask2 = taskManager.createSubtask(new task.Subtask(epic1.getId(), "Сабтакс 1.2", "...", task.Status.IN_PROGRESS));
        task.Subtask subtask3 = taskManager.createSubtask(new task.Subtask(epic1.getId(), "Сабтакс 1.3", "...", task.Status.IN_PROGRESS));
        assertEquals(task.Status.IN_PROGRESS, epic1.getStatus());
    }
}
