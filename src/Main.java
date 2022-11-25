import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        String text = null;
        if (args[0].equals("-s")) {
            text = args[1];
        }
        if (args[2].equals("-e")) {
            String request;
            HttpRequest httpRequest = null;

            request = args[3];
            switch (request) {
                case "add" -> {
                    String putText = """
                            {"text":""" + "\"" + text + "\"" + "}";
                    httpRequest = HttpRequest.newBuilder()
                            .uri(new URI("http://localhost:9200/test_index/_doc/1"))
                            .header("Content-Type", "application/json")
                            .timeout(Duration.of(10, ChronoUnit.SECONDS))
                            .PUT(HttpRequest.BodyPublishers.ofString(putText))
                            .build();

                }
                case "search" -> httpRequest = HttpRequest.newBuilder()
                        .uri(new URI("http://localhost:9200/test_index/_search"))
                        .timeout(Duration.of(10, ChronoUnit.SECONDS))
                        .GET()
                        .build();
                default -> {
                }
            }
            HttpResponse<String> response = HttpClient.newBuilder()
                    .build()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());
            System.out.println(response.body());

        }
    }
}
