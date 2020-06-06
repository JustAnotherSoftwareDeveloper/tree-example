package org.msila.treeexample.model;

import java.util.Objects;
import java.util.Set;

public class NodeRequest {
    private final Integer parent;
    private final Set<String> toAdd;

    public NodeRequest(Integer parent, Set<String> toAdd) {
        this.parent = parent;
        this.toAdd = toAdd;
    }

    public Integer getParent() {
        return parent;
    }

    public Set<String> getToAdd() {
        return toAdd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeRequest that = (NodeRequest) o;
        return Objects.equals(parent, that.parent) &&
                Objects.equals(toAdd, that.toAdd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, toAdd);
    }

    @Override
    public String toString() {
        return "NodeRequest{" +
                "parent=" + parent +
                ", toAdd=" + toAdd +
                '}';
    }
}
