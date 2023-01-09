import java.util.function.Consumer;

class AATree<T extends Comparable<T>> {
    private Node<T> root;

    public static class Node<T> {
        private T value;
        private Node<T> left;
        private Node<T> right;
        private int level;

        public Node(T value) {
            this.value = value;
            this.level = 1;
        }
    }

    public AATree() {
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public void clear() {
        this.root = null;
    }

    public void insert(T element) {
        this.root = insert(this.root, element);
    }

    public Node<T> insert(Node<T> node, T element) {
        if (node == null) {
            return new Node<>(element);
        }

        if (node.value.compareTo(element) > 0) {
            node.left = insert(node.left, element);
        } else if (node.value.compareTo(element) < 0) {
            node.right = insert(node.right, element);
        }

        node = skew(node);
        node = split(node);

        return node;
    }

    private Node<T> split(Node<T> node) {
        if (node == null) {
            return null;
        }
        if (node.right == null || node.right.right == null) {
            return node;
        }
        if (node.level == node.right.right.level) {
            Node<T> result = node.right;
            node.right = result.left;
            result.left = node;

            result.level++;
            return result;
        }
        return node;
    }

    private Node<T> skew(Node<T> node) {
        if (node == null) {
            return null;
        }
        if (node.left == null) {
            return node;
        }
        if (node.level == node.left.level) {
            Node<T> result = node.left;
            node.left = result.right;
            result.right = node;
            return result;
        }
        return node;
    }

    public int countNodes() {
        return this.getNodesCount(this.root);
    }

    public boolean search(T element) {
        if (isEmpty()) {
            return false;
        }
        Node<T> current = this.root;
        while (current != null) {
            if (element.compareTo(current.value) < 0) {
                current = current.left;
            } else if (element.compareTo(current.value) > 0) {
                current = current.right;
            } else {
                return true;
            }
        }
        return false;
    }

    public void inOrder(Consumer<T> consumer) {
        this.eachInOrder(this.root, consumer);
    }

    private void eachInOrder(Node<T> node, Consumer<T> consumer) {
        if (node == null) {
            return;
        }
        this.eachInOrder(node.left, consumer);
        consumer.accept(node.value);
        this.eachInOrder(node.right, consumer);
    }

    public void preOrder(Consumer<T> consumer) {
        this.preOrder(this.root, consumer);
    }

    private void preOrder(Node<T> node, Consumer<T> consumer) {
        if (node == null) {
            return;
        }
        consumer.accept(node.value);
        this.preOrder(node.left, consumer);
        this.preOrder(node.right, consumer);
    }

    public void postOrder(Consumer<T> consumer) {
        this.postOrder(this.root, consumer);
    }

    private void postOrder(Node<T> node, Consumer<T> consumer) {
        if (node == null) {
            return;
        }
        this.postOrder(node.left, consumer);
        this.postOrder(node.right, consumer);
        consumer.accept(node.value);
    }

    private int getNodesCount(Node<T> node) {
        if (node == null) {
            return 0;
        }
        int result = 1;
        result += getNodesCount(node.left);
        result += getNodesCount(node.right);
        return result;
    }
}