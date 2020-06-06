package org.msila.treeexample.model;

import java.util.Objects;

public class TreeDTO {
    private final Integer id;
    private final Integer parentId;
    private final String name;

    public TreeDTO(Integer id, Integer parentId, String name) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TreeDTO treeDTO = (TreeDTO) o;
        return Objects.equals(id, treeDTO.id) &&
                Objects.equals(parentId, treeDTO.parentId) &&
                Objects.equals(name, treeDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parentId, name);
    }

    @Override
    public String toString() {
        return "TreeDTO{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                '}';
    }
}
