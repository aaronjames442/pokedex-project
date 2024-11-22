package org.aaronjames.dao;

import org.aaronjames.model.Ability;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
public class JdbcAbilityDaoTest {

    @Autowired
    private JdbcAbilityDao abilityDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setup() {
        String sql = "INSERT INTO ability (name, type, description, effect) VALUES " +
                "('Static', 'Electric', 'May cause paralysis', 'Paralysis on contact'), " +
                "('Overgrow', 'Grass', 'Boosts grass moves', 'Increased grass damage'), " +
                "('Blaze', 'Fire', 'Boosts fire moves', 'Increased fire damage')";
        jdbcTemplate.update(sql);
    }

    @Test
    public void getAbilityById_returnsCorrectAbility() {
        String sql = "INSERT INTO ability (name, type, description, effect) VALUES " +
                "('Intimidate', 'Normal', 'Lowers opponent attack', 'Attack reduction') RETURNING id";
        int id = jdbcTemplate.queryForObject(sql, Integer.class);

        Ability result = abilityDao.getAbilityById(id);

        assertNotNull(result);
        assertEquals("Intimidate", result.getName());
        assertEquals("Normal", result.getType());
        assertEquals("Lowers opponent attack", result.getDescription());
        assertEquals("Attack reduction", result.getEffect());
    }

    @Test
    public void addAbility_insertsNewAbility() {
        Ability newAbility = new Ability(0, "Swift Swim", "Water", "Doubles speed in rain", "Increased speed");

        int newId = abilityDao.addAbility(newAbility);

        Ability insertedAbility = abilityDao.getAbilityById(newId);
        assertNotNull(insertedAbility);
        assertEquals("Swift Swim", insertedAbility.getName());
        assertEquals("Water", insertedAbility.getType());
        assertEquals("Doubles speed in rain", insertedAbility.getDescription());
        assertEquals("Increased speed", insertedAbility.getEffect());
    }

    @Test
    public void updateAbility_updatesExistingAbility() {
        String sql = "INSERT INTO ability (name, type, description, effect) VALUES " +
                "('Pressure', 'Normal', 'Increases PP usage', 'PP reduction') RETURNING id";
        int id = jdbcTemplate.queryForObject(sql, Integer.class);

        Ability updatedAbility = new Ability(id, "Pressure", "Normal", "Reduces PP quickly", "PP consumption increased");

        boolean success = abilityDao.updateAbility(updatedAbility);

        assertTrue(success);
        Ability result = abilityDao.getAbilityById(id);
        assertEquals("Reduces PP quickly", result.getDescription());
        assertEquals("PP consumption increased", result.getEffect());
    }

    @Test
    public void deleteAbilityById_removesAbility() {
        String sql = "INSERT INTO ability (name, type, description, effect) VALUES " +
                "('Flash Fire', 'Fire', 'Boosts fire resistance', 'Fire immunity') RETURNING id";
        int id = jdbcTemplate.queryForObject(sql, Integer.class);

        boolean success = abilityDao.deleteAbilityById(id);

        assertTrue(success);
        assertNull(abilityDao.getAbilityById(id));
    }
}