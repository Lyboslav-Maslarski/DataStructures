package implementations;

import interfaces.Deque;

import java.util.Iterator;

public class ArrayDeque<E> implements Deque<E> {
    private final int DEFAULT_CAPACITY = 7;
    private int head;
    private int tail;
    private int size;
    private Object[] elements;

    public ArrayDeque() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.head = this.elements.length / 2;
        this.tail = this.head;
        this.size = 0;
    }

    @Override
    public void add(E element) {
        addLast(element);
    }

    @Override
    public void offer(E element) {
        addLast(element);
    }

    @Override
    public void push(E element) {
        addFirst(element);
    }

    @Override
    public void addFirst(E element) {
        if (this.size == 0) {
            this.elements[this.head] = element;
        } else {
            if (this.head == 0) {
                this.elements = grow();
            }
            this.elements[--this.head] = element;
        }
        this.size++;
    }

    @Override
    public void addLast(E element) {
        if (this.size == 0) {
            this.elements[this.tail] = element;
        } else {
            if (this.tail == this.elements.length - 1) {
                this.elements = grow();
            }
            this.elements[++this.tail] = element;
        }
        this.size++;
    }

    @Override
    public void insert(int index, E element) {
        validateIndex(index);
        E lastElement = (E) this.elements[this.tail];
        for (int i = this.tail; i >= this.head + index; i--) {
            this.elements[i] = this.elements[i - 1];
        }
        this.elements[this.head + index] = element;
        addLast(lastElement);
    }


    @Override
    public void set(int index, E element) {
        validateIndex(index);
        this.elements[this.head + index] = element;
    }

    @Override
    public E peek() {
        if (isEmpty()) {
            return null;
        }

        return (E) this.elements[this.head];
    }

    @Override
    public E poll() {
        return removeFirst();
    }

    @Override
    public E pop() {
        return removeLast();
    }

    @Override
    public E get(int index) {
        validateIndex(index);
        E element = (E) this.elements[this.head + index];
        return element;
    }

    @Override
    public E get(Object object) {
        if (isEmpty()) {
            return null;
        }
        for (int i = this.head; i <= this.tail; i++) {
            if (this.elements[i].equals(object)) {
                return (E) this.elements[i];
            }
        }
        return null;
    }

    @Override
    public E remove(int index) {
        validateIndex(index);
        E element = (E) this.elements[this.head + index];
        for (int i = this.head+index; i < this.tail; i++) {
            this.elements[i] = this.elements[i + 1];
        }
        removeLast();
        return element;
    }

    @Override
    public E remove(Object object) {
        if (isEmpty()) {
            return null;
        }
        for (int i = this.head; i <= this.tail; i++) {
            if (this.elements[i].equals(object)) {
                E element = (E) this.elements[i];
                for (int j = i; j < this.tail; j++) {
                    this.elements[j] = this.elements[j + 1];
                }
                removeLast();
                return element;
            }
        }
        return null;
    }

    @Override
    public E removeFirst() {
        if (isEmpty()) {
            return null;
        }
        E element = (E) this.elements[this.head];
        this.elements[this.head] = null;
        this.head++;
        this.size--;
        return element;
    }

    @Override
    public E removeLast() {
        if (isEmpty()) {
            return null;
        }
        E element = (E) this.elements[this.tail];
        this.elements[this.tail] = null;
        this.tail--;
        this.size--;
        return element;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public int capacity() {
        return this.elements.length;
    }

    @Override
    public void trimToSize() {
        Object[] newArr = new Object[this.size];
        for (int i = this.head, index = 0; i <= this.tail; i++, index++) {
            newArr[index] = this.elements[i];
        }
        this.elements = newArr;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int index = head;

            @Override
            public boolean hasNext() {
                return index <= tail;
            }

            @Override
            public E next() {
                return (E) elements[index++];
            }
        };
    }

    private Object[] grow() {
        int newCapacity = this.elements.length * 2 + 1;
        int middle = newCapacity / 2;
        int start = middle - this.size / 2;
        Object[] newArr = new Object[newCapacity];
        int index = this.head;
        for (int i = start; index <= this.tail; i++) {
            newArr[i] = this.elements[index++];
        }
        this.head = start;
        this.tail = this.head + this.size - 1;

        return newArr;
    }

    private void validateIndex(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException();
        }
    }
}
