package com.Eremej;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.Eremej.ParseUtils.*;

public class Tests {
    public interface SimpleTest {
        void test(String[] args);
    }

    public static class NetworkTest implements SimpleTest {
        public void test(String[] args) {
            System.out.println("testing networking...");

            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .build();

            HttpRequest request = null;
            try {
                request = HttpRequest.newBuilder()
                        .GET()
                        .uri(new URI("https://www.google.com"))
                        .build();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            HttpResponse<String> response = null;
            try {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());

                System.out.println("response body: ");
                System.out.println(response.body());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                System.out.println("shit happened");
                e.printStackTrace();
            }

        }
    }

    public static class NetworkTestAsync implements SimpleTest {
        @Override
        public void test(String[] args) {
            System.out.println("testing async networking...");

            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .build();

            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .GET()
                        .uri(new URI("https://www.google.com"))
                        .build();

                CompletableFuture<HttpResponse<String>> completableFuture = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

                System.out.print("waiting response");
                while (!completableFuture.isDone()) {
                    System.out.print(".");
                    Thread.sleep(100);
                }
                System.out.println();

                HttpResponse<String> response = completableFuture.get();

                System.out.println(response.body());
            } catch (URISyntaxException | ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                System.out.println("shit happened");
                e.printStackTrace();
            }

        }
    }

    public static class URLParseTest implements SimpleTest {
        @Override
        public void test(String[] args) {
            String[] inputs = new String[] {
                    "rutracker.org",
                    "https://anidub.com",
                    "http://www.flibusta.is",
                    "http://",
                    "qwerty",
                    "",
                    ".",
                    ".ru",
                    "hello.",
                    "www.",
                    "www.mac-ru.net",
                    "tr.anidub.com",
                    "a",
                    "https://check-host.net/subnet-calculator?host=tt.ru",
            };

            System.out.println("input: ");
            for (String input: inputs)
                System.out.println(input);

            System.out.println("\noutput: ");
            for (String input : inputs)
                if (parseFirstURL(input) != null)
                    System.out.println(parseFirstURL(input));
        }
    }

}
