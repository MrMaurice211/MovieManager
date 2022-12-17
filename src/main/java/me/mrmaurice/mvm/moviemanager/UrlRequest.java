package me.mrmaurice.mvm.moviemanager;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class UrlRequest {

    private final Map<String, String> parameters = new HashMap<>();
    private final Map<String, String> headers = new HashMap<>();
    private final String requestUrl;

    public UrlRequest(String requestUrl, Object... params) {
        this.requestUrl = String.format(requestUrl, params);
    }

    public UrlRequest header(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public UrlRequest param(String name, String value) {
        parameters.put(name, value);
        return this;
    }

    public <T> T doCall(Type type) {
        try {

            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .connectTimeout(Duration.ofSeconds(30))
                    .build();

            String finalUrl = requestUrl + getParams();
            System.out.println("Doing request to: " + finalUrl);

            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(finalUrl))
                    .timeout(Duration.ofSeconds(30))
                    .GET();
            headers.forEach(builder::header);

            HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
            String body = response.body();
            System.out.println(body);
            return new Gson().fromJson(response.body(), type);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getParams() {

        if (parameters.isEmpty())
            return "";

        StringJoiner joiner = new StringJoiner("&", "?", "");
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            joiner.add(entry.getKey() + "=" + entry.getValue());
        }
        return joiner.toString();
    }

}
