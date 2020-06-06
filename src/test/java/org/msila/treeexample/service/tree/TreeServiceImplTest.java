package org.msila.treeexample.service.tree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.msila.treeexample.dao.TreeDao;
import org.msila.treeexample.model.NodeRequest;
import org.msila.treeexample.model.TreeDTO;
import org.msila.treeexample.model.TreeNode;
import org.msila.treeexample.service.validation.TreeValidationService;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TreeServiceImplTest {

    private TreeDao treeDaoMock;
    private TreeValidationService treeValidationServiceMock;
    private TreeServiceImpl treeServiceImpl;
    @BeforeEach
    void setUp() {
        treeDaoMock = mock(TreeDao.class);
        treeValidationServiceMock = mock(TreeValidationService.class);
        treeServiceImpl = new TreeServiceImpl(treeDaoMock,treeValidationServiceMock);
    }

    @Test
    void addNodes() {
        NodeRequest testRequest = new NodeRequest(1, Set.of("a","b","c"));
        treeServiceImpl.addNodes(testRequest);
        verify(treeValidationServiceMock).validateNodeRequest(testRequest);
        verify(treeValidationServiceMock).validateRequestNeedingNodeId(1);
        verify(treeDaoMock).addNodeForOneLevel(Set.of("a","b","c"),1);
    }

    @Test
    void addRootNodes() {
        var testNames = Set.of("a","b","c");
        treeServiceImpl.addRootNodes(testNames);
        verify(treeValidationServiceMock).validateNamesNotDuplicate(testNames);
        verify(treeDaoMock).addNodesForRootLevel(testNames);
    }

    @Test
    void getAllNodes() {
        var expectedTrees = List.of(
          new TreeNode("1",1,List.of(
                  new TreeNode("3",3, List.of(
                          new TreeNode("5",5,List.of())
                  )),
                  new TreeNode("4",4,List.of())
          )) ,
                new TreeNode("2",2,List.of())
        );
        when(treeDaoMock.getTopNodes()).thenReturn(List.of(new TreeDTO(1,null,"1"),new TreeDTO(2,null,"2")));
        when(treeDaoMock.getChildNodes(Set.of(1,2))).thenReturn(List.of(new TreeDTO(3,1,"3"),new TreeDTO(4,1,"4")));
        when(treeDaoMock.getChildNodes(Set.of(3,4))).thenReturn(List.of(new TreeDTO(5,3,"5")));
        var actual = treeServiceImpl.getAllNodes();
        verify(treeDaoMock).getTopNodes();
        verify(treeDaoMock).getChildNodes(Set.of(1,2));
        verify(treeDaoMock).getChildNodes(Set.of(3,4));
        verify(treeDaoMock).getChildNodes(Set.of(5));
        assertThat(actual).isEqualTo(expectedTrees);

    }

    @Test
    void getNodes() {
        var expectedTrees = List.of(
                new TreeNode("3",3,List.of(
                        new TreeNode("5",5,List.of())
                )),
                new TreeNode("4",4,List.of())
        );
        when(treeDaoMock.getChildNodes(Set.of(1))).thenReturn(List.of(new TreeDTO(3,1,"3"),new TreeDTO(4,1,"4")));
        when(treeDaoMock.getChildNodes(Set.of(3,4))).thenReturn(List.of(new TreeDTO(5,3,"5")));
        var actual = treeServiceImpl.getNodes(1);
        verify(treeDaoMock).getChildNodes(Set.of(1));
        verify(treeDaoMock).getChildNodes(Set.of(3,4));
        verify(treeDaoMock).getChildNodes(Set.of(5));
        assertThat(actual).isEqualTo(expectedTrees);
    }

    @Test
    void deleteAllNodes() {
        treeServiceImpl.deleteAllNodes();
        verify(treeDaoMock).deleteAllNodes();
    }

    @Test
    void deleteNode() {
        treeServiceImpl.deleteNode(1);
        verify(treeDaoMock).removeNodeAndChildren(1);
    }


}