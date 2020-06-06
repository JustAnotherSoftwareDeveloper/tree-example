package org.msila.treeexample.service.tree;

import org.msila.treeexample.model.NodeRequest;
import org.msila.treeexample.model.TreeNode;

import java.util.List;

public interface TreeService {

    List<TreeNode> addNodes(NodeRequest nodeRequest);
    List<TreeNode> getAllNodes();
    List<TreeNode> getNodes(Integer treeNodeId);
    List<TreeNode> deleteAllNodes();
    List<TreeNode> deleteNode(Integer treeNodeId);
}
