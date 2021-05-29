package com.jc.async.cs;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class CompletableFutureWithSupplier {
    public static void main(String[] args) {
        //init();
        //usingExecutor();
        //forcingToComplete();
        usingObtrude();
    }

    private static void init() {
        Supplier<String> supplier = () -> Thread.currentThread().getName();

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(supplier);

        String value = completableFuture.join();
        System.out.println("Result: " + value);
    }

    private static void usingExecutor() {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Supplier<String> supplier = () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return Thread.currentThread().getName();
        };

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(supplier, executor);

        String value = completableFuture.join();
        System.out.println("Result: " + value);
        executor.shutdown();
    }

    private static void forcingToComplete() {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Supplier<String> supplier = () -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return Thread.currentThread().getName();
        };

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(supplier, executor);

        completableFuture.complete("Too long!");

        String value = completableFuture.join();
        System.out.println("Result: " + value);
        executor.shutdown();
    }

    private static void usingObtrude() {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Supplier<String> supplier = () -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return Thread.currentThread().getName();
        };

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(supplier, executor);

        String value = completableFuture.join();
        System.out.println("Result: " + value);

        completableFuture.obtrudeValue("Too long!");
        value = completableFuture.join();
        System.out.println("Result: " + value);

        executor.shutdown();
    }
}
