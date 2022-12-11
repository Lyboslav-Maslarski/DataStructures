package core;

import model.Message;
import shared.DataTransferSystem;

import java.util.ArrayList;
import java.util.List;

public class MessagingSystem implements DataTransferSystem {
    private Node root;
    private int size;

    private static class Node {
        Message message;
        Node left;
        Node right;

        Node(Message message) {
            this.message = message;
        }
    }

    public MessagingSystem() {
    }

    @Override
    public void add(Message message) {
        Node newNode = new Node(message);
        if (this.root == null) {
            this.root = newNode;
        } else {
            Node current = this.root;
            Node prev = current;
            while (current != null) {
                prev = current;
                if (current.message.getWeight() > message.getWeight()) {
                    current = current.left;
                } else if (current.message.getWeight() < message.getWeight()) {
                    current = current.right;
                } else {
                    throw new IllegalArgumentException();
                }
            }

            current = newNode;

            if (prev.message.getWeight() > current.message.getWeight()) {
                prev.left = current;
            } else {
                prev.right = current;
            }
        }

        this.size++;
    }

    @Override
    public Message getByWeight(int weight) {
        return getNodeByKey(weight).message;
    }

    private Node getNodeByKey(int weight) {
        Node current = this.root;
        while (current != null) {
            if (current.message.getWeight() > weight) {
                current = current.left;
            } else if (current.message.getWeight() < weight) {
                current = current.right;
            } else {
                return current;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Message getLightest() {
        Node current = this.root;

        if (current == null) {
            throw new IllegalStateException();
        }

        Node prev = current;

        while (current != null) {
            prev = current;
            current = current.left;
        }

        return prev.message;
    }

    @Override
    public Message getHeaviest() {
        Node current = this.root;

        if (current == null) {
            throw new IllegalStateException();
        }

        Node prev = current;

        while (current != null) {
            prev = current;
            current = current.right;
        }

        return prev.message;
    }

    @Override
    public Message deleteLightest() {
        Node current = this.root;

        if (current == null) {
            throw new IllegalStateException();
        }

        Node prev = current;

        while (current.left != null) {
            prev = current;
            current = current.left;
        }
        Message message;
        if (prev.left == null) {
            message = prev.message;
            this.root = null;
        } else {
            message = prev.left.message;
            prev.left = null;
        }

        this.size--;
        return message;
    }

    @Override
    public Message deleteHeaviest() {
        Node current = this.root;

        if (current == null) {
            throw new IllegalStateException();
        }

        Node prev = current;

        while (current.right != null) {
            prev = current;
            current = current.right;
        }
        Message message;
        if (prev.right == null) {
            message = prev.message;
            this.root = null;
        } else {
            message = prev.right.message;
            prev.right = null;
        }

        this.size--;
        return message;
    }

    @Override
    public Boolean contains(Message message) {
        try {
            getNodeByKey(message.getWeight());
        } catch (IllegalArgumentException ignored) {
            return false;
        }
        return true;
    }

    @Override
    public List<Message> getOrderedByWeight() {
        return getInOrder();
    }

    @Override
    public List<Message> getPostOrder() {
        List<Message> messages = new ArrayList<>();
        this.postOrder(this.root, messages);
        return messages;
    }

    @Override
    public List<Message> getPreOrder() {
        List<Message> messages = new ArrayList<>();
        this.preOrder(this.root, messages);
        return messages;
    }

    private void preOrder(Node node, List<Message> messages) {
        if (node != null) {
            messages.add(node.message);
            preOrder(node.left, messages);
            preOrder(node.right, messages);
        }
    }

    @Override
    public List<Message> getInOrder() {
        List<Message> messages = new ArrayList<>();
        this.inOrder(this.root, messages);
        return messages;
    }

    private void inOrder(Node node, List<Message> messages) {
        if (node != null) {
            inOrder(node.left, messages);
            messages.add(node.message);
            inOrder(node.right, messages);
        }
    }

    private void postOrder(Node node, List<Message> messages) {
        if (node != null) {
            postOrder(node.left, messages);
            postOrder(node.right, messages);
            messages.add(node.message);
        }
    }

    @Override
    public int size() {
        return this.size;
    }
}
