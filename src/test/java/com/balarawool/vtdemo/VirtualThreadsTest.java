package com.balarawool.vtdemo;

import com.balarawool.vtdemo.util.HttpUtil;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class VirtualThreadsTest {

    @Test
    // Set GreeterClientApp.USE_VIRTUAL_THREADS to false and start it.
    // Then set GreeterApp.PLATFORM_THREADS_POOL_SIZE to 1.
    // And start GreeterApp.
    void runWithGreeterAppThreadPool1() {
        runTest(3, 2, 4);
    }

    @Test
    // Set GreeterClientApp.USE_VIRTUAL_THREADS to false and start it.
    // Then set GreeterApp.PLATFORM_THREADS_POOL_SIZE to 10.
    // And start GreeterApp.
    void runWithGreeterAppThreadPool10() {
        runTest(10, 10, 11);
    }

    @Test
    // Set GreeterClientApp.USE_VIRTUAL_THREADS to false and start it.
    // Then set GreeterApp.PLATFORM_THREADS_POOL_SIZE to 100.
    // And start GreeterApp.
    void runWithGreeterAppThreadPool100() {
        runTest(100, 1, 109);
    }

    @Test
    // Set GreeterClientApp.USE_VIRTUAL_THREADS to true and start it.
    // Then set GreeterApp.PLATFORM_THREADS_POOL_SIZE to 1.
    // And start GreeterApp.
    void runWithVTAndGreeterAppThreadPool1() {
        runTest(3, 2, 4);
    }

    @Test
    // Set GreeterClientApp.USE_VIRTUAL_THREADS to true and start it.
    // Then set GreeterApp.PLATFORM_THREADS_POOL_SIZE to 10.
    // And start GreeterApp.
    void runWithVTAndGreeterAppThreadPool10() {
        runTest(10, 1, 2);
    }

    @Test
    // Set GreeterClientApp.USE_VIRTUAL_THREADS to true and start it.
    // Then set GreeterApp.PLATFORM_THREADS_POOL_SIZE to 100.
    // And start GreeterApp.
    void runWithVTAndGreeterAppThreadPool100() {
        runTest(100, 1, 2);
    }

    @Test
        // Set GreeterClientApp.USE_VIRTUAL_THREADS to true and start it.
        // Then set GreeterApp.PLATFORM_THREADS_POOL_SIZE to 1000.
        // And start GreeterApp.
    void runWithGreeterAppThreadPool1000() {
        runTest(1000, 1, 2);
    }

    void runTest(int numRequests, int minTime, int maxTime) {
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