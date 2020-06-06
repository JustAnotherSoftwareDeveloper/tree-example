package org.msila.treeexample.service.tree;

import org.msila.treeexample.model.NodeRequest;
import org.msila.treeexample.model.TreeNode;

import java.util.List;
import java.util.Set;

public interface TreeService {

    void addNodes(NodeRequest nodeRequest);
    void addRootNodes(Set<String> names);
    List<TreeNode> getAllNodes();
    List<TreeNode> getNodes(Integer treeNodeId);
    void deleteAllNodes();
    void deleteNode(Integer treeNodeId);
}
