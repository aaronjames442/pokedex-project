package org.aaronjames.dao;

import org.aaronjames.model.Stats;

public interface StatsDao {


    Stats getStatsById(int statsId);

    boolean deleteStatsById(int statsId);

    boolean updateStats(Stats stats);

    int addStats(Stats stats);


}
