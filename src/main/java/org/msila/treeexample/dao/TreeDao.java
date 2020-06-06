package org.msila.treeexample.dao;

import org.msila.treeexample.model.TreeNode;

import java.util.List;
import java.util.Set;

public interface TreeDao {

    List<TreeNode> addNodeForOneLevel(TreeNode treeNode);
    void removeNodeAndChildren(Integer treeNodeId);
    Set<String> getAllNodeNames();
    void deleteAllNodes();
    List<TreeNode> getChildNodes(Integer treeNodeId);
    List<TreeNode> getAllNodes();
}
