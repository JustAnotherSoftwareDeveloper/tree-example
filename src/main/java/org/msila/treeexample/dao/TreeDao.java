package org.msila.treeexample.dao;

import org.msila.treeexample.model.TreeDTO;
import org.msila.treeexample.model.TreeNode;

import java.util.List;
import java.util.Set;

public interface TreeDao {

    void addNodeForOneLevel(Set<String> treeNodes, Integer parentId);
    void addNodesForRootLevel(Set<String> treeNodes);
    void removeNodeAndChildren(Integer treeNodeId);
    Set<String> getAllNodeNames();
    void deleteAllNodes();
    List<TreeDTO> getChildNodes(Set<Integer> treeNodeIds);
    List<TreeDTO> getTopNodes();
    Set<Integer> getNodeIds();
}
