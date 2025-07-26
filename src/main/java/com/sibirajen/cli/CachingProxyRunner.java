package com.sibirajen.cli;


import com.sibirajen.cli.command.CachingProxyCommand;
import com.sibirajen.cli.error.ShortErrorHandler;
import picocli.CommandLine;

public class CachingProxyRunner
{
    public static int run(String[] args) {
        return new CommandLine(new CachingProxyCommand())
                .setParameterExceptionHandler(new ShortErrorHandler())
                .execute(args);
    }

    public static void main( String[] args )
    {
        int exitCode = run(args);
        System.exit(exitCode);
    }
}
