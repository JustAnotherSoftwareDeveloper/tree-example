package org.msila.treeexample.service.tree;

import org.msila.treeexample.dao.TreeDao;
import org.msila.treeexample.model.NodeRequest;
import org.msila.treeexample.model.TreeDTO;
import org.msila.treeexample.model.TreeNode;
import org.msila.treeexample.service.validation.TreeValidationService;
import org.msila.treeexample.service.validation.TreeValidationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TreeServiceImpl implements TreeService {
    private TreeDao treeDao;
    private TreeValidationService treeValidationService;

    private static Logger logger = LoggerFactory.getLogger(TreeValidationServiceImpl.class);
    @Autowired
    public TreeServiceImpl(TreeDao treeDao, TreeValidationService treeValidationService) {
        this.treeDao = treeDao;
        this.treeValidationService = treeValidationService;
    }

    /**
     * Adds nodes for a given parent
     * @param nodeRequest The parentId and names to be added
     */
    @Override
    public void addNodes(NodeRequest nodeRequest) {

        treeValidationService.validateNodeRequest(nodeRequest);
        treeValidationService.validateRequestNeedingNodeId(nodeRequest.getParent());
        logger.info(String.format("Adding nodes for request %s",nodeRequest));
        treeDao.addNodeForOneLevel(nodeRequest.getToAdd(),nodeRequest.getParent());

    }

    /**
     * Add nodes at root
     * @param names the names to be added
     */
    @Override
    public void addRootNodes(Set<String> names) {
        treeValidationService.validateNamesNotDuplicate(names);
        logger.info(String.format("Adding root nodes %s",names));
        treeDao.addNodesForRootLevel(names);
    }

    /**
     * Retrives all the nodes
     * @return all the nodes
     */
    @Override
    public List<TreeNode> getAllNodes() {
        logger.info("Loading All Nodes");
        return loadNodes(null);
    }

    /**
     * Gets children of the given node id
     * @param treeNodeId the given node id
     * @return the subtree associated with that node
     */
    @Override
    public List<TreeNode> getNodes(Integer treeNodeId) {
        treeValidationService.validateRequestNeedingNodeId(treeNodeId);
        logger.info(String.format("Loading Nodes for Parent Id %s",treeNodeId));
        return loadNodes(treeNodeId);
    }

    /**
     * Deletes all nodes from the db
     */
    @Override
    public void deleteAllNodes() {
        logger.info("Deleting All Nodes");
        treeDao.deleteAllNodes();
    }

    /**
     * Deletes the given node and its children
     * @param treeNodeId the given node
     */
    @Override
    public void deleteNode(Integer treeNodeId) {
        treeValidationService.validateRequestNeedingNodeId(treeNodeId);
        logger.info(String.format("Deleting Nodes %s",treeNodeId));
        treeDao.removeNodeAndChildren(treeNodeId);
    }

    private List<TreeNode> loadNodes(Integer parentId) {
        /*
        Yes, I know I could have used a CTE. However, in my
        experience they are so slow and can become unusable if
        there is too much depth. This, while less elegant,
        is faster and more reliable.
         */
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
