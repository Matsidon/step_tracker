package managers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class HttpTaskServer {
    private static TaskManager manager;
    private static Gson gson;
    private final HttpServer httpServer;

    public HttpTaskServer() throws IOException {
        int port = 8080;
        manager = Managers.getDefault();
        gson = new Gson();
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(port), 0);
        httpServer.createContext("/tasks/task/", this::taskHandle);
        httpServer.createContext("/tasks/epic/", this::epicHandle);
        httpServer.createContext("/tasks/subtask/", this::subtaskHandle);
        httpServer.createContext("/tasks/subtask/epic/", this::epicSubtasksHandler);
        httpServer.createContext("/tasks/history", this::tasksHistoryHandler);
        httpServer.createContext("/tasks/", this::prioritizedTasksHandler);
        httpServer.start();
    }

    public static TaskManager getManager() {
        return manager;
    }

    private void taskHandle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        String taskId = httpExchange.getRequestURI().getQuery(); //вид: "id=номер"

        switch (method) {
            case "GET":
                if (path.endsWith("/task/")) {// GET/tasks/task/
                    if (taskId == null) {
                        httpExchange.sendResponseHeaders(200, 0);
                        String tasks = gson.toJson(manager.getTask());
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            outputStream.write(tasks.getBytes(StandardCharsets.UTF_8));
                        }
                        httpExchange.close();
                    } else { // GET/tasks/task/?id=
                        int id = Integer.parseInt(taskId.substring(3));
                        try {
                            manager.getTaskById(id);
                        } catch (NullPointerException e) {
                            httpExchange.sendResponseHeaders(400, 0);
                            httpExchange.close();
                            return;
                        }
                        httpExchange.sendResponseHeaders(200, 0);
                        String taskById = gson.toJson(manager.getTaskById(id));
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            outputStream.write(taskById.getBytes(StandardCharsets.UTF_8));
                        }
                        httpExchange.close();
                    }
                } else {
                    httpExchange.sendResponseHeaders(400, 0);
                    httpExchange.close();
                }
                break;
            case "POST":// POST/tasks/task/Body{task...}
                if (path.endsWith("/task/")) {
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    Task task = gson.fromJson(body, Task.class);
                    if (getTasksId().contains(task.getId())) {// обновленная задача
                        try {
                            manager.updateTask(task);
                            httpExchange.sendResponseHeaders(201, 0);
                        } catch (ManagerSaveException e) {
                            httpExchange.sendResponseHeaders(400, 0);
                        } finally {
                            inputStream.close();
                            httpExchange.close();
                        }
                    } else { // новая задача
                        try {
                            manager.createTask(task);
                            httpExchange.sendResponseHeaders(201, 0);
                        } catch (ManagerSaveException e) {
                            httpExchange.sendResponseHeaders(400, 0);
                        } finally {
                            inputStream.close();
                            httpExchange.close();
                        }
                    }
                } else {
                    httpExchange.sendResponseHeaders(400, 0);
                    httpExchange.close();
                }
            case "DELETE":
                if (path.endsWith("/task/")) {// DELETE/tasks/task/
                    if (taskId == null) {
                        httpExchange.sendResponseHeaders(200, 0);
                        manager.removeAllTasks();
                        httpExchange.close();
                    } else { // DELETE/tasks/task/?id=
                        int id = Integer.parseInt(taskId.substring(3));
                        if (getTasksId().contains(id)) {
                            httpExchange.sendResponseHeaders(200, 0);
                            manager.removeTaskById(id);
                            httpExchange.close();
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                            httpExchange.close();
                        }
                    }
                } else {
                    httpExchange.sendResponseHeaders(400, 0);
                    httpExchange.close();
                }
                break;
            default:
                httpExchange.sendResponseHeaders(404, 0);
                httpExchange.close();
                break;
        }
    }

    private void epicHandle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        String epicId = httpExchange.getRequestURI().getQuery(); //вид: "id=номер"

        switch (method) {
            case "GET":
                if (path.endsWith("/epic/")) { // GET/tasks/epic/
                    if (epicId == null) {
                        httpExchange.sendResponseHeaders(200, 0);
                        String epics = gson.toJson(manager.getEpic());
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            outputStream.write(epics.getBytes(StandardCharsets.UTF_8));
                        }
                        httpExchange.close();
                    } else { // GET/tasks/epic/?id=
                        int id = Integer.parseInt(epicId.substring(3));
                        try {
                            manager.getEpicById(id);
                        } catch (NullPointerException e) {
                            httpExchange.sendResponseHeaders(400, 0);
                            httpExchange.close();
                            return;
                        }
                        httpExchange.sendResponseHeaders(200, 0);
                        String epic = gson.toJson(manager.getEpicById(id));
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            outputStream.write(epic.getBytes(StandardCharsets.UTF_8));
                        }
                        httpExchange.close();
                    }
                } else {
                    httpExchange.sendResponseHeaders(404, 0);
                    httpExchange.close();
                }
                break;
            case "POST":
                if (path.endsWith("/epic/")) { // POST/tasks/epic/
                    httpExchange.sendResponseHeaders(201, 0);
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    Epic epic = gson.fromJson(body, Epic.class);
                    if (getEpicsId().contains(epic.getId())) { // обновление epic
                        try {
                            manager.updateEpic(epic);
                            httpExchange.sendResponseHeaders(201, 0);
                        } catch (ManagerSaveException e) {
                            httpExchange.sendResponseHeaders(400, 0);
                        } finally {
                            inputStream.close();
                            httpExchange.close();
                        }
                    } else { // создание epic
                        try {
                            manager.createEpic(epic);
                            httpExchange.sendResponseHeaders(201, 0);
                        } catch (ManagerSaveException e) {
                            httpExchange.sendResponseHeaders(400, 0);
                        } finally {
                            inputStream.close();
                            httpExchange.close();
                        }
                    }
                } else {
                    httpExchange.sendResponseHeaders(400, 0);
                    httpExchange.close();
                }
                break;
            case "DELETE":
                if (path.endsWith("/epic/")) { // DELETE/tasks/epic/
                    if (epicId == null) {
                        httpExchange.sendResponseHeaders(200, 0);
                        manager.removeAllEpics();
                        httpExchange.close();
                    } else { // DELETE/tasks/epic/?id=
                        int id = Integer.parseInt(epicId.substring(3));
                        if (getEpicsId().contains(id)) {
                            httpExchange.sendResponseHeaders(200, 0);
                            manager.removeEpicById(id);
                            httpExchange.close();
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                            httpExchange.close();
                        }
                    }
                } else {
                    httpExchange.sendResponseHeaders(400, 0);
                    httpExchange.close();
                }
                break;
            default:
                httpExchange.sendResponseHeaders(404, 0);
                httpExchange.close();
                break;
        }
    }

    private void subtaskHandle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        String subtaskId = httpExchange.getRequestURI().getQuery(); //вид: "id=номер"

        switch (method) {
            case "GET":
                if (path.endsWith("/subtask/")) { // GET/tasks/subtask/
                    if (subtaskId == null) {
                        httpExchange.sendResponseHeaders(200, 0);
                        String subtasks = gson.toJson(manager.getSubtask());
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            outputStream.write(subtasks.getBytes(StandardCharsets.UTF_8));
                        }
                        httpExchange.close();
                    } else { // GET/tasks/subtask/?id=
                        int id = Integer.parseInt(subtaskId.substring(3));
                        try {
                            manager.getSubtaskById(id);
                        } catch (NullPointerException e) {
                            httpExchange.sendResponseHeaders(400, 0);
                            httpExchange.close();
                            return;
                        }
                        httpExchange.sendResponseHeaders(200, 0);
                        String subtask = gson.toJson(manager.getSubtaskById(id));
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            outputStream.write(subtask.getBytes(StandardCharsets.UTF_8));
                        }
                        httpExchange.close();
                    }
                } else {
                    httpExchange.sendResponseHeaders(400, 0);
                    httpExchange.close();
                }
                break;
            case "POST":
                if (path.endsWith("/subtask/")) { // POST/tasks/subtask/?id=
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    Subtask subtask = gson.fromJson(body, Subtask.class);
                    if (getSubtasksId().contains(subtask.getId())) { // обновление
                        try {
                            manager.updateSubtask(subtask);
                            httpExchange.sendResponseHeaders(201, 0);
                        } catch (ManagerSaveException e) {
                            httpExchange.sendResponseHeaders(400, 0);
                        } finally {
                            httpExchange.close();
                            inputStream.close();
                        }
                    } else { // создание
                        try {
                            manager.createSubtask(subtask);
                            httpExchange.sendResponseHeaders(201, 0);
                        } catch (ManagerSaveException e) {
                            httpExchange.sendResponseHeaders(400, 0);
                        } finally {
                            inputStream.close();
                            httpExchange.close();
                        }
                    }
                } else {
                    httpExchange.sendResponseHeaders(400, 0);
                    httpExchange.close();
                }
                break;
            case "DELETE":
                if (path.endsWith("/subtask/")) { // DELETE/tasks/subtask/
                    if (subtaskId == null) {
                        httpExchange.sendResponseHeaders(200, 0);
                        manager.removeAllSubtasks();
                        httpExchange.close();
                    } else { // DELETE/tasks/subtask/?id=
                        int id = Integer.parseInt(subtaskId.substring(3));
                        if (getSubtasksId().contains(id)) {
                            manager.removeSubtaskById(id);
                            httpExchange.sendResponseHeaders(200, 0);
                            httpExchange.close();
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                            httpExchange.close();
                        }
                    }
                } else {
                    httpExchange.sendResponseHeaders(400, 0);
                    httpExchange.close();
                }
                break;
            default:
                httpExchange.sendResponseHeaders(404, 0);
                httpExchange.close();
                break;
        }
    }

    private void epicSubtasksHandler(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        String epicId = httpExchange.getRequestURI().getQuery();
        System.out.println(path);
        System.out.println(epicId);
        switch (method) { //switch вместо ветвления для повышения читабельности
            case "GET":
                if (path.endsWith("/epic/") && epicId != null) { // GET/subtask/epic/?id=
                    int id = Integer.parseInt(epicId.substring(3));
                    try {
                        manager.getListOfSubtaskByEpic(id);
                    } catch (NullPointerException e) {
                        httpExchange.sendResponseHeaders(400, 0);
                        httpExchange.close();
                        return;
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                    String subtasks = gson.toJson(manager.getListOfSubtaskByEpic(id));
                    try (OutputStream outputStream = httpExchange.getResponseBody()) {
                        outputStream.write(subtasks.getBytes(StandardCharsets.UTF_8));
                    }
                    httpExchange.close();
                } else { // GET/subtask/epic/?id=
                    httpExchange.sendResponseHeaders(400, 0);
                    httpExchange.close();
                }
                break;
            default:
                httpExchange.sendResponseHeaders(404, 0);
                httpExchange.close();
                break;
        }
    }

    private void tasksHistoryHandler(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();

        switch (method) {
            case "GET":
                if (path.endsWith("/history")) { // GET/tasks/history
                    httpExchange.sendResponseHeaders(200, 0);
                    String historyList = gson.toJson(manager.getHistory());
                    try (OutputStream outputStream = httpExchange.getResponseBody()) {
                        outputStream.write(historyList.getBytes(StandardCharsets.UTF_8));
                    }
                    httpExchange.close();
                } else {
                    httpExchange.sendResponseHeaders(400, 0);
                    httpExchange.close();
                }
                break;
            default:
                httpExchange.sendResponseHeaders(400, 0);
                httpExchange.close();
                break;
        }
    }

    private void prioritizedTasksHandler(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();

        switch (method) {
            case "GET": // GET/tasks/
                httpExchange.sendResponseHeaders(200, 0);
                String tasksList = gson.toJson(manager.getPrioritizedTasks());
                try (OutputStream outputStream = httpExchange.getResponseBody()) {
                    outputStream.write(tasksList.getBytes(StandardCharsets.UTF_8));
                }
                httpExchange.close();
                break;
            default:
                httpExchange.sendResponseHeaders(400, 0);
                httpExchange.close();
                break;
        }
    }

    public void stopServer() {
        httpServer.stop(0);
    }

    private ArrayList<Integer> getTasksId() {
        ArrayList<Integer> tasksId = new ArrayList<>();
        if (manager.getTask() != null) {
            for (Task task1 : manager.getTask()) {
                tasksId.add(task1.getId());
            }
        }
        return tasksId;
    }

    private ArrayList<Integer> getEpicsId() {
        ArrayList<Integer> epicsId = new ArrayList<>();
        if (manager.getEpic() != null) {
            for (Task task1 : manager.getEpic()) {
                epicsId.add(task1.getId());
            }
        }
        return epicsId;
    }

    private ArrayList<Integer> getSubtasksId() {
        ArrayList<Integer> subtasksId = new ArrayList<>();
        if (manager.getSubtask() != null) {
            for (Task task1 : manager.getSubtask()) {
                subtasksId.add(task1.getId());
            }
        }
        return subtasksId;
    }
}

