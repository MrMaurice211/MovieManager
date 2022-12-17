package me.mrmaurice.mvm.moviemanager;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class PostRequest {

    private HttpRequest.BodyPublisher postBody = HttpRequest.BodyPublishers.noBody();
    private final Map<String, String> parameters = new HashMap<>();
    private final Map<String, String> headers = new HashMap<>();
    private final String requestUrl;

    public PostRequest(String requestUrl, Object... params) {
        this.requestUrl = String.format(requestUrl, params);
    }

    public PostRequest header(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public PostRequest param(String name, String value) {
        parameters.put(name, value);
        return this;
    }

    public PostRequest stringBody(String data) {
        postBody = HttpRequest.BodyPublishers.ofString(data);
        return this;
    }

    public PostRequest jsonBody(Object object) {
        postBody = HttpRequest.BodyPublishers.ofString(new Gson().toJson(object));
        System.out.println("Post body: " + new Gson().toJson(object));
        return this;
    }

    public PostRequest fileBody(File file) {
        try {
            postBody = HttpRequest.BodyPublishers.ofFile(file.toPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return this;
    }

    public PostRequest inputStreamBody(InputStream is) {
        postBody = HttpRequest.BodyPublishers.ofInputStream(() -> is);
        return this;
    }

    public PostRequest byteArrayBody(byte[] data) {
        postBody = HttpRequest.BodyPublishers.ofByteArray(data);
        return this;
    }

    public <T> T execute(Type returnType) {
        try {

            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .connectTimeout(Duration.ofSeconds(30))
                    .build();

            String finalUrl = requestUrl + getParams();
            System.out.println("Doing POST request to: " + finalUrl);

            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(finalUrl))
                    .timeout(Duration.ofSeconds(30))
                    .POST(postBody);
            headers.forEach(builder::header);

            HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
            String body = response.body();
            System.out.println("Response body: " + body);
            return new Gson().fromJson(response.body(), returnType);
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