package implementations;

import interfaces.Heap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MaxHeap<E extends Comparable<E>> implements Heap<E> {

    private List<E> elements;

    public MaxHeap() {
        this.elements = new ArrayList<>();
    }

    @Override
    public int size() {
        return this.elements.size();
    }

    @Override
    public void add(E element) {
        this.elements.add(element);
        heapifyUp(this.size() - 1);
    }

    private void heapifyUp(int index) {
        while (index > 0 && isLess(getParentAt(index), index)) {
            Collections.swap(this.elements, getParentAt(index), index);
            index = getParentAt(index);
        }
    }

    private boolean isLess(int childIndex, int parentIndex) {
        return getAt(childIndex).compareTo(getAt(parentIndex)) < 0;
    }

    private E getAt(int index) {
        return this.elements.get(index);
    }

    private int getParentAt(int index) {
        return (index - 1) / 2;
    }

    @Override
    public E peek() {
        if (this.elements.size() == 0) {
            throw new IllegalStateException();
        }
        return this.elements.get(0);
    }
}
