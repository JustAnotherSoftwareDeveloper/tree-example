package org.msila.treeexample.service.tree;

import org.msila.treeexample.dao.TreeDao;
import org.msila.treeexample.model.NodeRequest;
import org.msila.treeexample.model.TreeDTO;
import org.msila.treeexample.model.TreeNode;
import org.msila.treeexample.service.validation.TreeValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TreeServiceImpl implements TreeService {
    private TreeDao treeDao;
    private TreeValidationService treeValidationService;

    @Autowired
    public TreeServiceImpl(TreeDao treeDao, TreeValidationService treeValidationService) {
        this.treeDao = treeDao;
        this.treeValidationService = treeValidationService;
    }
    @Override
    public void addNodes(NodeRequest nodeRequest) {
        treeValidationService.validateNodeRequest(nodeRequest);
        treeValidationService.validateRequestNeedingNodeId(nodeRequest.getParent());
        treeDao.addNodeForOneLevel(nodeRequest.getToAdd(),nodeRequest.getParent());

    }

    @Override
    public void addRootNodes(Set<String> names) {
        treeValidationService.validateNamesNotDuplicate(names);
        treeDao.addNodesForRootLevel(names);
    }

    @Override
    public List<TreeNode> getAllNodes() {
        return loadNodes(null);
    }

    @Override
    public List<TreeNode> getNodes(Integer treeNodeId) {
        treeValidationService.validateRequestNeedingNodeId(treeNodeId);
        return loadNodes(treeNodeId);
    }

    @Override
    public void deleteAllNodes() {

        treeDao.deleteAllNodes();
    }

    @Override
    public void deleteNode(Integer treeNodeId) {
        treeValidationService.validateRequestNeedingNodeId(treeNodeId);
        treeDao.removeNodeAndChildren(treeNodeId);
    }

    private List<TreeNode> loadNodes(Integer parentId) {
        List<TreeDTO> topEntities;
        if (parentId == null) {
            topEntities = treeDao.getTopNodes();
        }
        else {
            topEntities = treeDao.getChildNodes(Set.of(parentId));
        }
        Map<Integer,TreeNode> nodeIdtoNodeMap = new HashMap<>();
        topEntities.forEach(dto -> {
            var treeNode = new TreeNode(dto.getName(),dto.getId(),new ArrayList<>());
            nodeIdtoNodeMap.put(dto.getId(),treeNode);
        });
        var idsToQuery = topEntities.stream().map(dto -> dto.getId()).collect(Collectors.toSet());
        while (!idsToQuery.isEmpty()) {
            var entities = treeDao.getChildNodes(idsToQuery);
            entities.forEach(dto -> {
                var newNode = new TreeNode(dto.getName(),dto.getId(),new ArrayList<>());
                nodeIdtoNodeMap.get(dto.getParentId()).addChild(newNode);
                nodeIdtoNodeMap.put(dto.getId(),newNode);
            });
            idsToQuery = entities.stream().map(dto -> dto.getId()).collect(Collectors.toSet());
        }
        return topEntities.stream().map(dto -> nodeIdtoNodeMap.get(dto.getId())).collect(Collectors.toList());
    }
}
