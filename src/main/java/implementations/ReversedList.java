package implementations;

import java.util.Iterator;

public class ReversedList<E> {
    private final int INITIAL_CAPACITY = 2;
    private Object[] elements;
    private int size;
    private int capacity;

    public ReversedList() {
        this.elements = new Object[INITIAL_CAPACITY];
        this.size = 0;
        this.capacity = INITIAL_CAPACITY;
    }

    public void add(E element) {
        if (this.size == this.capacity) {
            this.elements = grow();
        }
        this.elements[this.size++] = element;
    }

    private Object[] grow() {
        this.capacity *= 2;
        Object[] newArr = new Object[this.capacity];
        for (int i = 0; i < this.size; i++) {
            newArr[i] = this.elements[i];
        }
        return newArr;
    }

    public int size() {
        return this.size;
    }

    public int capacity() {
        return this.capacity;
    }

    public E get(int index) {
        validateIndex(index);
        return (E) this.elements[this.size - 1 - index];
    }

    public void removeAt(int index) {
        validateIndex(index);
        for (int i =  index; i < this.size - 1; i++) {
            this.elements[i] = this.elements[i + 1];
        }
        this.size--;
    }

    public Iterator<E> iterator() {
        return new Iterator<>() {
            int index = size - 1;

            @Override
            public boolean hasNext() {
                return index >= 0;
            }

            @Override
            public E next() {
                return (E) elements[index--];
            }
        };
    }

    private void validateIndex(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException();
        }
    }
}
