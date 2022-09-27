package core;

import interfaces.Buffer;
import interfaces.Entity;
import model.BaseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Loader implements Buffer {
    private List<Entity> data;

    public Loader() {
        this.data = new ArrayList<>();
    }

    @Override
    public void add(Entity entity) {
        this.data.add(entity);
    }

    @Override
    public Entity extract(int id) {
        for (Entity entity : this.data) {
            if (entity.getId() == id) {
                this.data.remove(entity);
                return entity;
            }
        }
        return null;
    }

    @Override
    public Entity find(Entity entity) {
        return this.data.stream().filter(entity::equals).findFirst().orElse(null);
    }

    @Override
    public boolean contains(Entity entity) {
        return this.data.contains(entity);
    }

    @Override
    public int entitiesCount() {
        return this.data.size();
    }

    @Override
    public void replace(Entity oldEntity, Entity newEntity) {
        int index = this.data.indexOf(oldEntity);
        if (index < 0) {
            throw new IllegalStateException("Entity not found");
        }
        this.data.set(index, newEntity);

    }

    @Override
    public void swap(Entity first, Entity second) {
        int index1 = this.data.indexOf(first);
        int index2 = this.data.indexOf(second);
        if (index1 < 0 || index2 < 0) {
            throw new IllegalStateException("Entity not found");
        }
        Collections.swap(this.data, index1, index2);
    }

    @Override
    public void clear() {
        this.data.clear();
    }

    @Override
    public Entity[] toArray() {
        return this.data.toArray(Entity[]::new);
    }

    @Override
    public List<Entity> retainAllFromTo(BaseEntity.Status lowerBound, BaseEntity.Status upperBound) {
        return this.data.stream()
                .filter(entity -> entity.getStatus().ordinal() >= lowerBound.ordinal())
                .filter(entity -> entity.getStatus().ordinal() <= upperBound.ordinal())
                .collect(Collectors.toList());
    }

    @Override
    public List<Entity> getAll() {
        return this.data;
    }

    @Override
    public void updateAll(BaseEntity.Status oldStatus, BaseEntity.Status newStatus) {
        this.data.stream().filter(entity -> entity.getStatus().ordinal() == oldStatus.ordinal())
                .forEach(entity -> entity.setStatus(newStatus));
    }

    @Override
    public void removeSold() {
        this.data.removeIf(entity -> entity.getStatus() == BaseEntity.Status.SOLD);
    }

    @Override
    public Iterator<Entity> iterator() {
        return this.data.iterator();
    }
}
