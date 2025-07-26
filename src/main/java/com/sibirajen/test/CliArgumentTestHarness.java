package com.sibirajen.test;

import com.sibirajen.cli.CachingProxyRunner;

public class CliArgumentTestHarness {
    public static void main(String[] args) {
        String[][] commands = {
                // ---------------------
                // ✅ Positive Test Cases
                // ---------------------
                {"-h"}, // help
                {"-V"}, // version

                {"-p", "8080", "-o", "http://example.com"},                      // valid HTTP
                {"--port", "9091", "--origin", "https://dummyjson.com"},        // valid HTTPS
                {"-o", "https://google.com"},                                   // default port
                {"--origin", "https://api.example.com", "--port", "8082"},      // long flags
                {"-p", "65535", "-o", "https://127.0.0.1"},                      // max port
                {"--port", "3000", "--origin", "https://localhost:3000"},       // with port in origin URL
                {"-p", "8080", "-o", "http://localhost/path/to/resource"},      // with path
                {"-p", "8080", "-o", "https://www.example.com?query=param"},    // with query params

                // ----------------------
                // ❌ Negative Test Cases
                // ----------------------
                {"-p", "80", "-o", "https://localhost"},
                {"-p", "8080"},                                     // missing origin
                {"-p", "8080", "-o"},                               // missing value for origin
                {"-p", "8080", "-o", "ftp://example.com"},          // unsupported URL scheme
                {"-o", "http:/incomplete.com"},                     // malformed URL
                {"-x", "123", "-o", "https://example.com"},         // unknown option
                {"-p", "notANumber", "-o", "https://example.com"},  // non-integer port
                {"-p", "-1", "-o", "https://example.com"},          // invalid port: negative
                {"-p", "70000", "-o", "https://example.com"},       // invalid port: too large
                {"-p", "", "-o", "https://example.com"},            // empty port string
                {"-p"},                                             // missing both port and origin
                {"--origin"},                                       // missing origin value
                {"--origin", "", "-p", "8080"},                     // empty origin string
                {"-p", "8080", "--origin", "example.com"},          // origin without scheme
                {"--port", "8080", "--origin", "ht!tp://invalid"},  // origin with invalid characters
        };


        for (String[] command: commands){
            System.out.println("-----------------------------------------------------------------------------");
            System.out.println("Running command: caching-proxy " + String.join(" ", command));
            try {
                CachingProxyRunner.run(command);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
