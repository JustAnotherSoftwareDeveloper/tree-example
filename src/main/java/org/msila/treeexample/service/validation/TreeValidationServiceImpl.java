package org.msila.treeexample.service.validation;

import org.msila.treeexample.dao.TreeDao;
import org.msila.treeexample.exception.TreeValidationException;
import org.msila.treeexample.model.NodeRequest;
import org.msila.treeexample.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TreeValidationServiceImpl implements TreeValidationService {
    private TreeDao treeDao;
    private static Logger logger = LoggerFactory.getLogger(TreeValidationServiceImpl.class);

    @Autowired
    public TreeValidationServiceImpl(TreeDao treeDao) {
        this.treeDao = treeDao;
    }
    @Override
    public void validateNodeRequest(NodeRequest nodeRequest) throws TreeValidationException {
        logger.info(String.format("Validating Node Request %s",nodeRequest));
        if (nodeRequest == null) {
            throw new TreeValidationException("Request to add new nodes is null");
        }
        Set<String> newNames = nodeRequest.getToAdd();
        if (newNames == null) {
            throw new TreeValidationException("Request to add new nodes has null name set");
        }
        Set<String> existingNames = treeDao.getAllNodeNames();
        if (newNames.stream().anyMatch(name -> existingNames.contains(name))) {
            throw new TreeValidationException("At least one name already exists in DB for a node");
        }
    }

    @Override
    public void validateRequestNeedingNodeId(Integer treeNodeId) throws TreeValidationException {
        logger.info(String.format("Validation Node Id %s",treeNodeId));
        if (treeNodeId == null) {
            throw new TreeValidationException("Node Id is null");
        }
        Set<Integer> existingIds = treeDao.getNodeIds();
        if (!existingIds.contains(treeNodeId)) {
            throw new TreeValidationException("Id does not exist in database");
        }

    }

    @Override
    public void validateNamesNotDuplicate(Set<String> names) throws TreeValidationException {
        Set<String> existingNames = treeDao.getAllNodeNames();
        if (existingNames.stream().anyMatch(name -> names.contains(name))) {
            throw new TreeValidationException("At least one name already exists in db for node");
        }
    }
}
