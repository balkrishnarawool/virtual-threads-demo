package com.balarawool.vtdemo;

import com.balarawool.vtdemo.util.HttpUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class VirtualThreadsTest {

    @Test
    void runWithGreeterAppThreadPool1() {
        runTest(false, 1, 3, 2, 4);
    }

    @Test
    void runWithGreeterAppThreadPool10() {
        runTest(false, 10, 10, 10, 12);
    }

    @Test
    void runWithGreeterAppThreadPool100() {
        runTest(false, 100, 100, 100, 109);
    }

    @Test
    void runWithVTAndGreeterAppThreadPool1() {
        runTest(true, 1, 3, 2, 4);
    }

    @Test
    void runWithVTAndGreeterAppThreadPool10() {
        runTest(true, 10, 10, 1, 2);
    }

    @Test
    void runWithVTAndGreeterAppThreadPool100() {
        runTest(true, 100, 100, 1, 2);
    }

    @Test
    void runWithVTAndGreeterAppThreadPool1000() {
        runTest(true, 1000, 1000, 1, 2.5);
    }

    public void runTest(boolean useVirtualThreadsForClientApp, int threadPoolSizeForGreeterApp, int numRequests, int minTime, double maxTime) {
        var greeterClientApp = startApp(STR."com.balarawool.vtdemo.GreeterClientApp --useVT=\{useVirtualThreadsForClientApp}");
        var greeterApp = startApp(STR."com.balarawool.vtdemo.GreeterApp --threadPoolSize=\{threadPoolSizeForGreeterApp}");

        try {
            sendRequestsAndAssert(numRequests, minTime, maxTime);
        }
        finally {
            greeterClientApp.destroy();
            greeterApp.destroy();
        }
    }

    private Process startApp(String app) {
        var line = STR."""
                    /Users/TS90XD/.sdkman/candidates/java/23.ea.16-open/bin/java \
                        --enable-preview \
                        -classpath /Users/TS90XD/dev/java/loom/virtual-threads-demo/virtual-threads-demo/target/classes \
                        --add-opens=java.base/java.lang=ALL-UNNAMED \
                        \{app}
                    """;
        try {
            var process = new ProcessBuilder(line.trim().split("\s+")).start();
            //Thread.sleep(1_000); //Uncomment if process cannot be started properly in time.
            System.out.println(STR."App started: \{app}");
            return process;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void sendRequestsAndAssert(int numRequests, double minTime, double maxTime) {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            var t1 = System.currentTimeMillis();
            var tasks = IntStream.range(0, numRequests)
                    .mapToObj(i -> scope.fork(() -> HttpUtil.doSimpleGet("http://localhost:8000/testVT"))).toList();
            scope.join().throwIfFailed();
            var t2 = System.currentTimeMillis();

            tasks.stream().map(StructuredTaskScope.Subtask::get).forEach(System.out::println);
            tasks.stream().map(StructuredTaskScope.Subtask::get).forEach(msg -> assertEquals("Hello", msg));

            var time = (t2 - t1) / (1_000d);
            System.out.println(time);
            assertTrue( time > minTime && time < maxTime);

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}