import managers.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    void createManager() {
        super.manager = new InMemoryTaskManager();
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
}
