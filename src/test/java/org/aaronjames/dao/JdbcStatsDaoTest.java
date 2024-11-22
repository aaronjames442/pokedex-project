package org.aaronjames.dao;

import org.aaronjames.model.Stats;
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
public class JdbcStatsDaoTest {

    @Autowired
    private JdbcStatsDao statsDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setup() {
        String sql = "INSERT INTO stats (hp, attack, defense, specialAttack, specialDefense, speed) VALUES " +
                "(35, 55, 40, 50, 50, 90), " +
                "(45, 49, 49, 65, 65, 45), " +
                "(39, 52, 43, 60, 50, 65)";
        jdbcTemplate.update(sql);
    }

    @Test
    public void getStatsById_returnsCorrectStats() {
        String sql = "INSERT INTO stats (hp, attack, defense, specialAttack, specialDefense, speed) " +
                "VALUES (50, 60, 45, 70, 65, 80) RETURNING id";
        int id = jdbcTemplate.queryForObject(sql, Integer.class);

        Stats result = statsDao.getStatsById(id);

        assertNotNull(result);
        assertEquals(50, result.getHp());
        assertEquals(60, result.getAttack());
        assertEquals(45, result.getDefense());
        assertEquals(70, result.getSpecialAttack());
        assertEquals(65, result.getSpecialDefense());
        assertEquals(80, result.getSpeed());
    }

    @Test
    public void addStats_insertsNewStats() {
        Stats newStats = new Stats(0, 60, 70, 50, 75, 80, 95);

        int newId = statsDao.addStats(newStats);

        Stats insertedStats = statsDao.getStatsById(newId);
        assertNotNull(insertedStats);
        assertEquals(60, insertedStats.getHp());
        assertEquals(70, insertedStats.getAttack());
        assertEquals(50, insertedStats.getDefense());
        assertEquals(75, insertedStats.getSpecialAttack());
        assertEquals(80, insertedStats.getSpecialDefense());
        assertEquals(95, insertedStats.getSpeed());
    }

    @Test
    public void updateStats_updatesExistingStats() {
        String sql = "INSERT INTO stats (hp, attack, defense, specialAttack, specialDefense, speed) " +
                "VALUES (40, 50, 55, 65, 70, 75) RETURNING id";
        int id = jdbcTemplate.queryForObject(sql, Integer.class);

        Stats updatedStats = new Stats(id, 45, 55, 60, 70, 75, 80);

        boolean success = statsDao.updateStats(updatedStats);

        assertTrue(success);
        Stats result = statsDao.getStatsById(id);
        assertEquals(45, result.getHp());
        assertEquals(55, result.getAttack());
        assertEquals(60, result.getDefense());
        assertEquals(70, result.getSpecialAttack());
        assertEquals(75, result.getSpecialDefense());
        assertEquals(80, result.getSpeed());
    }

    @Test
    public void deleteStatsById_removesStats() {
        String sql = "INSERT INTO stats (hp, attack, defense, specialAttack, specialDefense, speed) " +
                "VALUES (30, 40, 35, 45, 50, 55) RETURNING id";
        int id = jdbcTemplate.queryForObject(sql, Integer.class);

        boolean success = statsDao.deleteStatsById(id);

        assertTrue(success);
        assertNull(statsDao.getStatsById(id));
    }


}
