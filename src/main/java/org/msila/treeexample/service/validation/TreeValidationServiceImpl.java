package org.msila.treeexample.service.validation;

import org.msila.treeexample.dao.TreeDao;
import org.msila.treeexample.exception.TreeValidationException;
import org.msila.treeexample.model.NodeRequest;
import org.msila.treeexample.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TreeValidationServiceImpl implements TreeValidationService {
    private TreeDao treeDao;

    @Autowired
    public TreeValidationServiceImpl(TreeDao treeDao) {
        this.treeDao = treeDao;
    }
    @Override
    public void validateNodeRequest(NodeRequest nodeRequest) throws TreeValidationException {
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
