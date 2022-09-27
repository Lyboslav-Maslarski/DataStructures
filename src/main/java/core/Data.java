package core;

import interfaces.Entity;
import interfaces.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

public class Data implements Repository {
    private Queue<Entity> data;

    public Data() {
        this.data = new PriorityQueue<>();
    }

    public Data(Data other) {
        this.data = new PriorityQueue<>(other.data);
    }

    @Override
    public void add(Entity entity) {
        this.data.offer(entity);
    }

    @Override
    public Entity getById(int id) {
        for (Entity entity : this.data) {
            if (entity.getId() == id) {
                return entity;
            }
        }
        return null;
    }

    @Override
    public List<Entity> getByParentId(int id) {
        List<Entity> result = new ArrayList<>();
        for (Entity entity : this.data) {
            if (entity.getParentId() == id) {
                result.add(entity);
            }
        }
        return result;
    }

    @Override
    public List<Entity> getAll() {
        return new ArrayList<>(this.data);
    }

    @Override
    public Repository copy() {
        return new Data(this);
    }

    @Override
    public List<Entity> getAllByType(String type) {
        boolean isRightType = type.equals("Invoice") || type.equals("StoreClient") || type.equals("User");
        if (!isRightType) {
            throw new IllegalArgumentException("Illegal type: " + type);
        }
        return this.data.stream()
                .filter(entity -> entity.getClass().getSimpleName().equals(type))
                .collect(Collectors.toList());
    }

    @Override
    public Entity peekMostRecent() {
        ensureNotEmpty();
        return this.data.peek();
    }

    @Override
    public Entity pollMostRecent() {
        ensureNotEmpty();
        return this.data.poll();
    }

    @Override
    public int size() {
        return this.data.size();
    }

    private void ensureNotEmpty() {
        if (this.size() == 0) {
            throw new IllegalStateException("Operation on empty Data");
        }
    }
}
