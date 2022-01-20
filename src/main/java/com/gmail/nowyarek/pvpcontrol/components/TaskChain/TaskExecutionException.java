package com.gmail.nowyarek.pvpcontrol.components.TaskChain;

import co.aikar.taskchain.TaskChainTasks.Task;

public class TaskExecutionException extends Exception {

    public TaskExecutionException(Throwable cause, Task<?, ?> task) {
        super(
            String.format(
                "Task with index `%s` failed to execute.",
                task.getCurrentChain().getCurrentActionIndex()
            ),
            cause
        );
    }
}
