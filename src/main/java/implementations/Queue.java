package implementations;

import interfaces.AbstractQueue;

import java.util.Iterator;

public class Queue<E> implements AbstractQueue<E> {
    private Node head;
    private int size;

    private class Node {
        private E element;
        private Node next;

        Node(E element) {
            this.element = element;
            this.next = null;
        }
    }

    @Override
    public void offer(E element) {
        if (isEmpty()) {
            this.head = new Node(element);
        } else {
            Node current = this.head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = new Node(element);
        }
        this.size++;
    }

    @Override
    public E poll() {
        ensureNotEmpty();
        E element = this.head.element;
        this.head = this.head.next;
        this.size--;
        return element;
    }

    @Override
    public E peek() {
        ensureNotEmpty();
        return this.head.element;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.head == null;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node current = head;

            @Override
            public boolean hasNext() {
                return this.current.next != null;
            }

            @Override
            public E next() {
                E element = this.current.element;
                this.current = this.current.next;
                return element;
            }
        };
    }

    private void ensureNotEmpty() {
        if (isEmpty()) {
            throw new IllegalStateException();
        }
    }
}
