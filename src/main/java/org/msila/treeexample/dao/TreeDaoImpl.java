package org.msila.treeexample.dao;

import org.msila.treeexample.model.TreeDTO;
import org.msila.treeexample.model.TreeNode;
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

    @Autowired
    public TreeDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

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


    }

    @Override
    public void addNodesForRootLevel(Set<String> treeNodes) {
        String sql = "INSERT INTO Tree (name) VALUES (:name)";
        List<SqlParameterSource> toInsert = treeNodes.stream().map(name -> new MapSqlParameterSource("name", name)).collect(Collectors.toList());
        namedParameterJdbcTemplate.batchUpdate(sql, toInsert.toArray(new MapSqlParameterSource[]{}));
    }

    @Override
    public void removeNodeAndChildren(Integer treeNodeId) {
        String sql = "DELETE FROM Tree WHERE id = :id";
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource("id", treeNodeId);
        namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);

    }

    @Override
    public Set<String> getAllNodeNames() {
        String sql = "SELECT name FROM Tree";
        List<String> nodeNames = namedParameterJdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("name"));
        return new HashSet<>(nodeNames);
    }

    @Override
    public void deleteAllNodes() {
        String sql = "DELETE From Tree";
        namedParameterJdbcTemplate.update(sql, new HashMap<>());
    }

    @Override
    public List<TreeDTO> getChildNodes(Set<Integer> treeNodeIds) {
        SqlParameterSource parameterSource = new MapSqlParameterSource("ids", treeNodeIds);
        String sql = "SELECT * FROM Tree WHERE parent_id IN (:ids)";
        return namedParameterJdbcTemplate.query(sql, parameterSource, getRowMapper());
    }

    @Override
    public List<TreeDTO> getTopNodes() {
        String sql = "SELECT * FROM Tree WHERE parent_id IS NULL";
        return namedParameterJdbcTemplate.query(sql, getRowMapper());
    }

    @Override
    public Set<Integer> getNodeIds() {
        String sql = "SELECT id FROM Tree";
        List<Integer> ids = namedParameterJdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("id"));
        return new HashSet<>(ids);
    }

    private RowMapper<TreeDTO> getRowMapper() {
        return (rs, rowNum) -> new TreeDTO(rs.getInt("id"), rs.getInt("parent_id"), rs.getString("name"));
    }
}
