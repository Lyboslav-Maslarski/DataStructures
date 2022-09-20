package implementations;

import interfaces.List;

import java.util.Iterator;

public class ArrayList<E> implements List<E> {
    private Object[] elements;
    private int size;
    private int capacity;

    public ArrayList() {
        this.elements = new Object[4];
        this.size = 0;
        this.capacity = 4;
    }

    @Override
    public boolean add(E element) {
        if (this.size == this.elements.length) {
            this.elements = grow();
        }
        this.elements[size++] = element;
        return true;
    }


    @Override
    public boolean add(int index, E element) {
        validateIndex(index);
        if (this.size == this.elements.length) {
            this.elements = grow();
        }
        shiftRight(index);
        this.elements[index] = element;
        this.size++;
        return true;
    }


    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        validateIndex(index);
        return (E) this.elements[index];
    }

    @Override
    public E set(int index, E element) {
        validateIndex(index);
        E elementToReturn = (E) this.elements[index];
        this.elements[index] = element;
        return elementToReturn;
    }

    @Override
    public E remove(int index) {
        validateIndex(index);
        E elementToReturn = (E) this.elements[index];
        shiftLeft(index);
        this.size--;
        if (this.size < this.capacity / 3) {
            this.elements = shrink();
        }
        return elementToReturn;
    }

    private Object[] shrink() {
        this.capacity /= 2;
        Object[] newArr = new Object[this.capacity];
        for (int i = 0; i < this.size; i++) {
            newArr[i] = this.elements[i];
        }
        return newArr;
    }


    @Override
    public int size() {
        return this.size;
    }

    @Override
    public int indexOf(E element) {
        for (int i = 0; i < this.size; i++) {
            if (this.elements[i].equals(element)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean contains(E element) {
        return this.indexOf(element) >= 0;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size();
            }

            @Override
            public E next() {
                return get(index++);
            }
        };
    }

    private Object[] grow() {
        this.capacity *= 2;
        Object[] newArr = new Object[this.capacity];
        for (int i = 0; i < this.size; i++) {
            newArr[i] = this.elements[i];
        }
        return newArr;
    }

    private void shiftRight(int index) {
        for (int i = this.size - 1; i >= index; i--) {
            this.elements[i + 1] = this.elements[i];
        }
    }

    private void shiftLeft(int index) {
        for (int i = index; i < this.size - 1; i++) {
            this.elements[i] = this.elements[i + 1];
        }
    }

    private void validateIndex(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException();
        }
    }
}
