package core;

import models.Category;

import java.util.*;
import java.util.stream.Collectors;

public class CategorizatorImpl implements Categorizator {
    Map<String, Category> categoriesByID;
    Map<String, Set<String>> parentCategoriesWithChildren;
    Map<String, String> childWithParent;

    public CategorizatorImpl() {
        this.categoriesByID = new LinkedHashMap<>();
        this.parentCategoriesWithChildren = new LinkedHashMap<>();
        this.childWithParent = new LinkedHashMap<>();
    }

    @Override
    public void addCategory(Category category) {
        if (contains(category)) {
            throw new IllegalArgumentException();
        }
        categoriesByID.put(category.getId(), category);
        parentCategoriesWithChildren.put(category.getId(), new LinkedHashSet<>());
    }

    @Override
    public void assignParent(String childCategoryId, String parentCategoryId) {
        if (!categoriesByID.containsKey(childCategoryId) || !categoriesByID.containsKey(parentCategoryId)) {
            throw new IllegalArgumentException();
        }

        Set<String> children = parentCategoriesWithChildren.get(parentCategoryId);

        if (children.contains(childCategoryId)) {
            throw new IllegalArgumentException();
        }

        children.add(childCategoryId);
        childWithParent.put(childCategoryId, parentCategoryId);
    }

    @Override
    public void removeCategory(String categoryId) {
        if (!categoriesByID.containsKey(categoryId)) {
            throw new IllegalArgumentException();
        }
        Category category = categoriesByID.remove(categoryId);

        Set<String> children = parentCategoriesWithChildren.get(category.getId());
        for (String child : children) {
            categoriesByID.remove(child);
            parentCategoriesWithChildren.remove(child);
            childWithParent.remove(child);
        }
        parentCategoriesWithChildren.remove(categoryId);
        childWithParent.remove(categoryId);
    }

    @Override
    public boolean contains(Category category) {
        return categoriesByID.containsKey(category.getId());
    }

    @Override
    public int size() {
        return categoriesByID.size();
    }

    @Override
    public Iterable<Category> getChildren(String categoryId) {
        if (!categoriesByID.containsKey(categoryId)) {
            throw new IllegalArgumentException();
        }

        List<Category> result = new ArrayList<>();
        Deque<String> deque = new ArrayDeque<>();

        deque.offer(categoryId);

        while (!deque.isEmpty()) {
            String childID = deque.poll();
            if (parentCategoriesWithChildren.containsKey(childID)) {
                Set<String> directChildren = parentCategoriesWithChildren.get(childID);
                for (String id : directChildren) {
                    deque.offer(id);
                    if (categoriesByID.containsKey(id)) {
                        result.add(categoriesByID.get(id));
                    }
                }
            }
        }

        return result;
    }

    @Override
    public Iterable<Category> getHierarchy(String categoryId) {
        if (!categoriesByID.containsKey(categoryId)) {
            throw new IllegalArgumentException();
        }
        List<Category> result = new ArrayList<>();

        result.add(categoriesByID.get(categoryId));
        String parent = null;
        if (childWithParent.containsKey(categoryId)) {
            parent = childWithParent.get(categoryId);
        }
        while (parent != null) {
            Category category = null;
            if (categoriesByID.containsKey(parent)) {
                category = categoriesByID.get(parent);
            }
            result.add(category);
            if (childWithParent.containsKey(parent)) {
                parent = childWithParent.get(parent);
            }
        }
        Collections.reverse(result);
        return result;
    }

    @Override
    public Iterable<Category> getTop3CategoriesOrderedByDepthOfChildrenThenByName() {
        if (categoriesByID.isEmpty()) {
            return new ArrayList<>();
        }
        return categoriesByID.values().stream()
                .sorted((l, r) -> {
                    int lDepth = getDepth(1, l.getId());
                    int rDepth = getDepth(1, r.getId());

                    if (lDepth == rDepth) {
                        return l.getName().compareTo(r.getName());
                    }
                    return rDepth - lDepth;
                })
                .limit(3)
                .collect(Collectors.toList());
    }

    private int getDepth(int result, String id) {
        Set<String> strings = parentCategoriesWithChildren.get(id);
        if (strings == null || strings.isEmpty()) {
            return 1;
        }
        for (String string : strings) {
            return result + getDepth(result, string);
        }

        return result;
    }
}
