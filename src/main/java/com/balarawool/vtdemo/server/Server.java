package com.balarawool.vtdemo.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Server {

    private final Map<String, RequestRunner> routes;
    private final ServerSocket socket;
    private final Executor threadPool;
    private HttpHandler handler;

    public Server(int port) throws IOException {
        routes = new HashMap<>();
        threadPool = Executors.newFixedThreadPool(1);
        socket = new ServerSocket(port);
    }

    public void start() throws IOException {
        handler = new HttpHandler(routes);

        while (true) {
            Socket clientConnection = socket.accept();
            handleConnection(clientConnection);
        }
    }

    /*
     * Capture each Request / Response lifecycle in a thread
     * executed on the threadPool.
     */
    private void handleConnection(Socket clientConnection) {
        Runnable httpRequestRunner = () -> {
            try {
                handler.handleConnection(clientConnection.getInputStream(), clientConnection.getOutputStream());
            } catch (IOException ignored) {
            }
        };
        threadPool.execute(httpRequestRunner);
    }

    public void addRoute(final HttpMethod opCode, final String route, final RequestRunner runner) {
        routes.put(opCode.name().concat(route), runner);
    }
}