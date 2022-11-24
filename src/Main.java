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
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите текст: ");
        String text = scanner.nextLine();
        System.out.print("Введите команду 'add' или 'search', для того чтобы закрыть программу напишите 'end': ");
        String request = null;
        HttpRequest httpRequest = null;
        int id = 1;
        label:
        while (true) {
            request = scanner.next();
            switch (request) {
                case "add":
                    String putText = """
                            {"text":""" +"\""+ text+"\"" + "}";
                    httpRequest = HttpRequest.newBuilder()
                            .uri(new URI("http://localhost:9200/test_index/_doc/" + id))
                            .header("Content-Type", "application/json")
                            .timeout(Duration.of(10, ChronoUnit.SECONDS))
                            .PUT(HttpRequest.BodyPublishers.ofString(putText))
                            .build();
                    id++;
                    break;
                case "search":
                    httpRequest = HttpRequest.newBuilder()
                            .uri(new URI("http://localhost:9200/test_index/_search"))
                            .timeout(Duration.of(10, ChronoUnit.SECONDS))
                            .GET()
                            .build();
                    break;
                case "end":
                    break label;
                default:
                    System.out.print("Введите правильную команду, для того чтобы закрыть программу напишите 'end': ");
                    break;
            }
            HttpResponse<String> response = HttpClient.newBuilder()
                    .build()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        }
    }
}
