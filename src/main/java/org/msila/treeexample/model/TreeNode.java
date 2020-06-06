package org.msila.treeexample.model;

import java.util.List;
import java.util.Objects;

/**
 * The main data object
 * used to represent the tree
 */
public class TreeNode {
    private final String name;
    private final Integer id;
    private final List<TreeNode> children;

    public TreeNode(String name, Integer id, List<TreeNode> children) {
        this.name = name;
        this.id = id;
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void addChild(TreeNode child) {
        children.add(child);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TreeNode treeNode = (TreeNode) o;
        return Objects.equals(name, treeNode.name) &&
                Objects.equals(id, treeNode.id) &&
                Objects.equals(children, treeNode.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, children);
    }
}
