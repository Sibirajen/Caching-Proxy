package com.sibirajen.server;

import io.undertow.Undertow;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ProxyServer {
    private static final String HOST = "localhost";

    public static void start(int port, String origin) {
        Undertow server = Undertow.builder()
                .addHttpListener(port, HOST)
                .setHandler(new ProxyHttpHandler(origin))
                .build();
        server.start();
        log.info("Listening on http://{}:{}", HOST, port);
    }

    public static void main(String[] args) {
        start(8080, "http://dummyjson.com");
    }
}
