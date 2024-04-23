package com.balarawool.vtdemo;

import com.balarawool.vtdemo.server.HttpMethod;
import com.balarawool.vtdemo.server.HttpResponse;
import com.balarawool.vtdemo.server.Server;

import java.io.IOException;

public class GreeterApp {
    public static int PLATFORM_THREADS_POOL_SIZE = 10;

    public static void main(String[] args) throws IOException {
        Server myServer = new Server(8001, false, PLATFORM_THREADS_POOL_SIZE);
        myServer.addRoute(HttpMethod.GET, "/greet",
                (req) -> {
                    try {
                        Thread.sleep(1_000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                return new HttpResponse.Builder()
                            .setStatusCode(200)
                            .addHeader("Content-Type", "text")
                            .setEntity("Hello")
                            .build();
                }
        );
        myServer.start();
    }
}