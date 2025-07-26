package com.sibirajen.server;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class ProxyHttpHandler implements HttpHandler {
    private final String origin;

    @Override
    public void handleRequest(HttpServerExchange httpServerExchange) throws Exception {
        final String requestURI = httpServerExchange.getRequestURI();
        final String forwardRequestURL = origin + requestURI;
        log.info("{}", forwardRequestURL);
    }
}
