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
        SqlRowSet

    }

    @Override
    public boolean deleteAbilityById(int abilityId) {
        return false;
    }

    @Override
    public boolean updateAbility(Ability ability) {
        return false;
    }

    @Override
    public Ability addAbility(Ability ability) {
        return null;
    }
}
