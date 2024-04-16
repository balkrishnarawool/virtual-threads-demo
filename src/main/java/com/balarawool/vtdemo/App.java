package com.balarawool.vtdemo;

import com.balarawool.vtdemo.server.HttpMethod;
import com.balarawool.vtdemo.server.HttpResponse;
import com.balarawool.vtdemo.server.Server;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        Server myServer = new Server(8080);
        myServer.addRoute(HttpMethod.GET, "/testOne",
                (req) -> {
                    System.out.println("Request received");
                    try {
                        Thread.sleep(5*1_000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return new HttpResponse.Builder()
                            .setStatusCode(200)
                            .addHeader("Content-Type", "text/html")
                            .setEntity("<HTML> <P> Hello There... </P> </HTML>")
                            .build();
                }
        );
        myServer.start();
    }
}