package com.jc.async.cs;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FirstCompletableFutures {
    public static void main(String[] args) throws Exception {
        //initial();
        usingExecutor();
    }

    private static void initial() throws InterruptedException {
        CompletableFuture.runAsync(() -> System.out.print("I am running asynchronously in a thread: " +
                Thread.currentThread().getName()));
        Thread.sleep(500);
    }

    private static void usingExecutor() throws InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Runnable task = () -> {
            System.out.print("I am running asynchronously in a thread: " + Thread.currentThread().getName());
        };
        CompletableFuture.runAsync(task, executor);
        //Thread.sleep(500);
        // Initiates an orderly shutdown in which previously submitted tasks are executed,
        // but no new tasks will be accepteed. Invocation has no additional effect if already shut down
        executor.shutdown();
    }
}
