package org.aaronjames.dao;

import org.aaronjames.model.Ability;

public interface AbilityDao {


    Ability getAbilityById(int abilityId);

    boolean deleteAbilityById(int abilityId);

    boolean updateAbility(Ability ability);

    int addAbility(Ability ability);
}
