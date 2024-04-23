package com.balarawool.vtdemo;

import com.balarawool.vtdemo.server.HttpMethod;
import com.balarawool.vtdemo.server.HttpResponse;
import com.balarawool.vtdemo.server.Server;

import java.io.IOException;

public class GreeterApp {
    public static void main(String[] args) throws IOException {
        var threadPoolSize = Integer.parseInt(args[0].split("--threadPoolSize=")[1]);

        Server myServer = new Server(8001, false, threadPoolSize);
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