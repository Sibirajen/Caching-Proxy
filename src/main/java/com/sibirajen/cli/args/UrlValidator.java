package com.sibirajen.cli.args;

import picocli.CommandLine;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Stack;

public class UrlValidator implements CommandLine.IParameterPreprocessor {
    @Override
    public boolean preprocess(Stack<String> args, CommandLine.Model.CommandSpec commandSpec,
                              CommandLine.Model.ArgSpec argSpec, Map<String, Object> info) {
        if (!args.isEmpty()) {
            String value = args.peek();

            if (!value.startsWith("http://") && !value.startsWith("https://")) {
                throw new CommandLine.ParameterException(commandSpec.commandLine(),
                        "Invalid origin URL: must start with http:// or https://");
            }

            try {
                new URL(value);
            } catch (MalformedURLException e) {
                throw new CommandLine.ParameterException(commandSpec.commandLine(),
                        "Invalid origin URL format: " + e.getMessage());
            }
        }

        return false;
    }
}
