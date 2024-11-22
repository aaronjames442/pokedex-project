package org.aaronjames.dao;

import org.aaronjames.model.Stats;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;

public class JdbcStatsDao implements StatsDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcStatsDao(DataSource dataSource)   {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }



    @Override
    public Stats getStatsById(int statsId) {
        String sql = "SELECT * FROM stats WHERE id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, statsId);
        if (result.next())
            return mapRowToStats(result);

        return null;
    }

    @Override
    public boolean deleteStatsById(int statsId) {
        String sql = "DELETE FROM stats WHERE id = ?";
        int deletedStat = jdbcTemplate.update(sql, statsId);
        return deletedStat > 0;
    }

    @Override
    public boolean updateStats(Stats stats) {
        String sql = "UPDATE stats SET hp = ?, attack = ?, defense = ?, specialAttack = ?, specialDefense = ?, speed = ? WHERE id = ?";
        int updatedStat = jdbcTemplate.update(sql, stats.getHp(), stats.getAttack(), stats.getDefense(), stats.getSpecialAttack(), stats.getSpecialDefense(), stats.getSpeed(), stats.getId());
        return updatedStat > 0;
    }

    @Override
    public int addStats(Stats stats) {
        String sql = "INSERT INTO stats (hp, attack, defense, specialAttack, specialDefense, speed) VALUES (?,?,?,?,?,?) RETURNING id";
        return jdbcTemplate.update(sql, Integer.class, stats.getHp(), stats.getAttack(), stats.getDefense(), stats.getSpecialAttack(), stats.getSpecialDefense(), stats.getSpeed());
    }

    private Stats mapRowToStats(SqlRowSet rowSet) {
        return new Stats(
                rowSet.getInt("id"),
                rowSet.getInt("hp"),
                rowSet.getInt("attack"),
                rowSet.getInt("defense"),
                rowSet.getInt("specialAttack"),
                rowSet.getInt("specialDefense"),
                rowSet.getInt("speed")

        );
    }

}
