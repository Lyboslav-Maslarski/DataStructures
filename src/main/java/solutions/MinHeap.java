package solutions;

import interfaces.Decrease;
import interfaces.Heap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MinHeap<E extends Comparable<E> & Decrease<E>> implements Heap<E> {
    private List<E> data;

    public MinHeap() {
        this.data = new ArrayList<>();
    }

    @Override
    public int size() {
        return this.data.size();
    }

    @Override
    public void add(E element) {
        this.data.add(element);
        this.heapifyUp(this.data.size() - 1);
    }

    private void heapifyUp(int index) {
        int parentIndex = getParentIndex(index);
        while (index > 0 && isLess(index, parentIndex)) {
            Collections.swap(this.data, index, parentIndex);
            index = parentIndex;
            parentIndex = getParentIndex(index);
        }
    }

    private boolean isLess(int index, int parentIndex) {
        return this.data.get(index).compareTo(this.data.get(parentIndex)) < 0;
    }

    private int getParentIndex(int index) {
        return (index - 1) / 2;
    }

    @Override
    public E peek() {
        ensureNotEmpty();
        return this.data.get(0);
    }

    private void ensureNotEmpty() {
        if (this.data.isEmpty()) {
            throw new IllegalStateException();
        }
    }

    @Override
    public E poll() {
        Collections.swap(this.data, 0, this.data.size() - 1);
        E elementToReturn = this.data.remove(this.data.size() - 1);
        this.heapifyDown();
        return elementToReturn;
    }

    private void heapifyDown() {
        int index = 0;
        int swapIndex = getLeftChildIndex(index);
        while (swapIndex < this.data.size()) {

            int rightChildIndex = getRightChildIndex(index);
            if (rightChildIndex < this.data.size()) {
                swapIndex = isLess(swapIndex, rightChildIndex) ? swapIndex : rightChildIndex;
            }
            if (isLess(index, swapIndex)) {
                break;
            }
            Collections.swap(this.data, index, swapIndex);
            index = swapIndex;
            swapIndex = getLeftChildIndex(index);
        }
    }

    private int getLeftChildIndex(int index) {
        return 2 * index + 1;
    }

    private int getRightChildIndex(int index) {
        return 2 * index + 2;
    }

    @Override
    public void decrease(E element) {
        int index = this.data.indexOf(element);
        E elementToDecrease = this.data.get(index);
        elementToDecrease.decrease();
        this.heapifyUp(index);
    }
}
