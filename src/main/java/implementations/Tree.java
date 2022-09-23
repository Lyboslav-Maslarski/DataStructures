package implementations;

import interfaces.AbstractTree;

import java.util.*;

public class Tree<E> implements AbstractTree<E> {
    private E value;
    private Tree<E> parent;
    private List<Tree<E>> children;

    public Tree(E value, Tree<E>... children) {
        this.value = value;
        this.parent = null;
        this.children = new ArrayList<>();
        for (Tree<E> child : children) {
            child.setParent(this);
            addChild(child);
        }
    }

    @Override
    public void setParent(Tree<E> parent) {
        this.parent = parent;
    }

    @Override
    public void addChild(Tree<E> child) {
        this.children.add(child);
    }

    @Override
    public Tree<E> getParent() {
        return this.parent;
    }

    @Override
    public E getKey() {
        return this.value;
    }

    @Override
    public String getAsString() {
        StringBuilder stringBuilder = new StringBuilder();
        traverseTree(stringBuilder, 0, this);
        return stringBuilder.toString().trim();
    }

    private void traverseTree(StringBuilder stringBuilder, int row, Tree<E> tree) {
        stringBuilder.append(this.getPadding(row)).append(tree.value).append(System.lineSeparator());
        for (Tree<E> child : tree.children) {
            traverseTree(stringBuilder, row + 1, child);
        }
    }

    private String getPadding(int row) {
        StringBuilder builder = new StringBuilder();
        builder.append("  ".repeat(Math.max(0, row)));
        return builder.toString();
    }

    @Override
    public List<E> getLeafKeys() {
        List<E> result = new ArrayList<>();
        ArrayDeque<Tree<E>> arrayDeque = new ArrayDeque<>();
        arrayDeque.offer(this);
        while (!arrayDeque.isEmpty()) {
            Tree<E> eTree = arrayDeque.poll();
            for (Tree<E> child : eTree.children) {
                arrayDeque.offer(child);
                if (child.children.isEmpty()) {
                    result.add(child.value);
                }
            }
        }
        return result;
    }

    @Override
    public List<E> getMiddleKeys() {
        List<E> result = new ArrayList<>();
        ArrayDeque<Tree<E>> arrayDeque = new ArrayDeque<>();
        arrayDeque.offer(this);
        while (!arrayDeque.isEmpty()) {
            Tree<E> eTree = arrayDeque.poll();
            for (Tree<E> child : eTree.children) {
                arrayDeque.offer(child);
                if (!child.children.isEmpty()) {
                    result.add(child.value);
                }
            }
        }
        return result;
    }

    @Override
    public Tree<E> getDeepestLeftmostNode() {
        List<Tree<E>> result = new ArrayList<>();
        ArrayDeque<Tree<E>> arrayDeque = new ArrayDeque<>();
        arrayDeque.offer(this);
        while (!arrayDeque.isEmpty()) {
            Tree<E> eTree = arrayDeque.poll();
            for (Tree<E> child : eTree.children) {
                arrayDeque.offer(child);
                if (child.children.isEmpty()) {
                    result.add(child);
                }
            }
        }
        int maxPath = 0;
        Tree<E> maxPathTree = null;
        for (Tree<E> tree : result) {
            int currentMaxPath = 0;
            Tree<E> currentTree = tree;
            while (currentTree.parent != null) {
                currentMaxPath++;
                currentTree = currentTree.parent;
            }
            if (currentMaxPath > maxPath) {
                maxPath = currentMaxPath;
                maxPathTree = tree;
            }
        }
        return maxPathTree;
    }

    @Override
    public List<E> getLongestPath() {
        List<E> result = new ArrayList<>();
        Tree<E> node = getDeepestLeftmostNode();
        while (node.parent != null) {
            result.add(node.value);
            node = node.parent;
        }
        result.add(node.value);
        Collections.reverse(result);
        return result;
    }

    @Override
    public List<List<E>> pathsWithGivenSum(int sum) {
        List<List<E>> result = new ArrayList<>();
        ArrayDeque<Tree<E>> arrayDeque = new ArrayDeque<>();
        arrayDeque.offer(this);
        while (!arrayDeque.isEmpty()) {
            Tree<E> eTree = arrayDeque.poll();
            for (Tree<E> child : eTree.children) {
                arrayDeque.offer(child);
                if (child.children.isEmpty()) {
                    List<E> current = new ArrayList<>();
                    while (child.parent != null) {
                        current.add(child.value);
                        child = child.parent;
                    }
                    current.add(child.value);
                    int currentSum = current.stream().mapToInt(e -> (int) e).sum();
                    if (currentSum == sum) {
                        Collections.reverse(current);
                        result.add(current);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<Tree<E>> subTreesWithGivenSum(int sum) {
        List<Tree<E>> result = new ArrayList<>();
        ArrayDeque<Tree<E>> arrayDeque = new ArrayDeque<>();
        arrayDeque.offer(this);
        while (!arrayDeque.isEmpty()) {
            Tree<E> eTree = arrayDeque.poll();
            int currentSum = calculateCurrentSum(eTree);
            if (currentSum == sum) {
                result.add(eTree);
            }
            for (Tree<E> child : eTree.children) {
                    arrayDeque.offer(child);
            }
        }
        return result;
    }

    private int calculateCurrentSum(Tree<E> tree) {
        int sum = (int) tree.value;
        ArrayDeque<Tree<E>> arrayDeque = new ArrayDeque<>();
        arrayDeque.offer(tree);
        while (!arrayDeque.isEmpty()) {
            Tree<E> eTree = arrayDeque.poll();
            for (Tree<E> child : eTree.children) {
                arrayDeque.offer(child);
                sum += (int) child.value;
            }
        }
        return sum;
    }
}



