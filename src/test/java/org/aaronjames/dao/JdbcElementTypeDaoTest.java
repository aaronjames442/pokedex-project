package org.aaronjames.dao;

import org.aaronjames.model.ElementType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
public class JdbcElementTypeDaoTest {

    @Autowired
    private JdbcElementTypeDao elementTypeDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setup() {
        String sql = "INSERT INTO element_type (name, description, color) VALUES " +
                "('Electric', 'Electric-type moves are effective against Water-types.', 'Yellow'), " +
                "('Fire', 'Fire-type moves are effective against Grass-types.', 'Red'), " +
                "('Grass', 'Grass-type moves are effective against Water-types.', 'Green')";
        jdbcTemplate.update(sql);

        String relationshipSql = "INSERT INTO type_relationship (type_id, related_type_id, relationship_type) VALUES " +
                "(1, 3, 'strength'), " + // Electric is strong against Water
                "(2, 3, 'strength'), " + // Fire is strong against Grass
                "(3, 2, 'weakness')";   // Grass is weak against Fire
        jdbcTemplate.update(relationshipSql);
    }

    @Test
    public void getTypeById_returnsCorrectElementType() {
        String sql = "INSERT INTO element_type (name, description, color) VALUES " +
                "('Water', 'Water-type moves are effective against Fire-types.', 'Blue') RETURNING id";
        int id = jdbcTemplate.queryForObject(sql, Integer.class);

        ElementType result = elementTypeDao.getTypeById(id);

        assertNotNull(result);
        assertEquals("Water", result.getName());
        assertEquals("Water-type moves are effective against Fire-types.", result.getDescription());
        assertEquals("Blue", result.getColor());
    }

    @Test
    public void addType_insertsNewElementType() {
        ElementType newType = new ElementType(0, "Ice", List.of(), List.of(), "Ice-type moves are effective against Grass-types.", "Light Blue");

        int newId = elementTypeDao.addType(newType);

        ElementType insertedType = elementTypeDao.getTypeById(newId);
        assertNotNull(insertedType);
        assertEquals("Ice", insertedType.getName());
        assertEquals("Ice-type moves are effective against Grass-types.", insertedType.getDescription());
        assertEquals("Light Blue", insertedType.getColor());
    }

    @Test
    public void updateType_updatesExistingElementType() {
        String sql = "INSERT INTO element_type (name, description, color) VALUES " +
                "('Psychic', 'Psychic-type moves are effective against Poison-types.', 'Purple') RETURNING id";
        int id = jdbcTemplate.queryForObject(sql, Integer.class);

        ElementType updatedType = new ElementType(id, "Psychic", List.of("Fighting"), List.of("Dark"), "Updated description", "Pink");

        boolean success = elementTypeDao.updateType(updatedType);

        assertTrue(success);
        ElementType result = elementTypeDao.getTypeById(id);
        assertEquals("Updated description", result.getDescription());
        assertEquals("Pink", result.getColor());
    }

    @Test
    public void deleteTypeById_removesElementType() {
        String sql = "INSERT INTO element_type (name, description, color) VALUES " +
                "('Dragon', 'Dragon-type moves are effective against Dragon-types.', 'Deep Blue') RETURNING id";
        int id = jdbcTemplate.queryForObject(sql, Integer.class);

        boolean success = elementTypeDao.deleteTypeById(id);

        assertTrue(success);
        assertNull(elementTypeDao.getTypeById(id));
    }
}

