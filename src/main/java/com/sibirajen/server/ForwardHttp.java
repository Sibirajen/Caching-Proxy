package com.sibirajen.server;

import io.undertow.util.HeaderMap;
import io.undertow.util.HeaderValues;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Set;

public class ForwardHttp {

    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(20))
            .build();

    private static final Set<String> restrictedHeaders = Set.of(
            "connection", "host", "content-length", "expect",
            "upgrade", "transfer-encoding", "te"
    );

    public static HttpResponse<String> send(String url, HeaderMap headers)
            throws IOException, InterruptedException {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("Forward URL cannot be blank");
        }

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(20))
                .GET();

        if (headers != null){
            for (HeaderValues hv: headers){
                String headerName = hv.getHeaderName().toString();
                if (restrictedHeaders.contains(headerName.toLowerCase())) {
                    continue;
                }
                for (String value : hv) {
                    builder.header(headerName, value);
                }
            }
        }

        return CLIENT.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }
}
