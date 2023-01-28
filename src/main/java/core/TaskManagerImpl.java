package core;

import models.Task;

import java.util.*;
import java.util.stream.Collectors;

public class TaskManagerImpl implements TaskManager {
    Map<String, Task> tasksByID;
    Deque<Task> unExecutedTasks;
    Map<String, Task> executedTasks;

    public TaskManagerImpl() {
        this.tasksByID = new LinkedHashMap<>();
        this.unExecutedTasks = new ArrayDeque<>();
        this.executedTasks = new HashMap<>();
    }

    @Override
    public void addTask(Task task) {
        tasksByID.put(task.getId(), task);
        unExecutedTasks.offer(task);
    }

    @Override
    public boolean contains(Task task) {
        return tasksByID.containsKey(task.getId());
    }

    @Override
    public int size() {
        return tasksByID.size();
    }

    @Override
    public Task getTask(String taskId) {
        Task task = tasksByID.get(taskId);
        if (task == null) {
            throw new IllegalArgumentException();
        }
        return task;
    }

    @Override
    public void deleteTask(String taskId) {
        Task task = tasksByID.remove(taskId);
        if (task == null) {
            throw new IllegalArgumentException();
        }
        unExecutedTasks.remove(task);
        executedTasks.remove(task.getId());
    }

    @Override
    public Task executeTask() {
//        Task toExecute = tasksByID.values().iterator().next();
        if (unExecutedTasks.isEmpty()) {
            throw new IllegalArgumentException();
        }
        Task task = unExecutedTasks.poll();
        executedTasks.put(task.getId(), task);
        return task;
    }

    @Override
    public void rescheduleTask(String taskId) {
        Task task = executedTasks.remove(taskId);
        if (task == null) {
            throw new IllegalArgumentException();
        }
        unExecutedTasks.offer(task);
    }

    @Override
    public Iterable<Task> getDomainTasks(String domain) {
        List<Task> result = unExecutedTasks.stream()
                .filter(task -> task.getDomain().equals(domain))
                .collect(Collectors.toList());
        if (result.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return result;
    }

    @Override
    public Iterable<Task> getTasksInEETRange(int lowerBound, int upperBound) {
        return unExecutedTasks.stream()
                .filter(task -> task.getEstimatedExecutionTime() >= lowerBound && task.getEstimatedExecutionTime() <= upperBound)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Task> getAllTasksOrderedByEETThenByName() {
        return tasksByID.values().stream()
                .sorted((l, r) -> {
                    int cmp = r.getEstimatedExecutionTime() - l.getEstimatedExecutionTime();
                    if (cmp == 0) {
                        cmp = l.getName().length() - r.getName().length();
                    }
                    return cmp;
                })
                .collect(Collectors.toList());
    }
}
