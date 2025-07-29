package com.sibirajen.server;

import com.sibirajen.cache.InMemoryCacheManager;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.http.HttpResponse;

@Log4j2
@RequiredArgsConstructor
public class ProxyHttpHandler implements HttpHandler {
    private final String origin;
    private final InMemoryCacheManager<String, HttpResponse<String>> cacheManager = new InMemoryCacheManager<>();

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        if (!exchange.getRequestMethod().toString().equalsIgnoreCase("GET")){
            exchange.setStatusCode(405);
            exchange.getResponseSender().send("Only GET requests are supported");
            return;
        }

        final String requestURI = exchange.getRequestURI();
        final String forwardRequestURL = this.origin + requestURI;

        HttpResponse<String> response;
        boolean fromCache = false;

        if (this.cacheManager.contains(forwardRequestURL)){
            log.info("[CACHE HIT] Serving from cache for {}", requestURI);
            response = this.cacheManager.get(forwardRequestURL);
            fromCache = true;
        }
        else{
            log.info("[CACHE MISS] Forwarding to origin: {}", forwardRequestURL);
            try {
                response = ForwardHttp.send(forwardRequestURL, exchange.getRequestHeaders());
                if (response.statusCode() >= 200 && response.statusCode() < 300) {
                    this.cacheManager.put(forwardRequestURL, response);
                }
            } catch (IOException | InterruptedException e) {
                log.error("Error forwarding request", e);
                exchange.setStatusCode(502);
                exchange.getResponseSender().send("Failed to fetch from origin.");
                return;
            }
        }

        response.headers().map().forEach(
                (header, values) ->
                        values.forEach(value ->
                                exchange.getResponseHeaders().add(new HttpString(header), value)
                        )
        );
        exchange.getResponseHeaders().add(new HttpString("X-Cache"), fromCache ? "HIT" : "MISS");
        exchange.setStatusCode(response.statusCode());
        exchange.getResponseSender().send(response.body());
    }
}
