package com.jc.async.cs.m04;

import com.jc.async.cs.m04.model.Email;
import com.jc.async.cs.m04.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class AsyncExampleMultiBranch {
    public static void main(String[] args) {
        longRunning();
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

        Function<List<Long>, CompletableFuture<List<Email>>> fetchEmails = ids -> {
            sleep(300);
            Supplier<List<Email>> userSupplier = () -> {
                System.out.println("Running fetchUsers in a thread - " + Thread.currentThread().getName());
                return ids.stream().map(Email::new).collect(Collectors.toList());
            };
            return CompletableFuture.supplyAsync(userSupplier);
        };

        CompletableFuture<List<Long>> completableFuture = CompletableFuture.supplyAsync(supplyIds);
        CompletableFuture<List<User>> userFuture = completableFuture.thenCompose(fetchUsers);
        CompletableFuture<List<Email>> emailFuture = completableFuture.thenCompose(fetchEmails);

        userFuture.thenAcceptBoth(emailFuture, ((users, emails) -> {
            System.out.println(users.size() + " - " + emails.size());
        }));

        sleep(1_000);
        executorService.shutdown();
    }

    private static void sleep(long timeInMillis) {
        try {
            Thread.sleep(timeInMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
