import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.function.Consumer;

import java.util.List;

public class BinarySearchTree<E extends Comparable<E>> {
    private Node<E> root;

    public BinarySearchTree() {
    }

    public BinarySearchTree(E element) {
        this.root = new Node<>(element);
    }

    public BinarySearchTree(Node<E> node) {
        this.root = new Node<>(node);
        this.root.leftChild = new Node<>(node.getLeft());
        this.root.rightChild = new Node<>(node.getRight());
    }

    public static class Node<E> {
        private E value;
        private Node<E> leftChild;
        private Node<E> rightChild;
        private int count;

        public Node(E value) {
            this.value = value;
            this.count = 1;
        }

        public Node(Node<E> other) {
            this.value = other.value;
            this.count = other.count;
            if (other.getRight() != null) {
                this.rightChild = other.getRight();
            }
            if (other.getLeft() != null) {
                this.leftChild = other.getLeft();
            }
        }

        public Node<E> getLeft() {
            return this.leftChild;
        }

        public Node<E> getRight() {
            return this.rightChild;
        }

        public E getValue() {
            return this.value;
        }

        public int getCount() {
            return this.count;
        }
    }

    public void eachInOrder(Consumer<E> consumer) {
        nodeInOrder(this.root, consumer);
    }

    private void nodeInOrder(Node<E> node, Consumer<E> consumer) {
        if (node == null) {
            return;
        }
        nodeInOrder(node.getLeft(), consumer);
        consumer.accept(node.getValue());
        nodeInOrder(node.getRight(), consumer);
    }

    public Node<E> getRoot() {
        return this.root;
    }

    public void insert(E element) {
        if (this.root == null) {
            this.root = new Node<>(element);
        } else {
            insertInto(element, this.root);
        }
    }

    private void insertInto(E element, Node<E> node) {
        if (isGreater(element, node)) {
            if (node.getRight() == null) {
                node.rightChild = new Node<>(element);
            } else {
                insertInto(element, node.getRight());
            }
        } else if (isLess(element, node)) {
            if (node.getLeft() == null) {
                node.leftChild = new Node<>(element);
            } else {
                insertInto(element, node.getLeft());
            }
        }
        node.count++;
    }

    public boolean contains(E element) {
        Node<E> current = this.root;
        while (current != null) {
            if (areEqual(element, current)) {
                break;
            } else if (isGreater(element, current)) {
                current = current.getRight();
            } else if (isLess(element, current)) {
                current = current.getLeft();
            }
        }
        return current != null;
    }

    public BinarySearchTree<E> search(E element) {
        Node<E> node = containsNode(this.root, element);
        if (node == null) {
            return null;
        } else {
            return new BinarySearchTree<>(node);
        }
    }

    private Node<E> containsNode(Node<E> node, E element) {
        if (node == null) {
            return null;
        }
        if (areEqual(element, node)) {
            return node;
        } else if (isGreater(element, node)) {
            return containsNode(node.getRight(), element);
        }
        return containsNode(node.getLeft(), element);
    }

    public List<E> range(E lower, E upper) {
        List<E> result = new ArrayList<>();
        ArrayDeque<Node<E>> deque = new ArrayDeque<>();
        deque.offer(this.root);
        while (!deque.isEmpty()) {
            Node<E> node = deque.poll();
            if (node.getLeft() != null) {
                deque.offer(node.getLeft());
            }
            if (node.getRight() != null) {
                deque.offer(node.getRight());
            }
            if (isLess(lower, node) && isGreater(upper, node)) {
                result.add(node.getValue());
            } else if (areEqual(lower, node) || areEqual(upper, node)) {
                result.add(node.getValue());
            }
        }

        return result;
    }

    public void deleteMin() {
        ensureNotEmpty();
        if (this.root.getLeft() == null) {
            this.root = this.root.getRight();
            return;
        }
        Node<E> current = this.root;
        while (current.getLeft().getLeft() != null) {
            current.count--;
            current = current.getLeft();
        }
        current.count--;
        current.leftChild = current.getLeft().getRight();
    }

    public void deleteMax() {
        ensureNotEmpty();
        if (this.root.getRight() == null) {
            this.root = this.root.getLeft();
            return;
        }
        Node<E> current = this.root;
        while (current.getRight().getRight() != null) {
            current.count--;
            current = current.getRight();
        }
        current.count--;
        current.rightChild = current.getRight().getLeft();
    }

    public int count() {
        return this.root == null ? 0 : this.root.count;
    }

    public int rank(E element) {
        return nodeRank(this.root, element);
    }

    private int nodeRank(Node<E> node, E element) {
        if (node == null) {
            return 0;
        }
        if (isLess(element, node)) {
            return nodeRank(node.getLeft(), element);
        } else if (areEqual(element, node)) {
            return getNodeCount(node.getLeft());
        }
        return getNodeCount(node.getLeft()) + 1 + nodeRank(node.getRight(), element);
    }

    private int getNodeCount(Node<E> node) {
        return node == null ? 0 : node.getCount();
    }

    public E floor(E element) {
        if (this.root == null) {
            return null;
        }
        Node<E> current = this.root;
        Node<E> nearestSmaller = null;
        while (current != null) {
            if (isGreater(element, current)) {
                nearestSmaller = current;
                current = current.getRight();
            } else if (isLess(element, current)) {
                current = current.getLeft();
            } else {
                Node<E> left = current.getLeft();
                if (left != null && nearestSmaller != null) {
                    nearestSmaller = isGreater(left.getValue(), nearestSmaller) ? left : nearestSmaller;
                } else if (nearestSmaller == null) {
                    nearestSmaller = left;
                }
                break;
            }
        }

        return nearestSmaller == null ? null : nearestSmaller.getValue();
    }

    public E ceil(E element) {
        if (this.root == null) {
            return null;
        }
        Node<E> current = this.root;
        Node<E> nearestSmaller = null;
        while (current != null) {
            if (isLess(element, current)) {
                nearestSmaller = current;
                current = current.getLeft();
            } else if (isGreater(element, current)) {
                current = current.getRight();
            } else {
                Node<E> right = current.getRight();
                if (right != null && nearestSmaller != null) {
                    nearestSmaller = isLess(right.getValue(), nearestSmaller) ? right : nearestSmaller;
                } else if (nearestSmaller == null) {
                    nearestSmaller = right;
                }
                break;
            }
        }

        return nearestSmaller == null ? null : nearestSmaller.getValue();
    }

    private void ensureNotEmpty() {
        if (this.root == null) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isGreater(E element, Node<E> node) {
        return element.compareTo(node.getValue()) > 0;
    }

    private boolean isLess(E element, Node<E> node) {
        return element.compareTo(node.getValue()) < 0;
    }

    private boolean areEqual(E element, Node<E> node) {
        return element.compareTo(node.getValue()) == 0;
    }
}
