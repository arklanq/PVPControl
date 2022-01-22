package com.gmail.nowyarek.pvpcontrol.components.TaskChain;

import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainTasks;

import java.util.concurrent.CompletableFuture;

public final class TaskChains {

    public static <T> CompletableFuture<T> wrapWithFuture(TaskChain<T> taskChain) {
        CompletableFuture<T> future = new CompletableFuture<>();

        taskChain.execute(
            (Boolean done) -> future.complete(null),
            (Exception e, TaskChainTasks.Task<?, ?> task) -> future.completeExceptionally(new TaskExecutionException(e, task))
        );

        return future;
    }

}
