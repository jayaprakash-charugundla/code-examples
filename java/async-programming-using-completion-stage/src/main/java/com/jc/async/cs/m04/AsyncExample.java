package com.jc.async.cs.m04;

import com.jc.async.cs.m04.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class AsyncExample {
    public static void main(String[] args) {
        //normal();
        //usingExecutor();
        //longRunning();
        usingMultipleExecutors();
    }

    private static void normal() {
        Supplier<List<Long>> supplyIds = () -> {
            sleep(200);
            return Arrays.asList(1L, 2L, 3L);
        };

        Function<List<Long>, List<User>> fetchUsers = ids -> {
            sleep(300);
            return ids.stream().map(User::new).collect(Collectors.toList());
        };

        Consumer<List<User>> displayUser = users -> {
            users.forEach(System.out::println);
        };

        CompletableFuture.supplyAsync(supplyIds)
                .thenApply(fetchUsers)
                .thenAccept(displayUser);
        sleep(1_000);
    }

    private static void usingExecutor() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Supplier<List<Long>> supplyIds = () -> {
            sleep(200);
            return Arrays.asList(1L, 2L, 3L);
        };

        Function<List<Long>, List<User>> fetchUsers = ids -> {
            sleep(300);
            return ids.stream().map(User::new).collect(Collectors.toList());
        };

        Consumer<List<User>> displayUser = users -> {
            System.out.println("Running in a thread - " + Thread.currentThread().getName());
            users.forEach(System.out::println);
        };

        CompletableFuture.supplyAsync(supplyIds)
                .thenApply(fetchUsers)
                .thenAcceptAsync(displayUser, executorService);
        sleep(1_000);
        executorService.shutdown();
    }

    private static void longRunning() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Supplier<List<Long>> supplyIds = () -> {
            sleep(200);
            return Arrays.asList(1L, 2L, 3L);
        };

        Function<List<Long>, CompletableFuture<List<User>>> fetchUsers = ids -> {
            sleep(300);
            Supplier<List<User>> userSupplier = () -> {
                System.out.println("Running fetchUsers in a thread - " + Thread.currentThread().getName());
                return ids.stream().map(User::new).collect(Collectors.toList());
            };
            return CompletableFuture.supplyAsync(userSupplier);
        };

        Consumer<List<User>> displayUser = users -> {
            System.out.println("Running displayUser in a thread - " + Thread.currentThread().getName());
            users.forEach(System.out::println);
        };

        CompletableFuture.supplyAsync(supplyIds)
                .thenCompose(fetchUsers)
                .thenAcceptAsync(displayUser, executorService);
        sleep(1_000);
        executorService.shutdown();
    }

    private static void usingMultipleExecutors() {
        ExecutorService executor1 = Executors.newSingleThreadExecutor();
        ExecutorService executor2 = Executors.newSingleThreadExecutor();
        Supplier<List<Long>> supplyIds = () -> {
            sleep(200);
            return Arrays.asList(1L, 2L, 3L);
        };

        Function<List<Long>, CompletableFuture<List<User>>> fetchUsers = ids -> {
            sleep(300);
            System.out.println("Before running fetchUsers in a thread - " + Thread.currentThread().getName());
            Supplier<List<User>> userSupplier = () -> {
                System.out.println("Running fetchUsers in a thread - " + Thread.currentThread().getName());
                return ids.stream().map(User::new).collect(Collectors.toList());
            };
            return CompletableFuture.supplyAsync(userSupplier, executor1);
        };

        Consumer<List<User>> displayUser = users -> {
            System.out.println("Running displayUser in a thread - " + Thread.currentThread().getName());
            users.forEach(System.out::println);
        };

        CompletableFuture.supplyAsync(supplyIds)
                .thenComposeAsync(fetchUsers, executor1)
                .thenAcceptAsync(displayUser, executor2);
        sleep(1_000);
        executor1.shutdown();
        executor2.shutdown();
    }

    private static void sleep(long timeInMillis) {
        try {
            Thread.sleep(timeInMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
