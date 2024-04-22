package com.balarawool.vtdemo;

import com.balarawool.vtdemo.util.HttpUtil;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class VirtualThreadsAppTest {


    @Test
    // Make sure VirtualThreadsApp is running.
    // Then set GreeterApp.PLATFORM_THREADS_POOL_SIZE to 1.
    // And start GreeterApp.
    void runWithGreeterAppThreadPool1() {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            var t1 = System.currentTimeMillis();
            var tasks = IntStream.range(0, 3)
                    .mapToObj(i -> scope.fork(() -> HttpUtil.doSimpleGet("http://localhost:8000/testVT"))).toList();
            scope.join().throwIfFailed();
            var t2 = System.currentTimeMillis();

            tasks.stream().map(StructuredTaskScope.Subtask::get).forEach(System.out::println);
            tasks.stream().map(StructuredTaskScope.Subtask::get).forEach(msg -> assertEquals("Hello", msg));

            var time = (t2 - t1) / (1_000d);
            assertTrue( time > 2 && time < 4);

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Test
    // Make sure VirtualThreadsApp is running.
    // Then set GreeterApp.PLATFORM_THREADS_POOL_SIZE to 10.
    // And start GreeterApp.
    void runWithGreeterAppThreadPool10() {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            var t1 = System.currentTimeMillis();
            var tasks = IntStream.range(0, 10)
                    .mapToObj(i -> scope.fork(() -> HttpUtil.doSimpleGet("http://localhost:8000/testVT"))).toList();
            scope.join().throwIfFailed();
            var t2 = System.currentTimeMillis();

            tasks.stream().map(StructuredTaskScope.Subtask::get).forEach(System.out::println);
            tasks.stream().map(StructuredTaskScope.Subtask::get).forEach(msg -> assertEquals("Hello", msg));

            var time = (t2 - t1) / (1_000d);
            assertTrue( time > 1 && time < 2);

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Test
    // Make sure VirtualThreadsApp is running.
    // Then set GreeterApp.PLATFORM_THREADS_POOL_SIZE to 100.
    // And start GreeterApp.
    void runWithGreeterAppThreadPool100() {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            var t1 = System.currentTimeMillis();
            var tasks = IntStream.range(0, 100)
                    .mapToObj(i -> scope.fork(() -> HttpUtil.doSimpleGet("http://localhost:8000/testVT"))).toList();
            scope.join().throwIfFailed();
            var t2 = System.currentTimeMillis();

            tasks.stream().map(StructuredTaskScope.Subtask::get).forEach(System.out::println);
            tasks.stream().map(StructuredTaskScope.Subtask::get).forEach(msg -> assertEquals("Hello", msg));

            var time = (t2 - t1) / (1_000d);
            assertTrue( time > 1 && time < 2);

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Test
    // Make sure VirtualThreadsApp is running.
    // Then set GreeterApp.PLATFORM_THREADS_POOL_SIZE to 1000.
    // And start GreeterApp.
    void runWithGreeterAppThreadPool1000() {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            var t1 = System.currentTimeMillis();
            var tasks = IntStream.range(0, 1000)
                    .mapToObj(i -> scope.fork(() -> HttpUtil.doSimpleGet("http://localhost:8000/testVT"))).toList();
            scope.join().throwIfFailed();
            var t2 = System.currentTimeMillis();

            tasks.stream().map(StructuredTaskScope.Subtask::get).forEach(System.out::println);
            tasks.stream().map(StructuredTaskScope.Subtask::get).forEach(msg -> assertEquals("Hello", msg));

            var time = (t2 - t1) / (1_000d);
            assertTrue( time > 1 && time < 2);

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}