package org.msila.treeexample.ws;

import org.msila.treeexample.model.NodeRequest;
import org.msila.treeexample.model.TreeNode;
import org.msila.treeexample.service.tree.TreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/trees")
public class TreeRestController {
    private TreeService treeService;

    @Autowired
    public TreeRestController(TreeService treeService) {
        this.treeService = treeService;
    }

    @GetMapping
    public List<TreeNode> getAllNodes() {
        return treeService.getAllNodes();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAllNodes() {
        treeService.deleteAllNodes();
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> addRootNodes(@RequestBody Set<String> names) {
        treeService.addRootNodes(names);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/for-node/{id}")
    public List<TreeNode> getTreeNodesForId(@PathVariable("id") Integer id) {
        return treeService.getNodes(id);
    }

    @PostMapping("/for-node")
    public ResponseEntity<?> addNodes(@RequestBody NodeRequest nodeRequest) {
        treeService.addNodes(nodeRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/for-node/{id}")
    public ResponseEntity<?> deleteNodes(@PathVariable("id") Integer id) {
        treeService.deleteNode(id);
        return ResponseEntity.ok().build();
    }
}
