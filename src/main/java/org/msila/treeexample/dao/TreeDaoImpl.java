package org.msila.treeexample.dao;

import org.apache.juli.logging.Log;
import org.msila.treeexample.model.TreeDTO;
import org.msila.treeexample.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TreeDaoImpl implements TreeDao {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static Logger logger = LoggerFactory.getLogger(TreeDaoImpl.class);

    @Autowired
    public TreeDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    /**
     * Creates and Inserts TreeNodes into the db, given a parent
     * @param treeNodes The list of node names to be added
     * @param parentId the parent node id
     */
    @Override
    public void addNodeForOneLevel(Set<String> treeNodes, Integer parentId) {
        String sql = "INSERT INTO Tree (parent_id,name) VALUES (:parentId,:name)";
        List<SqlParameterSource> toInsert = treeNodes.stream().map(name -> {
            MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
            sqlParameterSource.addValue("parentId", parentId);
            sqlParameterSource.addValue("name", name);
            return sqlParameterSource;
        }).collect(Collectors.toList());
        namedParameterJdbcTemplate.batchUpdate(sql, toInsert.toArray(new MapSqlParameterSource[]{}));
        logger.info(String.format("Added Nodes for %s",parentId));


    }

    /**
     * Adds Nodes at the root level
     * @param treeNodes List of names to add
     */
    @Override
    public void addNodesForRootLevel(Set<String> treeNodes) {
        String sql = "INSERT INTO Tree (name) VALUES (:name)";
        List<SqlParameterSource> toInsert = treeNodes.stream().map(name -> new MapSqlParameterSource("name", name)).collect(Collectors.toList());
        namedParameterJdbcTemplate.batchUpdate(sql, toInsert.toArray(new MapSqlParameterSource[]{}));
        logger.info("Added Root Nodes");
    }

    /**
     * Removes Node and Children Specified. Works
     * because of the delete cascade parameter set in the db
     * @param treeNodeId The node to delete
     */
    @Override
    public void removeNodeAndChildren(Integer treeNodeId) {
        String sql = "DELETE FROM Tree WHERE id = :id";
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource("id", treeNodeId);
        logger.info(String.format("Removed Node %s and children",treeNodeId));
        namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);

    }

    /**
     * Retrives all the node names
     * @return the names of all nodes in tree
     */
    @Override
    public Set<String> getAllNodeNames() {
        String sql = "SELECT name FROM Tree";
        List<String> nodeNames = namedParameterJdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("name"));
        logger.info("Retrieved all node names");
        return new HashSet<>(nodeNames);
    }

    /**
     * Deletes All the Nodes
     */
    @Override
    public void deleteAllNodes() {
        String sql = "DELETE From Tree";
        namedParameterJdbcTemplate.update(sql, new HashMap<>());
        logger.info("Deleted All Nodes");
    }

    /**
     * Gets Child Nodes of The specified tree node ids
     * @param treeNodeIds The list of tree node ids to query
     * @return The child node dtos
     */
    @Override
    public List<TreeDTO> getChildNodes(Set<Integer> treeNodeIds) {
        SqlParameterSource parameterSource = new MapSqlParameterSource("ids", treeNodeIds);
        String sql = "SELECT * FROM Tree WHERE parent_id IN (:ids)";
        return namedParameterJdbcTemplate.query(sql, parameterSource, getRowMapper());
    }

    /**
     * Gets the root nodes
     * @return The rows corresponding to root nodes
     */
    @Override
    public List<TreeDTO> getTopNodes() {
        String sql = "SELECT * FROM Tree WHERE parent_id IS NULL";
        logger.info("Retrieved Root Nodes");
        return namedParameterJdbcTemplate.query(sql, getRowMapper());
    }

    /**
     * Gets all Ids in Table
     * @return the node ids in the table
     */
    @Override
    public Set<Integer> getNodeIds() {
        String sql = "SELECT id FROM Tree";
        List<Integer> ids = namedParameterJdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("id"));
        logger.info("Retrieved All Node Ids");
        return new HashSet<>(ids);
    }

    private RowMapper<TreeDTO> getRowMapper() {
        return (rs, rowNum) -> new TreeDTO(rs.getInt("id"), rs.getInt("parent_id"), rs.getString("name"));
    }
}
