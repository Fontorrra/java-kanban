package manager.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    private final String apiToken;
    private final int port;
    private final static String HOST = "localhost";
    private final static String REGISTER_ENDPOINT = "/register";

    public KVTaskClient(int port) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            this.port = port;
            URI newUrl = URI.create("http://" + HOST + ":" + port + REGISTER_ENDPOINT);
            HttpRequest request = HttpRequest.newBuilder().uri(newUrl).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            apiToken = response.body();
        } catch (IOException | InterruptedException e) {
            throw new KVTaskClientException();
        }
    }

    String getApiToken() {
        return apiToken;
    }

    public void put(String key, String json) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:" + port + "/save/" + key + "?API_TOKEN=" + apiToken);
            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(json)).build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new KVTaskClientException();
        }
    }

    public String load(String key){
        try {
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:" + port + "/load/" + key + "?API_TOKEN=" + apiToken);
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new KVTaskClientException();
        }
    }
}
