import com.google.gson.Gson;
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
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HTTPTaskServerTest {
    private KVServer kvServer;
    private HttpTaskServer httpTaskServer;
    private Gson gson;
    private final URI urlTasks = URI.create("http://localhost:8080/tasks/");
    private final URI urlTasksTask = URI.create("http://localhost:8080/tasks/task/");
    private final URI urlTasksEpic = URI.create("http://localhost:8080/tasks/epic/");
    private final URI urlTasksSubtask = URI.create("http://localhost:8080/tasks/subtask/");
    private final URI urlTasksTaskId = URI.create("http://localhost:8080/tasks/task/?id=1");
    private final URI urlTasksEpicId = URI.create("http://localhost:8080/tasks/epic/?id=1");
    private final URI urlTasksSubtaskId = URI.create("http://localhost:8080/tasks/subtask/?id=2");
    private final URI urlTasksHistory = URI.create("http://localhost:8080/tasks/history");
    private final URI urlTasksSubtaskEpicId = URI.create("http://localhost:8080/tasks/subtask/epic/?id=1");

    @BeforeEach
    void startServer() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        gson = new Gson();
        httpTaskServer = new HttpTaskServer();
    }

    @AfterEach
    void stopServer() {
        kvServer.stopServer();
        httpTaskServer.stopServer();
    }

    @Test
    void createTasksTask() throws IOException, InterruptedException {
        Task task1 = new Task("Задача 1", "...", Status.NEW,
                LocalDateTime.of(2000, 1, 3, 0, 0, 0), 15);
        HttpClient client = HttpClient.newHttpClient();
        String json = gson.toJson(task1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(urlTasksTask).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertEquals("", response.body());
    }

    @Test
    void createTasksEpic() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Эпик 1", "...", Status.NEW);
        HttpClient client = HttpClient.newHttpClient();
        String json = gson.toJson(epic1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(urlTasksEpic).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertEquals("", response.body());
    }

    @Test
    void createTasksSubtask() throws IOException, InterruptedException {
        Epic epic1 = HttpTaskServer.getManager().createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Subtask subtask1 = new Subtask(epic1.getId(), "Сабтакс 1.1",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0), 15);
        HttpClient client = HttpClient.newHttpClient();
        String jsonSubtask = gson.toJson(subtask1);
        final HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(jsonSubtask);
        HttpRequest requestSubtask = HttpRequest.newBuilder().uri(urlTasksSubtask).POST(body1).build();
        HttpResponse<String> responseSubtask = client.send(requestSubtask, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, responseSubtask.statusCode());
        assertEquals("", responseSubtask.body());
    }

    @Test
    void updateTasksTask() throws IOException, InterruptedException {
        Task task1 = HttpTaskServer.getManager().createTask(new Task("Задача 1", "...", Status.NEW,
                LocalDateTime.of(2000, 1, 3, 0, 0, 0), 15));
        HttpClient client = HttpClient.newHttpClient();
        String json = gson.toJson(task1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(urlTasksTask).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertEquals(task1, HttpTaskServer.getManager().getTaskById(1));
    }

    @Test
    void updateTasksEpic() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Эпик 1", "...", Status.NEW);
        HttpTaskServer.getManager().createEpic(epic1);
        HttpClient client = HttpClient.newHttpClient();
        String json = gson.toJson(epic1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(urlTasksEpic).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertEquals(epic1, HttpTaskServer.getManager().getEpicById(1));
    }

    //todo
    @Test
    void updateTasksSubtask() throws IOException, InterruptedException {
        Epic epic1 = HttpTaskServer.getManager().createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Subtask subtask1 = HttpTaskServer.getManager().createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.1",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0), 15));

        HttpClient client = HttpClient.newHttpClient();
        String jsonSubtask = gson.toJson(subtask1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonSubtask);
        HttpRequest request = HttpRequest.newBuilder().uri(urlTasksSubtask).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertEquals(subtask1, HttpTaskServer.getManager().getSubtaskById(2));
    }

    @Test
    void getTasksTaskId() throws IOException, InterruptedException {
        Task task1 = HttpTaskServer.getManager().createTask(new Task("Задача 1", "...", Status.NEW,
                LocalDateTime.of(2000, 1, 3, 0, 0, 0), 15));
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(urlTasksTaskId).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(1, HttpTaskServer.getManager().getHistory().size());
    }

    @Test
    void getTasksEpicId() throws IOException, InterruptedException {
        Epic epic1 = HttpTaskServer.getManager().createEpic(new Epic("Эпик 1", "...", Status.NEW));
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(urlTasksEpicId).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(1, HttpTaskServer.getManager().getHistory().size());
    }

    @Test
    void getTasksSubtasksId() throws IOException, InterruptedException {
        Epic epic1 = HttpTaskServer.getManager().createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Subtask subtask1 = HttpTaskServer.getManager().createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.1",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0), 15));
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(urlTasksSubtaskId).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(1, HttpTaskServer.getManager().getHistory().size());
    }

    @Test
    void getTasksTask() throws IOException, InterruptedException {
        Task task1 = HttpTaskServer.getManager().createTask(new Task("Задача 1", "...", Status.NEW,
                LocalDateTime.of(2000, 1, 3, 0, 0, 0), 15));
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(urlTasksTask).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void getTasksEpic() throws IOException, InterruptedException {
        Epic epic1 = HttpTaskServer.getManager().createEpic(new Epic("Эпик 1", "...", Status.NEW));
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(urlTasksEpic).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void getTasksSubtasks() throws IOException, InterruptedException {
        Epic epic1 = HttpTaskServer.getManager().createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Subtask subtask1 = HttpTaskServer.getManager().createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.1",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0), 15));
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(urlTasksSubtask).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void deleteTasksTaskId() throws IOException, InterruptedException {
        Task task1 = HttpTaskServer.getManager().createTask(new Task("Задача 1", "...", Status.NEW,
                LocalDateTime.of(2000, 1, 3, 0, 0, 0), 15));
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(urlTasksTaskId).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(0, HttpTaskServer.getManager().getTask().size());
    }

    @Test
    void deleteTasksEpicId() throws IOException, InterruptedException {
        Epic epic1 = HttpTaskServer.getManager().createEpic(new Epic("Эпик 1", "...", Status.NEW));
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(urlTasksEpicId).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(0, HttpTaskServer.getManager().getEpic().size());
    }

    @Test
    void deleteTasksSubtaskId() throws IOException, InterruptedException {
        Epic epic1 = HttpTaskServer.getManager().createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Subtask subtask1 = HttpTaskServer.getManager().createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.1",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0), 15));
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(urlTasksSubtaskId).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(0, HttpTaskServer.getManager().getSubtask().size());
    }

    @Test
    void deleteTasksTask() throws IOException, InterruptedException {
        Task task1 = HttpTaskServer.getManager().createTask(new Task("Задача 1", "...", Status.NEW,
                LocalDateTime.of(2000, 1, 3, 0, 0, 0), 15));
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(urlTasksTask).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(0, HttpTaskServer.getManager().getTask().size());
    }

    @Test
    void deleteTasksEpic() throws IOException, InterruptedException {
        Epic epic1 = HttpTaskServer.getManager().createEpic(new Epic("Эпик 1", "...", Status.NEW));
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(urlTasksEpic).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(0, HttpTaskServer.getManager().getEpic().size());
    }

    @Test
    void deleteTasksSubtask() throws IOException, InterruptedException {
        Epic epic1 = HttpTaskServer.getManager().createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Subtask subtask1 = HttpTaskServer.getManager().createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.1",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0), 15));
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(urlTasksSubtask).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(0, HttpTaskServer.getManager().getSubtask().size());
    }

    @Test
    void getTasks() throws IOException, InterruptedException {
        Task task1 = HttpTaskServer.getManager().createTask(new Task("Задача 1", "...", Status.NEW,
                LocalDateTime.of(2000, 1, 3, 0, 0, 0), 15));
        Epic epic1 = HttpTaskServer.getManager().createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Subtask subtask1 = HttpTaskServer.getManager().createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.1",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0), 15));
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(urlTasks).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void getTasksHistory() throws IOException, InterruptedException {
        Task task1 = HttpTaskServer.getManager().createTask(new Task("Задача 1", "...", Status.NEW,
                LocalDateTime.of(2000, 1, 3, 0, 0, 0), 15));
        Epic epic1 = HttpTaskServer.getManager().createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Subtask subtask1 = HttpTaskServer.getManager().createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.1",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0), 15));
        HttpTaskServer.getManager().getTaskById(1);
        HttpTaskServer.getManager().getSubtaskById(3);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(urlTasksHistory).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void getTasksSubtaskEpicId() throws IOException, InterruptedException {
        Epic epic1 = HttpTaskServer.getManager().createEpic(new Epic("Эпик 1", "...", Status.NEW));
        Subtask subtask1 = HttpTaskServer.getManager().createSubtask(new Subtask(epic1.getId(), "Сабтакс 1.1",
                "...", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0), 15));
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(urlTasksSubtaskEpicId).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }
}
