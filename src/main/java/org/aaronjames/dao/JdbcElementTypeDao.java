package org.aaronjames.dao;

import org.aaronjames.model.ElementType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class JdbcElementTypeDao implements ElementTypeDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcElementTypeDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public ElementType getTypeById(int typeId) {
        String sql = "SELECT * FROM element_type WHERE id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, typeId);
        if (result.next()) {
            return mapRowToElementType(result);
        }
        return null;
    }

    @Override
    public boolean deleteTypeById(int typeId) {
        String sql = "DELETE FROM element_type WHERE id = ?";
        int deletedType = jdbcTemplate.update(sql, typeId);
        return deletedType > 0;
    }

    @Override
    public boolean updateType(ElementType elementType) {
        String sql = "UPDATE element_type SET name = ?, description = ?, color = ? WHERE id = ?";
        int rowsUpdated = jdbcTemplate.update(sql, elementType.getName(), elementType.getDescription(), elementType.getColor(), elementType.getId());

        if (rowsUpdated > 0) {
            updateRelationships(elementType.getId(), elementType.getStrengths(), elementType.getWeaknesses());
            return true;
        }
        return false;
    }

    @Override
    public int addType(ElementType elementType) {
        String sql = "INSERT INTO element_type (name, description, color) VALUES (?,?,?) RETURNING id";
        return jdbcTemplate.queryForObject(sql, Integer.class, elementType.getName(), elementType.getDescription(), elementType.getColor());
    }

    private void updateRelationships(int typeId, List<String> strengths, List<String> weaknesses) {
        String deleteSql = "DELETE FROM type_relationship WHERE type_id = ?";
        jdbcTemplate.update(deleteSql, typeId);

        String insertSql = "INSERT INTO type_relationship (type_id, related_type_id, relationship_type) VALUES (?, ?, ?)";
        for (String strength : strengths) {
            int relatedTypeId = getElementTypeIdByName(strength);
            jdbcTemplate.update(insertSql, typeId, relatedTypeId, "strength");
        }
        for (String weakness : weaknesses) {
            int relatedTypeId = getElementTypeIdByName(weakness);
            jdbcTemplate.update(insertSql, typeId, relatedTypeId, "weakness");
        }
    }

    private int getElementTypeIdByName(String typeName) {
        String sql = "SELECT id FROM element_type WHERE name = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, typeName);
        } catch (Exception e) {
            throw new IllegalArgumentException("ElementType name not found: " + typeName, e);
        }
    }

    private ElementType mapRowToElementType(SqlRowSet rowSet) {
        int id = rowSet.getInt("id");
        String name = rowSet.getString("name");
        String description = rowSet.getString("description");
        String color = rowSet.getString("color");

        List<String> strengths = getStrengths(id);
        List<String> weaknesses = getWeaknesses(id);

        return new ElementType(id, name, strengths, weaknesses, description, color);
    }

    private List<String> getStrengths(int elementTypeId) {
        return getRelationships(elementTypeId, "strength");
    }

    private List<String> getWeaknesses(int elementTypeId) {
        return getRelationships(elementTypeId, "weakness");
    }

    private List<String> getRelationships(int elementTypeId, String relationshipType) {
        List<String> relationships = new ArrayList<>();
        String sql = "SELECT et2.name FROM type_relationship tr " +
                "JOIN element_type et2 ON tr.related_type_id = et2.id " +
                "WHERE tr.type_id = ? AND tr.relationship_type = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, elementTypeId, relationshipType);
        while (results.next()) {
            relationships.add(results.getString("name"));
        }
        return relationships;
    }
}