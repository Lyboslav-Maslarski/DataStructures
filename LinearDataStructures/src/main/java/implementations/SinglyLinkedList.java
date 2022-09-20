package implementations;

import interfaces.LinkedList;

import java.util.Iterator;

public class SinglyLinkedList<E> implements LinkedList<E> {

    private Node<E> head;
    private int size;

    public SinglyLinkedList() {
        this.head = null;
        this.size = 0;
    }

    private static class Node<E> {
        private E element;
        private Node<E> next;

        public Node(E element) {
            this.element = element;
        }
    }

    @Override
    public void addFirst(E element) {
        Node<E> nextNode = new Node<>(element);
        nextNode.next = this.head;
        this.head = nextNode;
        this.size++;
    }

    @Override
    public void addLast(E element) {
        if (isEmpty()) {
            this.head = new Node<>(element);
        } else {
            Node<E> currentNode = this.head;
            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }
            currentNode.next = new Node<>(element);
        }
        this.size++;
    }

    @Override
    public E removeFirst() {
        assertNotEmpty();
        E element = this.head.element;
        this.head = this.head.next;
        this.size--;
        return element;
    }


    @Override
    public E removeLast() {
        assertNotEmpty();
        E element;
        if (this.size == 1) {
            element = this.head.element;
            this.head = null;
        } else {
            Node<E> currentNode = this.head;
            while (currentNode.next.next != null) {
                currentNode = currentNode.next;
            }
            element = currentNode.next.element;
            currentNode.next = null;
        }
        this.size--;
        return element;
    }

    @Override
    public E getFirst() {
        assertNotEmpty();
        return this.head.element;
    }

    @Override
    public E getLast() {
        assertNotEmpty();
        Node<E> currentNode = this.head;
        while (currentNode.next != null) {
            currentNode = currentNode.next;
        }
        return currentNode.element;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                E element = current.element;
                current = current.next;
                return element;
            }
        };
    }

    private void assertNotEmpty() {
        if (isEmpty()) {
            throw new IllegalStateException();
        }
    }
}
