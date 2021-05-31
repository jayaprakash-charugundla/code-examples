package com.jc.async.cs.m03;

import java.util.concurrent.CompletableFuture;

public class SimpleCompletableFuture {
    public static void main(String[] args) {
        CompletableFuture<Void> cf = new CompletableFuture<>();
        Runnable task = () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cf.complete(null);
        };
        Void nil = cf.join();
        System.out.println("We are done");
    }
}
