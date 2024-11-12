package org.aaronjames.dao;

import org.aaronjames.model.Pokemon;

public interface PokemonDao {

    Pokemon getPokemonById(int pokemonId);
    boolean deletePokemonById(int pokemonId);
    boolean updatePokemonById(Pokemon pokemon);
    Pokemon addPokemon(Pokemon pokemon);




}
