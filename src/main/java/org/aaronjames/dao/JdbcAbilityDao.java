package org.aaronjames.dao;

import org.aaronjames.model.Ability;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class JdbcAbilityDao implements AbilityDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAbilityDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Ability getAbilityById(int abilityId) {
        String sql = "SELECT * FROM ability WHERE id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, abilityId);

        if (results.next()) {
            return mapRowToAbility(results);
        } else {
            return null;
        }

    }

    @Override
    public boolean deleteAbilityById(int abilityId) {
        String sql = "DELETE FROM ability WHERE id = ?";
        int rowsDeleted = jdbcTemplate.update(sql, abilityId);

        return rowsDeleted > 0;
    }

    @Override
    public boolean updateAbility(Ability ability) {
        String sql = "UPDATE ability SET name = ?, type = ?, description = ?, effect = ? WHERE id = ?";
        int rowsUpdated = jdbcTemplate.update(sql, ability.getName(), ability.getType(), ability.getDescription(), ability.getEffect(), ability.getId());
        return rowsUpdated > 0 ;
    }

    @Override
    public int addAbility(Ability ability) {
        String sql = "INSERT INTO ability (name, type, description, effect) VALUES (?,?,?,?) RETURNING id";
         return jdbcTemplate.queryForObject(sql, Integer.class, ability.getName(), ability.getType(), ability.getDescription(), ability.getEffect());

    }

    private Ability mapRowToAbility(SqlRowSet rowSet) {
        return new Ability(
                rowSet.getInt("id"),
                rowSet.getString("name"),
                rowSet.getString("type"),
                rowSet.getString("description"),
                rowSet.getString("effect")
                );
    }
}
