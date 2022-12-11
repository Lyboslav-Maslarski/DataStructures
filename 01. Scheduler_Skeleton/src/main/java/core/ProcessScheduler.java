package core;

import model.Task;
import shared.Scheduler;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProcessScheduler implements Scheduler {
    private ArrayDeque<Task> scheduledTasks;

    public ProcessScheduler() {
        this.scheduledTasks = new ArrayDeque<>();
    }

    @Override
    public void add(Task task) {
        if (contains(task)) {
            throw new IllegalArgumentException();
        }
        scheduledTasks.offer(task);
    }

    @Override
    public Task process() {
        return scheduledTasks.poll();
    }

    @Override
    public Task peek() {
        return scheduledTasks.peek();
    }

    @Override
    public Boolean contains(Task task) {
        return scheduledTasks.contains(task);
    }

    @Override
    public int size() {
        return scheduledTasks.size();
    }

    @Override
    public Boolean remove(Task task) {
        boolean successful = scheduledTasks.remove(task);

        if (!successful) {
            throw new IllegalArgumentException();
        }

        return true;
    }

    @Override
    public Boolean remove(int id) {
        boolean successful = scheduledTasks.removeIf(task -> task.getId() == id);

        if (!successful) {
            throw new IllegalArgumentException();
        }

        return true;
    }

    @Override
    public void insertBefore(int id, Task task) {
        if (scheduledTasks.stream().noneMatch(t -> t.getId() == id)) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < scheduledTasks.size(); i++) {
            Task poll = scheduledTasks.poll();
            assert poll != null;
            if (poll.getId() == id) {
                scheduledTasks.offer(task);
                scheduledTasks.offer(poll);
                i++;
            }else {
                scheduledTasks.offer(poll);
            }
        }
    }

    @Override
    public void insertAfter(int id, Task task) {
        if (scheduledTasks.stream().noneMatch(t -> t.getId() == id)) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < scheduledTasks.size(); i++) {
            Task poll = scheduledTasks.poll();
            assert poll != null;
            if (poll.getId() == id) {
                scheduledTasks.offer(poll);
                scheduledTasks.offer(task);
                i++;
            } else {
                scheduledTasks.offer(poll);
            }
        }
    }

    @Override
    public void clear() {
        scheduledTasks.clear();
    }

    @Override
    public Task[] toArray() {
        return scheduledTasks.toArray(new Task[0]);
    }

    @Override
    public void reschedule(Task first, Task second) {
        if (!contains(first) || !contains(second)) {
            throw new IllegalArgumentException();
        }
        List<Task> tasks = toList();
        Collections.swap(tasks,tasks.indexOf(first),tasks.indexOf(second));
        scheduledTasks.clear();
        tasks.forEach(task -> scheduledTasks.offer(task));
    }

    @Override
    public List<Task> toList() {
        return new ArrayList<>(scheduledTasks);
    }

    @Override
    public void reverse() {
        List<Task> tasks = toList();
        Collections.reverse(tasks);
        scheduledTasks.clear();
        tasks.forEach(task -> scheduledTasks.offer(task));
    }

    @Override
    public Task find(int id) {
        Task taskToReturn = scheduledTasks.stream().filter(t -> t.getId() == id).findAny().orElse(null);
        if (taskToReturn == null) {
            throw new IllegalArgumentException();
        }
        return taskToReturn;
    }

    @Override
    public Task find(Task task) {
        Task taskToReturn = scheduledTasks.stream().filter(t -> t.getId() == task.getId()).findAny().orElse(null);
        if (taskToReturn == null) {
            throw new IllegalArgumentException();
        }
        return taskToReturn;
    }
}
