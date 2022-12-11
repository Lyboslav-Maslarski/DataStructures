package core;

import model.Task;
import shared.Scheduler;

import java.util.ArrayDeque;
import java.util.List;

public class ProcessScheduler implements Scheduler {

    @Override
    public void add(Task task) {

    }

    @Override
    public Task process() {
        return null;
    }

    @Override
    public Task peek() {
        return null;
    }

    @Override
    public Boolean contains(Task task) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Boolean remove(Task task) {
        return null;
    }

    @Override
    public Boolean remove(int id) {
        return null;
    }

    @Override
    public void insertBefore(int id, Task task) {

    }

    @Override
    public void insertAfter(int id, Task task) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Task[] toArray() {
        return new Task[0];
    }

    @Override
    public void reschedule(Task first, Task second) {

    }

    @Override
    public List<Task> toList() {
        return null;
    }

    @Override
    public void reverse() {

    }

    @Override
    public Task find(int id) {
        return null;
    }

    @Override
    public Task find(Task task) {
        return null;
    }
}
