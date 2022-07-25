package managers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class HTTPTaskManager extends FileBackedTasksManager {
    private static Gson gson;
    private KVTaskClient kvTaskClient;

    public HTTPTaskManager(String urlToKVServer) {
        gson = new Gson();
        this.kvTaskClient = new KVTaskClient(urlToKVServer);
    }

    @Override
    protected void save() {
        String jsonTasks = gson.toJson(tasks);
        String jsonEpics = gson.toJson(epics);
        String jsonSubtasks = gson.toJson(subtasks);
        String jsonHistory = gson.toJson(historyManager.getHistory());

        try {
            kvTaskClient.put("tasks/", jsonTasks);
            kvTaskClient.put("epics/", jsonEpics);
            kvTaskClient.put("subtasks/", jsonSubtasks);
            kvTaskClient.put("history/", jsonHistory);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static HTTPTaskManager load() {
        HTTPTaskManager httpTaskManager = new HTTPTaskManager("http://localhost:8078");
        try {
            String tasksList = KVTaskClient.load("tasks/");
            String epicsList = KVTaskClient.load("epics/");
            String subtaskList = KVTaskClient.load("subtasks/");
            String historyList = KVTaskClient.load("history/");
            httpTaskManager.tasks = gson.fromJson(tasksList, new TypeToken<HashMap<Integer, Task>>() {
            }.getType());
            httpTaskManager.epics = gson.fromJson(epicsList, new TypeToken<HashMap<Integer, Epic>>() {
            }.getType());
            httpTaskManager.subtasks = gson.fromJson(subtaskList, new TypeToken<HashMap<Integer, Subtask>>() {
            }.getType());
            List<Task> history = gson.fromJson(historyList, new TypeToken<List<Task>>() {
            }.getType());
            if (history != null) {
                for (Task task : history) {
                    if (task instanceof Epic) {
                        httpTaskManager.historyManager.add((Epic) task);
                    } else if (task instanceof Subtask) {
                        httpTaskManager.historyManager.add((Subtask) task);
                    } else {
                        httpTaskManager.historyManager.add(task);
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return httpTaskManager;
    }
}




