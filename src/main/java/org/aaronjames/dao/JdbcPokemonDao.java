package org.aaronjames.dao;

import org.aaronjames.model.Pokemon;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class JdbcPokemonDao implements PokemonDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcPokemonDao(DataSource   dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Pokemon getPokemonById(int pokemonId) {
        String sql = "SELECT * FROM pokemon WHERE id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, pokemonId);
        if (result.next()) {
            return mapRowToPokemon(result);
        }
        return null;
    }

    @Override
    public boolean deletePokemonById(int pokemonId) {
        String sql = "DELETE FROM pokemon WHERE id = ?";
        int deletedPokemon = jdbcTemplate.update(sql, pokemonId);
        return deletedPokemon > 0;
    }

    @Override
    public boolean updatePokemonById(Pokemon pokemon) {
        String gender = String.join(",", pokemon.getGender());
        String sql = "UPDATE pokemon SET name = ?, description = ?, height = ?, weight = ?, gender = ? WHERE id = ?";
        int updatedPokemon = jdbcTemplate.update(sql, pokemon.getName(), pokemon.getDescription(), pokemon.getHeight(), pokemon.getWeight(), gender, pokemon.getId());
        return updatedPokemon > 0;
    }

    @Override
    public int addPokemon(Pokemon pokemon) {
        String gender = String.join(",", pokemon.getGender());
        String sql = "INSERT INTO pokemon (name, description, height, weight, gender) VALUES (?,?,?,?,?) RETURNING id";
        return jdbcTemplate.queryForObject(sql, Integer.class, pokemon.getName(), pokemon.getDescription(), pokemon.getHeight(), pokemon.getWeight(), gender);

    }

    private Pokemon mapRowToPokemon(SqlRowSet rowSet) {

        int id = rowSet.getInt("id");
        String name = rowSet.getString("name");
        String description = rowSet.getString("description");
        String height = rowSet.getString("height");
        String weight = rowSet.getString("weight");

        // Fetch the gender list using a helper method
        List<String> genders = getGendersForPokemon(id);

        // Return the fully populated Pokemon object
        return new Pokemon(id, name, description, height, weight, genders);
    }

    // Helper method to fetch the list of genders for a given Pokémon ID
    private List<String> getGendersForPokemon(int pokemonId) {
        List<String> genders = new ArrayList<>();
        String sql = "SELECT g.name FROM gender g " +
                "JOIN pokemon_gender pg ON g.id = pg.gender_id " +
                "WHERE pg.pokemon_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, pokemonId);
        while (results.next()) {
            genders.add(results.getString("name"));
        }

        return genders;
    }
}
