package com.sibirajen.cli.args;

import picocli.CommandLine;

import java.net.ServerSocket;
import java.util.Map;
import java.util.Stack;

public class PortValidator implements CommandLine.IParameterPreprocessor {
    @Override
    public boolean preprocess(Stack<String> args, CommandLine.Model.CommandSpec commandSpec, CommandLine.Model.ArgSpec argSpec, Map<String, Object> info) {
        if (!args.isEmpty()) {
            String portStr = args.peek();
            int port;

            try {
                port = Integer.parseInt(portStr);
            } catch (NumberFormatException e) {
                throw new CommandLine.ParameterException(commandSpec.commandLine(),
                        "Invalid port number: " + portStr + ". Must be an integer between 1 and 65535.");
            }

            if (port < 1024 || port > 65535) {
                throw new CommandLine.ParameterException(commandSpec.commandLine(),
                        "Port must be between 1024 and 65535 (non-privileged).");
            }

            try (ServerSocket socket = new ServerSocket(port)) {
                socket.setReuseAddress(true);
            } catch (Exception e) {
                throw new CommandLine.ParameterException(commandSpec.commandLine(),
                        "Port " + port + " is already in use or unavailable.");
            }
        }

        return false;
    }
}
