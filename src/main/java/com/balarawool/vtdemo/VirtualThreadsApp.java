package com.balarawool.vtdemo;

import com.balarawool.vtdemo.server.HttpMethod;
import com.balarawool.vtdemo.server.HttpResponse;
import com.balarawool.vtdemo.server.Server;
import com.balarawool.vtdemo.util.HttpUtil;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

// Run this with --add-opens=java.base/java.lang=ALL-UNNAMED
// Because there is a virtual-thread-reflection-trickery going on in class VirtualThreadHelper
// which requires access to VirtualThread package-private constructor.

public class VirtualThreadsApp {
    public static void main(String[] args) throws IOException {
        Server myServer = new Server(8000, true, 1);
        myServer.addRoute(HttpMethod.GET, "/testVT",
                (req) -> {
                    var response = HttpUtil.doSimpleGet("http://localhost:8001/greet");
                    return new HttpResponse.Builder()
                        .setStatusCode(200)
                        .addHeader("Content-Type", "text")
                        .setEntity(response)
                        .build();
                }
        );
        myServer.start();
    }
}