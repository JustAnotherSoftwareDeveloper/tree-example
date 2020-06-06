package org.msila.treeexample.service.validation;

import org.msila.treeexample.exception.TreeValidationException;
import org.msila.treeexample.model.NodeRequest;

import java.util.List;
import java.util.Set;


public interface TreeValidationService {

    void validateNodeRequest(NodeRequest nodeRequest) throws TreeValidationException;
    void validateRequestNeedingNodeId(Integer treeNodeId) throws TreeValidationException;
    void validateNamesNotDuplicate(Set<String> names) throws TreeValidationException;
}
