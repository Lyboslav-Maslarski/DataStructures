package implementations;

import interfaces.AbstractBinaryTree;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BinaryTree<E> implements AbstractBinaryTree<E> {
    private E key;
    private BinaryTree<E> leftChild;
    private BinaryTree<E> rightChild;

    public BinaryTree(E key, BinaryTree<E> leftChild, BinaryTree<E> rightChild) {
        this.key = key;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    @Override
    public E getKey() {
        return this.key;
    }

    @Override
    public AbstractBinaryTree<E> getLeft() {
        return this.leftChild;
    }

    @Override
    public AbstractBinaryTree<E> getRight() {
        return this.rightChild;
    }

    @Override
    public void setKey(E key) {
        this.key = key;
    }

    @Override
    public String asIndentedPreOrder(int indent) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getPadding(indent)).append(this.key);
        if (this.leftChild != null) {
            stringBuilder.append(System.lineSeparator()).append(this.leftChild.asIndentedPreOrder(indent + 2));
        }
        if (this.rightChild != null) {
            stringBuilder.append(System.lineSeparator()).append(this.rightChild.asIndentedPreOrder(indent + 2));
        }

        return stringBuilder.toString();
    }

    private String getPadding(int indent) {
        return " ".repeat(Math.max(0, indent));
    }

    @Override
    public List<AbstractBinaryTree<E>> preOrder() {
        List<AbstractBinaryTree<E>> result = new ArrayList<>();
        result.add(this);
        if (this.leftChild != null) {
            result.addAll(this.leftChild.preOrder());
        }
        if (this.rightChild != null) {
            result.addAll(this.rightChild.preOrder());
        }
        return result;
    }

    @Override
    public List<AbstractBinaryTree<E>> inOrder() {
        List<AbstractBinaryTree<E>> result = new ArrayList<>();
        if (this.leftChild != null) {
            result.addAll(this.leftChild.inOrder());
        }
        result.add(this);
        if (this.rightChild != null) {
            result.addAll(this.rightChild.inOrder());
        }
        return result;
    }

    @Override
    public List<AbstractBinaryTree<E>> postOrder() {
        List<AbstractBinaryTree<E>> result = new ArrayList<>();
        if (this.leftChild != null) {
            result.addAll(this.leftChild.postOrder());
        }
        if (this.rightChild != null) {
            result.addAll(this.rightChild.postOrder());
        }
        result.add(this);
        return result;
    }

    @Override
    public void forEachInOrder(Consumer<E> consumer) {
        if (this.leftChild != null) {
            this.leftChild.forEachInOrder(consumer);
        }
        consumer.accept(this.key);
        if (this.rightChild != null) {
            this.rightChild.forEachInOrder(consumer);
        }
    }
}
