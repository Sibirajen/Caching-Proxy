# Caching Proxy

This is a simple reverse proxy server implemented in Java using [Undertow](https://undertow.io/) that supports:

- Forwarding HTTP GET requests to a specified origin server
- In-memory caching of successful responses
- Automatic handling of restricted headers
- Logging of cache hits and misses

## Features

- Supports only `GET` requests (returns 405 for others)
- Caches responses in-memory using a simple LRU mechanism
- Reuses responses for repeated requests to the same URL

## Tech Stack

- Java 17+
- Undertow (HTTP server)
- Java HTTP Client (`java.net.http`)
- Lombok
- Log4j2

---

This project is a taken from [Caching-proxy](https://roadmap.sh/projects/caching-server)