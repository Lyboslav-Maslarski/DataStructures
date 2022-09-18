package implementations;

import interfaces.AbstractStack;

import java.util.Iterator;

public class Stack<E> implements AbstractStack<E> {
    private Node head;
    private int size;

    private class Node {
        private E element;
        private Node previous;

        Node(E element) {
            this.element = element;
            this.previous = null;
        }
    }

    @Override
    public void push(E element) {
        Node node = new Node(element);
        node.previous = this.head;
        this.head = node;
        this.size++;
    }

    @Override
    public E pop() {
        ensureNotEmpty();
        E element = this.head.element;
        this.head = this.head.previous;
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
        return new Iterator<>() {
            Node current = head;

            @Override
            public boolean hasNext() {
                return this.current.previous != null;
            }

            @Override
            public E next() {
                E element = this.current.element;
                this.current = this.current.previous;
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
