package com.Eremej;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.Eremej.ParseUtils.*;

public abstract class InputLoaders {
    public static InputLoader getCheckHostLoader() {
        return new InputLoader() {
            final String CHECK_HOST_URL = "https://check-host.net/ip-info?host=";
            final String CHECK_SUBNET_URL = "https://check-host.net/subnet-calculator?host=";

            @Override
            public String loadRaw(String query) throws IOException, InterruptedException {
                HttpClient client = HttpClient.newBuilder()
                        .version(HttpClient.Version.HTTP_2)
                        .build();

                try {
                    return client.send(HttpRequest.newBuilder()
                            .GET()
                            .uri(new URI(CHECK_SUBNET_URL + parseFirstIPRange(
                                    client.send(HttpRequest.newBuilder()
                                            .GET()
                                            .uri(new URI(CHECK_HOST_URL + query))
                                            .build(), HttpResponse.BodyHandlers.ofString()).body()
                            )))
                            .build(), HttpResponse.BodyHandlers.ofString()).body();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                    return "";
                }
            }
        };
    }
}
