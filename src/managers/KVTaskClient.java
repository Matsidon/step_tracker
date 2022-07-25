package managers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String url;
    private static String apiToken;

    public KVTaskClient(String url) {
        this.url = url;
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create(url + "/register");
        HttpRequest httpRequest = HttpRequest.newBuilder().GET().uri(uri).build();
        try {
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (httpResponse.statusCode() == 200) {
                apiToken = httpResponse.body();
            } else {
                throw new ManagerRegisterException("Проблемы с регистрацией на KVServer");
            }
        } catch (IOException | InterruptedException e) {
            throw new ManagerRegisterException("Проблемы с регистрацией на KVServer");
        }
    }

    void put(String key, String json) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create(url + "/save/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest httpRequest = HttpRequest.newBuilder().POST(body).uri(uri).build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(httpResponse.statusCode());
    }

    static String load(String key) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8078" + "/load/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest httpRequest = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return httpResponse.body();
    }
}
