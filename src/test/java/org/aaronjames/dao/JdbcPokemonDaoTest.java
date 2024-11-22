package org.aaronjames.dao;

import org.aaronjames.model.Pokemon;
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

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)

public class JdbcPokemonDaoTest {

    @Autowired
    private JdbcPokemonDao pokemonDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setup() {
        String sql = "INSERT INTO pokemon (name, description, height, weight, gender) VALUES " +
                "('Pikachu', 'Electric mouse', '0.4', '6.0', 'Male'), " +
                "('Charmander', 'Fire lizard', '0.6', '8.5', 'Male')";
        jdbcTemplate.update(sql);
    }

    @Test
    public void getPokemonById_returnsCorrectPokemon() {
        String sql = "INSERT INTO pokemon (name, description, height, weight, gender) VALUES ('Bulbasaur', 'Grass type', '0.7', '6.9', 'Male') RETURNING id";
        int id = jdbcTemplate.queryForObject(sql, Integer.class);

        Pokemon result = pokemonDao.getPokemonById(id);

        assertNotNull(result);
        assertEquals("Bulbasaur", result.getName());
        assertEquals("Grass type", result.getDescription());
    }

    @Test
    public void addPokemon_insertsNewPokemon() {
        Pokemon newPokemon = new Pokemon(0, "Squirtle", "Water type", "0.5", "9.0", List.of("Male"));

        int newId = pokemonDao.addPokemon(newPokemon);

        Pokemon insertedPokemon = pokemonDao.getPokemonById(newId);
        assertNotNull(insertedPokemon);
        assertEquals("Squirtle", insertedPokemon.getName());
    }

    @Test
    public void updatePokemonById_updatesExistingPokemon() {
        String sql = "INSERT INTO pokemon (name, description, height, weight, gender) VALUES ('Eevee', 'Normal type', '0.3', '6.5', 'Female') RETURNING id";
        int id = jdbcTemplate.queryForObject(sql, Integer.class);

        Pokemon updatedPokemon = new Pokemon(id, "Eevee", "Normal/Type - Updated", "0.3", "6.5", List.of("Female"));

        boolean success = pokemonDao.updatePokemonById(updatedPokemon);

        assertTrue(success);
        Pokemon result = pokemonDao.getPokemonById(id);
        assertEquals("Normal/Type - Updated", result.getDescription());
    }

    @Test
    public void deletePokemonById_removesPokemon() {
        String sql = "INSERT INTO pokemon (name, description, height, weight, gender) VALUES ('Snorlax', 'Normal type', '2.1', '460.0', 'Male') RETURNING id";
        int id = jdbcTemplate.queryForObject(sql, Integer.class);

        boolean success = pokemonDao.deletePokemonById(id);

        assertTrue(success);
        assertNull(pokemonDao.getPokemonById(id));
    }
}



