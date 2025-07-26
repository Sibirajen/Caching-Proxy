package com.sibirajen.cli.command;

import com.sibirajen.cli.args.PortValidator;
import com.sibirajen.cli.args.UrlValidator;
import com.sibirajen.server.ProxyServer;
import lombok.extern.log4j.Log4j2;
import picocli.CommandLine;

@Log4j2
@CommandLine.Command(
        name = "caching-proxy",
        version = "Caching Proxy version 1.0",
        description = "A command-line tool to run a caching proxy server that forwards and caches responses from a target server.",
        mixinStandardHelpOptions = true
)
public class CachingProxyCommand implements Runnable{
    @CommandLine.Option(
            names = {"-p", "--port"},
            description = "Port on which the caching proxy server will run (default: ${DEFAULT-VALUE}).",
            defaultValue = "9090",
            preprocessor = PortValidator.class
    )
    private int port;

    @CommandLine.Option(
            names = {"-o", "--origin"},
            description = "Origin server URL to which requests will be forwarded (e.g., http://example.com).",
            required = true,
            preprocessor = UrlValidator.class
    )
    private String origin;

    @Override
    public void run() {
        log.info("Starting proxy server on port {} with origin {}", port, origin);
        ProxyServer.start(this.port, this.origin);
    }
}
